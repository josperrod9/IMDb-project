package co.empathy.academy.searchAPI.services;

import co.empathy.academy.searchAPI.models.QueryResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface SearchService {
    QueryResponse search(String query) throws IOException;
}
