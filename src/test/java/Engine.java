import io.gatling.app.Gatling;

public class Engine {

    public static void main(String[] args) {
        String[] gatlingArgs = {
                "--simulation", "acetoys.AceToysSimulation",
                "--results-folder", IDEPathHelper.resultsDirectory.toString()
        };
        Gatling.main(gatlingArgs);
    }
}
