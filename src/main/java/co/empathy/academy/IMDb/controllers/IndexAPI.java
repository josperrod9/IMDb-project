package co.empathy.academy.IMDb.controllers;

import co.empathy.academy.IMDb.models.Movie;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(summary = "Index a single movie", tags = { "index" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully indexed",content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request",content = @Content),
            @ApiResponse(responseCode = "500", description = "Something went wrong indexing the doc",content = @Content)})
    ResponseEntity<HttpStatus> indexDoc(@PathVariable String indexName, @RequestBody Movie movie) throws IOException;

    @Operation(summary = "Get a single movie from an index", tags = { "index" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",content = @Content(schema = @Schema(implementation =Movie.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",content = @Content),
            @ApiResponse(responseCode = "500", description = "Something went wrong while retrieving",content = @Content)})
    public ResponseEntity<Movie> getDocFromIndex(@PathVariable String indexName) throws IOException;

    @Operation(summary = "Delete an indices", tags = { "index" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted",content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request",content = @Content),
            @ApiResponse(responseCode = "500", description = "Something went wrong while deleting",content = @Content)})

    public ResponseEntity<HttpStatus> deleteIndex(@PathVariable String indexName) throws IOException;

    @Operation(summary = "Creates a new 'IMDb' index", tags = { "index" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully indexed",content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request",content = @Content),
            @ApiResponse(responseCode = "500", description = "Something went wrong while indexing",content = @Content)})
    public ResponseEntity<String> indexDoc(@RequestParam("basics") MultipartFile basicsFile,
                                           @RequestParam("ratings") MultipartFile ratingsFile,
                                           @RequestParam("akas") MultipartFile akasFile,
                                           @RequestParam("crew") MultipartFile crewFile,
                                           @RequestParam("principals") MultipartFile principalsFile

    );
}
