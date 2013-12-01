package pl.edu.uj.tcs.memoizer.filesystem;

import java.io.*;
import java.net.URL;

/*
 * 
 * @author Retax
 */
public class ImageSaver implements IImageSaver{
    
    /*
     * Gets image from URL and saves it as file    
     */
    @Override
    public void saveImgFromURL(URL url, String path) throws IOException{
       InputStream is = url.openStream();
       OutputStream os = new FileOutputStream(path);
       
       byte[] b=new byte[2048];
       int len;
       
       while((len=is.read(b)) != -1) os.write(b, 0, len);
       
       is.close();
       os.close();
    }
}
