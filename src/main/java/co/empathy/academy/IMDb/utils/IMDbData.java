package co.empathy.academy.IMDb.utils;

import co.empathy.academy.IMDb.models.*;

import java.util.ArrayList;
import java.util.List;

public class IMDbData {

    /**
     *
     * @param line, a line from the title-basics file
     * @return, a movie with all the basic info
     */
    public Movie setBasicsLines(String line) {
        Movie movie = new Movie();
        if (line != null) {

            String[] fields = line.split("\t");
            movie.setTconst(fields[0]);
            movie.setTitleType(fields[1]);
            movie.setPrimaryTitle(fields[2]);
            movie.setOriginalTitle(fields[3]);
            movie.setIsAdult(Boolean.parseBoolean(fields[4]));
            movie.setStartYear(toInteger(fields[5]));
            movie.setEndYear(toInteger(fields[6]));
            movie.setRuntimeMinutes(toInteger(fields[7]));
            movie.setGenres(fields[8].split(","));
            initializeListMovie(movie);

        }
        return movie;
    }

    /**
     *
     * @param line, a line from the ratings file
     * @param movie, the movie
     */
    public void setRatings(String line, Movie movie) {

        if (line != null) {
            String[] fields = line.split("\t");
            movie.setAverageRating(Double.parseDouble(fields[1]));
            movie.setNumVotes(Integer.parseInt(fields[2]));

        }
    }

    /**
     *
     * @param line, a line from the akas file
     * @return an Aka object
     */

    public Aka readAka(String line){
        Aka aka= new Aka();
        if (line != null) {

            String[] fields = line.split("\t");

            aka.setTitle(fields[2]);
            aka.setRegion(fields[3]);
            aka.setLanguage(fields[4]);
            aka.setIsOriginalTitle(Boolean.parseBoolean(fields[5]));
        }
        return aka;
    }

    /**
     *
     * @param aka, the aka object
     * @param movie, a movie to add the aka in
     */
    public void setAka(Aka aka, Movie movie) {

        if (aka != null) {
            //add the aka to the akas list
            movie.getAkas().add(aka);
        }
    }

    /**
     *
     * @param line, a line from the Starring file
     * @return a Starring object which contains all the necessary info
     */
    public Starring readStarring(String line){
        Starring starring = new Starring();
        if (line != null) {
            String[] fields = line.split("\t");
            Name name= new Name();
            name.setNconst(fields[2]);
            starring.setName(name);
            starring.setCharacters(fields[5]);
        }
        return starring;
    }

    /**
     *
     * @param starring, the Starring object
     * @param movie, the movie in which the Starring object will be added to
     */
    public void setStarring(Starring starring, Movie movie) {

        if (starring != null) {
            movie.getStarring().add(starring);
        }
    }

    /**
     *
     * @param line a line from the directors file
     * @param movie, the movie in which the director object will be added to
     */
    public void setDirector(String line,Movie movie){

        if (line != null) {
            String[] fields = line.split("\t");
            //read the director field
            String [] directors= fields[1].split(",");
            //add each director to the list
            for (String directorString:directors){
                Director director= new Director();
                director.setNconst(directorString);
                movie.getDirectors().add(director);
            }

        }

    }

    /**
     * Adds the nonAdult movies to the list
     * @param list, a movie´s list
     * @param movie, the movie that will be added to the list
     */

    public void moviesList(List<Movie> list, Movie movie) {
        if (movie != null) {
            if (movie.getIsAdult() != null && !movie.getIsAdult())
                list.add(movie);
        }

    }

    /**
     * Handle /N value on number fields
     * @param field, a string with a number
     * @return the string as a number (value -1 if it is an invalid value)
     */

    public int toInteger(String field) {
        int integer;
        try {
            integer = Integer.parseInt(field);

        } catch (NumberFormatException e) {
            //no valid value
            integer = -1;
        }
        return integer;
    }

    /**
     *
     * @param line1, a readline with an id
     * @param line2, another readline with an id
     * @return true if both lines have the same id
     */

    public boolean sameId(String line1, String line2){
        boolean result= false;
        if(line1!=null && line2!=null){
            String id1 = line1.split("\t")[0];
            String id2 = line2.split("\t")[0];
            if (id1.equalsIgnoreCase(id2))
                result = true;
        }
        return result;
    }

    /**
     *
     * @param line1, the line with the first id
     * @param line2, the line with the second id
     * @return true if the id1 is smaller than the id2
     */
    public boolean smallerID(String line1, String line2){
        boolean result= false;
        if(line1!=null && line2!=null){
            String id1 = line1.split("\t")[0];
            String id2 = line2.split("\t")[0];
            int id1Num=Integer.parseInt(id1.split("tt")[1]);
            int id2Num=Integer.parseInt(id2.split("tt")[1]);
            if (id1Num<id2Num)
                result = true;
        }
        return result;

    }

    /**
     *
     * @param movie the movie to create new akas, starring and directors arrays
     */
    public void initializeListMovie(Movie movie){
        movie.setAkas(new ArrayList<Aka>());
        movie.setStarring(new ArrayList<Starring>());
        movie.setDirectors(new ArrayList<Director>());
    }
}
