package pl.edu.uj.tcs.memoizer.gui.models;

import pl.edu.uj.tcs.memoizer.gui.MetadataHandler;
import pl.edu.uj.tcs.memoizer.gui.views.ILegacyMemoizerView;
import pl.edu.uj.tcs.memoizer.plugins.*;
import pl.edu.uj.tcs.memoizer.plugins.communication.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa typu Utility. Zawiera fabryki modeli.
 *
 * @author Maciej Poleski
 */
public class MemoizerModels {

    /**
     * Tworzy nowy model ogólnego przeznaczenia (nie Search-model) dla danego typu widoku i zestawu plugin-ów przy
     * użyciu danego {@link PluginManager} i {@link MetadataHandler} zgłaszający zdarzenia do danego
     * {@link IMemoizerModelHandler}.
     *
     * @param viewType        Typ widoku dla tego modelu.
     * @param pluginsNames    Lista plugin-ów które będą wykorzystywane w tym modelu.
     * @param pluginManager   {@link IPluginManager} który będzie wykorzystany do wydobycia właściwej implementacji
     *                        podanych plugin-ów dla podanego typu widoku.
     * @param metadataHandler {@link MetadataHandler} który będzie wykorzystany jeżeli typ widoku to
     *                        {@link EViewType#UNSEEN}.
     * @param modelHandler    Handler na który będą zgłaszane rezultaty wykonywanych żądań.
     * @return Nowy model stworzony na podstawie podanych danych i pierwotnej implementacji implementujący interfejs
     *         {@link IMemoizerModelController}.
     */
    public static IMemoizerModelController newMemoizerModel(EViewType viewType,
                                                            List<String> pluginsNames,
                                                            IPluginManagerClient pluginManager,
                                                            MetadataHandler metadataHandler,
                                                            IMemoizerModelHandler modelHandler) {
        return new MemoizerModel(
                getLegacyMemoizerModel(viewType, pluginsNames, pluginManager, metadataHandler),
                modelHandler);
    }

    /**
     * Tworzy nowy model wyszukiwania dla widoku wyszukiwania i zestawu plugin-ów przy użyciu danego
     * {@link PluginManager} zgłaszający zdarzenia do danego {@link IMemoizerModelHandler}.
     *
     * @param searchKey     Klucz wyszukiwania. Model będzie zawierał wyniki wyszukiwania przy użyciu tego klucza.
     * @param pluginsNames  Lista plugin-ów które będą wykorzystywane w tym modelu.
     * @param pluginManager {@link IPluginManager} który będzie wykorzystany do wydobycia właściwej implementacji
     *                      podanych plugin-ów dla podanego typu widoku.
     * @param modelHandler  Handler na który będą zgłaszane rezultaty wykonywanych żądań.
     * @return Nowy model stworzony na podstawie podanych danych i pierwotnej implementacji implementujący interfejs
     *         {@link IMemoizerModelController}.
     */
    public static IMemoizerModelController newMemoizerSearchModel(String searchKey,
                                                                  IPluginManagerClient pluginManager,
                                                                  List<String> pluginsNames,
                                                                  IMemoizerModelHandler modelHandler) {
        return new MemoizerModel(getLegacyMemoizerSearchModel(searchKey, pluginManager, pluginsNames), modelHandler);
    }

