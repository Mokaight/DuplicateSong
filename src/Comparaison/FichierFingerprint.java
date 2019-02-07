package Comparaison;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class FichierFingerprint {

	ArrayList<String> path;
	ArrayList<byte[]> key;
	ArrayList<Date>	date;
	
	public FichierFingerprint(ArrayList<String> a, ArrayList<byte[]> b){
		this.path = a;
		this.key = b;
		this.date = new ArrayList<Date>();
	}
	public FichierFingerprint(ArrayList<String> a, ArrayList<byte[]> b, ArrayList<Date> d){
		this.path = a;
		this.key = b;
		this.date = d;
	}

	public FichierFingerprint() {
		// TODO Auto-generated constructor stub
	}

	
	public ArrayList<Date> getDate() {
		return date;
	}
	public void setDate(ArrayList<Date> date) {
		this.date = date;
	}
	public ArrayList<String> getPath() {
		return path;
	}

	public void setPath(ArrayList<String> path) {
		this.path = path;
	}

	public ArrayList<byte[]> getKey() {
		return key;
	}

	public void setKey(ArrayList<byte[]> key) {
		this.key = key;
	}
	public void addPath(String a){
		path.add(a);
	}
	public void addKey (byte[] a){
		key.add(a);
	}
}
