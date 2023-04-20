package co.empathy.academy.IMDb.controllers;

import co.empathy.academy.IMDb.models.Movie;
import co.empathy.academy.IMDb.models.facets.Facet;
import co.empathy.academy.IMDb.services.SearchServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController implements SearchAPI{

    private final SearchServiceImpl searchService;

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> allFiltersSearch(@RequestParam Optional<String> title,
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
                                                        @RequestParam Optional<String> sortBy,
                                                        @RequestParam Optional<String> region) {
        try {
            List<Movie> movies = searchService.allFiltersSearch(title, genres, type, maxYear, minYear,
                    maxMinutes, minMinutes, maxScore, minScore, maxNHits, sortOrder, sortBy,region);
            return ResponseEntity.ok(movies);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/genres")
    public ResponseEntity<Facet> getGenres() {
        try {
            Facet genres = searchService.getGenres();
            return ResponseEntity.ok(genres);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/regions")
    public ResponseEntity<Facet> getRegions() {
        try {
            Facet regions = searchService.getRegions();
            return ResponseEntity.ok(regions);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/recent")
    public ResponseEntity<List<String>> getRecentSearches() {
        List<String> titles = searchService.getRecentTitles();
        return ResponseEntity.ok(titles);
    }

}
