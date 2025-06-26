import java.util.Vector;

public class Dielectic extends Materials{

    double refractionIndex;

    public Dielectic (double refractionIndex){
        this.refractionIndex = refractionIndex;
    }

    @Override
    public boolean scatter(Ray rayIn, HitRecord hitRecord, ScatterReturn scatterReturn) {

        vec3 outwardNormal;
        vec3 reflected = vec3.reflect(rayIn.getDirection(), hitRecord.normal);
        double ri;
        double reflectProbability;
        double cosine;
        scatterReturn.attenuation = new Color(1,1,1);

        if(vec3.dot(rayIn.getDirection(), hitRecord.normal) > 0){
            outwardNormal =  vec3.multiply(-1,hitRecord.normal);
            ri = refractionIndex;
            cosine = vec3.dot(rayIn.getDirection(), hitRecord.normal)/(rayIn.getDirection().length() * hitRecord.normal.length());
        }
        else {
            outwardNormal = hitRecord.normal;
            ri = 1/refractionIndex;
            cosine = vec3.dot(rayIn.getDirection(), hitRecord.normal) / (- rayIn.getDirection().length() * hitRecord.normal.length() );
        }
        if(vec3.refract(rayIn.getDirection(), outwardNormal, ri, scatterReturn)){
            reflectProbability = reflectance(cosine, refractionIndex);
        }
        else {
            reflectProbability = 1;
        }
        if(Math.random() < reflectProbability){
            scatterReturn.scattered = new Ray(hitRecord.point, reflected, rayIn.getTime());
        }
        else {
            scatterReturn.scattered = new Ray(hitRecord.point, scatterReturn.refracted, rayIn.getTime());
        }
        return true;
    }

    @Override
    public Color emitted(double u, double v, vec3 p) {
        return new Color(0, 0, 0); //This will just be black
    }

    static double reflectance(double cosine, double refractionIndex){
        //Use Schlick's approximation for reflectance
        double r0 = (1-refractionIndex)/(1+refractionIndex);
        r0 = r0 * r0;
        return r0 +(1-r0) * (Math.pow(1-cosine,5));

    }
}


