package pl.edu.uj.tcs.memoizer.plugins;

import java.awt.*;
import java.util.*;

/*
 * Interfejs wymagany do implementacji przez pluginy
 * @author pmikos (sokar92)
 */
public interface IPlugin {
	
	/*
	 * Zwraca opis pluginu np DemotywatoryDownloadPlugin
	 */
	public String getName();
	
	/*
	 * Zwraca obrazek stowarzyszony z pluginem (ikonke) 
	 */
	public Image getIcon();
	
	/*
	 * Pobiera mape wlasciwosci pluginu
	 */
	public Map<String, Object> getProperties();
	
	/*
	 * Ustawia mape wlasciwosci pluginu
	 */
	public void setProperties(Map<String, Object> properties);
}