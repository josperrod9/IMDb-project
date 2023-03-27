package co.empathy.academy.searchAPI.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.empathy.academy.searchAPI.models.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ElasticEngineImpl implements ElasticEngine{

    private final ElasticsearchClient client;

    @Autowired
    public ElasticEngineImpl(ElasticsearchClient elasticClient) {
        this.client = elasticClient;
    }

    @Override
    public Boolean createIndex(String name) {

        try {
            if (name==null) {
                return false;
            }
            CreateIndexResponse createIndexResponse = client.indices().create(c -> c.index(name));
            return createIndexResponse.acknowledged();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    @Override
    public Boolean deleteIndex(String indexName) {
        try {

            DeleteIndexResponse deleteIndexResponse = client.indices().delete(c -> c.index(indexName));
            if (deleteIndexResponse.acknowledged()){
                System.out.println("Deleted");
                return true;
            }
            else
                return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    @Override
    public Boolean indexDoc(String indexName, Movie movie) {
        try {

            GetResponse<Movie> existsResp = client.get(g -> g
                            .index(indexName)
                            .id(movie.getTconst()),
                    Movie.class
            );
            //checks if the movie's id already exists
            if (existsResp.found()) {
                return false;
            } else {
                IndexResponse response = client.index(i -> i
                        .index(indexName)
                        .id(movie.getTconst())
                        .document(movie)
                );
                System.out.println("Indexed with version " + response.version());
                return true;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override

    public Boolean indexMultipleDocs(String indexName, List<Movie> movies) {
        boolean response=false;
        if (!movies.isEmpty()) {
            try {
                BulkRequest.Builder br = new BulkRequest.Builder();

                for (Movie movie : movies) {
                    br.operations(op -> op
                            .index(idx -> idx
                                    .index(indexName)
                                    .id(movie.getTconst())
                                    .document(movie)
                            )
                    );
                }

                BulkResponse result = client.bulk(br.build());


                if (result.errors()) {
                    System.out.println("Bulk error indexing multiple docs");

                } else response=true;
            } catch (IOException e) {

                throw new RuntimeException(e);
            }
        }
        return response;
    }
}
