package hu.sm.poissonregressionfootball;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.File;

//@SpringBootApplication
public class PoissonRegressionFootballApplication {

    public static void main(String[] args) {
        try {
            // 📂 CSV fájl betöltése
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File("football_data.csv"));
            Instances data = loader.getDataSet();

            // 📌 Beállítjuk az osztályváltozót (hazai csapat góljai)
            data.setClassIndex(data.attribute("home_goals").index());

            // 🏆 Poisson regresszió tanítása
            LinearRegression model = new LinearRegression();
            model.buildClassifier(data);

            // 📊 Modell kiírása
            System.out.println(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
