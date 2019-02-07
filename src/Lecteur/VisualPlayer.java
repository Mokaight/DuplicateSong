package Lecteur;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import Lecteur.Mp3Player;
import javazoom.jl.decoder.*;
import javazoom.jl.player.JavaSoundAudioDevice;
import javax.sound.sampled.FloatControl;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import Classes.BackgroundPanel;
import Classes.MonBouton;
import Interface.LogicielVueControleur;


/**
 * Interface graphique de lecture d'un son wav
 */
public class VisualPlayer extends JFrame  implements ActionListener{
 
    private WavePlayerThread thread = null;
    //test
    private Mp3Player player;
    private JLabel jprogressbar = null;
   
    
    private String file = "";
    private String extension="";

    //controle de la lecture en cours 
    private boolean playing;
    //controle de la pause
    private boolean pausing = false;
	
    private JPanel buttonpanel;
    private VolumeSlider CS;
    //valeur sound
    private int Sound;
    //déclaration des boutons
    private JButton jButton_play;
    private JButton jButton_pause;
    private JButton jButton_stop;
    private JLabel jMusicImage;
    private JLabel jPlayingActual;
    //getters and setters
    
    public int getSound(){
    	return this.Sound;
    }
    public void setSound(int a){
    	this.Sound = a;
    }
    public void setString (String a){
    	this.setFile(a);
    }
    public JPanel getPanel (){
    	return this.buttonpanel;
    }
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
    public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public JLabel getjPlayingActual(){
		return jPlayingActual;
	}
	

    /**
     * Constructor
     * @param filename
     */
    public VisualPlayer (){
        buttonpanel = new BackgroundPanel(VisualPlayer.class.getResource("/lecteurfond.png"));
        Border b= BorderFactory.createBevelBorder(BevelBorder.LOWERED,new Color(232,232,230),new Color(118,118,118));
        buttonpanel.setBorder(b);
        jButton_play = new MonBouton(VisualPlayer.class.getResource("/play.png"),VisualPlayer.class.getResource("/play2.png"));
        jButton_pause = new MonBouton(VisualPlayer.class.getResource("/pause.png"),VisualPlayer.class.getResource("/pause2.png"));
        jButton_stop = new MonBouton(VisualPlayer.class.getResource("/stop.png"),VisualPlayer.class.getResource("/stop2.png"));
        
        jprogressbar = new JLabel();
        jprogressbar.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 10));
        jprogressbar.setForeground(new Color(232,232,230));
        jPlayingActual = new JLabel();
        jPlayingActual.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 11));
        jPlayingActual.setForeground(new Color(232,232,230));
        if (System.getProperty("os.name").contains("Mac") || System.getProperty("os.name").contains("mac")){
        	CS = new VolumeSlider(true);
        }
        else{
        	CS = new VolumeSlider();
        }
        CS.getJPanel().setOpaque(false);
        jButton_play.addActionListener(this);
        jButton_pause.addActionListener(this);
        jButton_stop.addActionListener(this);
      
        //Ajout de l'image au JLabel
