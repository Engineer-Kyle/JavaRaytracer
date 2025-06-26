public class NoiseTexture extends Texture{

    private Perlin noise;
    private double scale;

    public NoiseTexture() {
        noise = new Perlin();
    }
    public NoiseTexture(double scale) {
        noise = new Perlin();
        this.scale = scale;
    }

    @Override
    public Color value(double u, double v, vec3 p) {
        return new Color(1, 1, 1).multiply(noise.noise(vec3.multiply(scale, p))).multiply(.5);
    }

}
