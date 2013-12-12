package pl.edu.uj.tcs.memoizer.gui.views;

/**
 * Interfejs utworzony na podstawie {@link JMemoizerView} aby wyciągnąć z klasy pewne metody do interfejsu.
 *
 * @author Maciej Poleski
 */
public interface IMemoizerView {
    void notifyUpdate();

    void notifyStreamEnd();
}
