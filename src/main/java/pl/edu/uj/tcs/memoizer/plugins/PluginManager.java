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
public class PluginManager implements IPluginManager {
	private Set<String> _directories;
	private List<IPluginFactory> _pluginFactories;
	
	/*
	 * Instantiates new Plugin Manager
	 */
	public PluginManager(){
		_directories = new HashSet<String>();
		_pluginFactories = new ArrayList<IPluginFactory>();
	}
	
	/*
	 * Add new plugin folder to set
	 */
	@Override
	public void addPluginDirectory(String dir){
		_directories.add(dir);
	}
	
	/*
	 * Clear Plugin Directories Set
	 */
	@Override
	public void clearPluginDirectories(){
		_directories = new HashSet<String>();
	}
	
	/*
	 * Loads plugins from directories defined in
	 * Plugin Directories Set
	 */
	@Override
	public void loadPlugins(){
		_pluginFactories = new ArrayList<IPluginFactory>();
		
		for(String dir : _directories){
			//System.out.println(" Dir: " + dir);
			try {
				List<String> jars = findJars(new File(dir));
				for(String jar : jars)
					_pluginFactories.addAll(loadPluginsFromJar(jar));
			} catch(Exception e){}
		}
	}
	
	/*
	 * Returns set of loaded plugin factory instances.
	 */
	@Override
	public List<IPluginFactory> getLoadedPluginFactories(){
		return _pluginFactories;
	}
	
	/*
	 * Recursively finds all *.jar files in folder tree
	 * starting from rootDir
	 * Result: list of found jar files (their absolute paths)
	 */
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
					//System.out.println(" Found jar: " + file.getAbsolutePath());
				}
			}
	
			for (File directory : directories) {
				lst.addAll(findJars(directory));
			}
		}
		
		return lst;
	}
	
	/*
	 * Loads all classes implementing IPluginFactory from jar file
	 * Result: list of plugin factory instances
	 */
	private static List<IPluginFactory> loadPluginsFromJar(String jarName){
		List<IPluginFactory> list = new ArrayList<IPluginFactory>();
		String pkg = "pl.edu.uj.tcs.memoizer.plugins.impl";
		
		if(jarName == null)
			return list;
		
		JarInputStream jarFile = null;
		JarEntry jarEntry = null;
		
		try
		{
			jarFile = new JarInputStream(new FileInputStream (jarName));

			while(true) {
				jarEntry = jarFile.getNextJarEntry();

				if(jarEntry == null)
					break;
				
				String classname = jarEntry.getName().replaceAll("/", "\\.");
				//System.out.println("JarEntry: " + classname);
				
				if(classname.startsWith(pkg) && classname.endsWith(".class")) {
					classname = classname.substring(0, classname.length() - ".class".length());

					if(!classname.contains("$")) {
						ClassLoader.getSystemClassLoader();
						URL url = new URL("jar:file:" + jarName + "!/");
						URLClassLoader ucl = new URLClassLoader(new URL[] { url });
	                	
						try
						{
							//System.out.println("Try : " + classname);
							Class<IPluginFactory> myLoadedClass = (Class<IPluginFactory>)ucl.loadClass(classname);
							IPluginFactory myClass = (IPluginFactory)myLoadedClass.newInstance();
							list.add(myClass);
							//System.out.println("Added class: " + myClass.getName());
						} catch(Exception e){}
					}
				}
			}
			jarFile.close();
		} catch(Exception e){}
		return list;
	}
}