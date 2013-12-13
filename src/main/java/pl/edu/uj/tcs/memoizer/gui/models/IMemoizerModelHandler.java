package pl.edu.uj.tcs.memoizer.gui.models;

import pl.edu.uj.tcs.memoizer.plugins.Meme;

/**
 * Obiekt może implementować ten interfejs aby otrzymywać precyzyjne informacje z modelu o dostępności nowych danych
 * (Mem-ów).
 *
 * @author Maciej Poleski
 */
public interface IMemoizerModelHandler {

    /**
     * Źródło danych wydobyło kolejnego Mem-a. Ta funkcja jest wywoływana gdy worker wydobędzie i skonstruuje obiekt
     * {@link pl.edu.uj.tcs.memoizer.plugins.Meme}.
     *
     * @param meme Mem który został wydobyty.
     * @param k    Identyfikator wydobytego Mem-a. Powinny być one kolejnymi liczbami naturalnymi.
     */
    void notifyUpdate(Meme meme, int k);

    /**
     * Źródło danych zostało wyczerpane - już nic więcej nie uda się uzyskać z danego modelu. Ta funkcja jest wywoływana
     * gdy zajdzie taka okoliczność (czyli powinna to być sytuacja wyjątkowa).
     */
    void notifyStreamEnd();
}
