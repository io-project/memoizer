package pl.edu.uj.tcs.memoizer.serializationTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileStateSink implements IStateSink {
	
	private File file;
	
	public FileStateSink(File file) {
		this.file = file;
	}

	@Override
	public void saveData(String object) throws SerializationException {

		FileWriter fw = null;
		try {

			fw = new FileWriter(file, false);
			fw.write(object);

		} catch (IOException e) {
			throw new SerializationException(e);
		} finally {
			try {
				if(fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				throw new SerializationException(e);
			}
		}
	}

}
