import javax.swing.JFrame;

import java.awt.event.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.Canvas;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.util.concurrent.atomic.AtomicReference;

import org.voxeltech.visualizer.*;
import org.voxeltech.game.*;


/**
 *
 * @author Gary Fenstamaker
 */
public class MainWindow extends JFrame implements ActionListener {

    private static MainWindow frame;

    private JButton startVisualButton;
    private JButton startGameButton;
    private JButton exitButton;

    private Canvas gameCanvas;
   
    private JPanel overPanel;
    private JPanel buttonPanel;
    
    private Visualizer visualizer;
    private Game game;

    private static boolean closeRequested = false;
    private final static AtomicReference<Dimension> newCanvasSize = new AtomicReference<Dimension>();

    public MainWindow() {

	setLayout(new BorderLayout());

	overPanel = new JPanel();
	overPanel.setLayout(new CardLayout());

        buttonPanel = new JPanel();
	buttonPanel.setLayout(new GridLayout(3, 3, 25, 25));
	gameCanvas = new Canvas();

	overPanel.add(buttonPanel, "buttonPanel");
	overPanel.add(gameCanvas, "gameCanvas");

        startVisualButton = new JButton("Start Visualizer");
        startGameButton = new JButton("Start Game");
        exitButton = new JButton("Exit");
        startVisualButton.addActionListener(this);
        startGameButton.addActionListener(this);
        exitButton.addActionListener(this);
	
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(startVisualButton);
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(startGameButton);
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(exitButton);
        buttonPanel.add(new JLabel(""));
        
        add(overPanel, BorderLayout.CENTER);
    }
    
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton)e.getSource();
        System.out.println(button.getText());
        if (button.getText().equals("Start Visualizer") ) {
           
            visualizer = new Visualizer(gameCanvas);
            visualizer.setupDisplay();
            CardLayout c1 = (CardLayout)overPanel.getLayout();
	    c1.show(overPanel, "gameCanvas");
	    gameCanvas.setFocusable(true);
	    gameCanvas.requestFocus();
	    gameCanvas.setIgnoreRepaint(true);
            visualizer.start();
	    repaint();

        } else if (button.getText().equals("Start Game") ) {
           
            game = new Game();
            game.setupDisplay();
                        
            game.start();

        } else if (button.getText().equals("Exit") ) {
            dispose();
        }
    }

    public static void main(String[] args) {
	frame = new MainWindow();
	frame.setTitle("Welcome");
	frame.setSize(600, 450);
	frame.setLocationRelativeTo(null);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);        
    }

}
