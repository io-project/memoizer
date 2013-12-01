package pl.edu.uj.tcs.memoizer.gui;

import java.awt.Image;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import pl.edu.uj.tcs.memoizer.Main;
import pl.edu.uj.tcs.memoizer.plugins.EViewType;

/**
 * Static class providing icons for different items.
 * 
 * @author pkubiak
 */
public class IconManager {
	private static final Logger LOG = Logger.getLogger(Main.class);
	
	public static ImageIcon getIconForViewType(EViewType viewType){
		try{
			switch(viewType){
			case CHRONOLOGICAL:
				return new ImageIcon(MainWindow.class.getResource("/icons/clock.gif"));
			case FAVOURITE:
				return new ImageIcon(MainWindow.class.getResource("/icons/favourites.gif"));
			case LIKED:
				return new ImageIcon(MainWindow.class.getResource("/icons/bookmark.gif"));
			case QUEUE:
				return new ImageIcon(MainWindow.class.getResource("/icons/queue.gif"));
			case RANDOM:
				return new ImageIcon(MainWindow.class.getResource("/icons/random.gif"));
			case SEARCH:
				return new ImageIcon(MainWindow.class.getResource("/icons/search.gif"));
			case UNSEEN:
				return new ImageIcon(MainWindow.class.getResource("/icons/eye.gif"));
			default:
				LOG.debug("Missing icon for view: \""+viewType+"\"");
				return new ImageIcon(MainWindow.class.getResource("/icons/alert.gif"));
			}
		}catch(Exception e){
			LOG.debug("Missing resource for view: \""+viewType+"\"");
			return null;
		}
	}
	
	public static ImageIcon getIconForName(String name){
		try{
			switch(name){
				case "items-list":
					return new ImageIcon(MainWindow.class.getResource("/icons/itemslist.gif"));
				case "select-all":
					return new ImageIcon(MainWindow.class.getResource("/icons/checkall.gif"));
				case "unselect-all":
					return new ImageIcon(MainWindow.class.getResource("/icons/uncheckall.gif"));
				case "settings":
					return new ImageIcon(MainWindow.class.getResource("/icons/settings.gif"));
				case "help":
					return new ImageIcon(MainWindow.class.getResource("/icons/help.gif"));
				case "about":
					return new ImageIcon(MainWindow.class.getResource("/icons/about.gif"));
				case "plugin-manager":
					return new ImageIcon(MainWindow.class.getResource("/icons/package.gif"));
				case "tab-close-on":
					return new ImageIcon(MainWindow.class.getResource("/icons/tabclosehover.png"));
				case "tab-close-off":
					return new ImageIcon(MainWindow.class.getResource("/icons/tabclose.png"));
				default:
					LOG.debug("Missing icon for name: \""+name+"\"");
					return null;
			}
		}catch(Exception e){
			LOG.debug("Missing resource for name: \""+name+"\"");
			return null;
		}
	}
}
