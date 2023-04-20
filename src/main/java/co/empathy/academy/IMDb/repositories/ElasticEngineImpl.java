package co.empathy.academy.IMDb.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.empathy.academy.IMDb.models.Movie;
import co.empathy.academy.IMDb.models.facets.Facet;
import co.empathy.academy.IMDb.models.facets.FacetValue;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ElasticEngineImpl implements ElasticEngine{

    private final ElasticsearchClient client;

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticEngineImpl.class);

    @Override
    public Movie getDocFromIndex(String name) {

        try {
            if (name==null) {
                throw new RuntimeException("Index name is null");
            }
            GetResponse<Movie> response = client.get(g -> g
                            .index(name)
                            .id("tt0000001"),
                    Movie.class
            );
            Movie movie = response.source();
            if (movie != null) {
                LOGGER.info("Movie title " + movie.getPrimaryTitle());
            }
            return movie;
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean createIndex(String name) {

        try {
            if (name==null) {
                return false;
            }
            CreateIndexResponse createIndexResponse = client.indices().create(c -> c.index(name));
            LOGGER.info("Index with name: "+name+" has been created");
            return createIndexResponse.acknowledged();
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return false;
        }
    }

    /**
     * Puts the settings of the index
     *
     * @throws IOException - If the settings cannot be loaded
     */
    @Override
    public void putSettings(String name) throws IOException {
        client.indices().close(c -> c.index(name));

        InputStream analyzer = getClass().getClassLoader().getResourceAsStream("custom_analyzer.json");
        client.indices().putSettings(p -> p.index(name).withJson(analyzer));

        client.indices().open(o -> o.index(name));
        LOGGER.info("Index with name: "+name+" has been setted");
    }

    /**
     * Puts the mapping of the index
     *
     * @throws IOException If the mapping cannot be loaded
     */
    @Override
    public void putMapping(String name) throws IOException {
        InputStream mapping = getClass().getClassLoader().getResourceAsStream("mapping.json");
        client.indices().putMapping(p -> p.index(name).withJson(mapping));
        LOGGER.info("Index with name: "+name+" has been mapped");
    }



    @Override
    public Boolean deleteIndex(String indexName) {
        try {
            DeleteIndexResponse deleteIndexResponse = client.indices().delete(c -> c.index(indexName));
            if (deleteIndexResponse.acknowledged()){
                LOGGER.info("Deleted");
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
                        .document(movie)
                );
                LOGGER.info("Indexed with version " + response.version());
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
                movies.forEach(movie -> {
                    br.operations(op -> op
                            .index(idx -> idx
                                    .index(indexName)
                                    .document(movie)
                            )
                    );
                });
                BulkResponse result = client.bulk(br.build());
                LOGGER.info("Indexing multiple docs");

                if (result.errors()) {
                    LOGGER.info("Bulk error indexing multiple docs");

                } else response=true;
            } catch (IOException e) {

                throw new RuntimeException(e);
            }
        }
        return response;
    }

    /**
     * Performs a query to elasticsearch
     *
     * @param query       Query to make
     * @param maxNHits    Maximum number of hits to return
     * @param sortOptions Sort options
     * @return List of movies that match the query
     */
    @Override
    public List<Movie> performQuery(Query query, Integer maxNHits, List<SortOptions> sortOptions) throws IOException {
        SearchResponse<Movie> response = client.search(s -> s
                .index("imdb")
                .query(query)
                .sort(sortOptions)
                .size(maxNHits), Movie.class);

        return response.hits().hits().stream()
                .map(Hit::source)
                .toList();

    }

    /**
     * Returns a list of genres
     *
     * @return List of genres
     * @throws IOException If the query fails
     */
    @Override
    public Facet getGenres() throws IOException {
        Query query = BoolQuery.of(b -> b
                .filter(MatchAllQuery.of(q -> q.queryName("MatchAll"))._toQuery()))._toQuery();

        SearchResponse<Void> response = client.search(b -> b
                .index("imdb")
                .size(0)
                .query(query)
                .aggregations("genres", a-> a.terms(m->m.field("genres"))), Void.class);

        List<FacetValue> values = new ArrayList<>();
        Aggregate genresAgg = response.aggregations().get("genres");
        genresAgg.sterms().buckets().array().forEach(bucket -> {
            values.add(new FacetValue(bucket.key(), bucket.key(),
                    bucket.docCount(), "genres:" + bucket.key()));
        });
        return new Facet("facetGenres", "values", values);
    }

    /**
     * Returns a list of countries
     *
     * @return List of countries
     * @throws IOException If the query fails
     *
     */

    @Override
    public Facet getRegions() throws IOException {
        Query query = BoolQuery.of(b -> b
                .filter(MatchAllQuery.of(q -> q.queryName("MatchAll"))._toQuery()))._toQuery();

        SearchResponse<Void> response = client.search(b -> b
                .index("imdb")
                .size(100)
                .query(query)
                .aggregations("nested", a-> a.nested(n->n.path("akas"))
                        .aggregations("regions",t->t.terms(m->m.field("akas.region")))), Void.class);

        List<FacetValue> values = new ArrayList<>();
        NestedAggregate regionsAgg = (NestedAggregate) response.aggregations().get("nested")._get();
        regionsAgg.aggregations().get("regions").sterms().buckets().array().forEach(bucket -> {
            values.add(new FacetValue(bucket.key(), bucket.key(),
                    bucket.docCount(), "region:" + bucket.key()));
        });
        return new Facet("facetRegions", "values", values);
    }
}
