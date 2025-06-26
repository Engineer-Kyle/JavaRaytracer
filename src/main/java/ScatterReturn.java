public class ScatterReturn {

    public Ray scattered;
    public Color attenuation;
    public vec3 refracted;

    public ScatterReturn() {
        scattered = new Ray();
        attenuation = new Color();
        refracted = new vec3();
    }
}
