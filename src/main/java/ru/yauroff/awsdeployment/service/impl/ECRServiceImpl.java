package ru.yauroff.awsdeployment.service.impl;

import com.amazonaws.services.ecr.AmazonECR;
import com.amazonaws.services.ecr.model.CreateRepositoryRequest;
import com.amazonaws.services.ecr.model.CreateRepositoryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yauroff.awsdeployment.service.ECRService;

@Service
@Slf4j
public class ECRServiceImpl implements ECRService {
    private final AmazonECR amazonECR;

    public ECRServiceImpl(AmazonECR amazonECR) {
        this.amazonECR = amazonECR;
    }

    @Override
    public String createPrivateRepository(String repositoryName) {
        CreateRepositoryResult createRepositoryResult = amazonECR.createRepository(new CreateRepositoryRequest()
                .withRepositoryName(repositoryName));
        String repositoryURI = createRepositoryResult.getRepository()
                                                     .getRepositoryUri();
        return repositoryURI;
    }
}
