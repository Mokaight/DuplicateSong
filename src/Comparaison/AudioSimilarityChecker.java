package Comparaison;

import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.wave.Wave;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.io.IOUtils;

public class AudioSimilarityChecker {
    
  
   private ArrayList<String> userMusics;
   private ArrayList<String> musics;
   private HashMap<Integer,Integer> doblets;
   private ArrayList<byte[]> listFingerprints;
   private AudioEncoderDecoder AED;
   //contient 2 arraylist;
   private FichierFingerprint FF;
   //liste de waves
   private ArrayList<Wave> waves;
   
   	public AudioSimilarityChecker() {
        this.userMusics = new ArrayList();
        this.musics = new ArrayList();
        this.doblets = new HashMap<Integer,Integer>();
        this.listFingerprints = new ArrayList();
        this.AED = new AudioEncoderDecoder();
        this.FF = new FichierFingerprint(new ArrayList<String>(), new ArrayList<byte[]>());
        this.waves = new ArrayList<Wave>();
   	}


   	public void ecritureFichierSauv(String s,byte[] b){
 		
 		// string byte date
 		// ? chemin du string
 		//pas top , on ouvre le fichier a chaque fois ...
 		try {
 			System.out.println("On est dans ecriture fichier ");
 			String fichier = getCheminAbsolueSansNom(s) + "save.txt";
 			BufferedWriter tampon = new BufferedWriter(new FileWriter(fichier,true));
 			
 			
 			
 	 		File file = new File(s);
 	 		String lol = new String(b,"UTF-8");
 			tampon.write(s+" "+ lol +" "+file.lastModified()+"\n");
 			System.out.println("On a écrit" +s+" "+ "la clef"  +" "+file.lastModified()+"dans " + fichier );
 			tampon.flush();
 			tampon.close();
 			System.out.print("Fin ecriture \n");
 		}
 		catch(IOException e){
 			System.out.println ("Erreur dans ecriture");
 			
 		}

   	}
   	public String getCheminAbsolueSansNom(String path){
   		String osOfficiel = System.getProperty("os.name");
   		String tempPath = null;
   		//si c'est windows 
        if (osOfficiel.contains("Windows")){
        	//récupère l'indice du dernier anti slash
        	int q = path.lastIndexOf("\\");
        	
        	if( q != -1){
        		//je stock le chemin sans le nom du fichier
        		tempPath = path.substring(0,q) + "\\";
        	}
        }
        else{
        	//récupère l'indice du dernier anti slash
        	int q = path.lastIndexOf("/");
        	if( q != -1){
        		//je stock le chemin sans le nom du fichier
        		tempPath = path.substring(0,q) + "/";
        	}
        }
        return tempPath;
   	}
   	
   	
   @SuppressWarnings("deprecation")
	public byte[] recupCleDansFichier(String a){
   			byte[]cleLu = null;
			//a changer
   			//String a contient le chemin absolue de la chanson, il suffit de récupérer le chemin sans le nom pour le fichier
   			
			String fichier =getCheminAbsolueSansNom(a) + "save.txt";
			String[] part = new String [3];
			
			long l;
			File tempA = new File(a);
			//lecture du fichier texte	
			try{
				InputStream ips=new FileInputStream(fichier); 
				InputStreamReader ipsr=new InputStreamReader(ips,"UTF-8");
				BufferedReader br=new BufferedReader(ipsr);
				String ligne;
				while ((ligne=br.readLine())!=null){
					
					part = ligne.split(" ");
					System.out.println("Val de part0 : " + part[0] + " de 1 " + part[1] + " de 2 " + part[2]);
					System.out.println("Val de a" + a);
					if (part[0].equals(a)){
						
						System.out.println("On est bien dans légalité des noms ");
						l = Long.parseLong(part[2]);
						if(l==(tempA.lastModified())){
							System.out.println("On est dans légalité des FICHIERS");
							//modifie la clé lu ? 
							//cleLu = part[1]; 
							cleLu =part[1].getBytes("UTF-8"); 
							
						}
					}
					
		
				}
				br.close(); 
				System.out.println("Récupération réussi");
				System.out.println("La cle de " + a + " est " + "cle lu" + " dans la fnction de récupération");
			}		
		    catch (UnsupportedEncodingException e)
		    {
				System.out.println(e.getMessage());
		    }
		    catch (IOException e)
		    {
				System.out.println(e.getMessage());
		    }
			catch (Exception e){
				System.out.println("Erreur dans la récupération");
			}
		
   		return cleLu;
   	}
   public void findDobletsAvance(ArrayList<String> pathDeBase, ArrayList<String> pathStrings, ArrayList<String> listeChemin,String strChemin1){
       Wave  wave1;
       Wave wave2;
       float  similitude;
       // Transformation en wawv trop degeulasse
       
       int i = 0;
       int j = 0;
       System.out.println("La taille de pathStrings : " + pathStrings.size());
       for(i=0;i<pathStrings.size();i++){
       	wave1 = new Wave(pathStrings.get(i));
       	for(j=i;j<pathStrings.size();j++){
       		wave2 = new Wave(pathStrings.get(j));
       		if (i != j){
           		FingerprintSimilarity similarity = wave1.getFingerprintSimilarity(wave2);
           		similitude = similarity.getSimilarity();
           		System.out.println("Val de i :"+i+" Val de j :" + j);
                   if (similitude >= 0.8f){
                   	
                   	//i = i+1;
                   	System.out.println("On est dans doblet Avance :" + similitude);
                   	
                   	doblets.put(i,j);
                   	similitude =0;
                   	j=pathStrings.size();
                   	//put dans doblets le couple de doublons 
                   	//on ne teste plus que i et non plus j
                   }
       		}

       	}
       }
		}
        public void findDoblets(ArrayList<String> pathDeBase, ArrayList<String> pathStrings, ArrayList<String> listeChemin,String strChemin1)
        {
            
            Wave cheminCourant;
            byte[] lesBytesI;
            byte[] lesBytesJ;
            int i,j;
            
            // on transforme les Strings en File et on ajoute a la liste des musiques à traiter
            for(i = 0; i < pathStrings.size();i++)
            {
            	
            	//System.out.println("Path de base " + pathDeBase.get(i)+"Path string : " + pathStrings.get(i));
            	if(!(FF.getPath().contains(pathDeBase.get(i)))){
            		
            		
            		//créer un wav a partir du fichier converti en wAV
	                cheminCourant = new Wave(pathStrings.get(i));
	                
	                
	                //Ajoute a la liste de WAV
	                waves.add(cheminCourant);
	                
	                
	                //on ajoute a FF le chemin de base ( si c'était un mp3 le .mp3 et pas le .wav)
	                FF.addPath(pathDeBase.get(i));
	                
	                /*
	                 * la gestion de lecture / ecriture de cle byte array bug , l'utf 8 n'est pas bien reconnu a l'écrite( soit non lu, soit converti de facon fausse 
	                 * Il suffit pourtant de transformer un String en byte [] ,
	                 * le String est bien la bonne clé mais la conversion de marche pas
	                 * fonction a modifier : 
	                 * 	recupCleDansFichier //// ecritureFichierSauv
	                 */
	                //byte[] temp = recupCleDansFichier(pathDeBase.get(i));
	                //System.out.println(waves.get(i));
	                
	                //if (temp == null){
	                //	System.out.println("Temps est null ");
	                	//si on rentre dans ce if, cela veut dire que la musique n'est pas stocké dans le fichier, donc on l'écrit
	                	//on lui ajoute la clé associé
	                	byte[] temp2 = waves.get(i).getFingerprint();
	                //	ecritureFichierSauv(pathDeBase.get(i), temp2);
		                FF.addKey(temp2);
		                
	                /*}
	                //else {
	                	FF.addKey(temp);
	                }*/
	                System.out.println("FIND DOBLETS : La cle de :" + FF.getPath().get(i) + " est donc : " + waves.get(i).getFingerprint()); 
	                
	                
	                //On ajoute la clé dans la liste des fingerPrints
	                this.listFingerprints.add(FF.getKey().get(i));
            	}
            	
            
            }

   
            // Verification bit à bit des clé uniques des morceaux
            for(i = 0; i<FF.getKey().size();i++)
            {
                lesBytesI = FF.getKey().get(i);
                for(j = 0; j<FF.getKey().size();j++)
                {
                    lesBytesJ = FF.getKey().get(j);
                    if(Arrays.equals(lesBytesI, lesBytesJ) && i!=j)
                    {
                    	if(checkSingularity(i,j))
                    	{
                    		System.out.println("On ajoute dans le doblets");
	                        this.doblets.put(i, j); 
                    	}
                    }
                }
            }
        }
		
