package pl.edu.uj.tcs.memoizer.plugins.communication.mocks;

import java.awt.Image;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPluginFactory;
import pl.edu.uj.tcs.memoizer.serialization.StateObject;

public class PluginFactoryMock implements IPluginFactory {
	
	private String name;
	
	public PluginFactoryMock(String name) {
		this.name = name;
	}

	@Override
	public String getServiceName() {
		return name;
	}

	@Override
	public Image getIcon() {
		return null;
	}

	@Override
	public IDownloadPlugin newInstance(StateObject pluginState,
			EViewType viewType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EViewType> getAvailableDownloadViews() {
		return Arrays.asList(EViewType.values());
	}

}
