package state;

public 	class ConcreteState extends State {
	
	private String linearization;
	
	@SuppressWarnings("rawtypes")
	public ConcreteState(Class cls, String ms,boolean isFirst) {
		super(cls,isFirst);
		linearization = ms;
	}
	
	@Override
	public int hashCode() {
		return linearization.hashCode();
	}
	
	@Override
	public String toString() {
		return linearization;
	}
	
	@Override
	public boolean equals(Object obj){

		boolean result = false;

		if(obj instanceof ConcreteState){
			ConcreteState state = (ConcreteState)obj;
			result = (this.hashCode() == state.hashCode());
		}

		return result;
	}
	
	public String toString(String[] names) {
		return toString();
	}
}
