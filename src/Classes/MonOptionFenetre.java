package Classes;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class MonOptionFenetre extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTree arbre;
	private DefaultMutableTreeNode racine;
	private JButton ok;
	private JButton annuler;
	private String cheminString;
	private String cheminStringReturn;
	private JPanel jp;
	
	public MonOptionFenetre(JFrame parent, String title, boolean modal){
		super(parent, title, modal);
	    this.setSize(340, 600);
	    this.setLocationRelativeTo(null);
	    this.setResizable(false);
	    init();
	}
	
	public void init(){
		cheminStringReturn = "";
		cheminString = "";
		ok = new JButton("Ok");
		annuler = new JButton("Annuler");
		jp = new JPanel();
		jp.add(ok);
		jp.add(annuler);

		listRoot();
		listener();
	}
	
	public void listener(){
	    ok.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	    	cheminStringReturn = cheminString;
	        setVisible(false);
	      }      
	    });
	    
		annuler.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	        setVisible(false);
	      }      
	    });
	}
	
	public String getString(){
		return cheminStringReturn;
	}
	
	private void addFils(MonArbre branche){
		
		System.out.println("Création dynamique du fichier parcouru");
		
		File mFile = branche.getFile();
		  try {
			    for(File nom : mFile.listFiles()){
			    	MonArbre node = new MonArbre(nom.getName()+"/");
			    	
			    	if(!nom.getName().substring(0,1).equals(".")){
				    	node.addFile(nom);
				        branche.add(node);
			    		
			    	}
			    }
			  } catch (NullPointerException e) {}
	}
	
	private void listRoot(){      
		this.racine = new MonArbre();

		for(File file : File.listRoots()){
			//Depart de l'arbre 
			MonArbre lecteur = new MonArbre(file.getAbsolutePath());
			lecteur.addFile(file);
			this.racine.add(lecteur);      
			System.out.println(lecteur.toString());
		}

		arbre = new JTree(this.racine);
		arbre.addTreeSelectionListener(new TreeSelectionListener(){

		      public void valueChanged(TreeSelectionEvent event) {
		        if(arbre.getLastSelectedPathComponent() != null){
		        	String extension = arbre.getLastSelectedPathComponent().toString().substring(arbre.getLastSelectedPathComponent().toString().lastIndexOf(".") + 1, arbre.getLastSelectedPathComponent().toString().length()-1);
		          System.out.println("Extension :  "+extension);
		          System.out.println("La selection :  "+ arbre.getLastSelectedPathComponent().toString());
		          cheminString = getAbsolutePath(event.getPath());
		          MonArbre dmtn = (MonArbre) arbre.getLastSelectedPathComponent();
		          if(dmtn.getGeneration()==false){
		        	  addFils(dmtn);
		        	  dmtn.setGeneration(true);
		          }
		          
		        }
		      }
		    });
		//Que nous placons sur le ContentPane de notre JFrame à l'aide d'un scroll 
		this.getContentPane().add(new JScrollPane(arbre),BorderLayout.CENTER);
		this.getContentPane().add(jp,BorderLayout.SOUTH);
	}

	private String getAbsolutePath(TreePath treePath){
		    String str = "";
		    //On balaie le contenu de l'objet TreePath
		    for(Object name : treePath.getPath()){
		      //Si l'objet a un nom, on l'ajoute au chemin
		      if(name.toString() != null)
		        str += name.toString();
		    }
		    return str;
	}
	
	public void setVisibleuh(){
		this.setVisible(true);
	}
	
}