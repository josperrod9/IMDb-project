package co.empathy.academy.searchAPI.controllers;

import co.empathy.academy.searchAPI.models.QueryResponse;
import co.empathy.academy.searchAPI.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@RestController
@RequestMapping("/search")
public class QueriesController {
    private final SearchService searchService;

    @Autowired
    public QueriesController(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * GET "/search" endpoint that returns the query and the cluster name
     *
     * @param query - query to search
     * @return QueryResponse with the query and the cluster names
     */

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QueryResponse> search(@RequestParam(name = "query", required = false, defaultValue="unknown") String query) throws IOException {
        return ResponseEntity.ok(searchService.search(query));
    }

}
