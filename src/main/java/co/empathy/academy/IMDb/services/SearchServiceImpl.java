package co.empathy.academy.IMDb.services;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.empathy.academy.IMDb.models.Movie;
import co.empathy.academy.IMDb.models.facets.Facet;
import co.empathy.academy.IMDb.repositories.ElasticEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService{

    private final ElasticEngine elasticEngine;

    private final QueriesService queriesService;

    private final Deque<String> recentTitles = new ArrayDeque<>();

    /**
     * Performs a search with all the filters
     *
     * @param title Title to search
     * @param genres     Genres to search
     * @param type      Types to search
     * @param maxYear    Maximum year to search
     * @param minYear    Minimum year to search
     * @param maxMinutes Maximum runtime minutes
     * @param minMinutes Minimum runtime minutes
     * @param maxScore   Maximum average rating
     * @param minScore   Minimum average rating
     * @param maxNHits   Maximum number of hits
     * @param sortOrder Sort by order (asc: ascending, desc: descending)
     * @param sortBy    Sort by field (primaryTitle, startYear, runtimeMinutes, averageRating)
     * @param region    Region to search
     * @return List of movies that match the filters
     * @throws IOException If the query fails
     */
    @Override
    public List<Movie> allFiltersSearch(Optional<String> title, Optional<String> genres,
                                        Optional<String> type, Optional<Integer> maxYear,
                                        Optional<Integer> minYear, Optional<Integer> maxMinutes,
                                        Optional<Integer> minMinutes, Optional<Double> maxScore,
                                        Optional<Double> minScore, Optional<Integer> maxNHits,
                                        Optional<String> sortOrder, Optional<String> sortBy,
                                        Optional<String> region) throws IOException {

        Deque<Query> filters = new ArrayDeque<>();

        title.ifPresent(s -> {
            filters.add(queriesService.multiMatchQuery(s, "primaryTitle"));
            recentTitles.addFirst(title.get());
        });

        if (genres.isPresent()) {
            String[] genresArray = genres.get().split(",");
            List<Query> genreQueries = queriesService.termQueries(genresArray, "genres");
            filters.add(queriesService.shouldQuery(genreQueries));
        }

        if (type.isPresent()) {
            String[] typesArray = type.get().split(",");
            List<Query> typeQueries = queriesService.termQueries(typesArray, "titleType");
            filters.add(queriesService.shouldQuery(typeQueries));
        }

        if (maxYear.isPresent() || minYear.isPresent()) {
            filters.add(queriesService.rangeIntegerQuery("startYear", minYear.orElse(0),
                    maxYear.orElse(Integer.MAX_VALUE)));
        }

        if (maxMinutes.isPresent() || minMinutes.isPresent()) {
            filters.add(queriesService.rangeIntegerQuery("runtimeMinutes", minMinutes.orElse(0),
                    maxMinutes.orElse(Integer.MAX_VALUE)));
        }

        if (maxScore.isPresent() || minScore.isPresent()) {
            filters.add(queriesService.rangeDoubleQuery("averageRating", minScore.orElse(0.0),
                    maxScore.orElse(Double.MAX_VALUE)));
        }
        if (region.isPresent()) {
            filters.add(queriesService.nestedQuery("akas", region.get()));
            title.ifPresent(s ->
                    {
                        filters.removeFirst();
                        filters.addFirst(queriesService.nestedPrefixQuery("akas", s, region.get()));
                    });
        }
        List<SortOptions> sortOptions = new ArrayList<>() {{
            if (sortBy.isPresent() && sortOrder.isPresent()) {
                if (sortBy.get().equals("averageRating")) {
                    add(queriesService.sort("averageRating", sortOrder.get()));
                    add(queriesService.sort("numVotes", sortOrder.get()));

                }else {
                    add(queriesService.sort(sortBy.get(), sortOrder.get()));
                }
            }
        }};

        Query query = queriesService.mustQuery(filters.stream().toList());

        return elasticEngine.performQuery(queriesService.functionScoreQuery(query), maxNHits.orElse(100), sortOptions);
    }

    /**
     * Returns a list of genres
     *
     * @return List of genres
     * @throws IOException If the query fails
     */

    @Override
    public Facet getGenres() throws IOException {
        return elasticEngine.getGenres();
    }

    /**
     * Returns a list of regions
     *
     * @return List of regions
     * @throws IOException If the query fails
     */

    @Override
    public Facet getRegions() throws IOException {
        return elasticEngine.getRegions();
    }

    @Override
    public List<String> getRecentTitles() {
        if (recentTitles.size() > 6)
            recentTitles.removeLast();
        return recentTitles.stream().toList();
    }
}
