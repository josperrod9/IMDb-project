package co.empathy.academy.IMDb.controllers;

import co.empathy.academy.IMDb.models.Movie;
import co.empathy.academy.IMDb.models.facets.Facet;
import co.empathy.academy.IMDb.models.facets.FacetValue;
import co.empathy.academy.IMDb.services.SearchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SearchControllerTest {

    private SearchController searchController;

    @Mock
    private SearchServiceImpl searchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        searchController = new SearchController(searchService);
    }

    @Test
    public void testAllFiltersSearch() throws IOException {
        Optional<String> title = Optional.of("The Godfather");
        Optional<String> genres = Optional.of("Drama");
        Optional<String> type = Optional.of("movie");
        Optional<Integer> maxYear = Optional.of(1975);
        Optional<Integer> minYear = Optional.of(1970);
        Optional<Integer> maxMinutes = Optional.of(200);
        Optional<Integer> minMinutes = Optional.of(100);
        Optional<Double> minScore = Optional.of(8.0);
        Optional<Double> maxScore = Optional.of(10.0);
        Optional<Integer> maxNHits = Optional.of(100);
        Optional<String> sortOrder = Optional.of("asc");
        Optional<String> sortBy = Optional.of("title");
        Optional<String> region = Optional.of("USA");

        List<Movie> expectedMovies = new ArrayList<>();
        expectedMovies.add(new Movie());

        when(searchService.allFiltersSearch(title, genres, type, maxYear, minYear, maxMinutes, minMinutes, maxScore, minScore, maxNHits, sortOrder, sortBy, region)).thenReturn(expectedMovies);

        ResponseEntity<List<Movie>> response = searchController.allFiltersSearch(title, genres, type, maxYear, minYear, maxMinutes, minMinutes, maxScore, minScore, maxNHits, sortOrder, sortBy, region);
        System.out.println(response.toString());
        assertEquals(expectedMovies, response.getBody());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, Objects.requireNonNull(response.getHeaders().getContentType()).toString());
    }

    @Test
    public void testGetGenres() throws IOException {
        FacetValue facetValue1 = new FacetValue("idValue", "facetValue", 10L, "filterValue");
        FacetValue facetValue2 = new FacetValue("idValue", "facetValue", 10L, "filterValue");
        List<FacetValue> values = new ArrayList<>();
        values.add(facetValue1);
        values.add(facetValue2);

        Facet expectedFacet = new Facet("facetName", "facetType", values);

        when(searchService.getGenres()).thenReturn(expectedFacet);

        ResponseEntity<Facet> response = searchController.getGenres();

        assertEquals(expectedFacet, response.getBody());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeaders().getContentType().toString());
    }

    @Test
    public void testGetRegions() throws IOException {
        FacetValue facetValue1 = new FacetValue("idValue", "facetValue", 10L, "filterValue");
        FacetValue facetValue2 = new FacetValue("idValue", "facetValue", 10L, "filterValue");
        List<FacetValue> values = new ArrayList<>();
        values.add(facetValue1);
        values.add(facetValue2);

        Facet expectedFacet = new Facet("facetName", "facetType", values);

        when(searchService.getRegions()).thenReturn(expectedFacet);

        ResponseEntity<Facet> response = searchController.getRegions();

        assertEquals(expectedFacet, response.getBody());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeaders().getContentType().toString());
    }

    @Test
    public void testGetRecentSearches() {
        List<String> expectedTitles = new ArrayList<>();
        expectedTitles.add("The Godfather");

        when(searchService.getRecentTitles()).thenReturn(expectedTitles);

        ResponseEntity<List<String>> response = searchController.getRecentSearches();

        assertEquals(expectedTitles, response.getBody());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeaders().getContentType().toString());
    }
}
