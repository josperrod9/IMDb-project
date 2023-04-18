package co.empathy.academy.IMDb.utils;

import co.empathy.academy.IMDb.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IMDbReader3 {

    private final BufferedReader basicsReader;
    private final BufferedReader ratingsReader;
    private final BufferedReader akasReader;
    private final BufferedReader crewReader;
    private final BufferedReader principalsReader;
    private boolean hasDocuments = true;

    private static final Logger LOGGER = LoggerFactory.getLogger(IMDbReader3.class);


    public IMDbReader3(MultipartFile basicsFile, MultipartFile ratingsFile, MultipartFile akasFile, MultipartFile crewFile,
                       MultipartFile principalsFile) {
        try {
            this.basicsReader = new BufferedReader(new InputStreamReader(basicsFile.getInputStream()));
            this.ratingsReader = new BufferedReader(new InputStreamReader(ratingsFile.getInputStream()));
            this.akasReader = new BufferedReader(new InputStreamReader(akasFile.getInputStream()));
            this.crewReader = new BufferedReader(new InputStreamReader(crewFile.getInputStream()));
            this.principalsReader = new BufferedReader(new InputStreamReader(principalsFile.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        readHeaders();
    }

    private void readHeaders() {
        try {
            basicsReader.readLine();
            ratingsReader.readLine();
            akasReader.readLine();
            crewReader.readLine();
            principalsReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Movie> readDocuments() throws IOException {
        List<Movie> result = new ArrayList<>();
        String basicsLine;
        String ratingsLine;
        String akasLine;
        String crewLine;
        String principalsLine;
        String[] basicsLineArray;
        String[] ratingsLineArray;
        String[] akasLineArray;
        String[] crewLineArray;
        String[] principalsLineArray;
        int documentsSize = 20000;
        while((basicsLine = this.basicsReader.readLine())!= null &&(ratingsLine = this.ratingsReader.readLine()) != null &&
                (akasLine = this.akasReader.readLine()) != null && (crewLine = this.crewReader.readLine()) != null &&
                (principalsLine = this.principalsReader.readLine()) != null && result.size() < documentsSize) {
            System.out.println(basicsLine+ " basics");
            System.out.println(ratingsLine+ " ratings");
            System.out.println(akasLine + " akas");
            System.out.println(crewLine+ " crew");
            System.out.println(principalsLine+ " principals");
            basicsLineArray = basicsLine.split("\t");
            ratingsLineArray = ratingsLine.split("\t");
            akasLineArray = akasLine.split("\t");
            crewLineArray = crewLine.split("\t");
            principalsLineArray = principalsLine.split("\t");
            boolean isNonAdultMovie = basicsLineArray[4].contentEquals("0")&&((basicsLineArray[1].equals("movie"))||
                    (basicsLineArray[1].equals("tvMovie")))&&!Arrays.asList(basicsLineArray[8].split(",")).contains("Adult");
            if (!isNonAdultMovie)continue;
            Movie movie = new Movie();
            movie.setTconst(basicsLineArray[0]);
            movie.setTitleType(basicsLineArray[1]);
            movie.setPrimaryTitle(basicsLineArray[2]);
            movie.setOriginalTitle(basicsLineArray[3]);
            movie.setIsAdult(basicsLineArray[4].contentEquals("1"));
            movie.setStartYear(toInt(basicsLineArray[5]));
            movie.setEndYear(toInt(basicsLineArray[6]));
            movie.setRuntimeMinutes(toInt(basicsLineArray[7]));
            movie.setGenres(basicsLineArray[8].split(","));
            List<Aka> akas = new ArrayList<>();
            List<Starring> starrings = new ArrayList<>();
            int count = 0;
            while(!ratingsLineArray[0].equals(basicsLineArray[0])&&(ratingsLine = this.ratingsReader.readLine()) != null&&count<100) {
                ratingsLineArray = ratingsLine.split("\t");
                count++;
            }
            movie.setAverageRating(toDouble(ratingsLineArray[1]));
            movie.setNumVotes(toInt(ratingsLineArray[2]));
            while(!akasLineArray[0].equals(basicsLineArray[0])&&(akasLine = this.akasReader.readLine()) != null) {
                akasLineArray = akasLine.split("\t");
            }
            while (akasLineArray[0].equals(basicsLineArray[0])) {
                Aka aka = new Aka(akasLineArray[2], akasLineArray[3], akasLineArray[4], akasLineArray[7].contentEquals("1"));
                akas.add(aka);
                this.akasReader.mark(1000);
                if ((akasLine = this.akasReader.readLine()) == null) break;
                akasLineArray = akasLine.split("\t");
            }
            if (akasLine != null) akasReader.reset();
            movie.setAkas(akas);
            while(!crewLineArray[0].equals(basicsLineArray[0])&&(crewLine = this.crewReader.readLine()) != null) {
                crewLineArray = crewLine.split("\t");
            }
            String[] directorsArray = crewLineArray[1].split(",");
            List<Director> directors = new ArrayList<>();
            for (String nconst : directorsArray) {
                directors.add(new Director(nconst));
            }
            movie.setDirectors(directors);
            while(!principalsLineArray[0].equals(basicsLineArray[0])&&(principalsLine = this.principalsReader.readLine()) != null) {
                principalsLineArray = principalsLine.split("\t");
            }
            while (principalsLineArray[0].equals(basicsLineArray[0])){
                String[] professions = principalsLineArray[4].split(" ");
                Starring starring = new Starring(new Name(principalsLineArray[2],principalsLineArray[3],0,0, professions), principalsLineArray[5]);
                starrings.add(starring);
                this.principalsReader.mark(1000);
                if ((principalsLine = this.principalsReader.readLine()) == null) break;
                principalsLineArray = principalsLine.split("\t");
            }
            if (principalsLine != null) this.principalsReader.reset();
            movie.setStarring(starrings);
            result.add(movie);
        }
        if (this.basicsReader.readLine() == null) hasDocuments = false;
        return result;
    }


    public static double toDouble(String string) {
        if (string.trim().equals("\\N")) {
            return 0.0;
        }
        try {
            return Double.parseDouble(string.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static int toInt(String value) {
        if (value.trim().contentEquals("\\N")) {
            return 0;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public boolean hasDocuments() {
        return hasDocuments;
    }
}
