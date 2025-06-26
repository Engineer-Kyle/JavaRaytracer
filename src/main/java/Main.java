import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {


        // bouncingSpheres();
        // checkerWorld();
        // OldEarth();
//        perlinSpheres();
        // quads();
        // simple_light();
        //  cornellBox();
        //Triangle();
        //Trianglemesh();
        // TrianglemeshFromImage();
        FinalScene();
        // FinalScene();
    }

    static void bouncingSpheres() throws IOException, InterruptedException {
        // Create a HittableList for the scene
        HittableList world = new HittableList();
        Materials MaterialGround = new Lambertian(new Color(.8, .8, .0)); // Ugly Yello
        world.add(new Sphere(new vec3(0.0, -1000, -1.0), 1000.0, MaterialGround));

        for (int i = -11; i < 11; i++) {
            for (int j = -11; j < 11; j++) {
                double chooseMat = Utils.randomDouble();
                vec3 center = new vec3(i + .9 * Utils.randomDouble(), .2, j + .9 * Utils.randomDouble());

                if (vec3.subtract(center, new vec3(4, .2, 0)).length() > .9) {
                    Materials sphereMaterial;

                    if (chooseMat < .8) {
                        //difuse
                        vec3 tvec = vec3.multiplyVec(vec3.random(), vec3.random());
                        Color albedo = new Color(tvec.getX(), tvec.getY(), tvec.getZ());
                        sphereMaterial = new Lambertian(albedo);
                        vec3 center2 = vec3.add(center, new vec3(0, Utils.randomDouble(0, .5), 0));
                        world.add(new Sphere(center, center2, 0.2, sphereMaterial));
                    } else if (chooseMat < .95) {
                        //metal
                        vec3 tvec = vec3.random(.5, 1);
                        Color albedo = new Color(tvec.getX(), tvec.getY(), tvec.getZ());
                        double fuzz = Utils.randomDouble(0., .5);
                        sphereMaterial = new Metal(albedo, fuzz);
                        world.add(new Sphere(center, 0.2, sphereMaterial));
                    } else {
                        //Glass
                        sphereMaterial = new Dielectic(1.5);
                        world.add(new Sphere(center, 0.2, sphereMaterial));
                    }
                }
            }
        }

        //Add some big balls ;)
        Materials bigBall1 = new Dielectic(1.5);
        Materials bigBall3 = new Metal(new Color(.7, .6, .5), 0);
        Materials bigBall2 = new Lambertian(new Color(.2, .9, .1));
        Materials bigBall4 = new Metal(new Color(.2, .6, .9), 0);
        Materials bigBall5 = new Metal(new Color(.9, .2, .1), 1);

        world.add(new Sphere(new vec3(4, 1, 0.0), .7, bigBall3));
        world.add(new Sphere(new vec3(-4, 1, 0.0), .6, bigBall2));
        world.add(new Sphere(new vec3(0.0, 1, 0.0), .5, bigBall1));

        HittableList world2 = new HittableList();
        world2.add(new BVHNode(world));


        Camera cam = new Camera();
        cam.aspectRatio = 16.0 / 9.0;
        cam.imageWidth = 800;
        cam.samplesPerPixel = 200;
        cam.maxDepth = 50;

        cam.vfov = 20;
        cam.lookFrom = new vec3(13, 2, 3);
        cam.lookAt = new vec3(0, 0, 0);
        cam.vUp = new vec3(0, 1, 0);

        cam.defocusAngle = .6;
        cam.focusDistance = 10;

        cam.render(world2);
    }

    static void checkerWorld() throws IOException, InterruptedException {
        Texture checker = new CheckerTexture(.32, new Color(.2, .3, 1), new Color(.9, .9, .9));
        HittableList world = new HittableList();
        Materials MaterialGround = new Lambertian(checker); // Ugly Yello
        world.add(new Sphere(new vec3(0.0, -10, 0), 10.0, MaterialGround));
        world.add(new Sphere(new vec3(0.0, 10, 0), 10.0, MaterialGround));


        HittableList world2 = new HittableList();
        world2.add(new BVHNode(world));

        Camera cam = new Camera();
        cam.aspectRatio = 16.0 / 9.0;
        cam.imageWidth = 400;
        cam.samplesPerPixel = 100;
        cam.maxDepth = 50;

        cam.vfov = 20;
        cam.lookFrom = new vec3(13, 2, 3);
        cam.lookAt = new vec3(0, 0, 0);
        cam.vUp = new vec3(0, 1, 0);

        cam.defocusAngle = .6;
        cam.focusDistance = 10;

        cam.render(world2);
    }


    static void OldEarth() throws IOException, InterruptedException {
        ImageTexture earthTexture = new ImageTexture("OldMapOfEarth.jpg");

        HittableList world = new HittableList();
        Materials EarthSurface = new Lambertian(earthTexture);
        Materials Pludo = new Metal(new Color(0.752941, 0.752941, 0.752941), 0); // Shiny Silver

        world.add(new Sphere(new vec3(0, 0, -2), 2.0, EarthSurface));
        world.add(new Sphere(new vec3(0, 0, 2), 2.0, Pludo));

        HittableList world2 = new HittableList();
        world2.add(new BVHNode(world));

        Camera cam = new Camera();
        cam.aspectRatio = 16.0 / 9.0;
        cam.imageWidth = 1000;
        cam.samplesPerPixel = 200;
        cam.maxDepth = 50;

        cam.vfov = 20;
        cam.lookFrom = new vec3(13, 2, 3);
        cam.lookAt = new vec3(0, 0, 0);
        cam.vUp = new vec3(0, 1, 0);

        cam.defocusAngle = .6;
        cam.focusDistance = 10;

        cam.render(world2);
    }


    static void perlinSpheres() throws IOException, InterruptedException {
        HittableList world = new HittableList();

        Texture pertext = new NoiseTexture(4);
        Materials perlinMaterial = new Lambertian(pertext);
        world.add(new Sphere(new vec3(0, -1000, 0), 1000, perlinMaterial));
        world.add(new Sphere(new vec3(0, 2, 0), 2, perlinMaterial));

        HittableList world2 = new HittableList();
        world2.add(new BVHNode(world));

        Camera cam = new Camera();

        cam.aspectRatio = 16.0 / 9.0;
        cam.imageWidth = 400;
        cam.samplesPerPixel = 100;
        cam.maxDepth = 50;

        cam.vfov = 20;
        cam.lookFrom = new vec3(13, 2, 3);
        cam.lookAt = new vec3(0, 0, 0);
        cam.vUp = new vec3(0, 1, 0);

        cam.defocusAngle = 0;

        cam.render(world2);
    }


    static void quads() throws IOException, InterruptedException {
        HittableList world = new HittableList();

        Materials left_red = new Lambertian(new Color(1.0, 0.2, 0.2));
        Materials back_green = new Lambertian(new Color(0.2, 1.0, 0.2));
        Materials right_blue = new Lambertian(new Color(0.2, 0.2, 1.0));
        Materials upper_orange = new Lambertian(new Color(1.0, 0.5, 0.0));
        Materials lower_teal = new Lambertian(new Color(0.2, 0.8, 0.8));

        // Quads
        world.add(new Quad(new vec3(-3, -2, 5), new vec3(0, 0, -4), new vec3(0, 4, 0), left_red));
        world.add(new Quad(new vec3(-2, -2, 0), new vec3(4, 0, 0), new vec3(0, 4, 0), back_green));
        world.add(new Quad(new vec3(3, -2, 1), new vec3(0, 0, 4), new vec3(0, 4, 0), right_blue));
        world.add(new Quad(new vec3(-2, 3, 1), new vec3(4, 0, 0), new vec3(0, 0, 4), upper_orange));
        world.add(new Quad(new vec3(-2, -3, 5), new vec3(4, 0, 0), new vec3(0, 0, -4), lower_teal));

//        HittableList world2 = new HittableList();
//        world2.add(new BVHNode(world));

        Camera cam = new Camera();
        cam.aspectRatio = 1;
        cam.imageWidth = 400;
        cam.samplesPerPixel = 100;
        cam.maxDepth = 50;

        cam.vfov = 80;
        cam.lookFrom = new vec3(0, 0, 9);
        cam.lookAt = new vec3(0, 0, 0);
        cam.vUp = new vec3(0, 1, 0);

        cam.defocusAngle = 0;

        cam.render(world);
    }


    static void simple_light() throws IOException, InterruptedException {

        ImageTexture earthTexture = new ImageTexture("OldMapOfEarth.jpg");
        HittableList world = new HittableList();
        Materials EarthSurface = new Lambertian(earthTexture);
        Materials ground = new Metal(new Color(0.752941, 0.752941, 0.752941), 0); // Shiny Silver
        Materials light = new DiffuseLight(new Color(1, .87450980392156862745098039215686, 0));
        world.add(new Sphere(new vec3(0, 2, 0), 2.0, EarthSurface));
        world.add(new Sphere(new vec3(0, -1000, 0), 1000.0, ground));
        world.add(new Sphere(new vec3(0, 7, -7), 4, light));
        world.add(new Sphere(new vec3(0, 7, 7), 4, light));

        HittableList world2 = new HittableList();
        world2.add(new BVHNode(world));
        world = world2;

        Camera cam = new Camera();
        cam.aspectRatio = 16.0 / 9.0;
        cam.imageWidth = 1200;
        cam.samplesPerPixel = 10000;
        cam.maxDepth = 50;
        cam.backgroundColor = new Color(0, 0, 0);

        cam.vfov = 20;
        cam.lookFrom = new vec3(26, 3, 6);
        cam.lookAt = new vec3(0, 2, 0);
        cam.vUp = new vec3(0, 1, 0);

        cam.defocusAngle = 0;

        cam.render(world);
    }

    static void cornellBox() throws IOException, InterruptedException {
        HittableList world = new HittableList();

        Materials red = new Lambertian(new Color(0.65, 0.05, 0.05));
        Materials white = new Lambertian(new Color(0.73, 0.73, 0.73));
        Materials green = new Lambertian(new Color(0.12, 0.45, 0.15));
        Materials light = new DiffuseLight(new Color(15, 15, 15));

        //world.add(new Quad(new vec3(555, 0, 0), new vec3(0, 555, 0), new vec3(0, 0, 555), green));
        // world.add(new Quad(new vec3(0, 0, 0), new vec3(0, 555, 0), new vec3(0, 0, 555), red));
        world.add(new Quad(new vec3(343, 554, 332), new vec3(-130, 0, 0), new vec3(0, 0, -105), light));
        //  world.add(new Quad(new vec3(0, 0, 0), new vec3(555, 0, 0), new vec3(0, 0, 555), white));
        //world.add(new Quad(new vec3(555, 555, 555), new vec3(-555, 0, 0), new vec3(0, 0, -555), white));
        //world.add(new Quad(new vec3(0, 0, 555), new vec3(555, 0, 0), new vec3(0, 555, 0), white));
        HittableList box = Quad.box(new vec3(130, 0, 65), new vec3(295, 165, 230), light);

        world.add(Quad.box(new vec3(130, 0, 65), new vec3(295, 165, 230), light));
        world.add(Quad.box(new vec3(265, 0, 295), new vec3(430, 330, 460), light));


        //HittableList world2 = new HittableList();
        //  world2.add(new BVHNode(world));

        Camera cam = new Camera();
        cam.aspectRatio = 1.0;
        cam.imageWidth = 600;
        cam.samplesPerPixel = 200;
        cam.maxDepth = 50;
        cam.backgroundColor = new Color(0, 0, 0);

        cam.vfov = 40;
        cam.lookFrom = new vec3(278, 278, -800);
        cam.lookAt = new vec3(278, 278, 0);
        cam.vUp = new vec3(0, 1, 0);

        cam.defocusAngle = 0;

        cam.render(world);
    }

    static void Triangle() throws IOException, InterruptedException {
        HittableList world = new HittableList();

        Materials light = new DiffuseLight(new Color(1, 0.0, 0.0));
        Materials metal = new Metal(new Color(0.8, 0.8, 0.7), 1);
        Materials glass = new Dielectic(.9);
        Texture pertext = new NoiseTexture(4);
        Materials lambertian = new Lambertian(new Color(0.5, 0.5, 0.5));
        Materials ground = new Lambertian(pertext); // Shiny Silver

        world.add(new Sphere(new vec3(0.0, -1000, -1.0), 1000.0, ground));

        world.add(new Triangle(new vec3(1, 0, 0), new vec3(0, 1, 0), new vec3(0, 0, 1), metal));
        world.add(new Triangle(new vec3(-1, 1, 0), new vec3(-2, 2, 0), new vec3(-2, 1, 1), glass));
        //world.add(new Triangle(new vec3(0, -1, -1), new vec3(-1, 0, -1), new vec3(-1, -1, 0), metal));
        world.add(new Triangle(new vec3(3, 0, 0), new vec3(2, 1, 0), new vec3(2, 0, 1), light));
        // world.add(new Triangle(new vec3(1, 0, 0), new vec3(0, 2, 0), new vec3(0, 0, 1), lambertian));

        Camera cam = new Camera();
        cam.aspectRatio = 16.0 / 9.0;
        cam.imageWidth = 400;
        cam.samplesPerPixel = 100;
        cam.maxDepth = 50;

        cam.vfov = 20;
        cam.lookFrom = new vec3(13, 2, 3);
        cam.lookAt = new vec3(0, 0, 0);
        cam.vUp = new vec3(0, 1, 0);

        cam.defocusAngle = .6;
        cam.focusDistance = 10;

        cam.render(world);
    }

    static void Trianglemesh() throws IOException, InterruptedException {
        HittableList world = new HittableList();

        Materials light = new DiffuseLight(new Color(1, 0.0, 0.0));
        Materials metal = new Metal(new Color(0.8, 0.8, 0.7), 1);
        Materials glass = new Dielectic(.9);
        Texture pertext = new NoiseTexture(4);
        Materials lambertian = new Lambertian(new Color(0.2, 0.5, 0.8));
        Materials ground = new Metal(new Color(0.752941, 0.752941, 0.752941), 0); // Shiny Silver

        //   Materials ground = new Lambertian(pertext); // Shiny Silver

        world.add(new Sphere(new vec3(0.0, -1000, -1.0), 1000.0, ground));

        // Create a list of vertices
        List<vec3> vertices = new ArrayList<>();
        vertices.add(new vec3(0, 0, 0));
        vertices.add(new vec3(1, 0, 0));
        vertices.add(new vec3(0, 1, 0));
        vertices.add(new vec3(0, 0, 1));
        vertices.add(new vec3(1, 1, 2));
        vertices.add(new vec3(-1, 0, -1));
        vertices.add(new vec3(2, 0, 1));
        vertices.add(new vec3(1, 0, 1));
        vertices.add(new vec3(2, 1, 0));
        vertices.add(new vec3(0, 0, 2));

        // Create a list of indices
        List<Integer> indices = new ArrayList<>();
        indices.add(0);
        indices.add(1);
        indices.add(2);
        indices.add(3);
        indices.add(4);
        indices.add(5);
        indices.add(6);
        indices.add(7);
        indices.add(5);

        ImageTexture earthTexture = new ImageTexture("OldMapOfEarth.jpg");
        Materials EarthSurface = new Lambertian(earthTexture);

        // Create a TriangleMesh instance
        Mesh triangleMesh = new Mesh(vertices, indices, EarthSurface);
        world.add(triangleMesh);


        Camera cam = new Camera();
        cam.aspectRatio = 16.0 / 9.0;
        cam.imageWidth = 400;
        cam.samplesPerPixel = 100;
        cam.maxDepth = 50;

        cam.vfov = 20;
        cam.lookFrom = new vec3(13, 2, 3);
        cam.lookAt = new vec3(0, 0, 0);
        cam.vUp = new vec3(0, 1, 0);

        cam.defocusAngle = .6;
        cam.focusDistance = 10;

        cam.render(world);
    }


    static void TrianglemeshFromImage() throws IOException, InterruptedException {

        ObjFile SailingShip = new ObjFile("objFiless/Fancy_Ship.obj");
        // ObjFile brain = new ObjFile("objFiless/brain.obj");
        //  ObjFile rocketship = new ObjFile("objFiless/Rocket_Ship.obj");
        //     ObjFile starfighter = new ObjFile("objFiless/N-1StarFighter.obj");
        // Materials metal = new Metal(new Color(0.752941,0.752941,0.752941),0 ); // Shiny Silver
        // ImageTexture earthTexture = new ImageTexture("OldMapOfEarth.jpg");
        //   Materials EarthSurface = new Lambertian(earthTexture);
        HittableList world = new HittableList();
        //  ConstantMedium smoke = new ConstantMedium(new Sphere(new vec3(0, 10, 0), 10, new Dielectic(1.5)), 0.1, new Color(1, 0.5, 0));
        //  world.add(smoke);

        Materials metal = new Metal(new Color(0.8, 0.8, 0.7), 0);
        Lambertian blueLam = new Lambertian(new Color(65 / 255.0, 105 / 255.0, 225 / 255.0));

        // Create a TriangleMesh instance
        //Mesh brainMesh = new Mesh(brain.getVertices(), brain.getFaces(), metal);
        //  world.add(brainMesh);
        //   Mesh triangleMesh = new Mesh(standfordDragon.getVertices(), standfordDragon.getFaces(), standfordDragon.getNormals(), standfordDragon.getTexCoords(), metal);
        //    world.add(triangleMesh);
        //  Mesh newBrainMesh = new Mesh(brain.getVertices(), brain.getFaces(), metal);
        //  Mesh farRightBrainMesh = new Mesh(brain.getVertices(), brain.getFaces(), metal);

        // Mesh starfighterMesh = new Mesh(starfighter.getVertices(), starfighter.getFaces(), starfighter.getNormals(), starfighter.getTexCoords(), metal);
        //   Mesh starfighterMesh = new Mesh(SailingShip.getVertices(), SailingShip.getFaces(), metal);
        //   world.add(starfighterMesh);

        // brainMesh.scale(.2); // Scale the model by a factor of 0.2
        //brainMesh.translate(8, 0, 0); // Translate the big brain to the right
        // farRightBrainMesh.translate(4, 0, 0); // Translate the big brain to the right
        // farRightBrainMesh.scale(.2); // Scale the model by a factor of 0.2

        //world.add(newBrainMesh);
        //
        CubeMap cubemap = CubeMap.load(
                "right.png",
                "left.png",
                "top.png",
                "bottom.png",
                "front.png",
                "back.png"
        );

        HittableList world2 = new HittableList();
        world2.add(new BVHNode(world));

        Camera cam = new Camera();
        cam.aspectRatio = 16.0 / 9.0;
        cam.imageWidth = 400;
        cam.samplesPerPixel = 100;
        cam.maxDepth = 50;

        cam.vfov = 30;
        cam.lookFrom = new vec3(50, 40, 70);
        cam.lookAt = new vec3(1, 15, 0);
        cam.vUp = new vec3(0, 1, 0);

        cam.defocusAngle = .6;
        cam.focusDistance = 70;

        cam.cubemap = cubemap;
        System.out.println("Rendering");
        cam.render(world2);
    }


    static void FinalScene() throws IOException, InterruptedException {

        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Number of CPU cores available: " + cores);


        ObjFile SailingShip = new ObjFile("objFiless/Fancy_Ship.obj");
        ObjFile brain = new ObjFile("objFiless/brain.obj");
        // ObjFile rocketship = new ObjFile("objFiless/Rocket_Ship.obj");
        ObjFile starfighter = new ObjFile("objFiless/N-1StarFighter.obj");
        ObjFile Kraken = new ObjFile("objFiless/Kraken_v1.obj");
        ObjFile Tardis = new ObjFile("objFiless/Tardis.obj");
        ObjFile Chest = new ObjFile("objFiless/chest.obj");
        ObjFile Teapot = new ObjFile("objFiless/teapot.obj");
        //ObjFile Ship1 = new ObjFile("objFiless/teapot.obj");


        ImageTexture LOTRMapTexture = new ImageTexture("LOTRMap.jpg");
        Materials LOTRMap = new Lambertian(LOTRMapTexture);
        // ImageTexture earthTexture = new ImageTexture("OldMapOfEarth.jpg");
        //   Materials EarthSurface = new Lambertian(earthTexture);
        Lambertian blueLam = new Lambertian(new Color(65 / 255.0, 105 / 255.0, 225 / 255.0));
        Lambertian black = new Lambertian(new Color(0, 0, 0));
        Lambertian marronRedLam = new Lambertian(new Color(128 / 255.0, 0 / 255.0, 0 / 255.0));
        Materials shinyMetal = new Metal(new Color(0.752941, 0.752941, 0.752941), 0); // Shiny Silver
        Materials MildShinyMetal = new Metal(new Color(0, 0, 0), .15); // Shiny Silver
        Materials groundMetal = new Metal(new Color (0.752941, 0.752941, 0.752941), .15); // Shiny Silver
        Materials dullMetal = new Metal(new Color(0.852941, 0.852941, 0.852941), 1); // Shiny Silver
        Materials TartisBlue = new Metal(new Color(65 / 255.0, 105 / 255.0, 225 / 255.0), 1); // Shiny Silver


        HittableList world = new HittableList();

        Sphere baseSpere = new Sphere(new vec3(0.0, -10000, -1.0), 10000.0, groundMetal);
        // baseSpere.rotate(new vec3(0, 0, 1), 90);
         world.add(baseSpere);



        // Mesh SailingShipMesh = new Mesh(SailingShip.getVertices(), SailingShip.getFaces(), SailingShip.getNormals(), SailingShip.getTexCoords(), metal);
        Mesh SailingShipMesh = new Mesh(SailingShip.getVertices(), SailingShip.getFaces(), shinyMetal);
        Mesh StarfighterMesh = new Mesh(starfighter.getVertices(), starfighter.getFaces(), shinyMetal);
        Mesh KrakenMesh = new Mesh(Kraken.getVertices(), Kraken.getFaces(), dullMetal);
        Mesh TardisMesh = new Mesh(Tardis.getVertices(), Tardis.getFaces(), TartisBlue);
        Mesh ChestMesh = new Mesh(Chest.getVertices(), Chest.getFaces(), dullMetal);
        Mesh BrainMesh = new Mesh(brain.getVertices(), brain.getFaces(), dullMetal);
        Mesh TeapotMesh = new Mesh(Teapot.getVertices(), Teapot.getFaces(), dullMetal);

        SailingShipMesh.rotate(45, 'y');
       // world.add(StarfighterMesh);
        Sphere cannonBall1 = new Sphere(new vec3(-26, 0, -10),new vec3(-24, 0, -9), 2, MildShinyMetal);
        Sphere cannonBall2 = new Sphere(new vec3(-21, -3, -8),new vec3(-19, -3, -7), 2, MildShinyMetal);
        Sphere cannonBall3 = new Sphere(new vec3(-16, -6, -10),new vec3(-14, -6, -5), 2, MildShinyMetal);
        Sphere cannonBall4 = new Sphere(new vec3(-11, -9, -10),new vec3(-9, -9, -3), 2, MildShinyMetal);
        world.add(cannonBall1);
        world.add(cannonBall2);
        world.add(cannonBall3);
        world.add(cannonBall4);
        world.add(SailingShipMesh);
//        world.add(KrakenMesh);
        //TardisMesh.scale(6);
        TardisMesh.translate(-40, 45, 10);
        TeapotMesh.translate(21, 22, 10);
      //  TeapotMesh.scale(2);
       //world.add(BrainMesh);
       // world.add(TeapotMesh);
        world.add(TardisMesh);
       // world.add(ChestMesh);
        CubeMap cubemap = CubeMap.load(
                "right.png",
                "left.png",
                "top.png",
                "bottom.png",
                "front.png",
                "back.png"
        );

        ImageTexture earthTexture = new ImageTexture("OldMapOfEarth.jpg");
        Materials EarthSurface = new Lambertian(earthTexture);

        vec3 FireCenter = new vec3(-17, 4, -20);
        ConstantMedium FireSmoke = new ConstantMedium(new Sphere(FireCenter, 10, new Dielectic(1.5)), 0.10, new Color(1, 0.5, 0));
        Materials Fire = new DiffuseLight(new Color(.5, 0, 0));
        ConstantMedium FireLight = new ConstantMedium(new Sphere(FireCenter, 10, Fire), 0.02, new Color(1, 0.5, 0));


        Sphere ShipPropulsion = new Sphere(new vec3(-13, 4, -20), 4, Fire);
        Materials SunLight = new DiffuseLight(new Color(1, .87450980392156862745098039215686, 0));
        world.add(ShipPropulsion);
        world.add(FireSmoke);
        world.add(FireLight);
        Sphere oldEarth = new Sphere(new vec3(500, 50, -12000), 2000.0, EarthSurface);
        Sphere Lotr = new Sphere(new vec3(-50, 25, -2), 20.0, LOTRMap);
       // oldEarth.rotate('z', 23.5);
        Sphere Sun = new Sphere(new vec3(10000, 10000, -20000), 7000, SunLight);
        world.add(Sun);
        world.add(Lotr);
        world.add(oldEarth);

        HittableList world2 = new HittableList();
        world2.add(new BVHNode(world));

        Camera cam = new Camera();
        cam.aspectRatio = 16.0 / 9.0;
        cam.imageWidth = 800;
        cam.samplesPerPixel = 200;
        cam.maxDepth = 50;

        cam.vfov = 30;
        cam.lookFrom = new vec3(0, 20, 150);
        cam.lookAt = new vec3(0, 15, 0);
        cam.vUp = new vec3(0, 1, 0);

        cam.defocusAngle = .3;
        cam.focusDistance = 150;


        cam.cubemap = cubemap;
        System.out.println("Rendering");
        cam.render(world2);
        System.out.println("Done");
    }

//    public Sphere[] makeFireBall(vec3 FireCenter, double radius) {
//
//
//
//        Sphere[] fireBall = new Sphere[1000];
//        for (int k = 0; k < 1000; k++) {
//            vec3 center2 = vec3.add(FireCenter, new vec3(Utils.randomDouble(0, .5),Utils.randomDouble(0, .5),Utils.randomDouble(0, .5)));
//            world.add( new Sphere(center2, .2, Fire));
//        }
//        Sphere ShipPropulsion = new Sphere(new vec3(-13, 4, -20), .4, Fire);
//    }
}
