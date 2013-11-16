package pl.edu.uj.tcs.memoizer.plugins.communication.mocks;

import pl.edu.uj.tcs.memoizer.plugins.IPluginFactory;


public class PluginFactoryMockFactory {
	
	private Integer counter = 0;

	public IPluginFactory getAnotherFactory() {
		counter++;
		return new PluginFactoryMock("Plugin" + counter);
	}
}
