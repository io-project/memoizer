package pl.edu.uj.tcs.memoizer.plugins.impl.demoty;

import pl.edu.uj.tcs.memoizer.plugins.IPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPluginFactory;
import java.awt.Image;
import java.util.Map;

/*
 * Demotywatory.pl download plugin factory implementation
 * @author pmikos (sokar92)
 */
public class DemotyDownloadPluginFactory implements IPluginFactory {
	
	public String getPluginName(){
		return "Demotywatory download plugin";
	}
	
	public Image getIcon(){
		return null;
	}
	
	public IPlugin newInstance(Map<String, byte[]> pluginStateMap){
		return new DemotyDownloadPlugin(pluginStateMap);
	}
	
}