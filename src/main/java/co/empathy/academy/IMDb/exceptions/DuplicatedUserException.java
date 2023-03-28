package co.empathy.academy.IMDb.exceptions;

public class DuplicatedUserException extends Exception{
    public DuplicatedUserException(long id) {
        super("User already exists with id " + id);
    }
}
