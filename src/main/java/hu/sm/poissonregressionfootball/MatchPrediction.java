package hu.sm.poissonregressionfootball;

import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.ArrayList;

public class MatchPrediction {
    public static void main(String[] args) {
        try {
            // 🎯 Létrehozzuk a feature oszlopokat
            ArrayList<Attribute> attributes = new ArrayList<>();
            attributes.add(new Attribute("team_strength_home"));
            attributes.add(new Attribute("team_strength_away"));
            attributes.add(new Attribute("home_advantage"));
            attributes.add(new Attribute("home_goals")); // Ezt jósoljuk

            Instances dataset = new Instances("FootballDataset", attributes, 0);
            dataset.setClassIndex(3);

            // 🏆 Modell betöltése (példa)
            LinearRegression model = new LinearRegression();
            // (itt kellene betölteni egy korábban betanított modellt)

            // 🏟️ Új meccs adatai (példa: Barcelona vs. Real Madrid)
            double[] matchData = {1.2, 0.9, 1.0, 0}; // [Hazai csapat ereje, Vendég csapat ereje, Hazai pálya előny, gólok (nincs megadva)]
            dataset.add(new DenseInstance(1.0, matchData));

            // 🔮 Előrejelzés
            double predictedGoals = model.classifyInstance(dataset.instance(0));
            System.out.println("🔮 Becslés a hazai csapat góljaira: " + Math.round(predictedGoals));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}