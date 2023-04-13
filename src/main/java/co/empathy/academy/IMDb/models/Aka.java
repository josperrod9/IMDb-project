package co.empathy.academy.IMDb.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class Aka {

    public Aka(String title, String region, String language, Boolean isOriginalTitle) {
        this.title = title;
        this.region = region;
        this.language = language;
        this.isOriginalTitle = isOriginalTitle;
    }
    String title;

    String region;

    String language;

    Boolean isOriginalTitle;

}