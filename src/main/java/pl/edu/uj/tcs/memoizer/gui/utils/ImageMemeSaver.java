package pl.edu.uj.tcs.memoizer.gui.utils;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import pl.edu.uj.tcs.memoizer.filesystem.MemeImageSaver;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

/**
 * @author Marcin Ziemiński
 * @author Paweł Kubiak
 * @author Maciej Poleski
 */
public class ImageMemeSaver {

    /**
     * Służy do zapisu danego Memu na dysku (na żądanie użytkownika).
     *
     * @param meme Mem do zapisania
     */
	public static void saveImageToDisk(Meme meme){
		while(true){
			try{
				JFileChooser saveFile = new JFileChooser();
				saveFile.setSelectedFile(new File(meme.getSuggestedFileName()));
				if(saveFile.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					MemeImageSaver.saveMemeImage(meme, saveFile.getSelectedFile());
					return ;
				}else
					return ;
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}
