package Classes;

public enum Format {
	wav,
	mp3,
	aac,
	wma,
	mp4a,
	flac,
	ogg;
	
	public static boolean contains(String test) {

	    for (Format c : Format.values()) {
	        if (c.name().equals(test)) {
	            return true;
	        }
	    }

	    return false;
	}
}
