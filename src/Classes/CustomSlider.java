package Classes;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JSlider;

import Lecteur.VolumeSlider;

@SuppressWarnings("serial")
public class CustomSlider extends JSlider
{
	private Image img = null;

    public CustomSlider()
    {
        try
        {
        	img = ImageIO.read(CustomSlider.class.getResource("/sliderfond.png"));
            setOpaque(false);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        // Draw the previously loaded image to Component
        g.drawImage(img, 0, 0, null);
        super.paintComponent(g);
    }
}