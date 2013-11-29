package pl.edu.uj.tcs.memoizer.serialization;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class FileStateSinkSourceTest {

	@Test
	public void filesaveloadtest() throws SerializationException, DeserializationException {
		
		File f = new File("testFile");
		FileStateSink fsink = new FileStateSink(f);
		FileStateSource fsource = new FileStateSource(f);
		
		String text = "blablablablalbalblablalbalblallb_twoja_stara!";
		fsink.saveData(text);
		String loaded = fsource.getData();
		Assert.assertEquals(text, loaded);

		text = "NaNaNanNan Batman!!!";
		fsink.saveData(text);
		loaded = fsource.getData();
		Assert.assertEquals(text, loaded);
	}
		
}
