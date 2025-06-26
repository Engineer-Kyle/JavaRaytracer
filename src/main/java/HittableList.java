import java.util.ArrayList;
import java.util.List;

public class HittableList extends Hittable {
    private final List<Hittable> objects;
    private AABB bbox;


    public HittableList() {
        objects = new ArrayList<>();
        bbox = new AABB();
    }


    public void clear() {
        objects.clear();
    }

    public void add(Hittable object) {
        if (object.boundingBox() == null) {
            System.out.println("No object to add");
            return;
        }
        objects.add(object);
        bbox = new AABB(bbox, object.boundingBox());
    }

    public List<Hittable> getObjects() {
        return objects;
    }

    @Override
    public boolean hit(Ray r, Interval rayT, HitRecord rec) {
        HitRecord temp_rec = new HitRecord();
        boolean hitAnything = false;
        double closestSoFar = rayT.max;

        for (Hittable object : objects) {
            if (object.hit(r,new Interval(rayT.min, closestSoFar), temp_rec)) {
                hitAnything = true;
                closestSoFar = temp_rec.t;
                rec.clone(temp_rec); // Assuming a method to copy hit record data
            }
        }
        return hitAnything;
    }

    @Override
    public AABB boundingBox() {
        return bbox;
    }

}


