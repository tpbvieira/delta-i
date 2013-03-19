package stackSample;

public class State {

	@SuppressWarnings("rawtypes")
	protected Class cls;

	@SuppressWarnings("rawtypes")
	public State(Class cls) {
		super();
		this.cls = cls;
	}

	@SuppressWarnings("rawtypes")
	public Class getStateClass() {
		return cls;
	}

}