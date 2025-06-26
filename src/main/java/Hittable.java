import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Hittable {

    public abstract boolean hit(Ray r, Interval rayT, HitRecord rec);

    public abstract AABB boundingBox();
}
