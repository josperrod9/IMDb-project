package co.empathy.academy.searchAPI.models;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class User {

    @NotNull(message = "Please enter id")
    private long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Email is mandatory")
    private String email;

    // standard constructors / toString
    public User(@NotNull long id, @NotNull String name, @NotNull String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User [email=" + email + ", id=" + id + ", name=" + name + "]";
    }

}