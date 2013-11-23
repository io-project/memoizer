package pl.edu.uj.tcs.memoizer.filemanager;

import java.io.IOException;
import java.net.URL;

public interface IFileManager{
    
    void saveImgFromURL(URL url, String path) throws IOException;
}
