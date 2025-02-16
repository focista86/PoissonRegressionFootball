package hu.sm.poissonregressionfootball.csv_creator;
/*
FootballDataFetcher osztály létrehozása, ami a football_data.csv fájlt fogja letölteni és feldolgozni.
🎯 Oszlopok jelentése:

date: mérkőzés dátuma
home_team: hazai csapat
away_team: vendég csapat
home_goals / away_goals: lőtt gólok
home_xg / away_xg: várható gólok (expected goals – xG)
home_shots / away_shots: kapura lövések száma
home_possession / away_possession: labdabirtoklási százalék
home_pass_accuracy / away_pass_accuracy: passzpontosság
home_fouls / away_fouls: szabálytalanságok száma
weather: időjárás (eső, napos, felhős, stb.)
result: a mérkőzés eredménye (home_win, draw, away_win)
 */

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FootballDataFetcher {
    private static final String API_KEY = "99a079c0df93471f95358a8a2d9756d1"; // Ide írd az API kulcsodat
    private static final String API_URL = "https://api.football-data.org/v4/competitions/CL/matches";

    public static void main(String[] args) {
        try {
            String jsonResponse = getApiData(API_URL);
            writeMatchesToCSV(jsonResponse, "src\\main\\resources\\matches_CL.csv");
            System.out.println("✅ Adatok sikeresen letöltve és elmentve: matches_PL.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getApiData(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Auth-Token", API_KEY);

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Hiba történt az API elérésekor: " + responseCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    private static void writeMatchesToCSV(String jsonData, String fileName) throws IOException {
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray matches = jsonObject.getJSONArray("matches");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "date", "home_team", "away_team", "home_goals", "away_goals", "result"))) {

            for (int i = 0; i < matches.length(); i++) {
                JSONObject match = matches.getJSONObject(i);
                if (!match.getString("status").equals("FINISHED")) continue;

                String date = match.getString("utcDate").split("T")[0];
                String homeTeam = match.getJSONObject("homeTeam").getString("name");
                String awayTeam = match.getJSONObject("awayTeam").getString("name");
                int homeGoals = match.getJSONObject("score").getJSONObject("fullTime").optInt("home", -1);
                int awayGoals = match.getJSONObject("score").getJSONObject("fullTime").optInt("away", -1);

                String result = homeGoals == awayGoals ? "draw" : (homeGoals > awayGoals ? "home_win" : "away_win");

                csvPrinter.printRecord(date, homeTeam, awayTeam, homeGoals, awayGoals, result);
            }
        }
    }
}

