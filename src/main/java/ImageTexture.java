public class ImageTexture extends Texture{

    RTWImage image;
    public ImageTexture(String filename){
        this.image = new RTWImage(filename);
    }

    @Override
    public Color value(double u, double v, vec3 p) {
        // If we have no texture data, then return solid cyan as a debugging aid.

        if (image.getHeight() <= 0) return new Color(0,1,1);

        // Clamp input texture coordinates to [0,1] x [1,0]
        u =  new Interval(0,1).clamp(u);
        v = 1.0 - new Interval(0,1).clamp(v);  // Flip V to image coordinates

        int i = (int)(u * image.getWidth());
        int j = (int)(v * image.getHeight());

        //Anti Gamma effect
        double red = image.getPixelRed(i,j)*image.getPixelRed(i,j);
        double green = image.getPixelGreen(i,j)*image.getPixelGreen(i,j);
        double blue = image.getPixelBlue(i,j)*image.getPixelBlue(i,j);

        return new Color(red, green, blue);
    }
}
