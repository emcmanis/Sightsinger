import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import java.util.concurrent.LinkedBlockingQueue;
import java.lang.Math;

public class Histogram extends JPanel implements Runnable {

    LinkedBlockingQueue<double[]> queue;
    double[] data;
    int[] toprint;
    DataFrame frame;

    public Histogram(DataFrame myframe) {
        frame = myframe;
        data = new double[4096];
        toprint = new int[512];
        for(int i = 0; i < 512; i++) {
            toprint[i] = 0;
        }
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
                System.out.println("display:interrupted");
                return;
            }
            toprint = histHeights(data);
            repaint();
        }
        System.out.println("display:stopped");
    }
 
    public void paint(Graphics g) {
        g.clearRect(0,0,512,350);
        for(int i = 0; i < 512; i++) {
            g.drawLine(i,350,i,350-toprint[i]);
        }
        g.drawString("frequency",225,395);
    }

    private double biggest(double[] data) {
        double largest = data[0];
        for(int i = 1; i < 2048; i++) {
            largest = Math.max(largest,data[i]);
        }
        return(largest);
    }


    private int[] histHeights(double[] data) {
        int i;
        double height;
        double norm = biggest(data);
        int[] heights = new int[512];
        for(i = 0; i < 512; i++) {
            height = data[i];
            height = height/norm * 350;
            heights[i] = (int) height;
        }
        return(heights);
    }
}
