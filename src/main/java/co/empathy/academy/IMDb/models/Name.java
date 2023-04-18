package co.empathy.academy.IMDb.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class Name {

    public Name(String nconst, String primaryName, int birthYear, int deathYear, String[] primaryProfessions) {
        this.nconst = nconst;
        this.primaryName = primaryName;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        this.primaryProfessions = primaryProfessions;
    }
    String nconst;
    String primaryName;
    int birthYear;
    int deathYear;
    String[] primaryProfessions;
}
