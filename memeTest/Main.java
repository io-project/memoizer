package memeTest;

import pl.edu.uj.tcs.memoizer.plugins.*;

import java.util.*;

public class Main {
	public static void main(String[] args) {
		PluginManager manager = new PluginManager();
		manager.addPluginDirectory("C:\\plugin");
		
		manager.loadPlugins();
		List<IPluginFactory> list = manager.getLoadedPluginFactories();
		System.out.println("Found: " + list.size());
		
		for(IPluginFactory plug : list){
			System.out.println(plug.getName());
			
			List<EViewType> views = new ArrayList<EViewType>();
			views.add(EViewType.CHRONOLOGICAL);
			views.add(EViewType.FAVOURITE);
			views.add(EViewType.UNSEEN);
			views.add(EViewType.QUEUE);
			
			Map<String, Object> map = new HashMap<String, Object>();
			
			IPlugin plugin = plug.newInstance(map);
			IDownloadPlugin dwnlPlug = (IDownloadPlugin)plugin;
			
			for(EViewType view : views){
				try{
					dwnlPlug.setView(view);
					System.out.println("For view: " + view.toString());
					
					for(int i=0;i<100;++i){
						if(dwnlPlug.hasNext())
							printfMeme(dwnlPlug.getNext());
					}
				} catch(Exception e){
					System.out.println("There is a problem with view " + view.toString());
				}
			}
		}
	}

	private static void printfMeme(Meme meme) {
		System.out.println("Meme: ");
		System.out.println(" title: " + meme.getTitle());
		System.out.println(" desc: " + meme.getDescription());
		System.out.println(" imageLink: " + meme.getImageLink());
		System.out.println(" pageLink: " + meme.getPageLink());
		System.out.println(" size: " + meme.getWidth() + " x " + meme.getHeight());
		System.out.println();
	}
}
