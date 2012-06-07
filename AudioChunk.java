import javax.sound.sampled.AudioFormat;

public class AudioChunk{
    AudioFormat format;
    byte[] data;
   
    AudioChunk(){
        format = new AudioFormat(44000,8,1,true,true);
        data = new byte[4096]; //this gives me a delta-f of about 10 Hz.
    }


    //method to write data to the array. It writes (starting at outputs_offset) until the array is full or it runs out of data, then returns the number of bytes written. "len" is the length of the inputs.

    int write(byte[] inputs, int inputs_offset, int outputs_offset, int len) {
	int i = 0;
        for(i = 0; i + inputs_offset < len && i + outputs_offset < data.length; i++) {
            data[i + outputs_offset] = inputs[i + inputs_offset];
        }
	return(i);
    }
}
