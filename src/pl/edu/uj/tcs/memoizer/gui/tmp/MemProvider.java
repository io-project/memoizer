package pl.edu.uj.tcs.memoizer.gui.tmp;

interface Mem{
	public String getTitle();
	public String getImageUrl();
	public String getUrl();
	
	public void starIt();
}


public class MemProvider {
	public Mem next(){
		return null;		
	}
}
