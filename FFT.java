public class FFT {

    DoubleFFT_1D fft;
    AudioChunk head;

    public FFT() {
        fft = new DoubleFFT_1D(4096);
    }

    //linked list for a queue

    public synchronized void enqueue(AudioChunk data) {
        if(head == null) { //either this is the head
            head = data;
        }
        else {  //or sticks on somewhere at the end
            AudioChunk tail = head;
            while(tail.next != null) {
                tail = tail.next;
            }
            tail.next = data;

        }
        return;
    }

    private synchronized AudioChunk dequeue() {
        AudioChunk res = head;
        head = res.next;
        return(res);
    }

    //transforms data!
    public double[] transform(AudioChunk input) {
        double[] data = new double[4096];
        //casting bytes to doubles. I think this is wrong.
        for(int i = 0, i < 4096, i++) {
            data[i] = (double)input[i];
        }

        fft.realForward(data);
        return(data);
    } 

    public void process() {

        //again stopped is a global bool

        while(!stopped) {
            if(head != null) {
                double[] data;
                data = transform(dequeue());
                // hand off to graphics thread
            }
        }
    }

}
