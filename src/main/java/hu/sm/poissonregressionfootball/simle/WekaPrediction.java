package hu.sm.poissonregressionfootball.simle;

import org.springframework.stereotype.Service;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.Normalize;

import java.util.Random;

@Service
public class WekaPrediction {

    //private M5P model = new M5P();
    private RandomForest model = new RandomForest();
    //private J48 model = new J48();
    private Instances data;

    public WekaPrediction() {
        try {
            makeModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeModel() throws Exception {
        // 🔹 1. Adatok betöltése
        DataSource source = new DataSource("src\\main\\resources\\games.arff");
        data = source.getDataSet();

        if (data == null) {
            System.out.println("Hiba: Az ARFF fájl nem töltődött be!");
            return;
        }

        // 🔹 2. Target beállítása
        data.setClassIndex(data.numAttributes() - 1);
/*
        // 🔹 3. Attribútum kiválasztás (redundáns oszlopok eltávolítása)
        AttributeSelection filter = new AttributeSelection();
        filter.setEvaluator(new CfsSubsetEval()); // Legfontosabb attribútumok kiválasztása
        filter.setSearch(new BestFirst());
        filter.setInputFormat(data);
        data = Filter.useFilter(data, filter);

        // 🔹 4. Normalizálás (0-1 közé skálázás)
        Normalize normalize = new Normalize();
        normalize.setInputFormat(data);
        data = Filter.useFilter(data, normalize);

        // 🔹 5. Modell beállítása (Optimalizált paraméterek)
        model = new RandomForest();
        model.setNumIterations(100); // Kevesebb fa (csökkenti a túlillesztést)
        model.setMaxDepth(10); // Sekélyebb fák → jobb általánosítás
        model.setNumFeatures((int) Math.sqrt(data.numAttributes())); // √(attribútumok száma)
        model.setSeed(42); // Stabilabb, reprodukálható eredmények
*/
        // 🔹 6. Modell tanítása
        model.buildClassifier(data);
        System.out.println(model);

        // 🔹 7. Modell kiértékelése (5-fold cross-validation)
        Evaluation eval = new Evaluation(data);
        eval.crossValidateModel(model, data, 5, new Random(1));
        System.out.println(eval.toSummaryString("\nÉrtékelési eredmények\n", false));

        // 🔹 8. Hibamérő mutatók kiírása
        System.out.println("Mean Absolute Error: " + eval.meanAbsoluteError());
        System.out.println("Root Mean Squared Error: " + eval.rootMeanSquaredError());
        System.out.println("Correlation Coefficient: " + eval.correlationCoefficient());
    }


    public double predict(Integer homeTeam, Integer awayTeam) {
        try {
            Instance instance = new DenseInstance(data.numAttributes());
            instance.setDataset(data);

            // Példa: ha a 3. és 4. attribútum a gólok száma
            //instance.setValue(3, homeGoals);
            //instance.setValue(4, awayGoals);
            instance.setValue(5, homeTeam);
            instance.setValue(6, awayTeam);

            return model.classifyInstance(instance); // Predikció
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
