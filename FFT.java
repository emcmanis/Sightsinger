import java.util.concurrent.LinkedBlockingQueue;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D; //this is from the jtransforms jar.

//this class processes AudioChunks being put out by the recording thread, then passes them off to the graphics thread for display.

public class FFT extends Thread {

    DoubleFFT_1D fft;
    LinkedBlockingQueue<AudioChunk> queue; //queue of AudioChunks to fft
    DataFrame frame;

    public FFT(DataFrame myframe) {
        frame = myframe; //I pass in a DataFrame so this thread can query the "stopped" bool.
        fft = new DoubleFFT_1D(4096);
        queue = new LinkedBlockingQueue<AudioChunk>();
    }


    public void enqueue(AudioChunk data) {
        queue.add(data);
    }

    //transforms data!
    private double[] transform(AudioChunk input) {
        double[] data = new double[4096];
        for(int i = 0; i < 4096; i++) {
            data[i] = 0.1*(double)input.data[i]; //casting a byte to a double. Amplitudes run from 0 to 25.5 to make their magnitudes reasonable.
        }
        fft.realForward(data); 

        //this loop performs the complex conjugation in order to get a power spectrum.
        //we discard the data point at the nyquist frequency as its imaginary part is not available.
        
        for(int i = 1; i < data.length/2; i++) {
            data[i] = data[2*i]*data[2*i] + data[2*i+1]*data[2*i+1];
        }
        return(data); //transformed array of doubles
    } 

    public void run() {
        process();
    }

    private void process() {
        //again stopped is a global bool

        while(!frame.isStopped()) {
            AudioChunk input;
            try {
                input = queue.take();
            }
            catch(InterruptedException e) {
                return;
            }
            double[] data;
            data = transform(input);
            frame.hist.enqueue(data); //passes the data off to the histogram thread, which will display it.
        }
    }
}
