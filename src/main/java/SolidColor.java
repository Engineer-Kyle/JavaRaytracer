public class SolidColor extends Texture{
    Color albedo;

    public SolidColor(Color albedo){
        this.albedo = albedo;
    }
    public SolidColor(double red, double green, double blue){
        this(new Color(red,green,blue));
    }



    @Override
    public Color value(double u, double v, vec3 p) {
        return albedo;
    }
}
