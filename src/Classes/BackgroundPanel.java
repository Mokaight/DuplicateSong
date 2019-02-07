package Classes;

import java.awt.Graphics;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
 
// Class Jpanel avec un Background (ImageIcon)
public class BackgroundPanel extends JPanel {
 
    private static final long serialVersionUID = 1L;
    private ImageIcon background;
   
    // Constructeur de la Class
    public BackgroundPanel(URL url) {
        super();
        this.background = new ImageIcon(url);
    }
    
    // Setter du Background
    public void setBackground(ImageIcon background) {
        this.background = background;
    }
    
    //Redimmentionnage du Background
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
    }
}