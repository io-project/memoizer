package pl.edu.uj.tcs.memoizer.gui.views;

/**
 * Interfejs utworzony na podstawie {@link JMemoizerView} aby wyciągnąć z klasy pewne metody do interfejsu.
 * <p/>
 * Prawdopodobnie będzie potrzebny nowy interfejs umożliwiający kontrolę nad zlecanymi zadaniami do workera
 * i informujący dokładnie o charakterze zdarzenia (np. o tym jaki dokładnie Mem stał się dostępny dla widoku).
 *
 * @author Maciej Poleski
 */
public interface ILegacyMemoizerView {
    /**
     * Implementacja pierwotna próbuje wyświetlić kolejnego Mem-a. Jeżeli jest niedostępny - worker wywoła tą metodę
     * później - gdy będzie.
     * <p/>
     * Implementacja pierwotna wywołuje tą funkcję gdy chce wyświetlić kolejnego Mem-a (np. wtedy gdy już wie że jakiś
     * jest gotowy do wyświetlenia).
     */
    void notifyUpdate();

    /**
     * Implementacja pierwotna wyświetla informację o "końcu Mem-ów" (nie ma już nic więcej do wyświetlenia).
     */
    void notifyStreamEnd();
}
