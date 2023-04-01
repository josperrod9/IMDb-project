package co.empathy.academy.IMDb.services;

import co.empathy.academy.IMDb.models.Movie;
import co.empathy.academy.IMDb.repositories.ElasticEngine;
import co.empathy.academy.IMDb.utils.IMDbData;
import co.empathy.academy.IMDb.utils.IMDbReader;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ElasticServiceImpl implements ElasticService{

    private final ElasticEngine elasticEngine;

    private final IMDbData data = new IMDbData();

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticServiceImpl.class);

    @Override
    public Movie getDocFromIndex(String name) {
        return elasticEngine.getDocFromIndex(name);
    }

    @Override
    public Boolean deleteIndex(String name) {
        return elasticEngine.deleteIndex(name);
    }

    @Override
    public Boolean indexDoc(String indexName, Movie movie) {
        return elasticEngine.indexDoc(indexName, movie);
    }


    @Override
    public void indexIMDbData(MultipartFile basicsFile, MultipartFile ratingFile, MultipartFile akasFile, MultipartFile crewFile, MultipartFile principalsFile) throws IOException {

        try {
            //checks that there are not empty files
            if (basicsFile.isEmpty()||ratingFile.isEmpty()||akasFile.isEmpty()||crewFile.isEmpty()||principalsFile.isEmpty())
                throw new IOException();

            IMDbReader imdbReader = new IMDbReader(basicsFile, ratingFile, akasFile, crewFile, principalsFile);
            int blockMovies = 50000;
            String imdbIndex = "imdb";
            //starts reading the first lines
            imdbReader.initializeLines();
            //create imdb index
            elasticEngine.createIndex(imdbIndex);
            elasticEngine.putSettings(imdbIndex);
            elasticEngine.putMapping(imdbIndex);

            List<Movie> movieList = new ArrayList<>();
            Movie movie;
            int countMovies = 0;

            while (imdbReader.moreLines) {
                movie = imdbReader.readMovie();

                if (movie != null) {
                    //add the movie to the list
                    data.moviesList(movieList, movie);
                    countMovies++;
                }
                //number of movies that will be indexed together
                if (countMovies == blockMovies) {
                    //index a "small" movie's list
                    elasticEngine.indexMultipleDocs(imdbIndex, movieList);

                    //prepare the next list
                    countMovies = 0;
                    movieList.clear();
                }
            }
            //index the last list if is not empty
            elasticEngine.indexMultipleDocs(imdbIndex, movieList);
            LOGGER.info("Indexing finished");
        }
        catch (IOException e){
            throw e;
        }
    }

}
