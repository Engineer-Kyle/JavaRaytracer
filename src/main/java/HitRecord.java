import java.util.Optional;

public class HitRecord {

        public vec3 point;
        public vec3 normal;
        public double t;
        public Materials materials;
        double u;
        double v;

        public HitRecord() {
            t = 0;
            point = new vec3(0,0,0);
            normal = new vec3(0,0,0);

        }


        public void clone(HitRecord tempRec) {
            this.point = tempRec.point;
            this.normal = tempRec.normal;
            this.t = tempRec.t;
            this.materials = tempRec.materials;
            this.u = tempRec.u;
            this.v = tempRec.v;
        }

    public void setU(double u) {
        this.u = u;
    }

    public void setV(double v) {
       this.v = v;
    }
}

