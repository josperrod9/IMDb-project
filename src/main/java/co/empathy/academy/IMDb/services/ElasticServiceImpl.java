package co.empathy.academy.IMDb.services;

import co.empathy.academy.IMDb.models.Movie;
import co.empathy.academy.IMDb.repositories.ElasticEngine;
import co.empathy.academy.IMDb.utils.IMDbData;
import co.empathy.academy.IMDb.utils.IMDbReader2;
import co.empathy.academy.IMDb.utils.IMDbReader3;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ElasticServiceImpl implements ElasticService{

    private final ElasticEngine elasticEngine;

    private final IMDbData data = new IMDbData();

    private final Map<String, String> taskMap = new ConcurrentHashMap<>();

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
        taskMap.clear();
        taskMap.put("indexing", "started");
        try {
            //checks that there are not empty files
            if (basicsFile.isEmpty()||ratingFile.isEmpty()||akasFile.isEmpty()||crewFile.isEmpty()||principalsFile.isEmpty())
                throw new IOException();

            IMDbReader2 imdbReader = new IMDbReader2(basicsFile, ratingFile, akasFile, crewFile, principalsFile);
            String imdbIndex = "imdb";
            //starts reading the first lines
            //create imdb index
            elasticEngine.createIndex(imdbIndex);
            elasticEngine.putSettings(imdbIndex);
            elasticEngine.putMapping(imdbIndex);
            taskMap.put("indexing", "indexing");
            while (imdbReader.hasDocuments()){
                List<Movie> movies = imdbReader.readDocuments();
                elasticEngine.indexMultipleDocs(imdbIndex, movies);
            }
            taskMap.put("indexing", "finished");
            LOGGER.info("Indexing finished");
        }
        catch (IOException e){
            throw e;
        }
    }

    @Override
    public String indexStatus(String taskName) {
        return taskMap.get(taskName);
    }

}
