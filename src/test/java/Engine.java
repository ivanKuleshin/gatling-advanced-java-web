import io.gatling.app.Gatling;

public class Engine {

    public static void main(String[] args) {
        String[] gatlingArgs = {
                "--simulation", "computerdatabase.ComputerDatabaseSimulation",
                "--results-folder", IDEPathHelper.resultsDirectory.toString()
        };
        Gatling.main(gatlingArgs);
    }
}
