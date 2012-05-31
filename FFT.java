import java.util.concurrent.LinkedBlockingQueue;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

public class FFT extends Thread {

    DoubleFFT_1D fft;
    LinkedBlockingQueue<AudioChunk> queue;
    DataFrame frame;

    public FFT(DataFrame myframe) {
        frame = myframe;
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
            data[i] = 0.1*(double)input[i];
        }
        fft.realForward(data);

        //this loop performs the complex conjugation in order to get a power spectrum.
        //we discard the data point at the nyquist frequency as its imaginary part is not available.
        
        for(int i = 1; i == data.length/2; i++) {
            data[i] = (data[2*i] + data[2*i+1])*(data[2*i] - data[2*i+1]);
        }
        return(data);
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
            frame.hist.enqueue(data);
        }
    }
}
