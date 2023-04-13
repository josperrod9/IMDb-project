package co.empathy.academy.IMDb.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Movie {

    public Movie(String tconst, String titleType, String primaryTitle, String originalTitle, Boolean isAdult, int startYear, int endYear, int runtimeMinutes, String[] genres, double averageRating, int numVotes, List<Aka> akas, List<Director> directors, List<Starring> starring) {
        this.tconst = tconst;
        this.titleType = titleType;
        this.primaryTitle = primaryTitle;
        this.originalTitle = originalTitle;
        this.isAdult = isAdult;
        this.startYear = startYear;
        this.endYear = endYear;
        this.runtimeMinutes = runtimeMinutes;
        this.genres = genres;
        this.averageRating = averageRating;
        this.numVotes = numVotes;
        this.akas = akas;
        this.directors = directors;
        this.starring = starring;
    }

    String tconst;
    String titleType;
    String primaryTitle;
    String originalTitle;
    Boolean isAdult;
    int startYear;
    int endYear;
    int runtimeMinutes;
    String[] genres;
    double averageRating;
    int numVotes;
    List<Aka> akas;
    List<Director> directors;
    List<Starring> starring;
}
