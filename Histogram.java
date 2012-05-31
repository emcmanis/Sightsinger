import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;

public class Histogram extends JPanel {

    LinkedBlockingQueue<double[]> queue;

    public Histogram() {
        setPreferredSize(new Dimension(512,400)); //w,h in pixels
    }

    public void paint(Graphics g) {
        if(!frame.isStopped()) {
            double[] data;
            int[] heights = new int[512];
            try{
                data = queue.take();
            }
            catch(InterruptedException e) {
                return;
            }
            heights = histHeights(data);
            for(int i = 0; i < 512; i++) {
                g.drawLine(i,350,i,350-heights[i]);
            }
         }
        g.drawString("frequency",250,5);
    }

    public int[] histHeights(double[] data) {
        int i;
        double height;
        int[] heights = new int[512];
        for(i = 0; i < 512; i++) {
            height = data[i*4] + data[i*4 + 1] + data[i*4 + 2] + data[i*4 + 3];
            height = height/1250 * 350;
            heights[i] = (int) height;
        }
        return(heights);
    }
}
