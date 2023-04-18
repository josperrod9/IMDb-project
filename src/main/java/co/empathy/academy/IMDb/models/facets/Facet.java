package co.empathy.academy.IMDb.models.facets;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class Facet {

    String facet;
    String type;
    List<FacetValue> values;
}
