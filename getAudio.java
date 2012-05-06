//obtains audio data from the computer's microphone, stores it in an audioChunk for future processing

public class getAudio {

    TargetDataLine line;
    audioChunk output;
    
    int setup(){
        output = new audioChunk();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, output.format); // format is an AudioFormat object
        if (!AudioSystem.isLineSupported(Port.info.MICROPHONE)) {
            System.out.print("Something is wrong with your microphone.");
            return(-1);
        }
        // Obtain and open the line.
        try {
            line = (Port) AudioSystem.getLine(Port.Info.MICROPHONE);
            line.open(output.format);
        } catch (LineUnavailableException ex) {
            System.out.print("line is unavailable!");
            return(-1);
        }
    return(0);
    }

    void process() {
        // Assume that the TargetDataLine, line, has already
        // been obtained and opened.
        int numBytesRead = 0;
        int numBytesWrote = 0;
        byte[] data = new byte[line.getBufferSize() / 5];
        //byte[] audio = new byte[2048]; //for testing purposes -- recording and playing back some samples.

        // Begin audio capture.
        line.start();
        // Here, stopped is a global boolean set by another thread.
        while (!stopped) {
            //this loop starts with an input/output offset, writes as many bytes as possible, then loops. Once the audioChunk is full, it ffts it.

            int output_offset = numBytesWrote;
            int input_offset = 0;
            // Read the next chunk of data from the TargetDataLine.
            numBytesRead = line.read(data, 0, data.length);
            numBytesWrote = output.write(data, input_offset, output_offset, numBytesRead);
            input_offset = input_offset + numBytesWrote;
            
            //handle the case where the audioChunk is full after the start of the loop
            if(output_offset + numBytesWrote = output.data.Length()) {
                //fft here
            }
            
            //loop through data read (if necessary) and fft
            while(input_offset != numBytesRead) {
                numBytesWrote = output.write(data, input_offset, 0, numBytesRead); 
                input_offset = input_offset + numBytesWrote;
                //fft code goes here 
            }
        }  
        return;
    }
}
