package pl.edu.uj.tcs.memoizer.plugins.communication.mocks;

import java.util.Arrays;
import java.util.List;

import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.serialization.IStateObject;

public class SingleViewPluginFactoryMock extends PluginFactoryMock {
	
	private EViewType viewType;

	public SingleViewPluginFactoryMock(String name, EViewType viewType) {
		super(name);
		this.viewType = viewType;
	}

	@Override
	public List<EViewType> getAvailableDownloadViews() {
		return Arrays.asList( new EViewType[] { viewType });
	}

	@Override
	public IDownloadPlugin newInstance(IStateObject pluginState, EViewType viewType) throws IllegalArgumentException {
		if(this.viewType != viewType) {
			throw new IllegalArgumentException();
		}
		return new DownloadPluginMock(getServiceName(), viewType);
	}

	
}
