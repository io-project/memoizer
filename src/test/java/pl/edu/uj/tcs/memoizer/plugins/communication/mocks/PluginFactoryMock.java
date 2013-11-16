package pl.edu.uj.tcs.memoizer.plugins.communication.mocks;

import java.awt.Image;
import java.util.Map;

import pl.edu.uj.tcs.memoizer.plugins.IPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPluginFactory;

public class PluginFactoryMock implements IPluginFactory {
	
	private String name;
	
	public PluginFactoryMock(String name) {
		this.name = name;
	}

	@Override
	public String getPluginName() {
		return name;
	}

	@Override
	public Image getIcon() {
		return null;
	}

	@Override
	public IPlugin newInstance(Map<String, byte[]> pluginStateMap) {
		return new DownloadPluginMock(name);
	}

}
