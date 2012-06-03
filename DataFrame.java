import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

public class DataFrame extends JFrame {

    boolean stopped;
    FFT fft;
    GetAudio audioinput;
    Histogram hist;

    public DataFrame() {
        setTitle("Histogram");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JButton start = new JButton("Start");
        JButton stop = new JButton("Stop");
        hist = new Histogram(this);	
        final Thread histthread = new Thread(hist,"histthread");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopped = false;
                audioinput = new GetAudio(DataFrame.this);
                audioinput.setup(); 
                if(stopped == false) {
                    fft = new FFT(DataFrame.this);
                    fft.start();
                    audioinput.start();
                    histthread.start();
                }
            }
        });
        stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopped = true;
                try { 
                    fft.interrupt();
                }
                catch(NullPointerException ex) {
                    System.out.println("fft does not exist");
                }
                try {
                    histthread.interrupt();
                }
                catch(NullPointerException ex) {
                    System.out.println("histthread does not exist");
                }
            }
        }
        );
        add(start, BorderLayout.NORTH);
        add(stop, BorderLayout.SOUTH);
        add(hist, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    public void setStopped(boolean val) {
        stopped = val;
    }

    public boolean isStopped() {
        return(stopped);
    }

    public static void main(String[] args) {
        DataFrame foo = new DataFrame();
    }
}
