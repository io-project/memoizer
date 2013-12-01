package pl.edu.uj.tcs.memoizer.gui.utils;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import pl.edu.uj.tcs.memoizer.filesystem.MemeImageSaver;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

public class ImageMemeSaver {

	public static void saveImageToDisk(Meme meme) throws IOException {
		
		JFileChooser saveFile = new JFileChooser();
		saveFile.setSelectedFile(new File(meme.getSuggestedFileName()));
		if(saveFile.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			MemeImageSaver.saveMemeImage(meme, 
					saveFile.getSelectedFile());
		}
	}
}
