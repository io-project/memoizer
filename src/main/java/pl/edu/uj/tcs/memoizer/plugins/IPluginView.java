package pl.edu.uj.tcs.memoizer.plugins;

import java.util.List;

/**
 * @author ziemin
 *
 */
public interface IPluginView {

	EViewType getViewType();
	
	/**
	 * For list of memes returns the one to be printed first.
	 * Returned meme is extracted from given list.
	 * 
	 * @param memes 
	 * @return next meme to be printed
	 */
	MemeInfo extractNextMeme(List<MemeInfo> memes);
}
