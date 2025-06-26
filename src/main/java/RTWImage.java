import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class RTWImage {

    private int bytesPerPixel = 3;
    private float[] fdata;            // Linear floating point pixel data
    private byte[] bdata;   // Linear 8-bit pixel data
    int imageWidth = 0;             // Loaded image width
    int imageHeight = 0;            // Loaded image height
    int bytesPerScanline = 0;

    public  double[] red;
    public  double[] green;
    public  double[] blue;



    public RTWImage(String imageFilename) {
        // Loads image data from the specified file. If the RTW_IMAGES environment variable is
        // defined, looks only in that directory for the image file. If the image was not found,
        // searches for the specified image file first from the current directory, then in the
        // images/ subdirectory, then the _parent's_ images/ subdirectory, and then _that_
        // parent, on so on, for six levels up. If the image was not loaded successfully,
        // width() and height() will return 0.
        // Attempt to load the image from various locations
        String imagedir = System.getenv("RTW_IMAGES");
        load(imagedir + File.separator + imageFilename);
        if (imagedir != null && load(imagedir + File.separator + imageFilename)) return;
        if (load(imageFilename)) return;
        if (load("images" + File.separator + imageFilename)) return;
        if (load(".." + File.separator + "images" + File.separator + imageFilename)) return;
        if (load(".." + File.separator + ".." + File.separator + "images" + File.separator + imageFilename)) return;
        if (load(".." + File.separator + ".." + File.separator + ".." + File.separator + "images" + File.separator + imageFilename))
            return;

        System.err.println("ERROR: Could not load image file '" + imageFilename + "'.");
    }

    public boolean load(String filename) {
        try {
            File file = new File(filename);
            BufferedImage image = ImageIO.read(file);

            if (image == null) {
                System.out.println(ImageIO.read(file));
                return false;
            }

            imageWidth = image.getWidth();
            imageHeight = image.getHeight();
            bytesPerScanline = imageWidth * bytesPerPixel;

            fdata = new float[imageWidth * imageHeight * bytesPerPixel];
            bdata = new byte[imageWidth * imageHeight * bytesPerPixel];

            int[] rgbArray = new int[imageWidth * imageHeight];
            red = new double [imageWidth * imageHeight];
            green = new double [imageWidth * imageHeight];
            blue = new double [imageWidth * imageHeight];
            int rgbIndex = 0;
            rgbArray = image.getRGB(0, 0, imageWidth, imageHeight, rgbArray, 0, imageWidth);

            // Convert the pixel data to linear float and byte formats
            int index = 0;
            for (int rgb : rgbArray) {

                float r = ((rgb >> 16) & 0xFF) / 255.0f;
                float g = ((rgb >> 8) & 0xFF) / 255.0f;
                float b = (rgb & 0xFF) / 255.0f;
                red[rgbIndex] = r;
                green[rgbIndex] = g;
                blue[rgbIndex] = b;
                rgbIndex++;

            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public int getWidth() {
        return (fdata == null) ? 0 : imageWidth;
    }

    public int getHeight() {
        return (fdata == null) ? 0 : imageHeight;
    }


    public double getPixelRed(int x, int y){
        int xPart = x;
        int yPart = y * imageWidth;;
       return red[xPart+yPart];
    }
    public double getPixelGreen(int x, int y){
        int xPart = x;
        int yPart = y * imageWidth;;
        return green[xPart+yPart];
    }
    public double getPixelBlue(int x, int y){
        int xPart = x;
        int yPart = y * imageWidth;;
        return blue[xPart+yPart];
    }



}
