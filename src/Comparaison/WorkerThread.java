package Comparaison;

import java.util.concurrent.Callable;

public class WorkerThread implements Callable<String> {

    private AudioEncoderDecoder ad;
    private String s;
    private String st;

    public WorkerThread(AudioEncoderDecoder ad,String s,String st) {
        this.ad = ad;
        this.s = s;
        this.st = st;
    }
	
	@Override
	public String call() throws Exception	{

		return ad.convert(s,st);
	}
}