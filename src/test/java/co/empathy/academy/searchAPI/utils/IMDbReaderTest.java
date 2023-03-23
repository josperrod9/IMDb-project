package co.empathy.academy.searchAPI.utils;

import co.empathy.academy.searchAPI.models.Movie;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class IMDbReaderTest {
    public static void main(String[] args) throws IOException {
        MultipartFile basics = new MockMultipartFile("basics.tsv", new FileInputStream(new File("src/test/java/co/empathy/academy/searchAPI/data/title.basics.tsv")));
        MultipartFile principals = new MockMultipartFile("principals.tsv", new FileInputStream(new File("src/test/java/co/empathy/academy/searchAPI/data/title.principals.tsv")));
        MultipartFile ratings = new MockMultipartFile("ratings.tsv", new FileInputStream(new File("src/test/java/co/empathy/academy/searchAPI/data/title.ratings.tsv")));
        MultipartFile akas = new MockMultipartFile("akas.tsv", new FileInputStream(new File("src/test/java/co/empathy/academy/searchAPI/data/title.akas.tsv")));
        MultipartFile crew = new MockMultipartFile("crew.tsv", new FileInputStream(new File("src/test/java/co/empathy/academy/searchAPI/data/title.crew.tsv")));
        IMDbReader reader = new IMDbReader(basics, ratings, akas, crew, principals);
        Movie movie= reader.readMovie();
        System.out.println(movie);
    }
}
