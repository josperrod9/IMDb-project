package co.empathy.academy.IMDb.controllers;

import co.empathy.academy.IMDb.models.Movie;
import co.empathy.academy.IMDb.services.ElasticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IndexControllerTest {

    private IndexController indexController;

    @Mock
    private ElasticService elasticService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        indexController = new IndexController(elasticService);
    }

    @Test
    public void testIndexDoc() {
        Movie movie = new Movie();
        when(elasticService.indexDoc("testIndex", movie)).thenReturn(true);
        ResponseEntity<HttpStatus> response = indexController.indexDoc("testIndex", movie);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testDeleteIndex() {
        when(elasticService.deleteIndex("testIndex")).thenReturn(true);
        ResponseEntity<HttpStatus> response = indexController.deleteIndex("testIndex");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testIndexDocs() throws IOException {
        // create mock ElasticService object
        ElasticService elasticService = mock(ElasticService.class);

        // create mock files
        MultipartFile basicsFile = new MockMultipartFile("basics.tsv", new FileInputStream(new File("src/test/java/co/empathy/academy/IMDb/data/title.basics.tsv")));
        MultipartFile principalsFile = new MockMultipartFile("principals.tsv", new FileInputStream(new File("src/test/java/co/empathy/academy/IMDb/data/title.principals.tsv")));
        MultipartFile ratingsFile = new MockMultipartFile("ratings.tsv", new FileInputStream(new File("src/test/java/co/empathy/academy/IMDb/data/title.ratings.tsv")));
        MultipartFile akasFile = new MockMultipartFile("akas.tsv", new FileInputStream(new File("src/test/java/co/empathy/academy/IMDb/data/title.akas.tsv")));
        MultipartFile crewFile = new MockMultipartFile("crew.tsv", new FileInputStream(new File("src/test/java/co/empathy/academy/IMDb/data/title.crew.tsv")));
        Mockito.doNothing().when(elasticService).indexIMDbData(basicsFile, ratingsFile, akasFile, crewFile, principalsFile);        // create IndexController object and call indexDoc method
        IndexController indexController = new IndexController(elasticService);
        ResponseEntity<String> response = indexController.indexDoc(basicsFile, ratingsFile, akasFile, crewFile, principalsFile);

        // assert that the response is as expected
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

}
