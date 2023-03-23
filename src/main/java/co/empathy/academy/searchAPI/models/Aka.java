package co.empathy.academy.searchAPI.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@NoArgsConstructor
public class Aka {

    String title;

    String region;

    String language;

    Boolean isOriginalTitle;

}