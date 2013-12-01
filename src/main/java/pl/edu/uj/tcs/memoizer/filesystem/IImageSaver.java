package pl.edu.uj.tcs.memoizer.filesystem;

import java.io.IOException;
import java.net.URL;

public interface IImageSaver{
    
    void saveImgFromURL(URL url, String path) throws IOException;
}
