package pl.edu.uj.tcs.memoizer.plugins;

import java.net.*;

/*
 * Klasa opisujaca pojedynczego mema
 * @author pmikos (sokar92) 
 */
public class MemeInfo {
	private URL _link;
	private String _title;
	private String _desc;
	
	public MemeInfo(URL url){
		this(url, null, null);
	}
	
	public MemeInfo(URL url, String title, String description){
		_link = url;
		_title = title == null ? "" : title;
		_desc = description == null ? "" : description;
	}
	
	/*
	 * Zwraca Url do obrazka
	 */
	public URL getLink(){
		return _link;
	}
	
	/*
	 * Zwraca tytul obrazka jesli istnieje, wpp string empty
	 */
	public String getTitle(){
		return _title;
	}
	
	/*
	 * Zwraca opis obrazka jesli istnieje, wpp string empty
	 */
	public String getDescription(){
		return _desc;
	}
}