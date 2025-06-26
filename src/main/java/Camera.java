
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

//
//import com.aparapi.Kernel;
//import com.aparapi.Range;
//import org.jocl.CL;
//import org.jocl.CLContext;
//import org.jocl.CLDevice;
//import org.jocl.CLPlatform;
//import org.jocl.CLQueue;
//import org.jocl.CLProgram;
//import org.jocl.CLKernel;
//import org.jocl.Pointer;
//import org.jocl.Sizeof;

import java.io.FileWriter;
import java.io.IOException;

public class Camera {

    //Public
    public double aspectRatio = 16.0 / 9.0;
    public int imageWidth = 400;
    public int imageHeight;
    public int maxDepth = 10; //Max numb of ray bounces into scene
    public int samplesPerPixel = 10; //Count of random samples for each pixel

    public double vfov = 90;
    public vec3 lookFrom = new vec3(0,0,0);
    public vec3 lookAt = new vec3(0,0,-1);
    public vec3 vUp = new vec3(0,1,0);
    public double defocusAngle = 0;
    public double focusDistance = 10;
    public static Color backgroundColor = new Color(1, 1, 1);
    public static CubeMap cubemap;



    //Private
    private vec3 cameraCenter;
    private vec3 pixel00Loc;
    private vec3 pixelDeltaU;
    private vec3 pixelDeltaV;
    private final int MaxColorValue = 255;
    private double pixelSamplesScale;
    private vec3 u, v, w;
    private vec3 defocusDiskU;  //Defocus disk horizontal radius
    private vec3 defocusDiskV;  //Defoucs disk vertical radius
    private Color[][] pixls;

//     Attempt 4
//    public void render(Hittable world) throws IOException {
//        initialize();
//        FileWriter fw = MakeFile();
//
//        // Total number of pixels to render
//        int totalPixels = imageHeight * imageWidth;
//        float[] pixelColors = new float[totalPixels * 3]; // Flat array for RGB values
//
//        // Initialize JOCL
//        CL.setExceptionsEnabled(true);
//        CLPlatform platform = CLPlatform.listCLPlatforms()[0];
//        CLDevice device = platform.listCLDevices()[0];
//        CLContext context = CLContext.create(platform, device);
//        CLQueue queue = context.createQueue(device);
//
//        // Allocate memory on the GPU
//        Pointer pixelColorsPointer = Pointer.to(pixelColors);
//        CLBuffer pixelColorsBuffer = context.createBuffer(pixelColorsPointer, pixelColors.length * Sizeof.cl_float);
//
//        // Load and compile the kernel
//        String kernelSource = "__kernel void renderKernel(__global float* pixelColors, int imageWidth, int samplesPerPixel) {" +
//                "    int globalId = get_global_id(0);" +
//                "    int i = globalId / imageWidth;" +
//                "    int j = globalId % imageWidth;" +
//                "    float r = 0.0f, g = 0.0f, b = 0.0f;" +
//                "    for (int sample = 0; sample < samplesPerPixel; ++sample) {" +
//                "        // Simulate rayColor logic (GPU-compatible placeholder)" +
//                "        float3 color = (float3)(0.5f, 0.5f, 0.5f);" +
//                "        r += color.x;" +
//                "        g += color.y;" +
//                "        b += color.z;" +
//                "    }" +
//                "    int pixelIndex = globalId * 3;" +
//                "    pixelColors[pixelIndex] = r / samplesPerPixel;" +
//                "    pixelColors[pixelIndex + 1] = g / samplesPerPixel;" +
//                "    pixelColors[pixelIndex + 2] = b / samplesPerPixel;" +
//                "}";
//
//        CLProgram program = context.createProgram(kernelSource).build();
//        CLKernel kernel = program.createKernel("renderKernel");
//
//        // Set kernel arguments
//        kernel.setArg(0, pixelColorsBuffer);
//        kernel.setArg(1, imageWidth);
//        kernel.setArg(2, samplesPerPixel);
//
//        // Execute the kernel
//        queue.execute(kernel, totalPixels);
//
//        // Read back the results
//        queue.readBuffer(pixelColorsBuffer, true);
//
//        // Write the colors to the file
//        for (int i = 0; i < imageHeight; i++) {
//            for (int j = 0; j < imageWidth; j++) {
//                int pixelIndex = (i * imageWidth + j) * 3;
//                Color pixelColor = new Color(
//                        pixelColors[pixelIndex],
//                        pixelColors[pixelIndex + 1],
//                        pixelColors[pixelIndex + 2]
//                );
//                Color.write_color(fw, vec3.multiply(pixelSamplesScale, pixelColor));
//            }
//        }
//
//        fw.flush();
//        fw.close();
//
//        // Release resources
//        kernel.release();
//        program.release();
//        pixelColorsBuffer.release();
//        queue.release();
//        context.release();
//    }


