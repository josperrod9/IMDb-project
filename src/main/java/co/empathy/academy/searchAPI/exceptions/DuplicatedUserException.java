package co.empathy.academy.searchAPI.exceptions;

public class DuplicatedUserException extends Exception{
    public DuplicatedUserException(long id) {
        super("User already exists with id " + id);
    }
}
