public class AudioChunk{
    AudioFormat format;
    byte[] data;
    AudioChunk next;
   
    AudioChunk(){
        format = new AudioFormat(44000,8,1,true,true);
        data = new byte[4096];
    }

    int write(byte[] inputs; int inputs_offset; int outputs_offset; int len) {
	int i;
        for(i = 0; i + inputs_offset <= len || i + outputs_offset <= data.Length(); i++) {
            data[i + outputs_offset] = inputs[i + inputs_offset];
        }
	return(i);
    }
