package ru.yauroff.awsdeployment.service.impl;

import com.amazonaws.services.eks.AmazonEKS;
import com.amazonaws.services.eks.model.Cluster;
import com.amazonaws.services.eks.model.DescribeClusterRequest;
import com.amazonaws.services.eks.model.DescribeClusterResult;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yauroff.awsdeployment.model.Project;
import ru.yauroff.awsdeployment.model.ProjectStatus;
import ru.yauroff.awsdeployment.service.K8sService;
import ru.yauroff.awsdeployment.service.ProjectService;

import java.io.ByteArrayInputStream;
import java.util.List;

@Slf4j
@Service
public class K8sServiceImpl implements K8sService {
    @Value("${k8s.eks.clusterName}")
    private String clusterName;

    @Value("${aws.eks.region}")
    private String eksRegionName;

    @Value("${aws.account.id}")
    private String accountId;

    @Value("${k8s.author}")
    private String author;

    @Autowired
    private AmazonEKS amazonEKS;

    @Override
    public Project createServiceWithDeployment(Project project, String tagImage) {
        // Получение информации о кластере для дальнейшего взаимодействия с k8s
        Config config = k8sConfig();
        // Deploy сервиса в k8s кластер
        try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
            List<HasMetadata> res = client.load(new ByteArrayInputStream(getYamlFileServiceWithDeployment(project, tagImage).getBytes()))
                                          .inNamespace("default")
                                          .createOrReplace();
            project.setStatus(ProjectStatus.DEPLOYED);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            project.setStatus(ProjectStatus.DEPLOY_ERROR);
        }
        return project;
    }

    @Override
    public Project updateProjectUrl(Project project) {
        // Получение информации о кластере для дальнейшего взаимодействия с k8s
        Config config = k8sConfig();
        // Получение информации из LoadBalancer- а про URL проекта
        try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
            io.fabric8.kubernetes.api.model.Service service = client.services()
                                                                    .inNamespace("default")
                                                                    .withName(project.getName() + "-service")
                                                                    .get();
            String projectURL = service.getStatus()
                                       .getLoadBalancer()
                                       .getIngress()
                                       .get(0)
                                       .getHostname();
            project.setUrl(projectURL);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            project.setStatus(ProjectStatus.PROJECT_URL_ERROR);
        }
        return project;
    }

    private String getYamlFileServiceWithDeployment(Project project, String imageURI) {
        String projectName = project.getName();
        String replicas = "2";
        String yaml =
                "apiVersion : apps/v1\n" +
                        "kind : Deployment\n" +
                        "metadata:\n" +
                        "  name : " + projectName + "-deployment\n" +
                        "  labels:\n" +
                        "    app : " + projectName + "-k8s-application\n" +
                        "    env : prod\n" +
                        "    owner : " + author + "\n" +
                        "spec:\n" +
                        "  replicas : " + replicas + "\n" +
                        "  selector:\n" +
                        "    matchLabels:\n" +
                        "      project: " + projectName + "\n" +
                        "  template:\n" +
                        "    metadata:\n" +
                        "      labels:\n" +
                        "        project: " + projectName + "\n" +
                        "    spec:\n" +
                        "      containers:\n" +
                        "         - name : web\n" +
                        "           image: " + imageURI + "\n" +
                        "           ports:\n" +
                        "             - containerPort: 8080\n" +
                        "---\n" +
                        "apiVersion : v1\n" +
                        "kind: Service\n" +
                        "metadata:\n" +
                        "  name: " + projectName + "-service\n" +
                        "  labels:\n" +
                        "    env : prod\n" +
                        "    owner : " + author + "\n" +
                        "spec:\n" +
                        "  selector:\n" +
                        "    project: " + projectName + "\n" +
                        "  ports:\n" +
                        "    - name      : app-listener\n" +
                        "      protocol  : TCP\n" +
                        "      port      : 80\n" +
                        "      targetPort: 8080\n" +
                        "  type: LoadBalancer";
        return yaml;
    }

    private Config k8sConfig() {
        log.info("Create k8s config for cluster " + clusterName);
        DescribeClusterResult describeClusterResult = amazonEKS.describeCluster(new DescribeClusterRequest().withName(clusterName));
        Cluster cluster = describeClusterResult.getCluster();
        Config config = Config.fromKubeconfig(kubeConfig(cluster));
        return config;
    }

    private String kubeConfig(Cluster cluster) {
        String clusterPath = cluster.getName() + "." + eksRegionName + ".eksctl.io";
        String clusterPathWithName = accountId + "@" + clusterPath;
        String kubeConfig = "apiVersion: v1\n" +
                "clusters:\n" +
                "- cluster:\n" +
                "    certificate-authority-data: " + cluster.getCertificateAuthority()
                                                            .getData() + "\n" +
                "    server: " + cluster.getEndpoint() + "\n" +
                "  name: " + clusterPath + "\n" +
                "contexts:\n" +
                "- context:\n" +
                "    cluster: " + clusterPath + "\n" +
                "    user: " + clusterPathWithName + "\n" +
                "  name: " + clusterPathWithName + "\n" +
                "current-context: " + clusterPathWithName + "\n" +
                "kind: Config\n" +
                "preferences: {}\n" +
                "users:\n" +
                "- name: " + clusterPathWithName + "\n" +
                "  user:\n" +
                "    exec:\n" +
                "      apiVersion: client.authentication.k8s.io/v1alpha1\n" +
                "      args:\n" +
                "      - eks\n" +
                "      - get-token\n" +
                "      - --cluster-name\n" +
                "      - " + cluster.getName() + "\n" +
                "      - --region\n" +
                "      - " + eksRegionName + "\n" +
                "      command: aws\n" +
                "      env:\n" +
                "      - name: AWS_STS_REGIONAL_ENDPOINTS\n" +
                "        value: regional\n" +
                "      provideClusterInfo: false";
        return kubeConfig;
    }


}
