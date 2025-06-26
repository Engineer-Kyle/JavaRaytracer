public class Interval {
    public double min;
    public double max;

    public Interval() {
        this.min = Double.POSITIVE_INFINITY;  // Default interval is empty
        this.max = Double.NEGATIVE_INFINITY;
    }

    public Interval(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public Interval(Interval a, Interval b) {
        // Create the interval tightly enclosing the two input intervals.
        this.min = Math.min(a.min, b.min);
        this.max = Math.max(a.max, b.max);
    }

    public double size() {
        return max - min;
    }

    public boolean contains(double x) {
        return min <= x && x <= max;
    }

    public boolean surrounds(double x) {
        return min < x && x < max;
    }

    public double clamp(double x) {
        if (x < min) return min;
        if (x > max) return max;
        return x;
    }

    public Interval expand(double delta) {
        double padding = delta / 2;
        return new Interval(min - padding, max + padding);
    }

    public static final Interval EMPTY = new Interval(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    public static final Interval UNIVERSE = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

    public static Interval add(Interval ival, double displacement) {
        return new Interval(ival.min + displacement, ival.max + displacement);
    }

    public static Interval add(double displacement, Interval ival) {
        return add(ival, displacement);
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public boolean holds(Interval ival) {
        if(this.min <= ival.min && this.max > ival.min){
            return true;
        }
        else if(this.min < ival.max && this.max >= ival.max){
            return true;
        }
    return false;
    }

}





//public class Interval {
//    public double min;
//    public double max;
//
//    public Interval() {
//        this.min = Double.POSITIVE_INFINITY;  // Default interval is empty
//        this.max = Double.NEGATIVE_INFINITY;
//    }
//
//    public Interval(double min, double max) {
//        this.min = min;
//        this.max = max;
//    }
//
//    public Interval(Interval a, Interval b){
//        //Create the interval tightly enclosing the two input intervals.
//        min = a.min <= b.min ? a.min : b.min;
//        max = a.max <= b.max ? a.max : b.max;
//
//    }
//
//
//    public double size() {
//        return max - min;
//    }
//
//    public boolean contains(double x) {
//        return min <= x && x <= max;
//    }
//
//    public boolean surrounds(double x) {
//        return min < x && x < max;
//    }
//
//    public double clamp(double x){
//        if(x < min) return min;
//        return Math.min(x, max);
//    }
//
//
//    public Interval expand(double delta){
//        double padding = delta / 2;
//        return new Interval(min - padding, max + padding);
//    }
//
//    public static final Interval EMPTY = new Interval(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
//    public static final Interval UNIVERSE = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
//}
