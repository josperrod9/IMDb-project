package co.empathy.academy.IMDb.models.facets;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class FacetValue {
    String id;
    String value;
    Long count;
    String filter;
}
