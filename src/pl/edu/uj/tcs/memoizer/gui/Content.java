package pl.edu.uj.tcs.memoizer.gui;

/**
 * Prototyp dla klasy przechowującej informacje o obrazku
 * @author pkubiak
 */
public class Content {
	private String title, url, imageUrl, description;

	private String tokenId;//token unikatowy dla konkretnego obrazka
	
	public Content(String title, String descripton, String imageUrl, String url){
		this.title = title;
		this.url = url;
		this.imageUrl = imageUrl;
		this.description = descripton;
		
		/* TODO generowanie jakiegoś sensownego tokenId lub podawanie go jako argument */
		this.tokenId = "demotywatory:12321312321";
	}
	
	/** Gettery dla pól */
	public String getTitle(){return title;}
	public String getDescription(){return description;}
	public String getImageUrl(){return imageUrl;}
	public String getUrl(){return url;}
	
	/**
	 * Sprawdza czy obrazek o tym id został oznaczony gwiazdką
	 * @return 
	 */
	public boolean isStarred(){
		return false;
	}
	
	/**
	 * Ustawia ogwiazdkowanie obrazka. Operacja powinna być nie blokująca.
	 * @param isStarred wartość na jaką ustawić
	 * @return true jeżeli operacja zakończyła się powodzeniem
	 */
	public boolean setStarred(boolean isStarred){
		return false;
	}
}
