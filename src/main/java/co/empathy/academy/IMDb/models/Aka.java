package co.empathy.academy.IMDb.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Aka {

    String title;

    String region;

    String language;

    Boolean isOriginalTitle;

}