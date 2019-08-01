package parser.hackathon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ParserCsv {

    public static void drugstore() throws IOException {
        PrintWriter writer = new PrintWriter("drugstore.db", "UTF-8");
        String csvFile = "./pharmacies IDF.csv";
        String line = "";
        String cvsSplitBy = ";";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] thing = line.split(cvsSplitBy);
                writer.println("insert into drugstore (name, num, type, voie, departement, libdepartement, cp, commune, tel, lat, long) "
                                + String.format("VALUES('%s','%s%s','%s','%s',%s,'%s','%s','%s','%s','%s','%s'",
                                                thing[2], thing[4], thing[5],
                                                thing[6], thing[7], thing[9],
                                                thing[10], thing[11], thing[12],
                                                thing[13], thing[19], thing[20])
                                + ");");
            }
        } catch (IOException e) {
            throw e;
        }
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        drugstore();
    }
}
