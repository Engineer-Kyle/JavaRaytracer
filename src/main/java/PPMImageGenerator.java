import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.IOException;

public class PPMImageGenerator {

    static final int X = 1024;
    static final int Y = 768;

    static final int R = 0;
    static final int G = 1;
    static final int B = 2;

    public static void main(String[] args) {

        byte[][][] raster = new byte[X][Y][3];
        String messageToConsole = "Raster Lines remaining: ";
        // Create raster values
        for (int y = 0; y < Y; y++) {
            System.out.print("\r"+messageToConsole+(Y-y));
            for (int x = 0; x < X; x++) {
                raster[x][y][R] = (byte) (255 - (y * 255) / Y);
                raster[x][y][G] = (byte) ((x * 255) / X);
                raster[x][y][B] = (byte) ((y * 255) / Y);
            }
        }
        // Write binary PPM file
        try (FileOutputStream img = new FileOutputStream("image.ppm")) {
            img.write(("P6\n" + X + " " + Y + "\n255\n").getBytes());

            for (int y = 0; y < Y; y++) {
                System.out.print("\r Binary Lines remaining: "+(Y-y));

                for (int x = 0; x < X; x++) {
                    img.write(raster[x][y]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write ASCII PPM file
        try (PrintWriter imgAscii = new PrintWriter("image.ascii.ppm")) {
            imgAscii.println("P3");
            imgAscii.println(X + " " + Y);
            imgAscii.println("255");

            for (int y = 0; y < Y; y++) {
                imgAscii.println("# Row " + y);
                for (int x = 0; x < X; x++) {
                    imgAscii.printf("%d %d %d ", raster[x][y][R] & 0xFF, raster[x][y][G] & 0xFF, raster[x][y][B] & 0xFF);
                }
                imgAscii.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
  