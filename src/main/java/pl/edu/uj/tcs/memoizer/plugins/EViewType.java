package pl.edu.uj.tcs.memoizer.plugins;

public enum EViewType {

	CHRONOLOGICAL("Chronological"),
	FAVOURITE("Favourite"),
	UNSEEN("Unseen"),
	QUEUE("Queue");
	
	private String typeName;
	
	private EViewType(String typeName) {
		this.typeName = typeName;
	}
	
	public String getName() {
		return typeName;
	}
	
}
