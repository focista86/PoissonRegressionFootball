package hu.sm.poissonregressionfootball.simle;

import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.File;

public class CSVToARFF {
    public static void main(String[] args) throws Exception {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File("src\\main\\resources\\stats\\games.csv"));
        Instances data = loader.getDataSet();
        weka.core.converters.ConverterUtils.DataSink.write("src\\main\\resources\\games.arff", data);
    }
}
