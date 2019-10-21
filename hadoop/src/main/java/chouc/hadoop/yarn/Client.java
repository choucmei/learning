package chouc.hadoop.yarn;

import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse;
import org.apache.hadoop.yarn.api.records.*;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.Records;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chouc
 * @version V1.0
 * @Title: Client
 * @Package chouc.hadoop.yarn
 * @Description: 通过YarnClient 提交一个空格yarn 任务
 * @date 9/16/19
 */
public class Client {
    public static void main(String[] args) throws IOException, YarnException {
        YarnClient yarnClient = YarnClient.createYarnClient();
        yarnClient.init(new YarnConfiguration());
        yarnClient.start();
        YarnClientApplication application = yarnClient.createApplication();

        GetNewApplicationResponse getNewApplicationResponse = application.getNewApplicationResponse();

        ApplicationId id = getNewApplicationResponse.getApplicationId();
        ContainerLaunchContext amContainer = Records.newRecord(ContainerLaunchContext.class);
        Map<String, LocalResource> resource = new HashMap<>();
        amContainer.setLocalResources(resource);
        ApplicationSubmissionContext appContext = application.getApplicationSubmissionContext();
        appContext.setApplicationName("user define");
        appContext.setQueue("default");
        appContext.setAMContainerSpec(amContainer);
        appContext.setApplicationType("SPARK");
        appContext.setUnmanagedAM(false);

        ResourceRequest amRequest = Records.newRecord(ResourceRequest.class);
        amRequest.setResourceName(ResourceRequest.ANY);
        amRequest.setPriority(Priority.newInstance(0));
        Resource capability = Records.newRecord(Resource.class);
        capability.setVirtualCores(1);
        amRequest.setCapability(capability);
        amRequest.setNumContainers(1);
        appContext.setAMContainerResourceRequest(amRequest);
        yarnClient.submitApplication(appContext);

        System.out.println(id.getId());
    }
}
