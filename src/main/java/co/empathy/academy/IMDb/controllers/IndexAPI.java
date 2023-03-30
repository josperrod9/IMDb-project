package co.empathy.academy.IMDb.controllers;

import co.empathy.academy.IMDb.models.Movie;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "index", description = "the index API")
public interface IndexAPI {

    @Operation(summary = "Index a single movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully indexed"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Something went wrong indexing the doc")})
    ResponseEntity<HttpStatus> indexDoc(@PathVariable String indexName, @RequestBody Movie movie) throws IOException;

    @Operation(summary = "Get a single movie from an index")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Something went wrong while retrieving")})
    public ResponseEntity<Movie> getDocFromIndex(@PathVariable String indexName) throws IOException;

    @Operation(summary = "Delete an indices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Something went wrong while deleting")})

    public ResponseEntity<HttpStatus> deleteIndex(@PathVariable String indexName) throws IOException;

    @Operation(summary = "Creates a new 'IMDb' index")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully indexed"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Something went wrong while indexing")})
    public ResponseEntity<String> indexDoc(@RequestParam("basics") MultipartFile basicsFile,
                                           @RequestParam("ratings") MultipartFile ratingsFile,
                                           @RequestParam("akas") MultipartFile akasFile,
                                           @RequestParam("crew") MultipartFile crewFile,
                                           @RequestParam("principals") MultipartFile principalsFile

    );
}
