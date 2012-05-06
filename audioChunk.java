public class audioChunk{
    AudioFormat format;
    byte[] data;
    
    audioChunk(){
        format = new AudioFormat(44000,8,1,true,true);
        data = new byte[2048];
    }

    int write(byte[] inputs; int inputs_offset; int outputs_offset; int len) {
	int i;
        for(i = 0; i <= len || i <= data.Length(); i++) {
            data[i + outputs_offset] = inputs[i + inputs_offset];
        }
	return(i);
    }
