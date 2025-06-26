public class Quad extends Hittable {
    private vec3 Q;
    private vec3 u;
    private vec3 v;
    private Materials mat;
    private AABB bbox;

    private vec3 normal;
    private double d;
    private vec3 w;



    public Quad(vec3 Q, vec3 u, vec3 v, Materials mat) {
        this.Q = Q;
        this.u = u;
        this.v = v;
        this.mat = mat;
        vec3 n = vec3.cross(u,v);
        normal = vec3.unitVector(n);
        d = -vec3.dot(Q, normal); //Maybe not supposed to be negative
        w = vec3.divide(n,vec3.dot(n,n));
        setBoundingBox();
    }

    private void setBoundingBox() {
        // Compute the bounding box of all four vertices.
        AABB bboxDiagonal1 = new AABB(Q, vec3.add(Q, vec3.add(u, v)));
        AABB bboxDiagonal2 = new AABB(vec3.add(Q, u), vec3.add(Q, v));
        bbox = new AABB(bboxDiagonal1, bboxDiagonal2);
    }

    @Override
    public AABB boundingBox() {
        return bbox;
    }

    @Override
    public boolean hit(Ray r, Interval rayT, HitRecord rec) {
        double denom = vec3.dot(normal, r.getDirection());

        // No hit if the ray is parallel to the plane.
        if (Math.abs(denom) < 1e-8)
            return false;

        // Return false if the hit point parameter t is outside the ray interval.
        double t = (d - vec3.dot(normal, r.getOrigin())) / denom;
        if (!rayT.contains(t))
            return false;

        // Determine if the hit point lies within the planar shape using its plane coordinates.
        vec3 intersection = r.at(t);
        vec3 planarHitpointVector = vec3.subtract(intersection, Q);
        double alpha = vec3.dot(w, vec3.cross(v, planarHitpointVector));
        double beta = vec3.dot(w, vec3.cross(planarHitpointVector, u));

        if(!isInterior(alpha, beta, rec))
            return false;

        // Ray hits the 2D shape; set the rest of the hit record and return true.
        rec.t = t;
        rec.point = intersection;
        rec.materials = mat;
        //rec.setFaceNormal(r, normal); //This could break things
        rec.normal = normal; //This could break things
        return true;
    }
    public boolean isInterior(double a, double b, HitRecord rec) {
        Interval unitInterval = new Interval(0, 1);
        // Given the hit point in plane coordinates, return false if it is outside the
        // primitive, otherwise set the hit record UV coordinates and return true.

        if (!unitInterval.contains(a) || !unitInterval.contains(b))
            return false;

        rec.u = a;
        rec.v = b;
        return true;
    }
    
     
    public static HittableList box(vec3 a, vec3 b, Materials mat) {

        HittableList sides = new HittableList();
        vec3 min = new vec3(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()), Math.min(a.getZ(), b.getZ()));
        vec3 max = new vec3(Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()), Math.max(a.getZ(), b.getZ()));
        vec3 dx = new vec3(max.getX() - min.getX(), 0, 0);
        vec3 dy = new vec3(0, max.getY() - min.getY(), 0);
        vec3 dz = new vec3(0, 0, max.getZ() - min.getZ());
        sides.add(new Quad(new vec3(min.getX(), min.getY(), max.getZ()), dx, dy, mat)); // front
        sides.add(new Quad(new vec3(max.getX(), min.getY(), max.getZ()), dz.negate(), dy, mat)); // right
        sides.add(new Quad(new vec3(max.getX(), min.getY(), min.getZ()), dx.negate(), dy, mat)); // back
        sides.add(new Quad(new vec3(min.getX(), min.getY(), min.getZ()), dz, dy, mat)); // left
        sides.add(new Quad(new vec3(min.getX(), max.getY(), max.getZ()), dx, dz.negate(), mat)); // top
        sides.add(new Quad(new vec3(min.getX(), min.getY(), min.getZ()), dx, dz, mat)); // bottom

        return sides;
    }
    
}