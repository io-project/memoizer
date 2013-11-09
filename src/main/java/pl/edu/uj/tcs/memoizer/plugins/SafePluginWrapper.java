package pl.edu.uj.tcs.memoizer.plugins;

import java.awt.Image;
import java.util.List;
import java.util.Map;

public class SafePluginWrapper implements IPlugin {
	
	private IDownloadPlugin wrapped;
	
	public SafePluginWrapper(IDownloadPlugin plugin) {
		this.wrapped = plugin;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProperties(Map<String, Object> properties) {
		// TODO Auto-generated method stub

	}

}