        public boolean checkSingularity(int i, int j)
        {
        	if(this.doblets.containsKey(i))
        	{
        		if(this.doblets.get(i).intValue() == j)
        			return false;
        	}
        	if(this.doblets.containsKey(j))
        	{
        		if(this.doblets.get(j).intValue() == i)
        			return false;
        	}
        	return true;
        }
        
        public ArrayList<String> getUserMusics() {
    		return userMusics;
    	}
       public String getUserMusics(int a) {
    		return this.userMusics.get(a);
    	}
    	public void setUserMusics(ArrayList<String> userMusics) {
    		this.userMusics = userMusics;
    	}
    	public void addUserMusics (String a ){
    		this.userMusics.add(a);
    	}
    	public void removeUserMusics (String a){
    		this.userMusics.remove(a);
    	}
    	public void removeALLUsersMusics (){
    		this.userMusics.clear();
    	}
    	public HashMap<Integer,Integer> getDoblets() {
    		return doblets;
    	}
    	public void setDoblets(HashMap<Integer,Integer> doblets) {
    		this.doblets = doblets;
    	}
    	public ArrayList<byte[]> getListFingerprints() {
    		return listFingerprints;
    	}
    	public void setListFingerprints(ArrayList<byte[]> listFingerprints) {
    		this.listFingerprints = listFingerprints;
    	}
    	public void setAED(AudioEncoderDecoder aED) {
    		AED = aED;
    	}
       	public AudioEncoderDecoder getAED(){
       		return this.AED;
       	}
       	
       	public ArrayList<String> getMusics() {
    		return musics;
    	}
    	public String getMusics(int a) {
    		return this.musics.get(a);
    	}
    	public void setMusics(ArrayList<String> musics) {
    		this.musics = musics;
    	}
    	public void setoneMusics(String path){
    		this.musics.add(path);
    	}
    	public void removeDoblets(){
    		doblets.clear();
    	}
    	public void resolveAndPrintDoblets()
    	{
    		for (Integer key : doblets.keySet()) {
    			System.out.println(" le morceau : "+userMusics.get(key)+" est un doublon du morceau :"+userMusics.get((doblets.get(key).intValue())));
    		}
    	}

    	
        public ArrayList<String> getRes() {
			
			ArrayList<String> tmp = new ArrayList<String>();
		
	
			for(Integer key : doblets.keySet())
			{
				tmp.add(userMusics.get(key));
				int valeur = ((doblets.get(key).intValue()));
		//		System.out.println("Clef "+ key+"Valeur " + valeur);
				tmp.add(userMusics.get(valeur));
				
				
			}
		return tmp;
        }

     public void emptyDoblets()
     {
    	 	 doblets.clear();
    	 	 listFingerprints.clear();
     } 	
}