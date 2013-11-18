package pl.edu.uj.tcs.memoizer;

import java.awt.EventQueue;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import pl.edu.uj.tcs.memoizer.events.EventService;
import pl.edu.uj.tcs.memoizer.events.IEventService;
import pl.edu.uj.tcs.memoizer.gui.MainWindow;
import pl.edu.uj.tcs.memoizer.gui.OfflineContentProvider;
import pl.edu.uj.tcs.memoizer.plugins.IPluginFactory;
import pl.edu.uj.tcs.memoizer.plugins.communication.MemeProvider;
import pl.edu.uj.tcs.memoizer.plugins.communication.PluginManager;
import pl.edu.uj.tcs.memoizer.plugins.impl.demoty.DemotyDownloadPluginFactory;

public class Main {
	
	private static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String... args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LOG.info("Starting application");
					IEventService eventService = new EventService();
					IPluginFactory[] pluginFactory = new IPluginFactory[]{new DemotyDownloadPluginFactory()};
					PluginManager pluginManager = new PluginManager(Arrays.asList(pluginFactory), eventService);

					MainWindow window = new MainWindow(pluginManager);
					window.setVisible(true);
				} catch (Exception e) {
					LOG.error(e.getMessage());
					e.printStackTrace();
				}
			}
		});
    }
}
