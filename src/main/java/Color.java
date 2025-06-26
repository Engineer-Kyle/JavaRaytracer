import java.io.*;

public class Color extends vec3 {
    double r;
    double g;
    double b;

    //Make constructor
    public Color() {
        //life
    }

    public Color(double r, double g, double b) {
        super(r, g, b); //Calls parent constructor
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static double linearToGamma(double lienarComponent){
        if(lienarComponent > 0){
            return Math.sqrt(lienarComponent);
        }
        return 0;
    }



    public static void write_color(FileWriter fw, vec3 pixelColor) throws IOException {
        double r = pixelColor.get(0);
        double g = pixelColor.get(1);
        double b = pixelColor.get(2);



        //Apply a linear to gamma transform for gamma 2
        r = linearToGamma(r);
        g = linearToGamma(g);
        b = linearToGamma(b);


        // Translate the [0,1] component values to the byte range [0,255]
        Interval intensity = new Interval(0.000, 0.999);
        int rByte = (int) (255.999 * intensity.clamp(r));
        int gByte = (int) (255.999 * intensity.clamp(g));
        int bByte = (int) (255.999 * intensity.clamp(b));

        if(rByte == 255 && gByte == 255 && bByte == 255){
         //   System.out.println("r: " + rByte + " g: " + gByte + " b: " + bByte);
        }
        // Write out the pixel color components
        fw.write(rByte + " " + gByte + " " + bByte + "\n");

    }

    public Color multiply(double t) {
        vec3 newVec = super.multiply(t);
        return new Color(newVec.getX(), newVec.getY(), newVec.getZ());
    }

    public Color add(Color t) {
        vec3 newVec = super.add(t);
        return new Color(newVec.getX(), newVec.getY(), newVec.getZ());
    }

    @Override
    public Color clone(){
        vec3 newVec = super.clone();
        Color newColor = new Color(newVec.getX(), newVec.getY(), newVec.getZ());
        return newColor;
    }

}


