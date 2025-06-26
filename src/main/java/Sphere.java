import java.util.Optional;

// Sphere class in Java that implements the Hittable interface
public class Sphere extends Hittable {
    //private final vec3 center;
    private final double radius;
    private Materials material;

    private Ray rayCenter;
    public AABB bbox;

    //This could be a bad idea
    public double u;
    public double v;

    //Constructor for the Sphere
    //Stationary  Sphere
    public Sphere(vec3 staticCenter, double radius, Materials material) {
     //   this.center = staticCenter;
        this.radius = Math.max(0, radius); // Use Math.max to ensure non-negative radius
        this.material = material;
        vec3 rvec = new vec3(radius, radius, radius);
        rayCenter = new Ray (staticCenter, new vec3(0,0,0));

        bbox = new AABB(vec3.subtract(staticCenter, rvec), vec3.add(staticCenter, rvec));

    }

    //Moving sphere
    public Sphere(vec3 center1, vec3 center2, double radius, Materials material) {
        rayCenter = new Ray(center1, vec3.subtract(center2, center1)); // Center with motion
        this.radius = Math.max(0, radius); // Ensure radius is non-negative
        this.material = material;
        vec3 rvec = new vec3(radius, radius, radius);
        rayCenter = new Ray (center1, vec3.subtract(center2, center1));
        AABB box1 = new AABB(vec3.subtract(rayCenter.at(0),  rvec), vec3.add(rayCenter.at(0) , rvec));
        AABB box2 = new AABB(vec3.subtract(rayCenter.at(1),  rvec), vec3.add(rayCenter.at(1) , rvec));
        bbox = new AABB(box1, box2);

    }


    void getSphereUV(vec3 p) {
        // p: a given point on the sphere of radius one, centered at the origin.
        // u: returned value [0,1] of angle around the Y axis from X=-1.
        // v: returned value [0,1] of angle from Y=-1 to Y=+1.
        //     <1 0 0> yields <0.50 0.50>       <-1  0  0> yields <0.00 0.50>
        //     <0 1 0> yields <0.50 1.00>       < 0 -1  0> yields <0.50 0.00>
        //     <0 0 1> yields <0.25 0.50>       < 0  0 -1> yields <0.75 0.50>
        double theta = Math.acos(-p.getY());
        double phi = Math.atan2(-p.getZ(), p.getX())+ Utils.getPi();


        u = phi / (2*Utils.getPi()); //u
        v = theta / Utils.getPi(); //v
    }

    //Rewrite this method. It should rotate a sphere along an axis, it should not move the center of the sphere. Just the orientation of the sphere. It should rotate along its own center.
public void rotate(char axis, double angle) {
    vec3 center = rayCenter.at(0);
    vec3 rotatedCenter;

    switch (axis) {
        case 'x':
            rotatedCenter = center.rotate(new vec3(1, 0, 0), angle);
            break;
        case 'y':
            rotatedCenter = center.rotate(new vec3(0, 1, 0), angle);
            break;
        case 'z':
            rotatedCenter = center.rotate(new vec3(0, 0, 1), angle);
            break;
        default:
            throw new IllegalArgumentException("Axis must be 'x', 'y', or 'z'");
    }

    rayCenter = new Ray(rotatedCenter, new vec3(0, 0, 0));
    vec3 rvec = new vec3(radius, radius, radius);
    bbox = new AABB(vec3.subtract(rotatedCenter, rvec), vec3.add(rotatedCenter, rvec));
}

    // Override the hit method from Hittable
    @Override
    public boolean hit(Ray r, Interval rayT, HitRecord rec) {
        vec3 currentCenter = rayCenter.at(r.getTime());
        vec3 oc = vec3.subtract(currentCenter, r.getOrigin()); // center - r.origin()
        double a = vec3.dot(r.getDirection(), r.getDirection());
        double b = -2.0 * vec3.dot(r.getDirection(), oc);
        double c = vec3.dot(oc, oc) - radius * radius;
        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) return false;

        double sqrtd = Math.sqrt(discriminant);

        // Find the nearest root that lies in the acceptable range.
        double root = (-b - sqrtd) /(2.0* a);
        if (rayT.surrounds(root)) {
            rec.t = root;
            rec.point = r.at(rec.t);
            rec.normal =  vec3.multiply((1.0/radius),vec3.subtract(rec.point, currentCenter));

            getSphereUV(rec.normal);
            rec.setU(u);
            rec.setV(v);

            rec.materials = material;
            return true;
        }
        root = (-b + sqrtd) / (2.0*a);
        if (rayT.surrounds(root)) {
            rec.t = root;
            rec.point = r.at(rec.t);
            rec.normal =  vec3.multiply((1.0/radius),vec3.subtract(rec.point, currentCenter));

            getSphereUV(rec.normal);
            rec.setU(u);
            rec.setV(v);

            rec.materials = material;
            return true;
        }

        return false;
    }

    @Override
    public AABB boundingBox() {
        return bbox;
    }

}
