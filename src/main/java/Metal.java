public class Metal extends Materials{
    private Color albedo;
    double fuzz;

    public Metal(){}

    public Metal(Color albedo, double fuzz){
        this.albedo = albedo;
        if(fuzz < 1){
            this.fuzz = fuzz;
        }
        else {
            this.fuzz = 1;
        }
    }

    @Override
    public boolean scatter(Ray r, HitRecord rec, ScatterReturn scatterReturn) {
        vec3 reflected = vec3.reflect(r.getDirection(), rec.normal.normalize());
        reflected = vec3.add(vec3.unitVector(reflected), vec3.multiply(fuzz , vec3.randomUnitVector()));
        scatterReturn.scattered = new Ray(rec.point, reflected, r.getTime());    //p->ref
        scatterReturn.attenuation = albedo;
        return (vec3.dot(reflected, rec.normal) > 0);
    }

    @Override
    public Color emitted(double u, double v, vec3 p) {
        return new Color(.01,.01,0.01); //Maybe change this later
    }


}
