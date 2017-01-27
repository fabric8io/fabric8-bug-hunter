package io.fabric8.devops.bughunter.tests;

import io.fabric8.kubernetes.api.model.extensions.DeploymentList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kameshs
 */
public class K8sScrap {

    @Test
    public void testDeploymentMetdata() throws Exception {
        Config config = new ConfigBuilder().build();
        KubernetesClient kubernetesClient = new DefaultKubernetesClient();
        Map<String, String> labelMap = new HashMap<>();
        labelMap.put("group", "io.fabric8.devops.apps");
        labelMap.put("project", "simple-calculator-springboot");
        labelMap.put("version", "1.0.0-SNAPSHOT");

        DeploymentList deploymentList = kubernetesClient
            .extensions()
            .deployments()
            .inNamespace("default")
            .withLabels(labelMap)
            .list();

        deploymentList.getItems().forEach(deployment -> {
            System.out.println(deployment.getMetadata().getAnnotations());
        });
    }
}
