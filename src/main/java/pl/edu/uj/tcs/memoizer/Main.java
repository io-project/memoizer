package pl.edu.uj.tcs.memoizer;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import pl.edu.uj.tcs.memoizer.accounts.AccountsManager;
import pl.edu.uj.tcs.memoizer.accounts.AccountsStateManager;
import pl.edu.uj.tcs.memoizer.events.EventService;
import pl.edu.uj.tcs.memoizer.events.IEventService;
import pl.edu.uj.tcs.memoizer.gui.MainWindow;
import pl.edu.uj.tcs.memoizer.plugins.PluginLoader;
import pl.edu.uj.tcs.memoizer.plugins.communication.PluginManager;
import pl.edu.uj.tcs.memoizer.serialization.FileStateSink;
import pl.edu.uj.tcs.memoizer.serialization.FileStateSource;
import pl.edu.uj.tcs.memoizer.serialization.IStateSink;
import pl.edu.uj.tcs.memoizer.serialization.IStateSource;
import pl.edu.uj.tcs.memoizer.serialization.ScheduledStateSaver;
import pl.edu.uj.tcs.memoizer.serialization.StateMap;

public class Main {
	
	private static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String... args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String current = new java.io.File( "." ).getCanonicalPath();
				    LOG.debug("Current dir:"+current);
				        
				    /*UIManager.LookAndFeelInfo[] lafInfo = UIManager.getInstalledLookAndFeels();
				    for(UIManager.LookAndFeelInfo x: lafInfo){
				    	System.out.println("THEME: "+x);
				    }
				    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");*/
				    
					LOG.info("Starting application");
					IEventService eventService = new EventService();
					PluginLoader pluginLoader = new PluginLoader();
					pluginLoader.addPluginDirectory("./plugins/");
					pluginLoader.loadPlugins();
					
					
					/** Loading config */
					String configPath = "file://"+System.getProperty("user.home")+"/.memoizer.json";
					URI configURI = null;
					try{
						try{
							Reader r = new FileReader(new File("./config.path"));
							List<String> lines = IOUtils.readLines(r);
							configPath = lines.get(0);
						}catch(FileNotFoundException e){
							e.printStackTrace();
							File f = new File("./config.path");
							f.getParentFile().mkdirs();
							f.createNewFile();
							FileWriter fw = new FileWriter(f);
							fw.write(configPath);
							fw.flush();
							fw.close();
						}
						
						LOG.debug("Loading config file from: "+configPath);
						configURI = new URI(configPath);
					}catch(IndexOutOfBoundsException | NullPointerException | URISyntaxException e){
						e.printStackTrace();
						System.out.println("Config file bad location!");
						System.exit(1);
					}
					IStateSource stateSource = null;
					IStateSink stateSink = null;
					
					switch(configURI.getScheme()){
						case "file":{
							LOG.debug("CONFIG PATH: "+configURI.getRawPath());
							
							File config = new File(FilenameUtils.normalize(configURI.getPath()));
							//Create if doesn't exists
							if(!config.exists()){
								config.getParentFile().mkdirs();
								config.createNewFile();
							}
							LOG.debug("FILE CONFIG: "+config);
							
							stateSource = new FileStateSource(config);
							stateSink = new FileStateSink(config);
							break;
						}
						case "dropbox":{
							AccountsStateManager manager = new AccountsStateManager();
							
							stateSource = manager;
							stateSink = manager;
						}
						default:{
							System.out.println("Config file unknown scheme!");
							System.exit(1);
						}
					}
					
					//config file
					StateMap stateMap = new StateMap(stateSource, stateSink);
					eventService.attach(stateMap);
					ScheduledStateSaver sss = new ScheduledStateSaver(eventService, 5*60*1000);//auto save every 5min
					eventService.attach(sss);
					
					PluginManager pluginManager = new PluginManager(pluginLoader.getLoadedPluginFactories(), eventService);
					
					MainWindow window = new MainWindow(pluginManager, eventService, stateMap);
					//window.setVisible(true);//TODO is it nesessery?
				} catch (Exception e) {
					LOG.error(e.getMessage());
					e.printStackTrace();
				}
			}
		});
    }
}
