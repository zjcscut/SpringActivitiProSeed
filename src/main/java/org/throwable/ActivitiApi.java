package org.throwable;


import org.activiti.engine.repository.Deployment;

import java.io.InputStream;

/**
 * @author zjc
 * @version 2016/12/8 0:40
 * @description Activiti操作方法
 */

public interface ActivitiApi {

    Deployment deployProcessDefinition(String fileLocation);

    Deployment deployProcessDefinition(InputStream in,boolean isZipFile,String resourceName);
}
