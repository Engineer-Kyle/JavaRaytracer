public abstract class Materials {
    //My mod to make this work in java

    public abstract boolean scatter(Ray rayIn, HitRecord hitRecord, ScatterReturn scatterReturn);


    public abstract Color emitted(double u, double v, vec3 p);
}
