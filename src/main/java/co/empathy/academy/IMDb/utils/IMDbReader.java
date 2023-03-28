package co.empathy.academy.IMDb.utils;

import co.empathy.academy.IMDb.models.Movie;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IMDbReader {
    private final BufferedReader basicsReader;
    private final BufferedReader ratingsReader;
    private final BufferedReader akasReader;
    private final BufferedReader crewReader;
    private final BufferedReader principalsReader;
    String ratingLine;
    String akaLine;
    String crewLine;
    String principalsLine;
    private final int documentsSize = 20000;
    private final IMDbData data;
    public boolean moreLines=true;

    public IMDbReader(MultipartFile basicsFile, MultipartFile ratingsFile, MultipartFile akasFile, MultipartFile crewFile,
                      MultipartFile principalsFile) {
        this.basicsReader = reader(basicsFile);
        this.ratingsReader = reader(ratingsFile);
        this.akasReader = reader(akasFile);
        this.crewReader = reader(crewFile);
        this.principalsReader = reader(principalsFile);
        this.data= new IMDbData();
    }

    /**
     *
     * @param file, the multipart file
     * @return a buffered reader
     */
    public BufferedReader reader(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            //read the first line (no useful info)
            reader.readLine();
            return reader;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return the movie with all the info read from the files
     * @throws IOException
     */
    public Movie readMovie () throws IOException {

        String basicLine;
        Movie movie;
        while (moreLines) {

            //read basics and create a movie
            basicLine=basicsReader.readLine();

            movie= data.setBasicsLines(basicLine);


            if (basicLine == null)
                moreLines = false;

            //set ratings
            //if the rating id is smaller that the movie's id read the next line
            if (data.smallerID(ratingLine,basicLine))
                ratingLine=ratingsReader.readLine();

            //if they have the same id
            if (data.sameId(basicLine,ratingLine)){
                //adds the rating info
                data.setRatings(ratingLine,movie);
                //and read the next rating line
                ratingLine=ratingsReader.readLine();
            }

            //set aka
            //there are different aka for a unique movie
            while (data.smallerID(akaLine,basicLine))
                //read the next line if the aka´s id is smaller
                akaLine=akasReader.readLine();

            while (data.sameId(basicLine,akaLine)){
                //if they have the same id, add the aka to the movie
                data.setAka(data.readAka(akaLine),movie);
                //read the next line
                akaLine=akasReader.readLine();
            }

            //set directors
            //if the director id is smaller than the movie´s id, read next
            if ( data.smallerID(crewLine,basicLine))
                crewLine = crewReader.readLine();

            //if they have the same id
            if (data.sameId(basicLine, crewLine)) {
                //adds the director info
                data.setDirector(crewLine, movie);
                crewLine = crewReader.readLine();
            }

            //set principals
            while (data.smallerID(principalsLine,basicLine))
                principalsLine=principalsReader.readLine();

            while (data.sameId(basicLine,principalsLine)){
                data.setPrincipal(data.readPrincipal(principalsLine),movie);
                principalsLine=principalsReader.readLine();
            }
            return movie;

        }

        return null;
    }




    /**
     *  read the first line with info
     * @throws IOException
     */
    public void initializeLines() throws IOException {
        ratingLine = this.ratingsReader.readLine();
        akaLine = this.akasReader.readLine();
        crewLine = this.crewReader.readLine();
        principalsLine = this.principalsReader.readLine();
    }


}
