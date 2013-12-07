package pl.edu.uj.tcs.memoizer.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

import pl.edu.uj.tcs.memoizer.plugins.Meme;

/*
 * 
 * @author Retax
 */
public class MemeImageSaver {
    
    /*
     * Gets image from URL from Meme and saves it as file    
     */
    public static void saveMemeImage(Meme meme, File file) throws IOException{
       URLConnection c = meme.getImageLink().openConnection();
       c.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
       InputStream is = c.getInputStream();
       
       OutputStream os = new FileOutputStream(file);
       
       byte[] b = new byte[2048];
       int len;
       
       while((len = is.read(b)) != -1) os.write(b, 0, len);
       
       is.close();
       os.close();
    }
}
