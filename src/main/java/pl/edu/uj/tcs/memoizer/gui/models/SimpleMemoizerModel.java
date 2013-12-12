package pl.edu.uj.tcs.memoizer.gui.models;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import pl.edu.uj.tcs.memoizer.gui.views.IMemoizerView;
import pl.edu.uj.tcs.memoizer.handlers.IHandler;
import pl.edu.uj.tcs.memoizer.plugins.Meme;
import pl.edu.uj.tcs.memoizer.plugins.communication.DownloadMemeException;
import pl.edu.uj.tcs.memoizer.plugins.communication.IMemeProvider;


public class SimpleMemoizerModel implements IMemoizerModel, IHandler<Meme> {
	private IMemeProvider memeProvider;
	
	//Change to synchronizedd version
	private ArrayList<Meme> memes = new ArrayList<Meme>();
	
	private IMemoizerView view;
	
	private AtomicInteger requestedItems = new AtomicInteger(-1);//Greatest k, requested in tryGet/get 
	private int completedItems = -1;
	private Thread worker;
	private boolean isSinkDry = false;
	
	/**
	 * 
	 * @param connector Data source for this model
	 */
	public SimpleMemoizerModel(final IMemeProvider memeProvider){
		this.memeProvider = memeProvider;
		System.out.println("SimpleMemoizerModel: "+memeProvider.getCurrentView().getViewType());
		
		//TODO zabezpieczyć workera przed błędami
		worker = new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){
					while(completedItems<requestedItems.get()){
						Meme meme;
						try {
							meme = memeProvider.getNext();
							memes.add(meme);
							completedItems++;
							synchronized(SimpleMemoizerModel.this){
								SimpleMemoizerModel.this.notifyAll();
							}
							if(view!=null)
								view.notifyUpdate();
						} catch (ExecutionException e) {
							e.printStackTrace();
						} catch (DownloadMemeException e){
							e.printStackTrace();
							System.out.println("Koniec memów");
							SimpleMemoizerModel.this.isSinkDry = true;
							synchronized(SimpleMemoizerModel.this){
								SimpleMemoizerModel.this.notifyAll();
							}
							
							if(view!=null)
								view.notifyStreamEnd();
							
							break;
						}
					}
					try {
						synchronized(worker){
							worker.wait();
						}
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
	public void bindView(IMemoizerView view){
		this.view = view;
	}

	private void notifyView(int k){
		if(view!=null)
			view.notifyUpdate();
	}

	
	@Override
	public boolean tryGet(int k) {
		if(memes.size()<=k){
			//TODO inicjalizacja ładowania w tle
			while(true){
				int v = requestedItems.get();
				if(v>=k||requestedItems.compareAndSet(v, k))
					break;
			}
			synchronized(worker){
				worker.notifyAll();
			}
			return false;
		}
		return true;
	}

	
	@Override
	public Meme get(int k){
		while(true){
			int v = requestedItems.get();
			if(v>=k||requestedItems.compareAndSet(v, k))
				break;
		}
		
		synchronized(worker){
			worker.notify();
		}
		
		while(completedItems<k&&!isSinkDry)
			try {
				synchronized(this){wait();}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		if(memes.size()<k)
			try {
				throw new DownloadMemeException("No new memes to return");
			} catch (DownloadMemeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return memes.get(k);
	}

	@Override
	public void handle(Meme t) {
		// TODO Auto-generated method stub
		System.out.println("Meme handle: "+t);
		
	}
}
