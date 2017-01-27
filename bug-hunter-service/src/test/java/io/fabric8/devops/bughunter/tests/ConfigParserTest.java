package io.fabric8.devops.bughunter.tests;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.assertj.core.api.MapAssert;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author kameshs
 */
public class ConfigParserTest {

    @Test
    public void testValidateConfigMap() throws Exception {

        InputStream yamlDoc = this.getClass().getResourceAsStream("/test-configmap.yml");
        assertThat(yamlDoc).isNotNull();
        Yaml yaml = new Yaml();
        Map configMap = (Map)yaml.load(yamlDoc);
        assertThat(configMap).isNotNull();
        assertThat(configMap).isNotEmpty();
        Map data = (Map)configMap.get("data");
        assertThat(data).isNotNull();
        assertThat(data).isNotEmpty();
        String esQuery = (String) data.get("hunting-search-query");
        assertThat(esQuery).isNotNull();
        assertThat(esQuery).isEqualToIgnoringWhitespace("kubernetes.namespace_name: default AND kubernetes.labels.group: io.fabric8" +
            " AND log: Exception");
    }
}
