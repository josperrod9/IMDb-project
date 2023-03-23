package co.empathy.academy.searchAPI.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Name {
    String nconst;
    String primaryName;
    int birthYear;
    int deathYear;
    String[] primaryProfessions;
}