    /**
     * Tworzy model w sposób określony w pierwotnej implementacji.
     *
     * @param viewType        Typ widoku obsługiwany przez ten model.
     * @param pluginsNames    Nazwy plugin-ów z których będą czerpane dane (Mem-y).
     * @param pluginManager   Jakiś {@link pl.edu.uj.tcs.memoizer.plugins.communication.IPluginManager}.
     * @param metadataHandler Jakiś {@link pl.edu.uj.tcs.memoizer.gui.MetadataHandler}
     * @return Model obsługujący dany typ widoku i dany dobór plugin-ów (oraz wykorzystujący dany
     *         {@link pl.edu.uj.tcs.memoizer.gui.MetadataHandler}).
     */
    public static ILegacyMemoizerModel getLegacyMemoizerModel(final EViewType viewType, List<String> pluginsNames, IPluginManagerClient pluginManager, MetadataHandler metadataHandler) {
        IMemeProvider memeProvider;
        if (viewType == EViewType.UNSEEN)
            memeProvider = new MemeProviderUnseen(metadataHandler);
        else memeProvider = new MemeProvider();

        List<IDownloadPlugin> plugins = pluginManager.getPluginsInstancesForView(pluginsNames, viewType);

        try {
            memeProvider.setView(new IPluginView() {
                @Override
                public EViewType getViewType() {
                    return viewType;
                }

                @Override
                public Meme extractNextMeme(List<Meme> memes) {
                    if (memes.size() > 0)
                        return memes.remove(0);
                    return null;
                }
            }, plugins);
        } catch (InvalidPluginException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new SimpleMemoizerModel(memeProvider);
    }

    /**
     * Tworzy model w sposób określony w pierwotnej implementacji w celu obsługi wyszukiwania danego hasła.
     *
     * @param searchKey     Prawdopodobnie fraza do wyszukania przez implementacje pluginów.
     * @param pluginManager Jakiś {@link pl.edu.uj.tcs.memoizer.plugins.communication.IPluginManager}.
     * @param pluginsNames  Nazwy plugin-ów z których będą czerpane dane (Mem-y).
     * @return Model obsługujący dane wyszukiwanie.
     */
    public static ILegacyMemoizerModel getLegacyMemoizerSearchModel(String searchKey, IPluginManagerClient pluginManager, List<String> pluginsNames) {
        IMemeProvider memeProvider = new MemeProvider();
        List<IDownloadPlugin> plugins = pluginManager.getPluginsInstancesForView(pluginsNames, EViewType.SEARCH, searchKey);
        try {
            memeProvider.setView(new IPluginView() {
                @Override
                public EViewType getViewType() {
                    return EViewType.SEARCH;
                }

                @Override
                public Meme extractNextMeme(List<Meme> memes) {
                    if (memes.size() > 0)
                        return memes.remove(0);
                    return null;
                }
            }, plugins);
        } catch (InvalidPluginException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new SimpleMemoizerModel(memeProvider);
    }

    private static class MemoizerModel implements IMemoizerModelController {
        final ILegacyMemoizerModel legacyMemoizerModel;
        final IMemoizerModelHandler modelHandler;
        final List<Integer> requestedIds = new ArrayList<>();

        public MemoizerModel(ILegacyMemoizerModel legacyMemoizerModel, IMemoizerModelHandler modelHandler) {
            this.legacyMemoizerModel = legacyMemoizerModel;
            this.modelHandler = modelHandler;

            legacyMemoizerModel.bindView(new ILegacyMemoizerView() {
                @Override
                public void notifyUpdate() {
                    synchronized (MemoizerModel.this) {
                        List<Integer> idsToRemove = new ArrayList<>();
                        for (int id : requestedIds) {
                            if (MemoizerModel.this.legacyMemoizerModel.tryGet(id)) {
                                // Żądam tylko już wcześniej żądanych - nie będzie nadmiarowego pobierania i zgłaszania
                                idsToRemove.add(id);
                                MemoizerModel.this.modelHandler.notifyUpdate(
                                        MemoizerModel.this.legacyMemoizerModel.get(id),
                                        id);
                            }
                        }
                        MemoizerModel.this.requestedIds.removeAll(idsToRemove);
                    }
                }

                @Override
                public void notifyStreamEnd() {
                    synchronized (MemoizerModel.this) {
                        MemoizerModel.this.modelHandler.notifyStreamEnd();
                    }
                }
            });
        }

        @Override
        public boolean pull(int id) {
            synchronized (this) {
                if (legacyMemoizerModel.tryGet(id))
                    return false;
                requestedIds.add(id);
                return true;
            }
        }

        @Override
        public Meme get(int id) {
            return legacyMemoizerModel.get(id); // Po prostu deleguje żądanie
        }
    }
}
