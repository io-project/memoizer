package pl.edu.uj.tcs.memoizer.plugins;

import java.net.*;
import java.awt.Image;

/*
 * Describes Meme
 * @author pmikos (sokar92) 
 */
public class Meme {
	//links
	private URL _imageLink;
	private URL _pageLink;
	
	//description
	private String _title;
	private String _desc;
	
	//meme size
	private int _width;
	private int _heigth;
	
	//image
	private Image _image;
	
	//owner
	private IPlugin _owner;
	
	/*
	 * Instantiates new Meme linked to given owner
	 */
	public Meme(IPlugin owner, Meme meme){
		this(meme.getImageLink(), meme.getPageLink(),
				meme.getTitle(), meme.getDescription(),
				meme.getWidth(), meme.getHeight(),
				meme.getImage());
		_owner = owner;
	}
	
	/*
	 * Instantiates new Meme linked to specific URL
	 * with given title, description and size
	 */
	public Meme(URL imageUrl, URL pageUrl, 
			String title, String description, 
			int width, int heigth,
			Image image)
	{
		_owner = null;
		_imageLink = imageUrl;
		_pageLink = pageUrl;
		_title = title == null ? "" : title;
		_desc = description == null ? "" : description;
		_width = width;
		_heigth = heigth;
		_image = image;
	}
	
	/*
	 * Returns meme owner - plugin
	 * which generated this meme
	 */
	public IPlugin getOwner(){
		return _owner;
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
	
	/*
	 * Returns downloaded image
	 */
	public Image getImage(){
		return _image;
	}
	
	/*
	 * Sets downloaded image
	 */
	public void setImage(Image img){
		_image = img;
	}
}