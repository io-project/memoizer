package pl.edu.uj.tcs.memoizer;

import java.awt.EventQueue;
import org.apache.log4j.Logger;
import pl.edu.uj.tcs.memoizer.events.EventService;
import pl.edu.uj.tcs.memoizer.events.IEventService;
import pl.edu.uj.tcs.memoizer.gui.MainWindow;
import pl.edu.uj.tcs.memoizer.plugins.PluginLoader;
import pl.edu.uj.tcs.memoizer.plugins.communication.PluginManager;

public class Main {
	
	private static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String... args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String current = new java.io.File( "." ).getCanonicalPath();
				    System.out.println("Current dir:"+current);
				        
					LOG.info("Starting application");
					IEventService eventService = new EventService();
					PluginLoader pluginLoader = new PluginLoader();
					pluginLoader.addPluginDirectory("./plugins/");
					pluginLoader.loadPlugins();
					
					PluginManager pluginManager = new PluginManager(pluginLoader.getLoadedPluginFactories(), eventService);
					
					MainWindow window = new MainWindow(pluginManager);
					//window.setVisible(true);//TODO is it nesessery?
				} catch (Exception e) {
					LOG.error(e.getMessage());
					e.printStackTrace();
				}
			}
		});
    }
}
