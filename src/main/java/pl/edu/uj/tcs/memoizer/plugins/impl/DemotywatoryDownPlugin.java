package pl.edu.uj.tcs.memoizer.plugins.impl;

import pl.edu.uj.tcs.memoizer.plugins.*;

import java.net.*;
import java.util.*;
import java.awt.Image;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * Plugin do pobierania z Demotywatorow
 * @author pmikos (sokar92)
 */
public class DemotywatoryDownPlugin implements IDownloadPlugin {
	private static String _workingUrl = "http://www.demotywatory.pl";
	private Map<String, Object> _properties;
	
	/*
	 * Instantiates new download plugin designed
	 * for "Demotywatory.pl" page
	 */
	public DemotywatoryDownPlugin(){
		_properties = new HashMap<String, Object>();
	}
	
	public String getName() { return "Demotywatory"; }
	
	//TO DO
	public Image getIcon() { return null; }
	
	public Map<String, Object> getProperties(){
		return _properties;
	}
	
	public void setProperties(Map<String, Object> properties){
		if(properties == null)
			throw new NullPointerException();
		
		_properties = properties;
	}
	
	/*
	 * Returns all demots from star page (TO DO more)
	 */
	public Iterable<MemeInfo> getRecordsSinceLast(){
		return extractMemesFromNodes(
				extractMemeNodes(
				downloadPageSource(_workingUrl)));
	}
	
	//TO DO
	public Iterable<MemeInfo> getTopRecords(){
		return null;
	}
	
	
	private static URL extractPageLinkFromATag(Element aTagElement){
		try{
			return new URL(_workingUrl + aTagElement.attr("href"));
		}catch(Exception e){}
		return null;
	}
	
	private static URL extractImageLinkFromImgTag(Element imgTagElement){
		try{
			return new URL(imgTagElement.attr("src"));
		} catch(Exception e){}
		return null;
	}
	
	private static String extractTitleFromImgTag(Element imgTagElement){
		try{
			return imgTagElement.attr("alt");
		} catch(Exception e){}
		return "";
	}
	
	private static int extractWidthFromImgTag(Element imgTagElement){
		try{
			return Integer.parseInt(imgTagElement.attr("width"));
		} catch(Exception e){}
		return 0;
	}
	
	private static int extractHeightFromImgTag(Element imgTagElement){
		try{
			return Integer.parseInt(imgTagElement.attr("height"));
		} catch(Exception e){}
		return 0;
	}
	
	/*
	 * Parse page source and extract meme infos
	 * returns list of meme-linked html elements
	 */
	private static List<MemeInfo> extractMemesFromNodes(Elements memeNodes){
		List<MemeInfo> lst = new ArrayList<MemeInfo>();
		
		for(Element meme : memeNodes){
			try{
				String desc = "";
				
				Element picLink = meme.select("a.picwrapper[href]").first();
				URL pageLink = extractPageLinkFromATag(picLink);
				
				Element image = picLink.select("img.demot[src]").first();
				URL imageLink = extractImageLinkFromImgTag(image);
				String title = extractTitleFromImgTag(image);
				int width = extractWidthFromImgTag(image);
				int heigth = extractHeightFromImgTag(image);
				
				if(imageLink != null)
					lst.add(new MemeInfo(imageLink, pageLink, title, desc, width, heigth));
			} catch(Exception e){}
		}
		
		return lst;
	}
	
	/*
	 * Extract html meme-linked elements from page source
	 */
	private static Elements extractMemeNodes(Document demotyPageSource){
		Elements result = new Elements();
		
		if(demotyPageSource == null)
			return result;
		
		Elements demotySection = demotyPageSource.select("section.demots");
		for(Element elem : demotySection){
			Elements demotivators = elem.select("div.demotivator[id]");
			result.addAll(demotivators);
		}
		
		return result;
	}
	
	/*
	 * Download page source from specific url
	 * If success returns downloaded page
	 * otherwise returns null
	 */
	private static Document downloadPageSource(String url){
		try{
			return Jsoup
					.connect(url)
					.userAgent("Mozilla")
					.get();
		} catch(IOException e){}
		return null;
	}

	@Override
	public List<EViewType> getAvailableViews() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setView(EViewType viewType) {
		// TODO Auto-generated method stub
		
	}

}