    //Attempt three
//    public void render(Hittable world) throws IOException {
//        initialize();
//        FileWriter fw = MakeFile();
//
//        // Total number of pixels to render
//        int totalPixels = imageHeight * imageWidth;
//        //Color pixelColors = new Color() // Flat array for RGB values
//
//        // Define the GPU kernel
//        Kernel renderKernel = new Kernel() {
//            @Override
//            public void run() {
//                int globalId = getGlobalId(); // Unique ID for each thread
//
//                int i = globalId / imageWidth; // Row index
//                int j = globalId % imageWidth; // Column index
//
//                float red = 0, green = 0, blue = 0;
//
//                for (int sample = 0; sample < samplesPerPixel; sample++) {
//                    Ray r = getRay(j, i); // Replace with GPU-compatible logic
//                    vec3 color = rayColor(r, world, maxDepth); // Replace with GPU-compatible logic
//                    red += color.getX();
//                    green += color.getY();
//                    blue += color.getZ();
//                }
//
//                int pixelIndex = globalId * 3;
//                pixls[i][j] = new Color(red, green, blue);
//            }
//        };
//
//        // Execute the kernel on the GPU
//        renderKernel.execute(Range.create(totalPixels));
//
//        // Write the colors to the file
//        for (int i = 0; i < imageHeight; i++) {
//            for (int j = 0; j < imageWidth; j++) {
//                Color.write_color(fw, vec3.multiply(pixelSamplesScale, pixls[i][j]));
//            }
//        }
//
//        fw.flush();
//        fw.close();
//        renderKernel.dispose(); // Clean up GPU resources
//    }
//


