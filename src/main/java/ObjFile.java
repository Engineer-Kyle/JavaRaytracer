import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class ObjFile {
    private List<vec3> vertices;
    private List<Integer> faces;
    private List<vec3> normals = new ArrayList<>();
    private List<vec3> texCoords = new ArrayList<>();

    private int trycounter = 0;
    public ObjFile(String imageFilename) {

        vertices = new ArrayList<>();
        faces = new ArrayList<>();
        // Loads image data from the specified file. If the RTW_IMAGES environment variable is
        // defined, looks only in that directory for the image file. If the image was not found,
        // searches for the specified image file first from the current directory, then in the
        // images/ subdirectory, then the _parent's_ images/ subdirectory, and then _that_
        // parent, on so on, for six levels up. If the image was not loaded successfully,
        // width() and height() will return 0.
        // Attempt to load the image from various locations
        String imagedir = System.getenv("RTW_IMAGES");
        load(imagedir + File.separator + imageFilename);
        if (imagedir != null && load(imagedir + File.separator + imageFilename)) return;
        if (load(imageFilename)) return;
        if (load("images" + File.separator + imageFilename)) return;
        if (load(".." + File.separator + "images" + File.separator + imageFilename)) return;
        if (load(".." + File.separator + ".." + File.separator + "images" + File.separator + imageFilename)) return;
        if (load(".." + File.separator + ".." + File.separator + ".." + File.separator + "images" + File.separator + imageFilename))
            return;

        System.err.println("ERROR: Could not load image file '" + imageFilename + "'.");
    }


        public boolean load(String filename) {
            trycounter++;
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                System.out.println("Reading file: "+filename);
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("v ")) {
                        String[] parts = line.split("\\s+");
                        vec3 vertex = new vec3(
                                Float.parseFloat(parts[1]),
                                Float.parseFloat(parts[2]),
                                Float.parseFloat(parts[3])
                        );
                        vertices.add(vertex);
                    } else if (line.startsWith("f ")) {
                        String[] parts = line.split("\\s+");
                        faces.add(Integer.parseInt(parts[1].split("/")[0]) - 1);
                        faces.add(Integer.parseInt(parts[2].split("/")[0]) - 1);
                        faces.add(Integer.parseInt(parts[3].split("/")[0]) - 1);
                    }

                   else if (line.startsWith("vn ")) {
                        String[] tokens = line.split("\\s+");
                        vec3 normal = new vec3(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]));
                        normals.add(normal);
                    }
                    else if (line.startsWith("vt ")) {
                        String[] tokens = line.split("\\s+");
                        if (tokens.length == 3) {
                            vec3 texCoord = new vec3(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]), 0.0);
                            texCoords.add(texCoord);
                        } else if (tokens.length == 4) {
                            vec3 texCoord = new vec3(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]));
                            texCoords.add(texCoord);
                        }
                    }
                }
                System.out.println("Done reading file: "+ filename+"\n");
                return true;
            } catch (Exception e) {
                System.out.println("Could not read file: "+ filename+" attemped: "+trycounter+ " times");
                return false;
            }
        }

        public List<vec3> getVertices() {
            return vertices;
        }

        public List<Integer> getFaces() {
            return faces;
        }

        public List<vec3> getNormals() { return normals;}
        public List<vec3> getTexCoords() { return texCoords; }


}


