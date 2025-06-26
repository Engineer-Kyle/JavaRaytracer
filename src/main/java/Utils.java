import java.util.Random;

public class Utils {
    public double getInfinity(){
     return Double.POSITIVE_INFINITY;
    }

    public static double getPi(){
        return 3.1415926535897932385;
    }

    public static double degreesToRadians(double degrees) {
        return degrees * 3.1415926535897932385/180.0;
    }

    public static double randomDouble() {
        // Returns a random real in [0,1).
        Random rand = new Random();
        return rand.nextDouble();
    }

    public static double randomDouble(double min, double max) {
        //Returns a random real between min and max
        return min + (max-min)*randomDouble();
    }
}
