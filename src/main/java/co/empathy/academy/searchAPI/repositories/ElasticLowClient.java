package co.empathy.academy.searchAPI.repositories;

import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public interface ElasticLowClient {
    String getClusterName() throws IOException;
}
