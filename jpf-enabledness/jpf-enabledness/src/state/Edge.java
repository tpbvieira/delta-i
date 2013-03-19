package state;



public class Edge implements Comparable<Edge>{
	
	private boolean isException;
	private String name;
		
	public Edge(boolean isException, String name) {
		super();
		this.isException = isException;
		this.name = name;
	}
	
	public boolean isException() {
		return isException;
	}
	
	public void setException(boolean isException) {
		this.isException = isException;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		return (name + Boolean.toString(isException)).hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("[");
		sb.append(name);
		sb.append(":");
		sb.append(isException);
		sb.append("]");

		return sb.toString();
	}

	@Override
	public boolean equals(Object obj){

		boolean result = false;

		if(obj instanceof Edge){
			Edge edge = (Edge)obj;
			result = (this.hashCode() == edge.hashCode());
		}

		return result;
	}

	@Override
	public int compareTo(Edge e2) {
		return this.getName().compareTo(e2.getName());
	}
}
