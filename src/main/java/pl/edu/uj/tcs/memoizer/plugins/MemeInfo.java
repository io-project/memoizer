package pl.edu.uj.tcs.memoizer.plugins;

import java.net.*;

/*
 * Describes Meme
 * @author pmikos (sokar92) 
 */
public class MemeInfo {
	//links
	private URL _imageLink;
	private URL _pageLink;
	
	//description
	private String _title;
	private String _desc;
	
	//meme size
	private int _width;
	private int _heigth;
	
	/*
	 * Instantiates new Meme with
	 * image linked to specific URL
	 */
	public MemeInfo(URL imageUrl){
		this(imageUrl, null, null, null, 0, 0);
	}
	
	/*
	 * Instantiates new Meme with
	 * image linked to first URL
	 * and base page linked to second URL
	 */
	public MemeInfo(URL imageUrl, URL pageUrl){
		this(imageUrl, pageUrl, null, null, 0, 0);
	}
	
	/*
	 * Instantiates new Meme linked to specific URL
	 * with given title, description and size
	 */
	public MemeInfo(URL imageUrl, URL pageUrl, String title, String description, int width, int heigth){
		_imageLink = imageUrl;
		_pageLink = pageUrl;
		_title = title == null ? "" : title;
		_desc = description == null ? "" : description;
		_width = width;
		_heigth = heigth;
	}
	
	/*
	 * Returns URL to image linked with Meme
	 */
	public URL getImageLink(){
		return _imageLink;
	}
	
	/*
	 * Returns URL to Meme base page
	 * may be null if not defined!
	 */
	public URL getPageLink(){
		return _pageLink;
	}
	
	/*
	 * Returns Meme title if defined,
	 * otherwise empty string
	 */
	public String getTitle(){
		return _title;
	}
	
	/*
	 * Returns Meme description if defined,
	 * otherwise empty string
	 */
	public String getDescription(){
		return _desc;
	}
	
	/*
	 * Returns meme image width
	 */
	public int getWidth(){
		return _width;
	}
	
	/*
	 * Returns meme image heigth
	 */
	public int getHeight(){
		return _heigth;
	}
}