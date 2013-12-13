package pl.edu.uj.tcs.memoizer;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import pl.edu.uj.tcs.memoizer.accounts.AccountsStateManager;
import pl.edu.uj.tcs.memoizer.events.EventService;
import pl.edu.uj.tcs.memoizer.events.IEventService;
import pl.edu.uj.tcs.memoizer.gui.MainWindow;
import pl.edu.uj.tcs.memoizer.plugins.PluginLoader;
import pl.edu.uj.tcs.memoizer.plugins.communication.IPluginManagerClient;
import pl.edu.uj.tcs.memoizer.plugins.communication.PluginManager;
import pl.edu.uj.tcs.memoizer.serialization.*;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Marcin Ziemiński
 * @author Paweł Kubiak
 * @author Michał Kowalik
 * @author Maciej Poleski
 */
public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String... args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    String current = new java.io.File(".").getCanonicalPath();
                    LOG.debug("Current dir:" + current);

				    /*UIManager.LookAndFeelInfo[] lafInfo = UIManager.getInstalledLookAndFeels();
                    for(UIManager.LookAndFeelInfo x: lafInfo){
				    	System.out.println("THEME: "+x);
				    }
				    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");*/

                    LOG.info("Starting application");

                    /** Loading config */
                    String configPath = "file://" + System.getProperty("user.home") + "/.memoizer.json";
                    URI configURI = null;
                    try {
                        try {
                            Reader r = new FileReader(new File("./config.path"));
                            List<String> lines = IOUtils.readLines(r);
                            configPath = lines.get(0);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            File f = new File("./config.path");
                            f.getParentFile().mkdirs();
                            f.createNewFile();
                            FileWriter fw = new FileWriter(f);
                            fw.write(configPath);
                            fw.flush();
                            fw.close();
                        }

                        LOG.debug("Loading config file from: " + configPath);
                        configURI = new URI(configPath);
                    } catch (IndexOutOfBoundsException | NullPointerException | URISyntaxException e) {
                        e.printStackTrace();
                        System.out.println("Config file bad location!");
                        System.exit(1);
                    }
                    StateMap stateMap = getStateMap(configURI);
                    IEventService eventService = getEventService(stateMap);
                    IPluginManagerClient pluginManager = getPluginManager(".", eventService);

                    MainWindow window = new MainWindow(pluginManager, eventService, stateMap);
                    //window.setVisible(true);//TODO is it nesessery?
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Tworzy obiekt {@link PluginManager}. Oznacza to załadowanie pluginów w sposób określony przez pierwotnych twórców
     * aplikacji i wykorzystanie instancji {@link IEventService} uzyskanej za pomocą funkcji
     * {@link Main#getEventService(pl.edu.uj.tcs.memoizer.serialization.StateMap)}.
     *
     * @param applicationRootDirectory Główny katalog aplikacji (zgodnie z
     *                                 <a href="https://github.com/io-project/memoizer/wiki">Layout</a>-em).
     * @param eventService             Obiekt uzyskany za pomocą funkcji
     *                                 {@link pl.edu.uj.tcs.memoizer.Main#getEventService(pl.edu.uj.tcs.memoizer.serialization.StateMap)}.
     * @return {@link PluginManager} z podłączonym {@code eventService} i listą fabryk pluginów w sposób określony przez
     *         pierwotnych twrców aplikacji.
     */
    private static IPluginManagerClient getPluginManager(String applicationRootDirectory, IEventService eventService) {
        PluginLoader pluginLoader = new PluginLoader();
        pluginLoader.addPluginDirectory("./plugins/");
        pluginLoader.loadPlugins(new File(applicationRootDirectory));
        return new PluginManager(pluginLoader.getLoadedPluginFactories(), eventService);
    }

    /**
     * Tworzy obiekt {@link IEventService}, podłącza do niego {@code stateMap} oraz {@link ScheduledStateSaver}.
     *
     * @param stateMap Obiekt przygotowany za pomocą funkcji {@link Main#getStateMap(java.net.URI)}
     * @return {@link IEventService} z podłączonym {@code stateMap} i nowym {@link ScheduledStateSaver}
     */
    private static IEventService getEventService(StateMap stateMap) {
        IEventService eventService = new EventService();
        eventService.attach(stateMap);
        ScheduledStateSaver sss = new ScheduledStateSaver(eventService, 5 * 60 * 1000);//auto save every 5min
        eventService.attach(sss);
        return eventService;
    }

    /**
     * Tworzy obiekt {@link StateMap} na podstawie zawartości pliku wskazanego w parametrze {@code configURI}.
     *
     * @param configURI Ścieżka do pliku. Obsługiwane schematy to {@code file} oraz {@code dropbox}.
     * @return Obiekt {@link StateMap} stworzony na podstawie zawartości pliku {@code configURI}.
     * @throws IOException Jeżeli wystąpił błąd I/O podczas tworzenia pliku.
     */
    private static StateMap getStateMap(URI configURI) throws IOException {
        IStateSource stateSource = null;
        IStateSink stateSink = null;

        switch (configURI.getScheme()) {
            case "file": {
                LOG.debug("CONFIG PATH: " + configURI.getRawPath());

                File config = new File(FilenameUtils.normalize(configURI.getPath()));
                //Create if doesn't exists
                if (!config.exists()) {
                    config.getParentFile().mkdirs();
                    config.createNewFile();
                }
                LOG.debug("FILE CONFIG: " + config);

                stateSource = new FileStateSource(config);
                stateSink = new FileStateSink(config);
                break;
            }
            case "dropbox": {
                AccountsStateManager manager = new AccountsStateManager();

                stateSource = manager;
                stateSink = manager;
            }
            default: {
                System.out.println("Config file unknown scheme!");
                System.exit(1);
            }
        }

        //config file
        return new StateMap(stateSource, stateSink);
    }
}
