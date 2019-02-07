package Lecteur;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
 
/**
 * Player de sons wav
 * ajout de fonctions à la class AePlayWave
 * http://www.fobec.com/java/1043/jouer-son-musique-format-wav.html
 * -> pause et reprendre la lecture
 * -> arreter la lecture
 * -> calculer la progression de lecture.
 * @author fobec 2011 *
 */
public class WavePlayerThread extends Thread {
 
    private String filename;
    private Position curPosition;
    private final int EXTERNAL_BUFFER_SIZE = 65536; // 16Kb
    private boolean isSuspend = false;
    private boolean isCanceled = false;
    private VisualPlayer frame = null;
    private int readpercent = 0;
    private SourceDataLine auline = null;
    enum Position {
 
        LEFT, RIGHT, NORMAL
    };
 
    public SourceDataLine getSourceDataLine(){
    	return this.auline;
    }
    /**
     * Constructeur: fixer le fichier wav
     * @param wavfile
     */
    public WavePlayerThread(String wavfile) {
        filename = wavfile;
        curPosition = Position.NORMAL;
    }
 
    /**
     * Mettre en pause ou reprendre la lecture
     */
    public synchronized void pause() {
        if (this.isSuspend == true) {
            this.isSuspend = false;
        } else {
            this.isSuspend = true;
        }
        notify();
    }
 
    /**
     * Abandonner la lecture
     */
    public synchronized void cancel() {
        this.isCanceled = true;
        notify();
    }
 
    /**
     * Fixer la Frame VisualPlayer
     * @param visualplayer
     */
    public void setFrame(VisualPlayer visualplayer) {
        this.frame = visualplayer;
    }
 
    /**
     * Connaitre la progression de lecture
     * @return
     */
    public int getProgress() {
        return this.readpercent;
    }
    public void run() {
        File soundFile = new File(filename);
        if (!soundFile.exists()) {
            System.err.println("Wave file not found: " + filename);
            return;
        }
 
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (UnsupportedAudioFileException e1) {
            e1.printStackTrace();
            return;
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
 
        AudioFormat format = audioInputStream.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
 
        try {
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
 
        if (auline.isControlSupported(FloatControl.Type.PAN)) {
            FloatControl pan = (FloatControl) auline.getControl(FloatControl.Type.PAN);
            if (curPosition == Position.RIGHT) {
                pan.setValue(1.0f);
            } else if (curPosition == Position.LEFT) {
                pan.setValue(-1.0f);
            }
        }
 
        auline.start();
        int nBytesRead = 0;
        //longueur du fichier wav
        Long audiolength = soundFile.length();
        int audioreaded = 0;
        byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
 
        try {
            while (nBytesRead != -1 && this.isCanceled == false) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0) {
                    //calcul de la progression
                    readpercent = Math.round(audioreaded * 100 / audiolength);
                    auline.write(abData, 0, nBytesRead);
                    audioreaded = nBytesRead;
                }
                /**
                 * Annuler la lecture
                 */
                if (this.isCanceled == true) {
                    System.out.print("Thread has been stopped");
                    break;
                }
               /**
                 * Mettre en pause
                 */
                if (this.isSuspend == true) {
                    while (this.isSuspend == true) {
                        try {
                            Thread.sleep(250);
                            if (this.isCanceled == true) {
                                System.out.print("Thread has been stopped");
                                break;
                            }
                        } catch (InterruptedException ex) {
                            Logger.getLogger(WavePlayerThread.class.getName()).log(Level.SEVERE,
 
			 null, ex);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            auline.drain();
            auline.close();
        }
        /**
         * Fin du thread de lecture
         */
        readpercent = 100;
        this.frame.onTerminated();
    }
}