package co.empathy.academy.searchAPI.repositories;

import co.empathy.academy.searchAPI.configuration.ElasticSearchConfig;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ElasticLowClientImpl implements ElasticLowClient {

    private final ElasticSearchConfig searchConfig;

    public ElasticLowClientImpl(ElasticSearchConfig searchConfig) {
        this.searchConfig = searchConfig;
    }

    /**
     * Makes a petition to elastic search that calls to HealthResponse class which contains cluster name
     *
     * @return elasticsearch cluster name
     */
    @Override
    public String getClusterName() throws IOException {
        return searchConfig.getElasticSearchClient().cluster().health().clusterName();
    }
}
