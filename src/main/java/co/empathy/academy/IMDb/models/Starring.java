package co.empathy.academy.IMDb.models;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class Starring {
    public Starring(Name name, String characters) {
        this.name = name;
        this.characters = characters;
    }
    Name name;
    String characters;
}
