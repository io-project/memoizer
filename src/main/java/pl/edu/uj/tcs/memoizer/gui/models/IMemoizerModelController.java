package pl.edu.uj.tcs.memoizer.gui.models;

import pl.edu.uj.tcs.memoizer.plugins.Meme;
import pl.edu.uj.tcs.memoizer.plugins.communication.DownloadMemeException;

/**
 * Pozwala "kontrolować" model. Zleca zadania (wydobycia Mem-ów).
 *
 * @author Maciej Poleski
 */
public interface IMemoizerModelController {

    /**
     * Zleca zadanie (asynchronicznie) wydobycia Mem-a o identyfikatorze k. Jeżeli Mem został już wydobyty nic nie robi.
     * W przeciwnym wypadku zarejestrowany handler zostanie wykorzystany do przekazania wyciągniętego Mem-a gdy ten
     * będzie gotowy.
     *
     * @param id Identyfikator Mem-a do wyciągnięcia.
     * @return false - jeżeli Mem o podanym identyfikatorze został już wyciągnięty (można go natychmiast wydobyć
     *         funkcją {@link IMemoizerModelController#get(int)}. true - jeżeli zadanie wyciągnięcia Mem-a zostało
     *         pomyślnie zarejestrowane - w przyszłości należy oczekiwać notyfikacji z wydobytym Mem-em.
     */
    boolean pull(int id);

    /**
     * Natychmiast wydobywa Mem-a o podanym identyfikatorze. Jeżeli dany Mem nie został jeszcze wydobyty - zostanie to
     * wykonane blokująco. Używaj tej funkcji tylko wtedy, gdy wiesz że Mem jest już wydobyty i chcesz jedynie dostać
     * obiekt {@link Meme}.
     *
     * @param id Identyfikator Mem-a do natychmiastowego wydobycia.
     * @return Mem o identyfikatorze {code id}. Być może w niektórych przypadkach {@code null}.
     * @throws DownloadMemeException Jeżeli pobieranie Mem-a z jakiegoś powodu nie powiodło się.
     */
    Meme get(int id) throws DownloadMemeException;
}
