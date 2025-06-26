import java.util.*;


public class BVHNode extends Hittable {

    private Hittable left;
    private Hittable right;
    private AABB bbox;
    private List<Hittable>  hitlist;

    public BVHNode(HittableList hitlist){
        this(hitlist.getObjects() , 0 , hitlist.getObjects().size());
    }

    public BVHNode(List<Hittable> hitlist, int start, int end) {
        if(hitlist.size() == 0){
            return;
        }

        this.hitlist = hitlist;
        // left = right = hitlist.get(start);
        bbox = new AABB();
        for(int i = start; i <  end; i ++){
            bbox = new AABB(bbox, hitlist.get(i).boundingBox());
        }

        int axis = bbox.longestAxis();

        // Select comparator based on axis
        Comparator<Hittable> comparator = (axis == 0)
                ? BVHNode::boxXCompare
                : (axis == 1)
                ? BVHNode::boxYCompare
                : BVHNode::boxZCompare;

        int objectSpan = end - start;

        if(objectSpan == 1){
            //Only one object
            left = right = hitlist.get(start);
        } else if (objectSpan == 2) {
            //Two objects
            left = hitlist.get(start);
            right = hitlist.get(start+1);
        } else {
            // More than two objects: Sort and split

            Collections.sort(hitlist.subList(start, end), comparator);

            int mid = start + objectSpan / 2;
            left = new BVHNode(hitlist, start, mid);
            right = new BVHNode(hitlist, mid, end);
        }

      //  bbox = new AABB(left.boundingBox(), right.boundingBox());

    }
    public static int boxCompare(Hittable a, Hittable b, int axisIndex){
        double aMin = a.boundingBox().axisInterval(axisIndex).min;
        double bMin = b.boundingBox().axisInterval(axisIndex).min;
        return Double.compare(aMin, bMin);
    }


    public static int boxXCompare(Hittable a, Hittable b){
        return boxCompare(a,b,0);
    }
    public static int boxYCompare(Hittable a, Hittable b){
        return boxCompare(a,b,1);
    }
    public static int boxZCompare(Hittable a, Hittable b){
        return boxCompare(a,b,2);
    }

        @Override
    public boolean hit(Ray r, Interval rayT, HitRecord rec) {
        //If we hit the parent box, find out witch of the kids has it
        if(!bbox.hit(r,rayT)){
            return false;
        }
        boolean hit = false;
            for (Hittable object : hitlist) {
                if (object.hit(r, rayT,rec)) {
                    hit = true;
                }
            }
            return hit;
//
//        boolean hitLeft = left.hit(r,rayT, rec);
//        boolean hitRight = right.hit(r, new Interval(rayT.min, hitLeft ? rec.t : rayT.max), rec);
//
//        return hitLeft || hitRight;
    }

    @Override
    public AABB boundingBox() {
        return bbox;
    }
}