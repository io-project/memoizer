package pl.edu.uj.tcs.memoizer.gui.models;

import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import pl.edu.uj.tcs.memoizer.gui.views.JMemoizerView;
import pl.edu.uj.tcs.memoizer.handlers.IHandler;
import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.Meme;
import pl.edu.uj.tcs.memoizer.plugins.communication.MemeProvider;
import pl.edu.uj.tcs.memoizer.plugins.communication.PluginManager;
import pl.edu.uj.tcs.memoizer.plugins.impl.demoty.DemotyDownloadPluginFactory;


public class SimpleMemoizerModel implements IMemoizerModel, IHandler<Meme> {
	private MemeProvider connector;
	private ArrayList<Meme> memes = new ArrayList<Meme>();
	private JMemoizerView view;
	
	private AtomicInteger requestedItems = new AtomicInteger(-1);//Greatest k, requested in tryGet/get 
	private int completedItems = -1;
	private Thread worker;
	
	/**
	 * 
	 * @param connector Data source for this model
	 */
	public SimpleMemoizerModel(MemeProvider connector, PluginManager pluginManager){
		this.connector = connector;
		
		final IDownloadPlugin plugin = pluginManager.getPluginsForView(EViewType.CHRONOLOGICAL).get(0);
		plugin.setView(EViewType.CHRONOLOGICAL);
		
		//TODO zabezpieczyć workera przed błędami
		worker = new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){
//					System.out.println("Running");
					while(completedItems<requestedItems.get()){
						Meme meme = plugin.getNext();
						memes.add(meme);
						completedItems++;
						//System.out.println("Loaded next meme: "+completedItems);
//						System.out.println("Try synchronize");
						synchronized(SimpleMemoizerModel.this){
							SimpleMemoizerModel.this.notifyAll();
						}
//						System.out.println("Done");
						if(view!=null)
							view.notifyUpdate();
					}
					try {
//						System.out.println("Try synchronize2");
						synchronized(worker){
//							System.out.println("Sleeping");
							worker.wait(10000);
						}
//						System.out.println("Done2");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		worker.start();
	}
	
	public int getSize(){
		return memes.size();
	}
	
	@Override
	public void bindView(JMemoizerView view){
		this.view = view;
	}

	private void notifyView(int k){
		if(view!=null)
			view.notifyUpdate();
	}

	
	@Override
	public boolean tryGet(int k) {
//		System.out.println("tryGet: "+k);
		if(memes.size()<=k){
			//TODO inicjalizacja ładowania w tle
			while(true){
				int v = requestedItems.get();
				if(v>=k||requestedItems.compareAndSet(v, k))
					break;
			}
			synchronized(worker){
//				System.out.println("Notify worker");
				worker.notifyAll();
			}
			return false;
		}
		return true;
	}

	
	@Override
	public Meme get(int k){
//		System.out.println("get: "+k);
		// set 
		while(true){
			int v = requestedItems.get();
			if(v>=k||requestedItems.compareAndSet(v, k))
				break;
		}
		
		synchronized(worker){
			worker.notify();
		}
		
		while(completedItems<k)
			try {
				synchronized(this){wait();}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println("TAK: "+memes.get(0));
		return memes.get(k);
	}

	@Override
	public void handle(Meme t) {
		// TODO Auto-generated method stub
		System.out.println("Meme handle: "+t);
		
	}
}