package pl.edu.uj.tcs.memoizer.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;
import pl.edu.uj.tcs.memoizer.plugins.Meme;
import pl.edu.uj.tcs.memoizer.serialization.SerializationException;
import pl.edu.uj.tcs.memoizer.serialization.StateMap;


/**
 * Class providing metadata store for memes.
 * 
 * @author pkubiak
 */
public class MetadataHandler {
	private JSONObject bookmarked;
	private JSONObject seened;
	private StateMap stateMap;
	
	public MetadataHandler(StateMap stateMap){
		bookmarked =  stateMap.get("bookmarked").getJSON();
		seened = stateMap.get("seened").getJSON();
		this.stateMap = stateMap;
		
		
	}
	
	private String getKey(Meme meme){
		return (meme.getServiceName()+"-"+meme.getId());
	}
	
	public synchronized boolean isBookmarked(Meme meme){
		//TODO add cases if getPluginFactory()==null || getId() == null
		return bookmarked.containsKey(getKey(meme));
	}
	
	private JSONObject serializeMemeToJSONObject(Meme meme){
		JSONObject obj = new JSONObject();
		
		obj.put("image-link", (meme.getImageLink()==null?null:meme.getImageLink().toString()));
		obj.put("page-link",  (meme.getPageLink()==null?null:meme.getPageLink().toString()));
		
		obj.put("title", meme.getTitle());
		obj.put("description", meme.getDescription());
		
		obj.put("service", meme.getServiceName());
		
		obj.put("id", meme.getId());
		
		return obj;
	}
	
	private Meme deserializeJSONObjectToMeme(JSONObject obj){
		try{
			String imageLink = null, pageLink = null, title = null, description = null, serviceName = null;
			Integer id = null;
			
			if(obj.containsKey("image-link"))
				imageLink = obj.getString("image-link");
			
			if(obj.containsKey("page-link"))
				pageLink = obj.getString("page-link");
			
			if(obj.containsKey("title"))
				title = obj.getString("title");
			
			if(obj.containsKey("description"))
				description = obj.getString("description");
			
			if(obj.containsKey("service"))
				serviceName = obj.getString("service");
			
			if(obj.containsKey("id"))
				id = obj.getInt("id");
			Meme meme = new Meme(new URL(imageLink), new URL(pageLink), title, description, 0, 0, null, null);
			meme.setId(id);
			meme.setServiceName(serviceName);
			return meme;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	public synchronized void setBookmarked(Meme meme, boolean isBookmarked){
		System.out.println("MEME: "+meme.getTitle()+" "+isBookmarked);
		
		if(bookmarked.containsKey(getKey(meme))){
			if(isBookmarked==false){
				bookmarked.remove(getKey(meme));
				try {
					stateMap.save();
				} catch (SerializationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			if(isBookmarked==true){
				JSONObject x = serializeMemeToJSONObject(meme);
				bookmarked.put(getKey(meme), x);
				try {
					stateMap.save();
				} catch (SerializationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public synchronized List<Meme> getBookmarked(){
		List<Meme> list = new ArrayList<Meme>();
		
		for(Object obj: bookmarked.entrySet()){
			if(obj instanceof JSONObject){
				Meme x = deserializeJSONObjectToMeme((JSONObject)obj);
				if(x!=null)
					list.add(x);
			}
		}
		return list;
	}
	
	public synchronized boolean isSeened(Meme meme){
		if(meme.getServiceName()!=null&&seened.containsKey(meme.getServiceName())){
			try{
				JSONObject obj = seened.getJSONObject(meme.getServiceName());
				if(obj!=null&&obj.containsKey(meme.getId().toString()))
					return true;
				else return false;
			}catch(Exception e){
				return false;
			}
		}else 
			return false;
	}
	
	public void setSeened(Meme meme, boolean isSeened){
		if(isSeened(meme)){
			if(isSeened==false){
				try{
					JSONObject x = seened.getJSONObject(meme.getServiceName());
					x.remove(meme.getId().toString());
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}else{
			if(isSeened==true){
				if(!seened.containsKey(meme.getServiceName()))
					seened.put(meme.getServiceName(), new JSONObject());
				JSONObject x = seened.getJSONObject(meme.getServiceName());
				if(!x.containsKey(meme.getId().toString()))
					x.put(meme.getId().toString(), new Boolean(true));
			}
		}
	}
}
