package Lecteur;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.metal.MetalSliderUI;

import Classes.CustomSlider;
import Classes.MonBouton;
import Interface.LogicielVueControleur;

import java.awt.*;

public class VolumeSlider{
     JSlider slider;
     JLabel label;
     JPanel panel;
     public static String strCommand = new String();
     public static String str = new String();
     public Color couleurOfficiel = new Color(62,159,230);
     
     //Valeur de la bar
     private int value;
     
     public int getValue(){
    	 return this.value;
     }
     public void setValue(int a){
    	 this.value = a;
     }
     public void setString(String a){
    	 VolumeSlider.str = a;
     }
     public String getString(){
    	 return VolumeSlider.str;
     }
     public JLabel getLabel(){
    	 return this.label;
     }
     public JPanel getJPanel() {
    	 return this.panel;
    	 
     }
     public JSlider getSlider(){
    	 return this.slider;
     }
     public VolumeSlider(boolean a){
    	 Color couleurOfficiel = new Color(232,232,230);
    	 Font policeOfficiel = new Font("DejaVu Sans Condensed", Font.BOLD, 10);
         slider = new CustomSlider();
         slider.setOrientation(JSlider.HORIZONTAL);
         slider.setPreferredSize(new Dimension(200,50));
      	 
      	//fin test
         slider.setMinimum(0);
         slider.setMaximum(100);
         slider.setValue(50);
         slider.setMajorTickSpacing(20);
         slider.setMinorTickSpacing(1);
         slider.setPaintTrack(true);
         slider.setPaintLabels(true);
         slider.setForeground(couleurOfficiel);
         slider.addChangeListener(new MyChangeAction());
         
         label = new JLabel();
         label.setText("Volume: 50");
         label.setFont(policeOfficiel);
         label.setForeground(couleurOfficiel);
         
         value = 50;
         panel = new JPanel();
         
         Box b1 = Box.createHorizontalBox();
         b1.add(slider);
         Box b2 = Box.createHorizontalBox();
         b2.add(Box.createRigidArea(new Dimension(5,0)));
         b2.add(label);
         Box b4 = Box.createVerticalBox();
         b4.add(Box.createRigidArea(new Dimension(0,4)));
         b4.add(b1);
         b4.add(b2);
     
         slider.setBorder(null);
         panel.add(b4);
         panel.setOpaque(false);
     }
  public VolumeSlider(){
	 Color couleurOfficiel = new Color(232,232,230);
	 Font policeOfficiel = new Font("DejaVu Sans Condensed", Font.BOLD, 10);
	 slider = new CustomSlider();
     slider.setOrientation(JSlider.HORIZONTAL);
     slider.setPreferredSize(new Dimension(200,50));
     //test
     Icon maleIcon   = new ImageIcon(VolumeSlider.class.getResource("/Volume.png"));
     slider.setUI(new IconThumbSliderUI(maleIcon));
  	 slider.setFont(policeOfficiel);
  	//fin test
     slider.setMinimum(0);
     slider.setMaximum(100);
     slider.setValue(50);
     slider.setMajorTickSpacing(20);
     slider.setMinorTickSpacing(1);
     slider.setPaintTrack(true);
     slider.setPaintLabels(true);
     slider.setForeground(couleurOfficiel);
     slider.addChangeListener(new MyChangeAction());
     label = new JLabel();
     label.setText("Volume: 50");
     label.setFont(policeOfficiel);
     label.setForeground(couleurOfficiel);
     value = 50;
     panel = new JPanel();
     
     Box b1 = Box.createHorizontalBox();
     b1.add(slider);
     Box b2 = Box.createHorizontalBox();
     b2.add(Box.createRigidArea(new Dimension(5,0)));
     b2.add(label);
     Box b4 = Box.createVerticalBox();
     b4.add(Box.createRigidArea(new Dimension(0,4)));
     b4.add(b1);
     b4.add(b2);
 
     slider.setBorder(null);
     panel.add(b4);
     panel.setOpaque(false);
  }
  public class MyChangeAction implements ChangeListener{
	     public void stateChanged(ChangeEvent ce){
	        value = slider.getValue();
	        str = Integer.toString(value);
	        label.setText("Volume: " + str);
	     }
	   }
  //Cette class permet de réécrire le slider ( l'icone du slider )
  class IconThumbSliderUI extends MetalSliderUI {
	    protected Icon hThumbIcon = null;
	   
	    public IconThumbSliderUI(Icon hThumbIcon) {
	        setHorizontalThumbIcon(hThumbIcon);
	    }
	   
	    public void setHorizontalThumbIcon(Icon hThumbIcon) {
	       if (hThumbIcon == null) 
	    	   this.hThumbIcon = horizThumbIcon;
	       else                    
	    	   this.hThumbIcon = hThumbIcon;
	    }  
	    public void paintThumb(Graphics g)  {
	        Rectangle knobBounds = thumbRect;
	  
	        g.translate( knobBounds.x, knobBounds.y );
	  
	        if ( slider.getOrientation() == JSlider.HORIZONTAL ) {
	            hThumbIcon.paintIcon( slider, g, 0, 0 );
	        }	   
	        g.translate( -knobBounds.x, -knobBounds.y );
	    }
	  
	    protected Dimension getThumbSize() {
	        Dimension size = new Dimension();

	        size.width = hThumbIcon.getIconWidth();
	        size.height = hThumbIcon.getIconHeight();
	    
	  
	    return size;
	    }

	    protected void paintHorizontalLabel(Graphics g,int value,Component label)
	    {
	      JLabel lbl = (JLabel)label;
	      lbl.setForeground(Color.WHITE);
	      super.paintHorizontalLabel(g,value,lbl);
	    }
	  }
}
