package Comparaison;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import it.sauronsoftware.jave.FFMPEGLocator;

public class MyFFMPEGExecutableLocator extends FFMPEGLocator {

	@Override
	protected String getFFMPEGExecutablePath() {
		String path = null;
		//le chemin de fichier est a géré
		URL url;
		url = MyFFMPEGExecutableLocator.class.getResource("/ffmpeg_mac");
		System.out.println(" On est dans le getFFMPEGE : " + url.toString());
		
		path = url.toString();
		String [] lesSplits = new String[2];
		lesSplits = path.split(":");
		
		System.out.println(lesSplits[1]);
		// Need a chmod?
		Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec(new String[] { "/bin/chmod", "755",
						lesSplits[1] });
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		return lesSplits[1];
	}

}
