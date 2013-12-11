package pl.edu.uj.tcs.memoizer.plugins;

import java.io.File;
import java.util.List;

/**
 * @author Ziemin
 * @author Maciej Poleski
 */
public interface IPluginLoader {

    void addPluginDirectory(String dir);

    void clearPluginDirectories();

    /**
     * Ładuje wszystkie pluginy z podkatalogów katalogu {@code applicationRootDirectory} które zostały zarejestrowane
     * za pomocą funkcji {@code addPluginDirectory}. Załadowane pluginy można uzyskać za pomocą metody
     * {@code getLoadedPluginFactories}.
     *
     * @param applicationRootDirectory Główny katalog aplikacji (zgodnie z
     *                                 <a href="https://github.com/io-project/memoizer/wiki">Layout</a>-em).
     */
    void loadPlugins(File applicationRootDirectory);

    List<IPluginFactory> getLoadedPluginFactories();
}
