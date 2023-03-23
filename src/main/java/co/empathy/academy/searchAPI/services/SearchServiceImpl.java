package co.empathy.academy.searchAPI.services;

import co.empathy.academy.searchAPI.models.QueryResponse;
import co.empathy.academy.searchAPI.repositories.ElasticLowClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SearchServiceImpl implements SearchService {

    private final ElasticLowClient elasticLowClient;

    @Autowired
    public SearchServiceImpl(ElasticLowClient elasticLowClient) {
        this.elasticLowClient = elasticLowClient;
    }

    /**
     * Makes a request to obtain the cluster name and returns it and the query performed.
     *
     * @param query - query to search
     * @return QueryResponse
     */
    @Override
    public QueryResponse search(String query) throws IOException {
        String clusterName = elasticLowClient.getClusterName();
        return new QueryResponse(query, clusterName);
    }
}