//        ImageIcon icon = new ImageIcon(MonBouton.pathResourcesName("music.png"));
//        jMusicImage.setIcon(icon);
        //rajout des boutons dans la box

        //On crée un conteneur avec gestion horizontale
        Box b1 = Box.createHorizontalBox();
        b1.add(Box.createRigidArea(new Dimension(0,10)));
        b1.add(jPlayingActual);
        b1.add(Box.createRigidArea(new Dimension(0,10)));
        Box b2 = Box.createHorizontalBox();
        b2.add(jButton_play);
        b2.add(Box.createRigidArea(new Dimension(5,0)));
        b2.add(jButton_pause);
        b2.add(Box.createRigidArea(new Dimension(5,0)));
        b2.add(jButton_stop);
        b2.add(Box.createRigidArea(new Dimension(5,0)));
        
        b2.add(CS.getJPanel());
        //Idem
        
        //On cr�e un conteneur avec gestion verticale
        Box b4 = Box.createVerticalBox();
        b4.add(b1);
        b4.add(b2);

        buttonpanel.add(b4);
       
        CS.getSlider().addMouseListener(new MouseListener(){

			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println("TEST RENTRER DANS GET SOURCE VOLUME");
				Sound = CS.getValue();
				setVolume(Sound);
				
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        //add du boolean
        this.playing = false;
    }

      
    /**
     * Lancer la lecture
     */
    public void play() {
        if (this.thread == null) {
        	
            this.thread = new WavePlayerThread(this.file);
            this.thread.setFrame(this);
            this.thread.start();
           
        }
    }
 
    /**
     * Mettre en pause ou reprendre la lecture
     */
    public void pause() {
        if (this.thread != null) {
            this.thread.pause();
        }
    }
 
    /**
     * Abandonner la lecture
     */
    public void stop() {
        if (this.thread != null) {
        	playing = false;
            this.thread.cancel();
            //will call onTerminated
            onTerminated();
        }
    }
    

 
    /**
     * Thread event: fin du thread de lecture
     */
    public void onTerminated() {
        System.out.print("end");
       
        this.thread = null;
        // pour relancer la lecture
        // play();
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == jButton_play){
			//boolean pour le controle d'une seule musique lancer sans stop
			if(playing == true && pausing == false && this.thread !=null){
				stop();
			}
			if(playing == true && pausing == false && this.thread == null){
				player.stop();
			}
			
			//recuperer juste le nom de la musique
			String idStr = file.substring(file.lastIndexOf('/') + 1);
			jprogressbar.setText("Vous écoutez" + idStr);
            
			//cliquer sur play apres une pause relance la musique ( appelle de fonction pas clique ) 
			if (pausing == true){
				pausing = false;
				if (extension.equals("mp3")){
					playing = true;
	            	player.resume();
	            	
	            }
	            else {
	            	playing = true;
	            	pause();
	            	
	            }
				
			}
			else{
				if (extension.equals("mp3")){
					playing = true;
					 
			        try {
			        	FileInputStream input = new FileInputStream(file);
			        	player = new Mp3Player(input);
			        	player.play();
					} catch (JavaLayerException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            }
	            else {
	            	playing = true;
	            	play();
	            	
	            }    
            }
		}
		if(e.getSource() == jButton_pause){
            if(playing ==true){
            	playing = false;
				if (extension.equals("mp3")){
	            	pausing = true;
	            	player.pause();
	            }
	            
	            else {
	            	pausing = true;
	            	pause();
	                
	            }
            }
		
        }
		if(e.getSource() == jButton_stop){
			jprogressbar.setText("Vous n'écoutez plus rien :)");
            if (extension.equals("mp3")){
            	playing = false;
            	pausing = false;
            	player.close();
            }
            else {
            	pausing = false;
            	playing = false;
            	stop();
                
            }
		}
		if(e.getSource() == buttonpanel){
			System.out.println("TEST RENTRER DANS GET SOURCE VOLUME");
			Sound = CS.getValue();
			if (extension.equals("mp3")){
            	
            }
            
            else {
            	if (thread.getSourceDataLine() != null){
            		setVolume(Sound);
            		System.out.println("TEST ICI");
            	}
                
            }
		}
	}
	
	/**
	 * Permet de regler le gain de la musique ( le volume ) présent pour les threads 
	 */
    public void setVolume(float a){
    	a = 100- a;
        System.out.println("Sound modifier  TEST ");
        
        if (extension.equals("mp3")){
            if (player.getPlayer().getAudioDevice() instanceof JavaSoundAudioDevice)
            {
            	System.out.println("TEST");
            	JavaSoundAudioDevice jsAudio = (JavaSoundAudioDevice) player.getPlayer().getAudioDevice();
            	try{
            	
            	FloatControl volControl = (FloatControl) jsAudio.getLaSource().getControl(FloatControl.Type.MASTER_GAIN);
            	System.out.println("Valeur du son de la barre : " + a);
            	
            	System.out.println("Max" + volControl.getMaximum() + " +  Mini" +  volControl.getMinimum());
	            
            	float newGain = ((volControl.getMinimum()+volControl.getMaximum())*a)/100;
            	jsAudio.setLineGain(newGain,volControl);
                }catch(IllegalArgumentException e){
                	FloatControl volControl = (FloatControl) jsAudio.getLaSource().getControl(FloatControl.Type.VOLUME);
                	float newGain = ((volControl.getMinimum()+volControl.getMaximum())*a)/100;
                	jsAudio.setLineGain(newGain,volControl);
                }
           }

        }
        else {
        	if(thread != null && thread.getSourceDataLine()!= null){
        		
            	try{
                	
	            FloatControl volCtrl = (FloatControl) thread.getSourceDataLine().getControl(FloatControl.Type.MASTER_GAIN);
            	System.out.println("Valeur du son de la barre : " + a);
            	
	            System.out.println("Max" + volCtrl.getMaximum() + " +  Mini" +  volCtrl.getMinimum());
	            float newGain = ((volCtrl.getMinimum()+volCtrl.getMaximum())*a)/100;
	            volCtrl.setValue(newGain);
                }catch(IllegalArgumentException e){
                	FloatControl volCtrl = (FloatControl) thread.getSourceDataLine().getControl(FloatControl.Type.VOLUME);
                	float newGain = ((volCtrl.getMinimum()+volCtrl.getMaximum())*a)/100;
                	volCtrl.setValue(newGain);
                }
	        }
        }
    }
 

}