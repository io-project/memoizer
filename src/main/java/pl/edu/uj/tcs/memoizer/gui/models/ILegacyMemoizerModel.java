package pl.edu.uj.tcs.memoizer.gui.models;

import pl.edu.uj.tcs.memoizer.gui.views.ILegacyMemoizerView;
import pl.edu.uj.tcs.memoizer.plugins.Meme;
import pl.edu.uj.tcs.memoizer.plugins.communication.DownloadMemeException;

/**
 * @author pkubiak
 * @author Maciej Poleski
 */
public interface ILegacyMemoizerModel {
	/**
	 * Bind view for future notification.
	 * @param view
	 */
	public void bindView(ILegacyMemoizerView view);

    /**
     * Check if k'th element of models is available, if not acquire it and notify view
     * <p/>
     * Implementacja sprawdza czy k-ty jest dostępny. Jeżeli tak - {@code return true}, jeżeli nie - zleca
     * przygotowanie k-tego elementu wewnętrznemu workerowi i {@code return false}. Gdy worker wykona zadanie to albo
     * uruchomi {@link pl.edu.uj.tcs.memoizer.gui.views.ILegacyMemoizerView#notifyUpdate()} (jeżeli udało się
     * zrealizować żądanie) albo {@link pl.edu.uj.tcs.memoizer.gui.views.ILegacyMemoizerView#notifyStreamEnd()} (Jeżeli
     * nie udało się zrealizować zadania (zostanie założone że skończyły się Memy)).
     *
     * @param k
     * @return true - jeżeli k-ty Mem jest natychmiast dostępny do wydobycia z modelu, false w przeciwnym wypadku.
     */
    public boolean tryGet(int k);

	/**
     * Blocking request for k'th element of model, if it is already available
	 * it will be returned immediately otherwise thread will block until it
	 *
     * @param k
     * @return k'th element of model
	 */
	public Meme get(int k);
}
