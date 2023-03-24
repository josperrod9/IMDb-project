package co.empathy.academy.searchAPI.services;

import co.empathy.academy.searchAPI.models.Movie;
import co.empathy.academy.searchAPI.repositories.ElasticEngine;
import co.empathy.academy.searchAPI.utils.IMDbData;
import co.empathy.academy.searchAPI.utils.IMDbReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ElasticServiceImpl implements ElasticService{

    private final ElasticEngine elasticEngine;

    //number of movies that will be indexed together
    int blockMovies = 50000;

    private IMDbReader imdb;
    public IMDbData data;

    @Autowired
    public ElasticServiceImpl(ElasticEngine searchEngine) {
        this.elasticEngine = searchEngine;
        this.data= new IMDbData();
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
    public void indexIMDBData(MultipartFile basicsFile, MultipartFile ratingFile, MultipartFile akasFile, MultipartFile crewFile, MultipartFile principalsFile) throws IOException {

        try {
            //checks that there are not empty files
            if (basicsFile.isEmpty()||ratingFile.isEmpty()||akasFile.isEmpty()||crewFile.isEmpty()||principalsFile.isEmpty())
                throw new IOException();

            imdb = new IMDbReader(basicsFile, ratingFile, akasFile, crewFile, principalsFile);
            //starts reading the first lines
            imdb.initializeLines();
            //create imdb index
            String imdbIndex = "imdb";
            elasticEngine.createIndex(imdbIndex);


            List<Movie> movieList = new ArrayList<>();
            Movie movie;
            int countMovies = 0;

            while (imdb.moreLines) {
                movie = imdb.readMovie();

                if (movie != null) {
                    //add the movie to the list
                    data.moviesList(movieList, movie);
                    countMovies++;
                }
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

        }
        catch (IOException e){
            throw e;
        }
    }

}
