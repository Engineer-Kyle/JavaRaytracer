public class CheckerTexture extends Texture{

    private double invScale;
    private Texture even;
    private Texture odd;

    public CheckerTexture(double scale, Texture even, Texture odd){
        invScale = (1/scale);
        this.even = even;
        this.odd = odd;
    }

    public CheckerTexture(double scale, Color color1, Color color2){
        this(scale, new SolidColor(color1), new SolidColor(color2));
    }

    @Override
    public Color value(double u, double v, vec3 p) {
      int xInt = (int)(Math.floor(invScale * p.getX()));
      int yInt = (int)(Math.floor(invScale * p.getY()));
      int zInt = (int)(Math.floor(invScale * p.getZ()));

      boolean isEven = (xInt+yInt+zInt) % 2 == 0;

      return isEven ? even.value(u, v, p) : odd.value(u, v, p);
    }
}
