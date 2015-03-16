import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * simple little play toy to generate star like backgrounds. planned background
 * for spacewars.
 * 
 * @author mgosselin
 *
 */
public class Main {
    private static Variable nameVar = new Variable("", "name", "0", false);
    private static int stars = 100;
    private static BufferedImage buffer;
    private static Graphics2D graphics;
    
    public static void main(String[] args) {
        try {
            //while (true) {
                String name = "" + Integer.parseInt(nameVar.getValue());
                nameVar.setValue("" + (Integer.parseInt(nameVar.getValue()) + 1));

                Random rand = new Random();
                buffer = new BufferedImage(1024, 600, 3);
                graphics = (Graphics2D) buffer.getGraphics();

                final int WIDTH = 1024;
                final int HEIGHT = 600;
                
                final int MIN_STAR_SIZE = 20, MAX_STAR_SIZE = 50;
                
                for (int y = 0; y < HEIGHT; y++) {
                    for (int x = 0; x < WIDTH; x++) {

                        int k = rand.nextInt(20);
                        int r = 0 + k;
                        int g = 0 + k;
                        int b = 0 + k;
                        graphics.setColor(new Color(r, g, b));
                        graphics.fillRect(x, y, 1, 1);
                        
                    }
                }
                
                for(int i = 0; i < stars; i ++) {

                	int size = MIN_STAR_SIZE + (int)(Math.random() * (MAX_STAR_SIZE - MIN_STAR_SIZE));
                	
                    int r = (int)(Math.random() * 100);
                    int g = (int)(Math.random() * 50);
                    int b = (int)(Math.random() * 50);
                    
                    int x = (int)(Math.random() * WIDTH - (size * 2 + 1));
                    int y = (int)(Math.random() * HEIGHT - (size * 2 + 1));
                    
                    graphics.drawImage(getStar(r, g, b, size), x, y, null);
                	
                	
                    
                }
                
                ImageIO.write(buffer, "png", new File(name + ".png"));

                System.out.println("Saved!");
            //}
        } catch (Exception e) {

        }
    }

    /**
     * size, given such that the output dimensions = 2 * size + 1
     * @param r
     * @param g
     * @param b
     * @param size
     * @return
     */
    public static BufferedImage getStar(int r, int g, int b, int size) {
        // abs(sqrt(sqrt(abs(3.14x))+sqrt(abs(3.14y)))-2.4)

        BufferedImage image = new BufferedImage(size*2 + 1, size*2 + 1, 3);
        Graphics2D graphics = (Graphics2D) image.getGraphics();

        //lets make a huge image...
        for (int y = 0; y < size*2 + 1; y++)
            for (int x = 0; x < size*2 + 1; x++) {

                //lets use variables that dont make sense
                double x2 = x - size;
                x2 /= size/10;
                double y2 = y - size;
                y2 /= size/10;
                double a = 0 - (Math.sqrt(Math.sqrt(Math.abs(Math.PI * (x2))) + Math.sqrt(Math.abs(Math.PI * (y2)))) - 2.35);
                int baseColor = a >= 0 ? 255 : 0;
                int opacity = (int) ((a / 6d) * 255);
                opacity = opacity >= 255 ? 255 : opacity <= 0 ? 0 : opacity;
                graphics.setColor(new Color(baseColor == 255 ? baseColor - r : 0, baseColor == 255 ? baseColor - g : 0, baseColor == 255 ? baseColor - b : 0, opacity));
                graphics.fillRect(x, y, 1, 1);
                //whatever, this works.
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
