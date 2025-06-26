import java.util.ArrayList;
import java.util.List;

public class Mesh extends Hittable {

    private List<Triangle> triangles;
    private AABB bbox;
    private List<vec3> vertices;
    private List<Integer> faces;
    private Materials material;

    public Mesh(List<vec3> vertices, List<Integer> indices, Materials material) {
        System.out.println("Creating mesh with " + vertices.size() + " vertices and " + indices.size() + " indices");
        triangles = new ArrayList<>();
        this.vertices = vertices;
        this.faces = indices;
        this.material = material;

        for (int i = 0; i < indices.size(); ) {
            System.out.print("\r At index " + i);
            if (i + 2 < indices.size()) {
                vec3 v0 = vertices.get(indices.get(i));
                vec3 v1 = vertices.get(indices.get(i + 1));
                vec3 v2 = vertices.get(indices.get(i + 2));
                //System.out.println("v0: " + v0 + " v1: " + v1 + " v2: " + v2);
                triangles.add(new Triangle(v0, v1, v2, material));
                i += 3;
            }
            if (i + 3 < indices.size()) {
                vec3 v0 = vertices.get(indices.get(i));
                vec3 v1 = vertices.get(indices.get(i + 1));
                vec3 v2 = vertices.get(indices.get(i + 2));
                vec3 v3 = vertices.get(indices.get(i + 3));
                triangles.add(new Triangle(v0, v1, v2, material));
                triangles.add(new Triangle(v0, v2, v3, material));
                i += 4;
            }
            else {
                System.out.println("i: " + i + " indices.size(): " + indices.size());
                i += 1;
            }
        }
        System.out.println("\nMesh Created");
        computeBoundingBox();
    }

    public Mesh(List<vec3> vertices, List<Integer> indices, List<vec3> normals, List<vec3> texCoords, Materials material) {
        System.out.println("Creating mesh with " + vertices.size() + " vertices and " + indices.size() + " indices");
        this.vertices = vertices;
        this.faces = indices;
        this.material = material;

        triangles = new ArrayList<>();
        for (int i = 0; i < indices.size(); ) {
            System.out.print("\r At index " + i);
            if (i + 2 < indices.size()) {
                vec3 v0 = vertices.get(indices.get(i));
                vec3 v1 = vertices.get(indices.get(i + 1));
                vec3 v2 = vertices.get(indices.get(i + 2));
                vec3 n0 = new vec3(0, 0, 0);
                vec3 n1 = new vec3(0, 0, 0);
                vec3 n2 = new vec3(0, 0, 0);
                if(normals.size() >= i+2) {
                     n0 = normals.get(indices.get(i));
                     n1 = normals.get(indices.get(i + 1));
                     n2 = normals.get(indices.get(i + 2));
                }
                vec3 t0 = texCoords.get(indices.get(i));
                vec3 t1 = texCoords.get(indices.get(i + 1));
                vec3 t2 = texCoords.get(indices.get(i + 2));
                triangles.add(new Triangle(v0, v1, v2, n0, n1, n2, t0, t1, t2, material));
                i += 3;
            }
            if (i + 3 < indices.size()) {
                vec3 v0 = vertices.get(indices.get(i));
                vec3 v1 = vertices.get(indices.get(i + 1));
                vec3 v2 = vertices.get(indices.get(i + 2));
                vec3 v3 = vertices.get(indices.get(i + 3));
                vec3 n0 = new vec3(0, 0, 0);
                vec3 n1 = new vec3(0, 0, 0);
                vec3 n2 = new vec3(0, 0, 0);
                vec3 n3 = new vec3(0, 0, 0);
                if(normals.size() >= i+3) {
                    n0 = normals.get(indices.get(i));
                    n1 = normals.get(indices.get(i + 1));
                    n2 = normals.get(indices.get(i + 2));
                    n3 = normals.get(indices.get(i + 3));
                }

                vec3 t0 = texCoords.get(indices.get(i));
                vec3 t1 = texCoords.get(indices.get(i + 1));
                vec3 t2 = texCoords.get(indices.get(i + 2));
                vec3 t3 = texCoords.get(indices.get(i + 3));
                triangles.add(new Triangle(v0, v1, v2, n0, n1, n2, t0, t1, t2, material));
                triangles.add(new Triangle(v0, v2, v3, n0, n2, n3, t0, t2, t3, material));
                i += 4;
            }
            else {
                System.out.println("i: " + i + " indices.size(): " + indices.size());
                i += 1;
            }
        }
        System.out.println("Mesh Created");
        computeBoundingBox();
    }

    private void computeBoundingBox() {
        vec3 min = new vec3(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        vec3 max = new vec3(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        for (Triangle triangle : triangles) {
            AABB triangleBox = triangle.boundingBox();
            min = vec3.min(min, triangleBox.getMin(triangleBox));
            max = vec3.max(max, triangleBox.getMax(triangleBox));
        }
        bbox = new AABB(min, max);
    }

    @Override
    public boolean hit(Ray r, Interval rayT, HitRecord rec) {
        boolean hitAnything = false;
        double closestSoFar = rayT.getMax();
        for (Triangle triangle : triangles) {
            if (triangle.hit(r, new Interval(rayT.getMin(), closestSoFar), rec)) {
                hitAnything = true;
                closestSoFar = rec.t;
            }
        }
        return hitAnything;
    }

    @Override
    public AABB boundingBox() {
        return bbox;
    }

        public void scale(double factor) {
            for (vec3 vertex : vertices) {
                vertex.set(0, vertex.getX()*factor);
                vertex.set(1, vertex.getX()*factor);
                vertex.set(2, vertex.getX()*factor);

            }
           // new Mesh(vertices, faces, material);
        }

        public void rotate(double angle, char axis) {
            double radians = Math.toRadians(angle);
            double cos = Math.cos(radians);
            double sin = Math.sin(radians);

            for (vec3 vertex : vertices) {
                double x = vertex.getX();
                double y = vertex.getY();
                double z = vertex.getZ();

                switch (axis) {
                    case 'x':
                        vertex.set(1, y * cos - z * sin);
                        vertex.set(2, y * sin + z * cos);
                        break;
                    case 'y':
                        vertex.set(0, x * cos + z * sin);
                        vertex.set(2, -x * sin + z * cos);
                        break;
                    case 'z':
                        vertex.set(0, x * cos - y * sin);
                        vertex.set(1, x * sin + y * cos);
                        break;
                }
            }
            new Mesh(vertices, faces, material);
        }
    public void translate(double dx, double dy, double dz) {
        for (vec3 vertex : vertices) {
            vertex.set(0, vertex.getX() + dx);
            vertex.set(1, vertex.getY() + dy);
            vertex.set(2, vertex.getZ() + dz);
        }
        triangles.clear();
       triangles = new Mesh(vertices, faces, material).triangles;
    }


    public void translated(){
        new Mesh(vertices, faces, material);
    }
}