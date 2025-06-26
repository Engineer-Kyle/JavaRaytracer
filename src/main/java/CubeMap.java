import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CubeMap {
    private BufferedImage[] faces; // Array for six cube map images
    private static int attempts = 0;

    public CubeMap(BufferedImage right, BufferedImage left, BufferedImage top,
                   BufferedImage bottom, BufferedImage front, BufferedImage back) {
        faces = new BufferedImage[6];
        faces[0] = right;
        faces[1] = left;
        faces[2] = top;
        faces[3] = bottom;
        faces[4] = front;
        faces[5] = back;
    }

public static CubeMap load(String rightPath, String leftPath, String topPath,
                           String bottomPath, String frontPath, String backPath) throws IOException {
    String imagedir = System.getenv("images");
    CubeMap cubeMap = loadFromFiles(imagedir + File.separator + rightPath,
            imagedir + File.separator + leftPath,
            imagedir + File.separator + topPath,
            imagedir + File.separator + bottomPath,
            imagedir + File.separator + frontPath,
            imagedir + File.separator + backPath);
    if (imagedir != null &&  cubeMap != null) {
        return cubeMap;
    }
    else {
        cubeMap = loadFromFiles(rightPath, leftPath, topPath, bottomPath, frontPath, backPath);
        if (cubeMap != null) {
            return cubeMap;
        }
        else {
            cubeMap = loadFromFiles("images" + File.separator + rightPath,
                    "images" + File.separator + leftPath,
                    "images" + File.separator + topPath,
                    "images" + File.separator + bottomPath,
                    "images" + File.separator + frontPath,
                    "images" + File.separator + backPath);
            if (cubeMap != null) {
                return cubeMap;
            }
            else {
                cubeMap = loadFromFiles(".." + File.separator + "images" + File.separator + rightPath,
                        ".." + File.separator + "images" + File.separator + leftPath,
                        ".." + File.separator + "images" + File.separator + topPath,
                        ".." + File.separator + "images" + File.separator + bottomPath,
                        ".." + File.separator + "images" + File.separator + frontPath,
                        ".." + File.separator + "images" + File.separator + backPath);
                if (cubeMap != null) {
                    return cubeMap;
                }
                else {
                    cubeMap = loadFromFiles(".." + File.separator + ".." + File.separator + "images" + File.separator + rightPath,
                            ".." + File.separator + ".." + File.separator + "images" + File.separator + leftPath,
                            ".." + File.separator + ".." + File.separator + "images" + File.separator + topPath,
                            ".." + File.separator + ".." + File.separator + "images" + File.separator + bottomPath,
                            ".." + File.separator + ".." + File.separator + "images" + File.separator + frontPath,
                            ".." + File.separator + ".." + File.separator + "images" + File.separator + backPath);
                    if (cubeMap != null) {
                        return cubeMap;
                    }
                    else {
                        cubeMap = loadFromFiles(".." + File.separator + ".." + File.separator + ".." + File.separator + "images" + File.separator + rightPath,
                                ".." + File.separator + ".." + File.separator + ".." + File.separator + "images" + File.separator + leftPath,
                                ".." + File.separator + ".." + File.separator + ".." + File.separator + "images" + File.separator + topPath,
                                ".." + File.separator + ".." + File.separator + ".." + File.separator + "images" + File.separator + bottomPath,
                                ".." + File.separator + ".." + File.separator + ".." + File.separator + "images" + File.separator + frontPath,
                                ".." + File.separator + ".." + File.separator + ".." + File.separator + "images" + File.separator + backPath);
                        if (cubeMap != null) {
                            return cubeMap;
                        }
                        else {
                            System.err.println("ERROR: Could not load image file '" + rightPath + "'.");
                        }
                    }
                }
            }
        }
    }

    return cubeMap;
}

    // Static method to load a cubemap from file paths
    public static CubeMap loadFromFiles(String rightPath, String leftPath, String topPath,
                                        String bottomPath, String frontPath, String backPath) throws IOException {
        try {
            attempts++;
            BufferedImage right = ImageIO.read(new File(rightPath));
            BufferedImage left = ImageIO.read(new File(leftPath));
            BufferedImage top = ImageIO.read(new File(topPath));
            BufferedImage bottom = ImageIO.read(new File(bottomPath));
            BufferedImage front = ImageIO.read(new File(frontPath));
            BufferedImage back = ImageIO.read(new File(backPath));
            System.out.println("Succeed  on the: "+attempts+ " time");
            return new CubeMap(right, left, top, bottom, front, back);
        }catch (Exception e){
          //  System.out.println("Failed: "+attempts+ " time");
           // e.printStackTrace();
        }
        return null; //Should never reach here
    }

    // Get pixel color from the cube map
    public Color getColor(vec3 direction) {
        int faceIndex;
        double u, v;

        // Normalize direction
        direction = direction.normalize();

        // Determine which face of the cube the direction points to
        double absX = Math.abs(direction.getX());
        double absY = Math.abs(direction.getY());
        double absZ = Math.abs(direction.getZ());

        if (absX > absY && absX > absZ) {
            // Right or left face
            if (direction.getX() > 0) {
                faceIndex = 0; // Right
                u = -direction.getZ() / absX;
                v = -direction.getY() / absX;
            } else {
                faceIndex = 1; // Left
                u = direction.getZ() / absX;
                v = -direction.getY() / absX;
            }
        } else if (absY > absZ) {
            // Top or bottom face
            if (direction.getY() > 0) {
                faceIndex = 2; // Top
                u = direction.getX() / absY;
                v = direction.getZ() / absY;
            } else {
                faceIndex = 3; // Bottom
                u = direction.getX() / absY;
                v = -direction.getZ() / absY;
            }
        } else {
            // Front or back face
            if (direction.getZ() > 0) {
                faceIndex = 4; // Front
                u = direction.getX() / absZ;
                v = -direction.getY() / absZ;
            } else {
                faceIndex = 5; // Back
                u = -direction.getX() / absZ;
                v = -direction.getY() / absZ;
            }
        }




        // Convert (u, v) to pixel coordinates
        u = (u + 1) * 0.5 * (faces[faceIndex].getWidth() - 1);
        v = (v + 1) * 0.5 * (faces[faceIndex].getHeight() - 1);
       // System.out.println("We hit the "+ faceIndex + " face");
        int x = (int) (u);
        int y = (int) (v);

        // Clamp to valid range
        x = Math.min(Math.max(x, 0), faces[faceIndex].getWidth() - 1);
        y = Math.min(Math.max(y, 0), faces[faceIndex].getHeight() - 1);

        // Return the pixel color
   int rgb = faces[faceIndex].getRGB(x, y);
    Color color = new Color(((rgb >> 16) & 0xFF) / 255.0f, ((rgb >> 8) & 0xFF) / 255.0f, (rgb & 0xFF) / 255.0f);
   //     System.out.println("Color: "+color);
       return color;
    }
}