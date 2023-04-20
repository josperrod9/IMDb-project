package co.empathy.academy.IMDb.controllers;

import co.empathy.academy.IMDb.models.Movie;
import co.empathy.academy.IMDb.services.ElasticService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/index")
@RequiredArgsConstructor
public class IndexController implements IndexAPI{

    private final ElasticService elasticService;

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @PostMapping("/{indexName}/_doc")
    public ResponseEntity<HttpStatus> indexDoc(@PathVariable String indexName, @RequestBody Movie movie) {
        boolean created = elasticService.indexDoc(indexName, movie);
        if (created)
            return new ResponseEntity<>(HttpStatus.CREATED);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/{indexName}")
    public ResponseEntity<HttpStatus> deleteIndex(@PathVariable String indexName) {
        if(elasticService.deleteIndex(indexName))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> indexDoc(@RequestParam("basics") MultipartFile basicsFile,
                                           @RequestParam("ratings") MultipartFile ratingsFile,
                                           @RequestParam("akas") MultipartFile akasFile,
                                           @RequestParam("crew") MultipartFile crewFile,
                                           @RequestParam("principals") MultipartFile principalsFile) {

        try {
            LOGGER.info("Files received");
            elasticService.indexIMDbData(basicsFile, ratingsFile, akasFile, crewFile, principalsFile);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (IOException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/status/{taskName}")
    public ResponseEntity<String> indexStatus(@PathVariable String taskName) throws ExecutionException, InterruptedException {
        String task = elasticService.indexStatus(taskName);
        return new ResponseEntity<>(task, HttpStatus.ACCEPTED);
    }
}
