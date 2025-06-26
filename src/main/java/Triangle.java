public class Triangle extends Hittable {

    private final vec3 corner1;
    private final vec3 corner2;
    private final vec3 corner3;
    private Materials material;
    private vec3 v0, v1, v2, n0, n1, n2, t0, t1, t2;

    public AABB bbox;

    public Triangle(vec3 corner1,vec3 corner2,vec3 corner3,  Materials material){

        this.corner1 = corner1;
        this.corner2 = corner2;
        this.corner3 = corner3;
        this.material = material;
        vec3 min = new vec3(
                Math.min(corner1.getX(), Math.min(corner2.getX(), corner3.getX())),
                Math.min(corner1.getY(), Math.min(corner2.getY(), corner3.getY())),
                Math.min(corner1.getZ(), Math.min(corner2.getZ(), corner3.getZ()))
        );

        vec3 max = new vec3(
                Math.max(corner1.getX(), Math.max(corner2.getX(), corner3.getX())),
                Math.max(corner1.getY(), Math.max(corner2.getY(), corner3.getY())),
                Math.max(corner1.getZ(), Math.max(corner2.getZ(), corner3.getZ()))
        );

        bbox = new AABB(min, max);
    }

    //To complete Ray-triangle intersection:
    //first find t, for Ray-Plane inersection
    //with t, get p.
    //calculate barycentric coordinates of p.
    //If all are positive, we have hit the triangle.
    //If any negative, or if D*n is zero, we missed

    //Möller-Trumbore algorithm


    public Triangle(vec3 v0, vec3 v1, vec3 v2, vec3 n0, vec3 n1, vec3 n2, vec3 t0, vec3 t1, vec3 t2, Materials material) {
        this.corner1 = v0;
        this.corner2 = v1;
        this.corner3 = v2;
        this.n0 = n0;
        this.n1 = n1;
        this.n2 = n2;
        this.t0 = t0;
        this.t1 = t1;
        this.t2 = t2;
        this.material = material;

        vec3 min = new vec3(
                Math.min(corner1.getX(), Math.min(corner2.getX(), corner3.getX())),
                Math.min(corner1.getY(), Math.min(corner2.getY(), corner3.getY())),
                Math.min(corner1.getZ(), Math.min(corner2.getZ(), corner3.getZ()))
        );

        vec3 max = new vec3(
                Math.max(corner1.getX(), Math.max(corner2.getX(), corner3.getX())),
                Math.max(corner1.getY(), Math.max(corner2.getY(), corner3.getY())),
                Math.max(corner1.getZ(), Math.max(corner2.getZ(), corner3.getZ()))
        );
        bbox = new AABB(min, max);
       // computeNormal();
    }

    @Override
    public boolean hit(Ray r, Interval rayT, HitRecord rec) {
       // return true;
        vec3 edge1 = vec3.subtract(corner2 ,corner1);
        vec3 edge2 = vec3.subtract(corner3 ,corner1);
        vec3 h = vec3.cross(r.getDirection(), edge2);
        double a = vec3.dot(edge1, h);

        if (a > -1e-8 && a < 1e-8) {
            return false; // This ray is parallel to this triangle.
        }

        double f = 1.0 / a;
        vec3 s = vec3.subtract(r.getOrigin(), corner1);
        double u = f * vec3.dot(s, h);

        if (u < 0.0 || u > 1.0) {
            return false;
        }

        vec3 q = vec3.cross(s, edge1);
        double v = f * vec3.dot(r.getDirection(), q);

        if (v < 0.0 || u + v > 1.0) {
            return false;
        }

        double t = f * vec3.dot(edge2, q);

        if (t > rayT.getMin() && t < rayT.getMax()) {
            rec.t = t;
            rec.u = u;
            rec.v = v;
            rec.point = r.at(t);
            rec.normal = vec3.unitVector(vec3.cross(edge1, edge2));
            rec.materials = this.material;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public AABB boundingBox() {
        return bbox;
    }
}
