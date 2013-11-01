package pl.edu.uj.tcs.memoizer.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/*
 * Klasa zarzadzajaca pluginami
 * @author pmikos (sokar92)
 */
public class PluginManager {
	private Set<String> _directories;
	private List<IPlugin> _plugins;
	
	public PluginManager(){
		_directories = new HashSet<String>();
	}
	
	public void addPluginDirectory(String dir){
		_directories.add(dir);
	}
	
	public void clearPluginDirectories(){
		_directories = new HashSet<String>();
	}
	
	public void loadPlugins(){
		_plugins = new ArrayList<IPlugin>();
		
		for(String dir : _directories){
			try {
				List<String> jars = findJars(new File(dir));
				for(String jar : jars)
					_plugins.addAll(loadPluginsFromJar(jar));
			} catch(Exception e){}
		}
	}
	
	private static List<String> findJars(File rootDir){
		List<String> lst = new ArrayList<String>();
		
		if(rootDir == null)
			return lst;
		
		File[] files = rootDir.listFiles();
		if(files != null && files.length > 0) {
			List<File> directories = new ArrayList<File>();
			for (File file : files) {
				if (file.isDirectory()) {
					directories.add(file);
				} else if (file.getName().endsWith(".jar")) {
					lst.add(file.getAbsolutePath());
				}
			}
	
			for (File directory : directories) {
				lst.addAll(findJars(directory));
			}
		}
		
		return lst;
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
				
				if(jarEntry.getName().startsWith(pkg) && jarEntry.getName().endsWith(".class")) {
					String classname = jarEntry.getName().replaceAll("/", "\\.");
					classname = classname.substring(0, classname.length() - ".class".length());

					if(!classname.contains("$")) {
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