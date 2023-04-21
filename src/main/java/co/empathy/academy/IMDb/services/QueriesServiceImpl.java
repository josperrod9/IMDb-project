package co.empathy.academy.IMDb.services;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QueriesServiceImpl implements QueriesService{
    /**
     * Creates a multimatch query
     *
     * @param query  - query to search
     * @param fields - fields to search
     * @return Query
     */
    @Override
    public Query multiMatch(String query, String[] fields) {
        Query multiMatchQuery = MultiMatchQuery.of(m -> m
                .query(query)
                .fields(Arrays.stream(fields).toList()))._toQuery();

        return multiMatchQuery;
    }

    /**
     * Creates a term query
     *
     * @param field Field to search
     * @param value Value to search
     * @return Query
     */
    @Override
    public Query termQuery(String value, String field) {
        Query termQuery = TermQuery.of(t -> t
                .value(value)
                .field(field))._toQuery();

        return termQuery;
    }

    /**
     * Creates a list of term queries
     *
     * @param values - values to search
     * @param field  - field to search
     * @return List of term queries
     */
    @Override
    public List<Query> termQueries(String[] values, String field) {
        List<Query> termQueries = Arrays.stream(values)
                .map(value -> termQuery(value, field))
                .collect(Collectors.toList());

        return termQueries;
    }

    /**
     * Creates a terms query
     *
     * @param values Values to search
     * @param field  Field to search
     * @return Query
     */
    @Override
    public Query termsQuery(String[] values, String field) {
        TermsQueryField termsQueryField = TermsQueryField.of(t -> t
                .value(Arrays.stream(values).toList().stream().map(FieldValue::of).collect(Collectors.toList())));

        Query termsQuery = TermsQuery.of(t -> t
                .field(field)
                .terms(termsQueryField))._toQuery();

        return termsQuery;
    }

    /**
     * Creates a must query
     *
     * @param queries List of queries to be executed
     * @return Query to be executed
     */
    @Override
    public Query mustQuery(List<Query> queries) {
        Query boolQuery = BoolQuery.of(b -> b.must(queries))._toQuery();
        return boolQuery;
    }

    /**
     * Creates a should query
     *
     * @param queries List of queries to be executed
     * @return Query to be executed
     */
    @Override
    public Query shouldQuery(List<Query> queries) {
        Query shouldQuery = BoolQuery.of(b -> b.should(queries))._toQuery();
        return shouldQuery;
    }

    /**
     * Creates a range double filter
     *
     * @param field Field to search
     * @param min   Min value
     * @param max   Max value
     * @return Query to be executed
     */
    @Override
    public Query rangeDoubleQuery(String field, Double min, Double max) {
        return RangeQuery.of(r -> r
                .field(field)
                .gte(JsonData.of(min))
                .lte(JsonData.of(max)))._toQuery();
    }

    /**
     * Creates a range integer filter
     *
     * @param field Field to search
     * @param min   Min value
     * @param max   Max value
     * @return Query to be executed
     */
    @Override
    public Query rangeIntegerQuery(String field, Integer min, Integer max) {
        return RangeQuery.of(r -> r
                .field(field)
                .gte(JsonData.of(min))
                .lte(JsonData.of(max)))._toQuery();
    }

    /**
     * Creates the sort options for the query
     *
     * @param field Field to sort
     * @param order Ascending or descending
     * @return Sort option
     */
    @Override
    public SortOptions sort(String field, String order) {
        if (field.equals("primaryTitle")){
            String newField = "primaryTitle.keyword";
            SortOptions sortOptions = SortOptions.of(s -> s
                    .field(FieldSort.of(f -> f
                            .field(newField)
                            .order(order.equals("asc") ? SortOrder.Asc : SortOrder.Desc))));
            return sortOptions;
        }else {
            SortOptions sortOptions = SortOptions.of(s -> s
                    .field(FieldSort.of(f -> f
                            .field(field)
                            .order(order.equals("asc") ? SortOrder.Asc : SortOrder.Desc))));
            return sortOptions;
        }
    }

    /**
     * Creates a function score query
     *
     * @param query - Query to apply the score
     */
    @Override
    public Query functionScoreQuery(Query query) {
        FunctionScore numVotes = functionScore("numVotes", 2.0, "Log1p");
        FunctionScore averageRating = functionScore("averageRating", 2.0, "Ln1p");

        Query functionScore = FunctionScoreQuery.of(f -> f
                .query(query)
                .functions(numVotes, averageRating)
                .scoreMode(FunctionScoreMode.Multiply)
                .boostMode(FunctionBoostMode.Multiply))._toQuery();

        return functionScore;
    }
    /**
     * Creates a function score field
     *
     * @param field    Field used for the score
     * @param factor   Factor to multiply the score
     * @param modifier Modifier to apply to the score
     */
    @Override
    public FunctionScore functionScore(String field, Double factor, String modifier) {
        FunctionScore functionScore = FunctionScore.of(f -> f
                .fieldValueFactor(fv -> fv
                        .field(field)
                        .modifier(FieldValueFactorModifier.valueOf(modifier))
                        .factor(factor)));
        return functionScore;
    }


    /**
     * Creates a multi match query
     * @param query - Query to search
     * @param field - Field to search
     * @return Query to be executed
     */
    @Override
    public Query multiMatchQuery(String query, String field) {
        Query matchPhrasePrefixQuery = MatchPhrasePrefixQuery.of(m -> m
                .query(query)
                .field(field))._toQuery();
        return matchPhrasePrefixQuery;
    }

    public Query nestedQuery(String field,String value) {
        Query nestedQuery = NestedQuery.of(n -> n
                .path(field)
                .query(q->q.term(t->t.field("akas.region").value(value))))._toQuery();
        return nestedQuery;
    }

    public Query nestedPrefixQuery(String field,String value, String region) {
        Query nestedQuery = NestedQuery.of(n -> n
                .path(field)
                .query(q->q.term(t->t.field("akas.region").value(region)))
                .query(q -> q.matchPhrasePrefix(t -> t.field("akas.title").query(value))))._toQuery();
        return nestedQuery;
    }
}
