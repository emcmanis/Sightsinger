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
        frame = myframe; //again to allow stopped access
        data = new double[4096];
        toprint = new int[512];
        //initialize the toprint array to zero, meaning the screen starts out clear.
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
                return;
            }
            toprint = histHeights(data);
            repaint();
        }
    }
 
    public void paint(Graphics g) {
        g.clearRect(0,0,512,350); //clears the area being drawn on 
        for(int i = 0; i < 512; i++) {
            g.drawLine(i,350,i,350-toprint[i]);
        }
        g.drawString("frequency",225,395);
    }

    //this is purely a helper function -- I wanted to normalize my histogram so the largest frequency had a big line, so this just picks the largest frequency.
    //it expects an array of data coming from the fft thread. Everything after 2048 is leftover junk from the transform.
    private double biggest(double[] data) {
        double largest = data[0];
        for(int i = 1; i < 2048; i++) {
            largest = Math.max(largest,data[i]);
        }
        return(largest);
    }

    //this method takes the data from the fft and turns it into an array of integers to be used to determine the heights of the bars i nthe histogram.
    private int[] histHeights(double[] data) {
        int i;
        double height;
        double norm = biggest(data); 
        int[] heights = new int[512];
        for(i = 0; i < 512; i++) { //this loop goes through the first 512 data points, completely disregarding the rest. Due to the frequency grid spacing, the other points will be above the range of the human voice, and thus unimportant to this particular application.
            height = data[i];
            height = height/norm * 350; //height/norm should be between 0 and 1; this picks what percentage of 350 pixels this line will take up.
            heights[i] = (int) height; //finally, the drawLine method needs integers, so we cast to int. This will lose some information, but not much.
        }
        return(heights);
    }
}
