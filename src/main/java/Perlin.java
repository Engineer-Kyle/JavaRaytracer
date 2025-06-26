public class Perlin {
    private static final int pointCount = 256;
    private double[] randfloat = new double[pointCount];
    private int[] permX = new int[pointCount];
    private int[] permY = new int[pointCount];
    private int[] permZ = new int[pointCount];
    private vec3 randomVec[] = new vec3[pointCount];

    public Perlin() {
        for (int i = 0; i < pointCount; i++) {
            randomVec[i] = vec3.unitVector(vec3.randomUnitVector());
        }

        perlinGeneratePerm(permX);
        perlinGeneratePerm(permY);
        perlinGeneratePerm(permZ);
    }

    public double noise(vec3 p) {
        double u = p.getX() - Math.floor(p.getX());
        double v = p.getY() - Math.floor(p.getY());
        double w = p.getZ() - Math.floor(p.getZ());

        int i = (int) Math.floor(p.getX());
        int j = (int) Math.floor(p.getY());
        int k = (int) Math.floor(p.getZ());
        vec3[][][] c = new vec3[2][2][2];


        for (int di = 0; di < 2; di++) {
            for (int dj = 0; dj < 2; dj++) {
                for (int dk = 0; dk < 2; dk++) {
                    c[di][dj][dk] = randomVec[
                            permX[(i + di) & 255] ^
                                    permY[(j + dj) & 255] ^
                                    permZ[(k + dk) & 255]
                            ];
                }
            }
        }

        return perlinInterp(c, u, v, w);
    }

    private static void perlinGeneratePerm(int[] p) {
        for (int i = 0; i < pointCount; i++) {
            p[i] = i;
        }

        permute(p, pointCount);
    }

    private static void permute(int[] p, int n) {
        for (int i = n - 1; i > 0; i--) {
            int target = randomInt(0, i);
            int tmp = p[i];
            p[i] = p[target];
            p[target] = tmp;
        }
    }

    private static double perlinInterp(vec3[][][] c, double u, double v, double w) {
        double uu = u * u * (3 - 2 * u);
        double vv = v * v * (3 - 2 * v);
        double ww = w * w * (3 - 2 * w);
        double accum = 0.0;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    vec3 weightV = new vec3(u - i, v - j, w - k);
                    accum += (i * uu + (1 - i) * (1 - uu))
                            * (j * vv + (1 - j) * (1 - vv))
                            * (k * ww + (1 - k) * (1 - ww))
                            * vec3.dot(c[i][j][k], weightV);
                }
            }
        }

        return accum;
    }
    private static double trilinearInterp(double[][][] c, double u, double v, double w) {
        double accum = 0.0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    accum += (i * u + (1 - i) * (1 - u))
                            * (j * v + (1 - j) * (1 - v))
                            * (k * w + (1 - k) * (1 - w))
                            * c[i][j][k];
                }
            }
        }
        return accum;
    }

    private double randomDouble() {
        return Math.random();
    }

    private static int randomInt(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}