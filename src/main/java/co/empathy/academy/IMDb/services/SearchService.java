package co.empathy.academy.IMDb.services;


import co.empathy.academy.IMDb.models.Movie;
import co.empathy.academy.IMDb.models.facets.Facet;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public interface SearchService {

    List<Movie> allFiltersSearch(Optional<String> title, Optional<String> genres,
                                 Optional<String> type, Optional<Integer> maxYear,
                                 Optional<Integer> minYear, Optional<Integer> maxMinutes,
                                 Optional<Integer> minMinutes, Optional<Double> maxScore,
                                 Optional<Double> minScore, Optional<Integer> maxNHits,
                                 Optional<String> sortOrder, Optional<String> sortBy) throws IOException;

    Facet getGenres() throws IOException;

    Facet getRegions() throws IOException;
}
