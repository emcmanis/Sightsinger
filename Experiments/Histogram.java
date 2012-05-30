import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;

public class Histogram extends JPanel {

    public Histogram() {
        setPreferredSize(new Dimension(512,400)); //w,h in pixels
    }

    public void paint(Graphics g) {
        double[] data = new double[2048];
        int[] heights = new int[512];
        int i;
        for(i = 0; i < 2048; i++) {
            data[i] = i%255;
        }
        heights = histHeights(data);
        for(i = 0; i < 512; i++) {
            g.drawLine(i,400,i,400-heights[i]);
        }
    }

    public int[] histHeights(double[] data) {
        int i;
        double height;
        int[] heights = new int[512];
        for(i = 0; i < 512; i++) {
            height = data[i*4] + data[i*4 + 1] + data[i*4 + 2] + data[i*4 + 3];
            height = height/1024 * 400;
            heights[i] = (int) height;
        }
        return(heights);
    }

}