  //  Second Take on the multi thread render method: Current Best without GPU enhancement
    public void render(Hittable world) throws IOException, InterruptedException {
        initialize();
        FileWriter fw = MakeFile();

        int numThreads = Runtime.getRuntime().availableProcessors() ; // Number of threads
        System.out.println("We have: "+numThreads+" threads available");
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        // Create a thread-safe task queue
        BlockingQueue<PixelTask> taskQueue = new LinkedBlockingQueue<>();
        AtomicInteger pixelsRendered = new AtomicInteger(0); // Progress tracker
        int totalPixels = imageHeight * imageWidth;

        // Populate the queue with all pixel tasks
        for (int i = 0; i < imageHeight; i++) {
            for (int j = 0; j < imageWidth; j++) {
                taskQueue.add(new PixelTask(i, j));
            }
        }

        // Submit worker threads
        for (int t = 0; t < numThreads; t++) {
            executor.submit(() -> {
                try {
                    while (!taskQueue.isEmpty()) {
                        PixelTask task = taskQueue.poll();
                        if (task != null) {
                            renderPixel(task, world, fw, pixelsRendered, totalPixels);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);


        //Time to write to the file
        for(int i = 0; i < imageHeight; i++){
            for(int j = 0; j < imageWidth; j++){
                Color.write_color(fw, vec3.multiply(pixelSamplesScale, pixls[i][j]));
            }
        }


        fw.flush();
        fw.close();
    }

    private void renderPixel(PixelTask task, Hittable world, FileWriter fw, AtomicInteger pixelsRendered, int totalPixels) throws IOException {
        int i = task.row;
        int j = task.col;

        Color pixelColor = new Color(0, 0, 0);
        for (int sample = 0; sample < samplesPerPixel; sample++) {
            Ray r = getRay(j, i);
            vec3 vecRayColor = vec3.add(pixelColor, rayColor(r, world, maxDepth));
            pixelColor = new Color(vecRayColor.getX(), vecRayColor.getY(), vecRayColor.getZ());
        }

     synchronized (this) { // Synchronize access to the pixls array
    pixls[i][j] = pixelColor;
}

        // Update progress
        int rendered = pixelsRendered.incrementAndGet();
        if (rendered % (totalPixels / 100) == 0 || rendered == totalPixels) { // Report every 1% or when done
            System.out.printf("Progress: %.2f%%%n", (rendered * 100.0) / totalPixels);
        }
    }

    // A simple class to represent a pixel task
    private static class PixelTask {
        final int row;
        final int col;

        PixelTask(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }



    //THE OG RENDER METHOD

//    public void render(Hittable world) throws IOException {
//        initialize();
//        FileWriter fw = MakeFile();
//
//        //Render Image
//        double percentageLeft;
//        for (int i = 0; i < imageHeight; i++) {
//           percentageLeft = ((imageHeight-i) * 100/imageHeight);
//           System.out.println(percentageLeft+"% Left");
//            for (int j = 0; j < imageWidth; j++) {
//                Color pixelColor = new Color(0, 0, 0);
//             //   System.out.println("I: " + i + ", J: " + j);
//                for (int sample = 0; sample < samplesPerPixel; sample++) {
//                    Ray r = getRay(j, i);
//                    vec3 vecRayColor = vec3.add(pixelColor, rayColor(r, world, maxDepth));
//                    pixelColor = new Color(vecRayColor.getX(), vecRayColor.getY(), vecRayColor.getZ());
//
//                }
//                Color.write_color(fw, vec3.multiply(pixelSamplesScale, pixelColor));
//            }
//            if ((imageWidth - i) == 0) System.out.println("No more lines");
//        }
//        fw.flush();
//    }

    private void initialize() {
        // Camera setup

        // Calculate the image height, and ensure that it's at least 1
        imageHeight = (int) (imageWidth / aspectRatio);
        imageHeight = Math.max(imageHeight, 1);
        pixls = new Color[imageHeight][imageWidth];
        pixelSamplesScale = 1.0 / samplesPerPixel;
        cameraCenter = lookFrom;

        //Determine viewport dimensions
     //   double focalLength = vec3.subtract(lookFrom,lookAt).length();
        double theta = Utils.degreesToRadians(vfov);
        double h = Math.tan(theta/2);
        double viewportHeight = 2 * h * focusDistance;
        double viewportWidth = viewportHeight * ((double) imageWidth / imageHeight);

        //Calculate the u,v,w unit basis vectors for the camera coordinate frame
        w = vec3.unitVector(vec3.subtract(lookFrom,lookAt));
        u = vec3.unitVector(vec3.cross(vUp,w));
        v = vec3.cross(w,u);


        // Calculate the vectors across the horizontal and down the vertical viewport edges
        vec3 viewportU =  vec3.multiply(viewportWidth,u); // Vector across viewport horizontal edge
        vec3 viewportV =  vec3.multiply(-viewportHeight,v); // Vector down viewport vertical edge

        // Calculate the horizontal and vertical delta vectors from pixel to pixel
        pixelDeltaU = viewportU.divide(imageWidth);
        pixelDeltaV = viewportV.divide(imageHeight);



        //Right hand world space
        // Calculate the location of the upper-left pixel
        vec3 viewportUpperLeft = cameraCenter.clone();
        viewportUpperLeft = vec3.subtract(viewportUpperLeft,  vec3.multiply(focusDistance, w));
        //System.out.println("Focal Length: "+viewportUpperLeft.getZ());
        viewportUpperLeft = vec3.subtract(viewportUpperLeft, viewportU.divide(2));
        //System.out.println("X value: "+viewportUpperLeft.getX());
        viewportUpperLeft = vec3.subtract(viewportUpperLeft, viewportV.divide(2));
        //System.out.println("Y value: "+viewportUpperLeft.getY());

        //Calculate the camera defocus disk basis vectors.
        double defocusRadius = focusDistance * Math.tan(Utils.degreesToRadians(defocusAngle/2));
        defocusDiskU = vec3.multiply(defocusRadius, u);
        defocusDiskV = vec3.multiply(defocusRadius, v);


        pixel00Loc = viewportUpperLeft
                .add(pixelDeltaU.multiply(0.5))
                .add(pixelDeltaV.multiply(0.5));
    }

    public static Color rayColor(Ray r, Hittable world, int depth) {
            // If we've exceeded the ray bounce limit, no more light is gathered.
            if (depth <= 0) {
                return new Color(0, 0, 0);
            }

            HitRecord rec = new HitRecord();

            // If the ray hits nothing, return the background color.
            if (!world.hit(r, new Interval(0.001, Double.POSITIVE_INFINITY), rec)) {
                // Return cube map color
                return cubemap.getColor(r.getDirection());
            }

            Color colorFromEmission = rec.materials.emitted(rec.u, rec.v, rec.point);
            ScatterReturn scatterReturn = new ScatterReturn();

            if (!rec.materials.scatter(r, rec, scatterReturn)) {
                return colorFromEmission;
            }

       // System.out.println("Color: "+scatterReturn.attenuation.getX()+", "+scatterReturn.attenuation.getY()+", "+scatterReturn.attenuation.getZ());

            vec3 colorFromScatter = vec3.multiplyVec(scatterReturn.attenuation, rayColor(scatterReturn.scattered, world, depth - 1));
            vec3 finalVec = vec3.add(colorFromEmission, colorFromScatter);

            return new Color(finalVec.getX(), finalVec.getY(), finalVec.getZ());
        }

    private void writeColor(Color pixelColor) {
        // Output the pixel color
        System.out.printf("%d %d %d%n",
                (int) (255.999 * pixelColor.r),
                (int) (255.999 * pixelColor.g),
                (int) (255.999 * pixelColor.b));
    }

    private FileWriter MakeFile() throws IOException {
        //Make File
        File imagePPM = null;
        FileWriter fw = null;
        String fileName = "Ship.ppm" + LocalDate.now();
        try {
            imagePPM = new File(fileName);
            if (imagePPM.createNewFile()) {
                System.out.println("File created: " + imagePPM.getName());
            } else {
                System.out.println("File already exists. We are overwriting it");
            }
            fw = new FileWriter(imagePPM);

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        fw.write("P3\n");
        fw.write(imageWidth + " " + imageHeight + "\n");
        fw.write(MaxColorValue + "\n");
        return fw;
    }

    public Ray getRay(int i, int j) {
        // Construct a camera ray originating from the origin and directed at randomly sampled
        // point around the pixel location i, j.

        vec3 offset = sampleSquare();
        vec3 line1 = vec3.multiply((i + offset.getX()), pixelDeltaU);
        vec3 line2 = vec3.multiply((j + offset.getY()), pixelDeltaV);

        vec3 pixelSample = vec3.add(pixel00Loc, vec3.add(line1, line2));

        vec3 rayOrigin;
        if(defocusAngle <= 0){
            rayOrigin = cameraCenter;
        }
        else {
            rayOrigin = defocusDiskSample();
        }

        vec3 rayDirection = vec3.subtract(pixelSample, rayOrigin);
        double rayTime = Utils.randomDouble();

        return new Ray(rayOrigin, rayDirection, rayTime);
    }

    public vec3 sampleSquare() {
        // Returns the vector to a random point in the [-.5,-.5]-[+.5,+.5] unit square.
        return new vec3(Utils.randomDouble() - 0.5, Utils.randomDouble() - 0.5, 0);
    }

    public vec3 defocusDiskSample() {
        // Returns a random point in the camera defocus disk.
        vec3 p =  vec3.randomInUnitDisk();
        return vec3.add(cameraCenter, vec3.add(vec3.multiply(p.getX() , defocusDiskU), vec3.multiply(p.getY(), defocusDiskV)));
    }


}
