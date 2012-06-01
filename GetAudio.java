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

    public void setup() {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, output.format); // format is an AudioFormat object
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Something is wrong with this. Please fix and retry.");
            System.out.println("microphone: ");
            System.out.println(AudioSystem.isLineSupported(Port.Info.MICROPHONE));
            System.out.println("line in: ");
            System.out.println(AudioSystem.isLineSupported(Port.Info.LINE_IN));
            frame.setStopped(true);
            frame.setStopped(true);
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
        line.addLineListener(new LineListener(){
            public void update(LineEvent event) {
		if(event.getType() == LineEvent.Type.STOP || event.getType() == LineEvent.Type.CLOSE) {
	            frame.setStopped(true);
                }
            }
        });
    }

    public void run() {
        setup();
        if(!frame.isStopped()) {
            System.out.println("this should not be printing");
            record();
        }
    }

    private void record() {
        // Assume that the TargetDataLine, line, has already
        // been obtained and opened.
        int numBytesRead = 0;
        int numBytesWrote = 0;
        int offset = 0;
        byte[] data = new byte[line.getBufferSize() / 5];
        //byte[] audio = new byte[2048]; //for testing purposes -- recording and playing back some samples.

        // Begin audio capture.
        line.start();
        // Here, stopped is a global boolean set by another thread.
        while (!frame.isStopped()) {
            //this loop starts with an input/output offset, writes as many bytes as possible, then loops. Once the audioChunk is full, it ffts it.
            numBytesRead = line.read(data, 0, data.length);
            while(numBytesWrote != numBytesRead) {
                numBytesWrote = output.write(data, numBytesWrote, offset, output.data.length);
                offset = numBytesWrote % output.data.length;
                if(offset == 0) {
                    frame.fft.enqueue(output);
                    output = new AudioChunk();
                }
            }
        }  
    }
}
