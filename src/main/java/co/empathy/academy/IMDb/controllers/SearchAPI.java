package co.empathy.academy.IMDb.controllers;


import co.empathy.academy.IMDb.models.Movie;
import co.empathy.academy.IMDb.models.facets.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Tag(name = "search", description = "the search API")
public interface SearchAPI {

    @Operation(summary = "Get movies by a basic filters query")
    @Parameter(name = "title", description = "Title of the movie")
    @Parameter(name = "genres", description = "Genres of the movie. Can be multiple ones, separated by commas. It matches exactly")
    @Parameter(name = "type", description = "Type of the movie. Can be movie or tvMovie, separated by commas. It matches exactly")
    @Parameter(name = "maxYear", description = "Maximum year of the movie")
    @Parameter(name = "minYear", description = "Minimum year of the movie")
    @Parameter(name = "maxMinutes", description = "Maximum runtime minutes of the movie")
    @Parameter(name = "minMinutes", description = "Minimum runtime minutes of the movie")
    @Parameter(name = "maxScore", description = "Maximum average rating of the movie")
    @Parameter(name = "minScore", description = "Minimum average rating of the movie")
    @Parameter(name = "maxNHits", description = "Maximum number of hits to return")
    @Parameter(name = "sortOrder", description = "asc or desc")
    @Parameter(name = "sortBy", description = "Sort by field. Can be 'primaryTitle', 'startYear', 'runtimeMinutes' or 'averageRating'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of movies found, it can be empty"),
            @ApiResponse(responseCode = "500", description = "Error searching the movies")
    })
    ResponseEntity<List<Movie>> allFiltersSearch(@RequestParam Optional<String> title,
                                                 @RequestParam Optional<String> genres,
                                                 @RequestParam Optional<String> type,
                                                 @RequestParam Optional<Integer> maxYear,
                                                 @RequestParam(defaultValue = "0") Optional<Integer> minYear,
                                                 @RequestParam Optional<Integer> maxMinutes,
                                                 @RequestParam (defaultValue = "0") Optional<Integer> minMinutes,
                                                 @RequestParam(defaultValue = "0.0") Optional<Double> minScore,
                                                 @RequestParam(defaultValue = "10.0") Optional<Double> maxScore,
                                                 @RequestParam(defaultValue = "100") Optional<Integer> maxNHits,
                                                 @RequestParam Optional<String> sortOrder,
                                                 @RequestParam Optional<String> sortBy);

    @Operation(summary = "Get all genres")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of genres found, it can be empty"),
            @ApiResponse(responseCode = "500", description = "Error searching the genres")
    })
    ResponseEntity<Facet> getGenres();

    @Operation(summary = "Get all regions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of regions found, it can be empty"),
            @ApiResponse(responseCode = "500", description = "Error searching the regions")
    })
    ResponseEntity<Facet> getRegions();
}
