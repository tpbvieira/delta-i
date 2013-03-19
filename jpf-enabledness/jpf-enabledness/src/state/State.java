package state;

public class State {

	@SuppressWarnings("rawtypes")
	protected Class cls;
	private boolean isFirst;

	@SuppressWarnings("rawtypes")
	public State(Class cls, boolean isFrst) {
		super();
		this.cls = cls;
		isFirst = isFrst;
	}

	@SuppressWarnings("rawtypes")
	public Class getStateClass() {
		return cls;
	}
	
	public boolean isFirst(){
		return isFirst;
	}

}