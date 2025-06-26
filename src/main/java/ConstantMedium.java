import java.util.Random;

public class ConstantMedium extends Hittable{

        private final Hittable boundary;
        private final double negInvDensity;
        private final Materials phaseFunction;
        private final Random random = new Random();

        public ConstantMedium(Hittable boundary, double density, Texture tex) {
            this.boundary = boundary;
            this.negInvDensity = -1 / density;
            this.phaseFunction = new Isotropic(tex);
        }

        public ConstantMedium(Hittable boundary, double density, Color albedo) {
            this.boundary = boundary;
            this.negInvDensity = -1 / density;
            this.phaseFunction = new Isotropic(albedo);
        }

        @Override
        public boolean hit(Ray r, Interval rayT, HitRecord rec) {
            HitRecord rec1 = new HitRecord();
            HitRecord rec2 = new HitRecord();

            if (!boundary.hit(r, Interval.UNIVERSE, rec1)) {
                return false;
            }

            if (!boundary.hit(r, new Interval(rec1.t + 0.0001, Double.POSITIVE_INFINITY), rec2)) {
                return false;
            }

            if (rec1.t < rayT.min) rec1.t = rayT.min;
            if (rec2.t > rayT.max) rec2.t = rayT.max;

            if (rec1.t >= rec2.t) {
                return false;
            }

            if (rec1.t < 0) {
                rec1.t = 0;
            }

            double rayLength = r.getDirection().length();
            double distanceInsideBoundary = (rec2.t - rec1.t) * rayLength;
            double hitDistance = negInvDensity * Math.log(random.nextDouble());

            if (hitDistance > distanceInsideBoundary) {
                return false;
            }

            rec.t = rec1.t + hitDistance / rayLength;
            rec.point = r.at(rec.t);

            rec.normal = new vec3(1, 0, 0);  // arbitrary
           // rec.frontFace = true;            // also arbitrary
            rec.materials = phaseFunction;

            return true;
        }

        @Override
        public AABB boundingBox() {
            return boundary.boundingBox();
        }
    }
