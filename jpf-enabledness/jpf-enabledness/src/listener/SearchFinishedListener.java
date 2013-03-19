package listener;
import nativemap.JPF_nativemap_InterfaceGenerator;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.search.SearchListenerAdapter;


public class SearchFinishedListener extends SearchListenerAdapter {
	
	public void searchFinished(Search search) {
		JPF_nativemap_InterfaceGenerator.dumpFSM();		
	}

}
