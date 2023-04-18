package co.empathy.academy.IMDb.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class Director {

    public Director(String nconst) {
        this.nconst = nconst;}

    String nconst;

}
