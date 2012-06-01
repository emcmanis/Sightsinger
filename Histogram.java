import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import java.util.concurrent.LinkedBlockingQueue;

public class Histogram extends JPanel implements Runnable {

    LinkedBlockingQueue<double[]> queue;
    double[] data;
    int[] toprint;
    DataFrame frame;

    public Histogram(DataFrame myframe) {
        frame = myframe;
        data = new double[4096];
        toprint = new int[512];
        queue = new LinkedBlockingQueue<double[]>();
        setPreferredSize(new Dimension(512,400)); //w,h in pixels
    }

    public void enqueue(double[] stuff) {
        queue.add(stuff);
    }

    public void run() {
        while(!frame.isStopped()) {
            try {
                data = queue.take();
            }
            catch(InterruptedException e) {
                return;
            }
            toprint = histHeights(data);
            repaint();
        }
    }
 
    public void paint(Graphics g) {
        for(int i = 0; i < 512; i++) {
            g.drawLine(i,350,i,350-toprint[i]);
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
