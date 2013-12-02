package pl.edu.uj.tcs.memoizer.serializationTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class FileStateSource implements IStateSource {
	
	private File file;
	
	public FileStateSource(File file) {
		this.file = file;
	}

	@Override
	public String getData() throws DeserializationException {

		FileInputStream fis = null;
		try {

			fis = new FileInputStream(file);
			return IOUtils.toString(fis);

		} catch (FileNotFoundException e) {
			throw new DeserializationException(e);
		} catch (IOException e) {
			throw new DeserializationException(e);
		} finally {
			try {
				if(fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				throw new DeserializationException(e);
			}
		}
	}

}
