public class audioChunk{
    AudioFormat format;
    byte[] data;
    
    audioChunk(){
        format = new AudioFormat(44000,8,1,true,true);
        data = new byte[32];
    }

