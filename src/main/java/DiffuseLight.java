public class DiffuseLight extends Materials {
    private Texture texture;

    public DiffuseLight(Texture tex) {
        texture = tex;
    }
    public DiffuseLight(Color emit) {
        texture = new SolidColor(emit);
    }

    @Override
    public boolean scatter(Ray r, HitRecord rec, ScatterReturn scatterReturn) {
        return false;
    }

    @Override
    public Color emitted(double u, double v, vec3 p) {
        return texture.value(u, v, p);
    }
}
