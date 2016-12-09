package org.throwable.impl;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.throwable.ActivitiApi;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * @author zhangjinci
 * @version 2016/12/8 11:44
 * @function
 */
@Service
public class ActivitiApiImpl implements ActivitiApi {

    @Autowired
    private RepositoryService repositoryService;


    @Override
    public Deployment deployProcessDefinition(String fileLocation) {
        return repositoryService.createDeployment().addClasspathResource(fileLocation).deploy();
    }

    @Override
    public Deployment deployProcessDefinition(InputStream in, boolean isZipFile, String resourceName) {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        if (isZipFile) {
            deploymentBuilder.addZipInputStream((ZipInputStream) in);
        } else {
            deploymentBuilder.addInputStream(resourceName, in);
        }
        return deploymentBuilder.deploy();
    }
}
