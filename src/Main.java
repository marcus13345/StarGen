import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Main {
    private static Variable nameVar = new Variable("", "name", "0", false);
    private static int stars = 100;

    public static void main(String[] args) {
        try {
            while (true) {
                String name = "" + Integer.parseInt(nameVar.getValue());
                nameVar.setValue("" + (Integer.parseInt(nameVar.getValue()) + 1));

                Random rand = new Random();
                BufferedImage image = new BufferedImage(1024, 600, 3);
                Graphics2D graphics = (Graphics2D) image.getGraphics();


                for (int y = 0; y < 600; y++)
                    for (int x = 0; x < 1024; x++) {

                        int k = rand.nextInt(10);
                        int r = 0 + k;
                        int g = 0 + k;
                        int b = 0 + k;
                        graphics.setColor(new Color(r, g, b));
                        graphics.fillRect(x, y, 1, 1);
                    }

                // int stars = 1;
                for (int i = 0; i < stars; i++) {
                    int size = (int) Math.pow(2, rand.nextInt(4) + 3) + 1;
                    double x = rand.nextInt(1024 - size);
                    double y = rand.nextInt(600 - size);
                    int r = (rand.nextInt(4) * 10) + 10;
                    int g = (rand.nextInt(4) * 10) + 10;
                    int b = (rand.nextInt(4) * 10) + 10;
                    graphics.drawImage(getScaledImage(getStar(r, g, b), size, size), (int) x, (int) y, null);
                    if (size == 65 && rand.nextBoolean() && rand.nextBoolean() && rand.nextBoolean()) {

                        x += 32;
                        y += 32;

                        double angle = rand.nextDouble() * 360;
                        double modifier = 5 + (rand.nextDouble() * 16);

                        // UIFM UNIDENTIFIABLE FLYING MATH

                        x += Math.sin(angle) * modifier;
                        y += Math.cos(angle) * modifier;

                        size = (int) Math.pow(2, rand.nextInt(2) + 3) + 1;

                        x -= (size - 1) / 2;
                        y -= (size - 1) / 2;

                        r = (rand.nextInt(6) * 10) + 20;
                        g = (rand.nextInt(6) * 10) + 20;
                        b = (rand.nextInt(6) * 10) + 20;
                        graphics.drawImage(getScaledImage(getStar(r, g, b), size, size), (int) x, (int) y, null);
                    }
                }
                System.out.println("Saving...");

                ImageIO.write(image, "png", new File(name + ".png"));

                System.out.println("Saved!");
            }
        } catch (Exception e) {

        }
    }

    public static BufferedImage getStar(int r, int g, int b) {
        // abs(sqrt(sqrt(abs(3.14x))+sqrt(abs(3.14y)))-2.4)

        BufferedImage image = new BufferedImage(1001, 1001, 3);
        Graphics2D graphics = (Graphics2D) image.getGraphics();

        //lets make a huge image...
        for (int y = 0; y < 1001; y++)
            for (int x = 0; x < 1001; x++) {

                //lets use variables that dont make sense
                double x2 = x - 500;
                x2 /= 50;
                double y2 = y - 500;
                y2 /= 50;
                double a = 0 - (Math.sqrt(Math.sqrt(Math.abs(Math.PI * (x2))) + Math.sqrt(Math.abs(Math.PI * (y2)))) - 2.35);
                int baseColor = a >= 0 ? 255 : 0;
                int opacity = (int) ((a / 6d) * 255);
                opacity = opacity >= 255 ? 255 : opacity <= 0 ? 0 : opacity;
                graphics.setColor(new Color(baseColor == 255 ? baseColor - r : 0, baseColor == 255 ? baseColor - g : 0, baseColor == 255 ? baseColor - b : 0, opacity));
                graphics.fillRect(x, y, 1, 1);
                //whatever, this works.
            }

        try {
            //scale that shit down.
            image = getScaledImage(image, 129, 129);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

    public static final BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        double scaleX = (double) width / imageWidth;
        double scaleY = (double) height / imageHeight;
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
        return bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
    }

}
