package Comparaison;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 11310461
 */

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import it.sauronsoftware.jave.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AudioEncoderDecoder {
    
    private static final Integer bitrate = 16000;//Minimal bitrate only
    private static final Integer channels = 1; //2 for stereo, 1 for mono
    private static final Integer samplingRate = 8000;//For good quality
    
    private AudioAttributes audioAttr = new AudioAttributes();
    private EncodingAttributes encoAttrs = new EncodingAttributes();
    //GERE ICI SI ENCODER PRENDS DES ARGUMENT OU NON ;
    private Encoder encoder;
    
    private final String oggFormat = "ogg";
    private final String mp3Format = "mp3";
    private final String wavFormat = "wav";
    private String osOfficiel;
    
    private final String oggCodec = "vorbis";
    private ArrayList<String> testMusicSw;

    /* Set the default attributes
     * for encoding
     */
    public AudioEncoderDecoder(){
        audioAttr.setBitRate(bitrate);
        audioAttr.setChannels(channels);
        audioAttr.setSamplingRate(samplingRate);
        osOfficiel = System.getProperty("os.name");
        if (osOfficiel.contains("Mac") || osOfficiel.contains("MAC")){
        	this.encoder = new Encoder(new MyFFMPEGExecutableLocator());
        }
        else{
        	this.encoder = new Encoder();
        }
        testMusicSw = new ArrayList<String>();
    }
    public ArrayList<String> gettestMusicSw(){
    	return this.testMusicSw;
    }
    public void clearTestMusicSw(){
    	testMusicSw.clear();
    }
    public void addTestMusicSw(String a){
    	testMusicSw.add(a);
    }
    private void mp3ToOgg(File source, File target){
        //ADD CODE FOR CHANGING THE EXTENSION OF THE FILE
        encoAttrs.setFormat(oggFormat);
        audioAttr.setCodec(oggCodec);
        encoAttrs.setAudioAttributes(audioAttr);
        try{
        encoder.encode(source, target, encoAttrs);
        }catch(Exception e){
            System.out.println("Encoding Failed1");
        }
    }
    
      private void mp3ToWave(File source, File target){
        //ADD CODE FOR CHANGING THE EXTENSION OF THE FILE
        audioAttr.setCodec("pcm_u8");
        encoAttrs.setFormat("wav");
        encoAttrs.setAudioAttributes(audioAttr);
        try{
        encoder.encode(source, target, encoAttrs);
        }catch(Exception e){
            System.out.println("Encoding Failed2");
        }
    }

        public void encodeAudio(File source, File target, String mimeType)
        {
        //Change the hardcoded mime type later on
            if(mimeType.equals("audio/mp3"))
            {
                this.mp3ToOgg(source, target);
            }
            
            if(mimeType.equals("audio/wave"))
            {
                this.mp3ToWave(source,target);
            }
        }

    public String convert(String Path,String iniPath)
        {
            String newPath ;
            String nomDuFichier = null;
            File source = new  File(Path);
            System.out.println("converting file : "+Path);

            
            //Si c'est windows
            if (osOfficiel.contains("Windows")){
            	//récupère l'indice du dernier anti slash
            	//int q = Path.lastIndexOf("\\");
            	int q = iniPath.length();
            	String tempPath = null;
            	if( q != 0){
            		//je stock le chemin sans le nom du fichier
            		tempPath = Path.substring(0,q);
            	}
            	//Je récupère lol.mp3
            	//Path a = Paths.get(Path);
            	//converti en string
            	//nomDuFichier = a.getFileName().toString();
            	nomDuFichier = Path.substring(q);
            	newPath= tempPath + "\\tmp\\";
            }
            else{															// converting file : /tmp/MUSIQUE/tt/musiqueA4.mp3
            	//récupère l'indice du dernier anti slash
            	//int q = Path.lastIndexOf("/");
            	int q = iniPath.length();
            	String tempPath = null;
            	if( q != 0){
            		//je stock le chemin sans le nom du fichier
            		tempPath = Path.substring(0,q);							//tempPath = /tmp/MUSIQUE/
            	}
            	//Je récupère lol.mp3
            	//String mPath = Path.substring(q);							
            	//Path a = Paths.get(Path);
            	//converti en string
            	//nomDuFichier = a.getFileName().toString();
            	nomDuFichier = Path.substring(q);							//mPath = tt/musiqueA4.mp3
            	newPath= tempPath + "/tmp/";
            }
            
            int i = nomDuFichier.lastIndexOf (".");
            if ( i != -1 )
            {
            	//on a plus que le nom du fichier ici
            	nomDuFichier = nomDuFichier.substring (0, i);
            }
            newPath = newPath + nomDuFichier + ".wav";
        
        File target = new File(newPath);
        if(!target.exists()){
        //Test Mp3 To Ogg Convertion
        String mimeType = "audio/wave";
        //ICI/////////////////////////////////////////////////////////////////////////////////////:
        this.encodeAudio(source, target, mimeType);
        }
        return newPath;
       
    }
    
    /*
    public ArrayList<String> convertAll(ArrayList<String> allMusics,String st)
    {
    	
        ExecutorService tpes =
                Executors.newFixedThreadPool(20);
        int i;
        ArrayList<String> newPaths = new ArrayList<String>();
        
        for(i=0;i<allMusics.size();i++)
        {

        	Future<String> future = tpes.submit(new WorkerThread(this,allMusics.get(i),st));
        	try {
				testMusicSw.add(future.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

        }
        tpes.shutdown();
        return newPaths;
    }
    */
}