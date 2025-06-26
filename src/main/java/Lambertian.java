public class Lambertian extends Materials{
   // private Color albedo;
    private Texture texture;


    public Lambertian(Texture texture){
        this.texture = texture;
    }
    public Lambertian(Color albedo){
        texture = new SolidColor(albedo);
        //this.albedo = albedo.clone();
    }

    @Override
    public boolean scatter(Ray rayIn, HitRecord hitRecord, ScatterReturn scatterReturn) {
        //vec3 scatterDirection = vec3.add(vec3.add(hitRecord.normal, hitRecord.point), vec3.randomUnitVector());
        //if(scatterDirection.nearZero()) scatterDirection = hitRecord.normal;

        scatterReturn.scattered =  new Ray(hitRecord.point, vec3.randomOnHemisphere(hitRecord.normal), rayIn.getTime());

       // System.out.println("HitRecord:" + hitRecord.u+", " + hitRecord.v+", "+hitRecord.point);
        scatterReturn.attenuation = texture.value(hitRecord.u, hitRecord.v, hitRecord.point);
        //scatterReturn.attenuation = albedo.clone();
        return true;


    }

    @Override
    public Color emitted(double u, double v, vec3 p) {
        return new Color(0,.0,0); //This should be just black
    }

}
