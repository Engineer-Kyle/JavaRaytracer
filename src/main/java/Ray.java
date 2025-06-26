public class Ray {

    // Member variables for the origin and direction
    private  vec3 orig;
    private  vec3 dir;
    private  double time;

    public Ray() {
        this.orig = new vec3();
        this.dir = new vec3();
    }

    // Constructor with parameters
    public Ray(vec3 origin, vec3 direction) {
        this.orig = origin.clone();
        this.dir = direction.clone();
    }

    public Ray(vec3 origin, vec3 direction, double time) {
        this.orig = origin.clone();
        this.dir = direction.clone();
        this.time = time;
    }

    // Getter for origin
    public vec3 getOrigin() {
        return orig;
    }

    // Getter for direction
    public vec3 getDirection() {
        return dir;
    }

    // The 'at' method that calculates a point along the ray
    public vec3 at(double t) {
        return orig.add(dir.multiply(t)); // Assuming Vec3 supports 'add' and 'multiply' methods
    }

   public double getTime(){
        return time;
   }

}
