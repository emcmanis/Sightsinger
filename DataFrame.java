import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

public class DataFrame extends JFrame {

    boolean stopped;
    FFT fft;
    GetAudio audioinput;

    public DataFrame() {
        setTitle("Histogram");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JButton start = new JButton("Start");
        JButton stop = new JButton("Stop");
        Histogram hist = new Histogram();
        stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopped = true;
            }
        }
        );
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopped = false;
                audioinput = new GetAudio(this);
                fft = new FFT(this);
            }
        });
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
