package Interface;

import java.awt.*; 
import javax.swing.*;
  
public class LoadingBar
    {
    JProgressBar progress;
    Thread monThread;
    int rappidite;
    JLabel texte;
    int progressBar;
    public JPanel panneau;
    public JPanel getPanneau() {
		return panneau;
	}
	public void setPanneau(JPanel panneau) {
		this.panneau = panneau;
	}
	public JProgressBar getJprogressBar(){
		return progress;
	}
	public void renameText(String a){
		texte.setText(a);
	}
	public JLabel getTexte() {
		return texte;
	}
	public LoadingBar(int rappid){
		panneau = new JPanel();
        rappidite=rappid;
        // Création de l'interface
        


        texte = new JLabel("Barre de Chargement");
        progress = new JProgressBar(0, 100);
        progress.setStringPainted(true);
        progress.setMaximumSize(new Dimension(130,140));
        panneau.add("Center", progress); 
        panneau.add("Center", texte); 
	}
	
} 