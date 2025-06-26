public class Isotropic extends Materials {
    private final Texture tex;

    public Isotropic(Color albedo) {
        this.tex = new SolidColor(albedo);
    }

    public Isotropic(Texture tex) {
        this.tex = tex;
    }

    @Override
    public boolean scatter(Ray r_in, HitRecord rec, ScatterReturn scatterReturn) {
        scatterReturn.scattered = new Ray(rec.point, vec3.randomUnitVector(), r_in.getTime());
        scatterReturn.attenuation = tex.value(rec.u, rec.v, rec.point);
        return true;
    }

    @Override
    public Color emitted(double u, double v, vec3 p) {
        return new Color(0, 0, 0);    }
}
