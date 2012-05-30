import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

public class DataFrame extends JFrame implements ActionListener {

    public DataFrame() {
        setTitle("Histogram");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JButton button = new JButton("Hello!");
        JButton otherbutton = new JButton("Goodbye.");
        JButton east = new JButton("east");
        JButton west = new JButton("west");
        Histogram center = new Histogram();
        button.addActionListener(this);
        add(button, BorderLayout.NORTH);
        add(otherbutton, BorderLayout.SOUTH);
        add(east, BorderLayout.EAST);
        add(west, BorderLayout.WEST);
        add(center, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("Hey!"); 
    }

    public static void main(String[] args) {
        DataFrame foo = new DataFrame();
    }
}
