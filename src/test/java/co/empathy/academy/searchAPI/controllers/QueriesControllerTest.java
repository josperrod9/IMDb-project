package co.empathy.academy.searchAPI.controllers;

import co.empathy.academy.searchAPI.models.QueryResponse;
import co.empathy.academy.searchAPI.services.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QueriesControllerTest {

    @Mock
    private SearchService searchService;

    @InjectMocks
    QueriesController queriesController;

    @Test
    public void givenQuery_whenSearchInController_thenQueryResponse() throws IOException {
        //Setup
        String query = "Query";
        QueryResponse response = new QueryResponse(query, "clusterName");
        when(searchService.search(query)).thenReturn(response);

        //execute
        ResponseEntity<QueryResponse> result = queriesController.search(query);

        //validate
        assertEquals(response, result.getBody());
    }
}
