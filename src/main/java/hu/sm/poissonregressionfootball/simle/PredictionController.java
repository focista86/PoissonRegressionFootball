package hu.sm.poissonregressionfootball.simle;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prediction")
public class PredictionController {

    private WekaPrediction wekaPrediction;

    public PredictionController(WekaPrediction wekaPrediction) {
        this.wekaPrediction = wekaPrediction;
    }

    @GetMapping("/{homeTeam}/{awayTeam}")
    public String predictMatch(@PathVariable Integer homeTeam, @PathVariable Integer awayTeam) {
        // Itt futtatnánk a tanított ML modellt (pl. WEKA)
        //    return "A jósolt eredmény: " + homeTeam + " 2 - 1 " + awayTeam;

        double prediction = wekaPrediction.predict(homeTeam, awayTeam);
        System.out.println("Home team: " + homeTeam + " Away Team: " + awayTeam + " Prediction: " + prediction + " ; " + interpretPrediction(prediction));
        return "Home team: " + homeTeam + " Away Team: " + awayTeam + " prediction: " + prediction + " " + interpretPrediction(prediction);
    }

    public String interpretPrediction(double prediction) {
        if (prediction > 0.5) {
            return "Home Win";  // Ha közel van az 1-hez
        } else if (prediction < -0.5) {
            return "Away Win";  // Ha közel van a -1-hez
        } else {
            return "Draw";  // Ha 0 körüli
        }
    }
}
