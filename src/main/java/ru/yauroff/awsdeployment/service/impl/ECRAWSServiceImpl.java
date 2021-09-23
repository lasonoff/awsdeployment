package ru.yauroff.awsdeployment.service.impl;

import com.amazonaws.services.ecr.AmazonECR;
import com.amazonaws.services.ecr.model.CreateRepositoryRequest;
import com.amazonaws.services.ecr.model.CreateRepositoryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yauroff.awsdeployment.service.ECRAWSService;

@Service
@Slf4j
public class ECRAWSServiceImpl implements ECRAWSService {
    @Autowired
    private AmazonECR amazonECR;

    @Override
    public void createPrivateRepository(String repositoryName) {
        CreateRepositoryResult createRepositoryResult = amazonECR.createRepository(new CreateRepositoryRequest()
                .withRepositoryName(repositoryName));
    }
}
