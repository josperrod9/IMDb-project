package co.empathy.academy.IMDb.repositories;

import co.empathy.academy.IMDb.models.Movie;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public interface ElasticEngine {
    /**
     *
     * @param name, the index name
     * @return true if the index was successfully created
     */
    Boolean createIndex(String name);
    /**
     *
     * @param name, the index name that will be deleted
     * @return true is the index was successfully deleted
     */
    Boolean deleteIndex(String name);
    /**
     *
     * @param indexName, the index name
     * @param movie, the movie that will be added to the index
     * @return true if it was successfully added
     */
    Boolean indexDoc(String indexName, Movie movie);
    /**
     * Sends multiple docs in one request
     *
     * @param indexName, the index name
     * @param movies,    a list with movies that will be created or replaced if they exist
     * @return
     * @throws IOException
     */
    Boolean indexMultipleDocs(String indexName, List<Movie> movies) throws IOException;
}
