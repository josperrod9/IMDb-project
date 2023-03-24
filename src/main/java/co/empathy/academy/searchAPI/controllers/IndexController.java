package co.empathy.academy.searchAPI.controllers;

import co.empathy.academy.searchAPI.models.Movie;
import co.empathy.academy.searchAPI.services.ElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/index")
public class IndexController implements IndexAPI{

    private final ElasticService elasticService;

    @Autowired
    public IndexController(ElasticService elasticService) {
        this.elasticService = elasticService;
    }

    @PostMapping("/{indexName}/_doc")
    public ResponseEntity<HttpStatus> indexDoc(@PathVariable String indexName, @RequestBody Movie movie) throws IOException {
        boolean created = elasticService.indexDoc(indexName, movie);
        if (created)
            return new ResponseEntity<>(HttpStatus.CREATED);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{indexName}")
    public ResponseEntity<HttpStatus> deleteIndex(@PathVariable String indexName) throws IOException {
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
            elasticService.indexIMDBData(basicsFile, ratingsFile, akasFile, crewFile, principalsFile);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (IOException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
