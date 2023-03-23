package co.empathy.academy.searchAPI.exceptions;


public class UserNotFoundException extends Exception{
    public UserNotFoundException(long id) {
        super("User not found with id " + id);
    }
}
