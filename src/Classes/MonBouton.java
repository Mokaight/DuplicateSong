package Classes;

import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

//Class MonBouton extend du Jbutton de Java permet d'associer a une image (et celle quand la souris est dessus)
public class MonBouton extends JButton {
	
	private static final long serialVersionUID = 1L;
	private ImageIcon icon1;
	private ImageIcon icon2;
	private String chemin;
	
	
	// Renvoie le chemin d'un fichier contenu dans repertoire "resources"
	// Gere le chemin pour Windows/Linux/Mac 
	public static String pathResourcesName(String name){


			
			return name;

		}

	
	// Constructeur de la class : 1 icon  	
	public MonBouton(String str){
		super();
		chemin = pathResourcesName(str);
		File f = new File(chemin);
		if(f.exists()){
			icon1 =new ImageIcon(chemin);
		}else{
			this.setText(str);
		}
		this.setIcon(icon1);
		this.setBorderPainted(false);
		this.setBorder(null);
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setContentAreaFilled(false);
	}
	
	// Constructeur de la class : 2 icons
	public MonBouton(String str,String str2){
		super();
		chemin = pathResourcesName(str);
		File f = new File(chemin);
		if(f.exists()){
			icon1 = new ImageIcon(chemin);
		}else{
			this.setText(str);
		}
		chemin = pathResourcesName(str2);
		f = new File(chemin);
		if(f.exists()){
			icon2 = new ImageIcon(chemin);
		}
		this.setIcon(icon1);
		this.setBorderPainted(false);
		this.setBorder(null);
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setContentAreaFilled(false);
		this.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				changeIcon(2);
				//System.out.println("test");
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				changeIcon(1);
				//System.out.println("test");
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println("test");
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println("test");
				
			}
			
			
		});
	}
		
	
	public MonBouton(URL str, URL str2) {
	super();
	
	

	
		icon1 = new ImageIcon(str);
	
	
	
		icon2 = new ImageIcon(str2);

	this.setIcon(icon1);
	this.setBorderPainted(false);
	this.setBorder(null);
	this.setMargin(new Insets(0, 0, 0, 0));
	this.setContentAreaFilled(false);
	this.addMouseListener(new MouseListener(){

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			changeIcon(2);
			//System.out.println("test");
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			changeIcon(1);
			//System.out.println("test");
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			//System.out.println("test");
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			//System.out.println("test");
			
		}
		
		
	});
}


	// Swap entre les 2 icons	
	public void changeIcon(int x){
		if(x == 1){
			this.setIcon(icon1);
		}
		else if(x == 2){
			this.setIcon(icon2);
		}
	}


	public static String pathResourcesName(URL resource) {
		// TODO Auto-generated method stub
		return resource.getPath();
	}
	
	
		
}

