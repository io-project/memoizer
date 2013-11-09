package pl.edu.uj.tcs.memoizer.plugins.communication;

public interface IScheduledMemeDownloader extends IMemeDownloader {

	public void setMinRefreshRate(long time);

	public long getMinRefreshRate();

}
