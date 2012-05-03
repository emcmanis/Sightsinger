public class getAudio {

    TargetDataLine line;
    AudioFormat format;
    
    void setup(){
        format = new AudioFormat(44000,8,1,true,true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format); // format is an AudioFormat object
        if (!AudioSystem.isLineSupported(Port.info.MICROPHONE)) {
            System.out.print("Something is wrong with your microphone.");
            return;
        }
        // Obtain and open the line.
        try {
            line = (Port) AudioSystem.getLine(Port.Info.MICROPHONE);
            line.open(format);
        } catch (LineUnavailableException ex) {
            System.out.print("line is unavailable!");
            return;
        }
    }

    void read() {
        // Assume that the TargetDataLine, line, has already
        // been obtained and opened.
        ByteArrayOutputStream out  = new ByteArrayOutputStream();
        int numBytesRead = 0;
        byte[] data = new byte[line.getBufferSize() / 5];
        //byte[] audio = new byte[2048]; //for testing purposes -- recording and playing back some samples.
    
        // Begin audio capture.
        line.start();
    
        // Here, stopped is a global boolean set by another thread.
        while (numBytesRead < 64) {
            // Read the next chunk of data from the TargetDataLine.
            numBytesRead = numBytesRead + line.read(data, 0, data.length);
            // Save this chunk of data.
            out.write(data, 0, numBytesRead);
        }  
        return;
    }
}