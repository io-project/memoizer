package pl.edu.uj.tcs.memoizer;

import java.awt.EventQueue;

import org.apache.log4j.Logger;

import pl.edu.uj.tcs.memoizer.gui.MainWindow;
import pl.edu.uj.tcs.memoizer.gui.OfflineContentProvider;

public class Main {
	
	private static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String... args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LOG.info("Starting application");
					MainWindow window = new MainWindow(new OfflineContentProvider());
					window.setVisible(true);
				} catch (Exception e) {
					LOG.error(e.getMessage());
					e.printStackTrace();
				}
			}
		});
    }
}
