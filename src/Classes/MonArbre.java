package Classes;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

//Class permetant le parcours de l'arborescence
public class MonArbre extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 1L;
	private Boolean generation = false;	//si il a deja generé ses fils
	private File monFile = null;
	
	public MonArbre(String s){
		super(s);
	}
	
	public MonArbre(){
		super();
	}
	
	public void setGeneration(Boolean b){
		generation = b;
	}
	
	public Boolean getGeneration(){
		return generation;
	}
	
	public void addFile(File f){
		monFile = f;
	}
	
	public File getFile(){
		return monFile;
	}
	
    public boolean isLeaf() {
    	if(monFile != null)
    		return !monFile.isDirectory();
    	else
    		return false;
   }
	
}