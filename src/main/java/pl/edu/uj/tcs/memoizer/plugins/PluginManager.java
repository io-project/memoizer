package pl.edu.uj.tcs.memoizer.plugins;

import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/*
 * Klasa zarzadzajaca pluginami
 * @author pmikos (sokar92)
 */
public class PluginManager {
	private List<String> _directories;
	
	public PluginManager(){
		_directories = new ArrayList<String>();
	}
	
	public void addPluginDirectory(String dir){
		
	}
	
	private static List<IPlugin> loadPluginsFromJar(String jarName){
		List<IPlugin> list = new ArrayList<IPlugin>();
		String pkg = "pl.edu.uj.tcs.memoizer.plugins.impl";
		
		JarInputStream jarFile = null;
		JarEntry jarEntry = null;
		
		try
		{
			jarFile = new JarInputStream(new FileInputStream (jarName));

			while(true) {
				jarEntry = jarFile.getNextJarEntry();

				if(jarEntry == null)
					break;
				
				if(jarEntry.getName().startsWith(pkg) &&
					jarEntry.getName().endsWith(".class")) 
	            {
					String classname = jarEntry.getName().replaceAll("/", "\\.");
					classname = classname.substring(0, classname.length() - ".class".length());

	                if(!classname.contains("$"))
	                {
	                	ClassLoader.getSystemClassLoader();
	                	URL url = new URL("jar:file:" + jarName + "!/");
	                	URLClassLoader ucl = new URLClassLoader(new URL[] { url });
	                	
	                	try
	                	{
	                		Class<IPlugin> myLoadedClass = (Class<IPlugin>)ucl.loadClass(classname);
	                		IPlugin myClass = (IPlugin)myLoadedClass.newInstance();
	                		list.add(myClass);
	                	} catch(Exception e){}
	                }
	            }
			}
			jarFile.close();
		} catch(Exception e){}
		return list;
	}
}