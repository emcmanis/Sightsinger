import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Port;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent;

//obtains audio data from the computer's microphone, stores it in an audioChunk for future processing

public class GetAudio extends Thread {

    TargetDataLine line;
    AudioChunk output;
    DataFrame frame;
    
    public GetAudio(DataFrame myframe) {
        output = new AudioChunk();
        frame = myframe;
    }

    //this sets up the TargetDataLine for use recording. It's separate from the constructor so it can be retried if necessary.

    public void setup() {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, output.format); // format is an AudioFormat object
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Something is wrong with this. Please fix and retry. If you're on OS X, your audio system is set up differently from everyone else's. I'm sorry.");
            frame.setStopped(true); //set the DataFrame's stopped bool to true, preventing further execution.
           return;
        }
        // Obtain and open the line.
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(output.format);
        } catch (LineUnavailableException ex) {
            System.out.println("line is unavailable! Please fix and retry.");
            frame.setStopped(true);
        }

        //anonymous LineListener to handle if the line is suddenly closed or stopped, preventing wasted system resources.

        line.addLineListener(new LineListener(){
            public void update(LineEvent event) {
                if(event.getType() == LineEvent.Type.STOP || event.getType() == LineEvent.Type.CLOSE) {
                    frame.setStopped(true);
                }
            }
        });
    }

    public void run() {
        System.out.println("Starting...");
        record();
    }

    private void record() {
        // Assume that the TargetDataLine, line, has already
        // been obtained and opened.
        int numBytesRead = 0;
        int numBytesWrote = 0;
        int offset = 0;
        byte[] data = new byte[line.getBufferSize() / 5];

        // Begin audio capture.
        line.start();
        // Here, stopped is a global boolean set by another thread.
        while (!frame.isStopped()) {
            //this loop starts with an input/output offset, writes as many bytes as possible, then loops. Once the audioChunk is full, it ffts it.
            numBytesRead = line.read(data, 0, data.length);
            numBytesWrote = 0;
            while(numBytesWrote != numBytesRead) { //when numBytesWrote == numBytesRead, all the data has been copied and more data should be gathered.
                numBytesWrote = numBytesWrote + output.write(data, numBytesWrote, offset, numBytesRead); //write as many bytes as possible, add to the number of bytes written
                offset = numBytesWrote % output.data.length; //if this is zero, a whole chunk of data has been written, and it is ready to be FFT'd.
                if(offset == 0) {
                    frame.fft.enqueue(output); //queue up the old one
                    output = new AudioChunk(); //get a new one.
                }
            }
        }
    }
}
