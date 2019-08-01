package parser.hackathon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

public class ParserTxt {

    public static void main(String[] args) throws IOException {
        PrintWriter writer = new PrintWriter("pills.db", "UTF-8");
        String txtFile = "./liste-medicaments.txt";
        File file = new File(txtFile);
        String line = "";
        String cvsSplitBy = "\t";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), "UTF8"))) {
            while ((line = br.readLine()) != null) {
                StringBuilder renderBuilder = new StringBuilder();
                try {
                    renderBuilder.append(
                                    "insert into pills(num, name, format,ordonnance) VALUES(");
                    String[] numero = line.split(cvsSplitBy);
                    renderBuilder.append("'" + numero[0] + "',");
                    String apiRes = callAPI(numero[0]);
                    renderBuilder.append(apiRes + ");");
                    writer.println(renderBuilder.toString());
                } catch (Exception e) {
                    // TODO: handle exception
                    // e.printStackTrace();
                    System.err.println("Error");
                }
            }
        } catch (IOException e) {
            // e.printStackTrace();
        }
        writer.close();
    }

    private static String callAPI(String codeCIS) throws IOException {
        URL line_api_url = new URL(
                        "https://open-medicaments.fr/api/v1/medicaments/"
                                        + codeCIS);
        HttpsURLConnection lineConnection = (HttpsURLConnection) line_api_url
                        .openConnection();
        lineConnection.setDoInput(true);
        lineConnection.setDoOutput(true);
        lineConnection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(
                        new InputStreamReader(lineConnection.getInputStream()));
        String inputLine;
        StringBuilder retour = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            JSONObject jsonObject = new JSONObject(inputLine);
            retour.append("'"
                            + new String(jsonObject.getString("denomination")
                                            .getBytes("ISO-8859-1"), "UTF-8")
                            + "',");
            retour.append("'"
                            + new String(jsonObject
                                            .getString("formePharmaceutique")
                                            .getBytes("ISO-8859-1"), "UTF-8")
                            + "',");
            retour.append(jsonObject
                            .getJSONArray("conditionsPrescriptionDelivrance")
                            .isEmpty());
            System.out.println(retour.toString());
        }
        in.close();
        return retour.toString();
    }
}
