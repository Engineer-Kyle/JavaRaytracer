public class AABB {
    Interval x, y, z;

    public static final AABB EMPTY = new AABB(Interval.EMPTY, Interval.EMPTY, Interval.EMPTY);
    public static final AABB UNIVERSE = new AABB(Interval.UNIVERSE, Interval.UNIVERSE, Interval.UNIVERSE);

    public AABB() {
        x = new Interval();
        y = new Interval();
        z = new Interval();
    }

    public AABB(Interval x, Interval y, Interval z) {
        this.x = x;
        this.y = y;
        this.z = z;
        padToMinimums();
    }

    public AABB(vec3 a, vec3 b) {
        x = new Interval(Math.min(a.getX(), b.getX()), Math.max(a.getX(), b.getX()));
        y = new Interval(Math.min(a.getY(), b.getY()), Math.max(a.getY(), b.getY()));
        z = new Interval(Math.min(a.getZ(), b.getZ()), Math.max(a.getZ(), b.getZ()));
        padToMinimums();
    }

    public AABB(AABB box0, AABB box1) {
        x = new Interval(box0.x, box1.x);
        y = new Interval(box0.y, box1.y);
        z = new Interval(box0.z, box1.z);
    }

    public Interval axisInterval(int n) {
        if (n == 1) return y;
        if (n == 2) return z;
        return x;
    }

    public boolean hit(Ray rayin, Interval rayT) {
        vec3 rayOrigin = rayin.getOrigin();
        vec3 rayDirection = rayin.getDirection();

        for (int axis = 0; axis < 3; axis++) {
            Interval axisInter = axisInterval(axis);
            double adinv = 1.0 / rayDirection.get(axis);

            double t0 = (axisInter.min - rayOrigin.get(axis)) * adinv;
            double t1 = (axisInter.max - rayOrigin.get(axis)) * adinv;

            if (t0 < t1) {
                if (t0 > rayT.min) rayT.min = t0;
                if (t1 < rayT.max) rayT.max = t1;
            } else {
                if (t1 > rayT.min) rayT.min = t1;
                if (t0 < rayT.max) rayT.max = t0;
            }

            if (rayT.max <= rayT.min)
                return false;
        }
        return true;
    }

    public int longestAxis() {
        if (x.size() > y.size())
            return x.size() > z.size() ? 0 : 2;
        else
            return y.size() > z.size() ? 1 : 2;
    }

    private void padToMinimums() {
        double delta = 0.0001;
        if (x.size() < delta) x = x.expand(delta);
        if (y.size() < delta) y = y.expand(delta);
        if (z.size() < delta) z = z.expand(delta);
    }

    public static AABB add(AABB bbox, vec3 offset) {
        return new AABB(Interval.add(offset.getX(),bbox.x),Interval.add(offset.getY(),bbox.y),Interval.add(offset.getZ(),bbox.z));
    }

    public static AABB add(vec3 offset, AABB bbox) {
        return add(bbox, offset);
    }

    public static vec3 getMin(AABB bbox) {
        return new vec3(bbox.x.getMin(), bbox.y.getMin(), bbox.z.getMin());

    }
    public static vec3 getMax(AABB bbox){
             return new vec3(bbox.x.max, bbox.y.max, bbox.z.max);
    }

}



//public class AABB {
//
//    Interval x, y, z;
//
//    public static final AABB EMPTY = new AABB(Interval.EMPTY, Interval.EMPTY, Interval.EMPTY);
//    public static final AABB UNIVERSE = new AABB(Interval.UNIVERSE, Interval.UNIVERSE, Interval.UNIVERSE);
//
//
//    public AABB() {
//        x = new Interval();
//        y = new Interval();
//        z = new Interval();
//    }
//
//    public AABB(Interval x, Interval y, Interval z) {
//        this.x = x;
//        this.y = y;
//        this.z = z;
//        padToMinimums();
//    }
//
//
//    public AABB(vec3 a, vec3 b) {
//        // Treat the two points a and b as extrema for the bounding box, so we don't require a
////        // particular minimum/maximum coordinate order.
////        x = (a.getX() <= b.getX()) ? new Interval(a.getX(), b.getX()) : new Interval(b.getX(), a.getX());
////        y = (a.getY() <= b.getY()) ? new Interval(a.getY(), b.getY()) : new Interval(b.getY(), a.getY());
////        z = (a.getZ() <= b.getZ()) ? new Interval(a.getZ(), b.getZ()) : new Interval(b.getZ(), a.getZ());
////
//        x = new Interval(Math.min(a.getX(), b.getX()), Math.max(a.getX(), b.getX()));
//        y = new Interval(Math.min(a.getY(), b.getY()), Math.max(a.getY(), b.getY()));
//        z = new Interval(Math.min(a.getZ(), b.getZ()), Math.max(a.getZ(), b.getZ()));
//
//        padToMinimums();
//    }
//
//    public AABB(AABB box0, AABB box1){
//        //this could be bad
////        if(box0 == null) {
////            box0 = new AABB(new Interval(), new Interval(), new Interval());
////        }
////        if(box1 == null) {
////            box1 = new AABB(new Interval(), new Interval(), new Interval());
////        }
//
//        x = new Interval(box0.x, box1.x);
//        y = new Interval(box0.y, box1.y);
//        z = new Interval(box0.z, box1.z);
//        //padToMinimums();
//
//    }
//
//
//
//    public Interval axisInterval(int n) {
//        if (n == 1) return y;
//        if (n == 2) return z;
//        return x;
//    }
//
//    boolean hit(Ray rayin, Interval rayT) {
//        /**
//         *  I think this is wrong, or at least this is where a problem is
//         *  manifesting itself. It never makes it to axis 3. It the else
//         *  loop on axis 0, and the if loop and then return false on axis 1.
//         */
//
//        vec3 rayOrigin = rayin.getOrigin();
//        vec3 rayDirection = rayin.getDirection();
//
//        for (int axis = 0; axis < 3; axis++) {
//            Interval axisInter = axisInterval(axis);
//            double adinv = 1.0 / rayDirection.get(axis);
//
//            double t0 = (axisInter.min - rayOrigin.get(axis)) * adinv;
//            double t1 = (axisInter.max - rayOrigin.get(axis)) * adinv;
//
//
//            if (t0 < t1) {
//                if (t0 > rayT.min) rayT.min = t0;
//                if (t1 < rayT.max) rayT.max = t1;
//            } else {
//                if (t1 > rayT.min) rayT.min = t1;
//                if (t0 < rayT.max) rayT.max = t0;
//            }
//
//            if (rayT.max <= rayT.min)
//                return false;
//        }
//        return true;
//    }
//
//
//    public int longestAxis(){
//        // Returns the index of the longest axis of the bounding box.
//        if(x.size() > y.size()) {
//            return x.size() > z.size() ? 0 : 2;
//        }
//        else {
//            return y.size() > z.size() ? 1 : 2;
//        }
//
//    }
//
//   private void padToMinimums() {
//        // Adjust the AABB so that no side is narrower than some delta, padding if necessary.
//
//        double delta = 0.0001;
//        if (x.size() < delta) x = x.expand(delta);
//        if (y.size() < delta) y = y.expand(delta);
//        if (z.size() < delta) z = z.expand(delta);
//    }
//
//
//}
