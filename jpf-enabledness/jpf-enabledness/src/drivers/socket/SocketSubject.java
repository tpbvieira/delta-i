package drivers.socket;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketSubject extends Socket {

	//TODO IO exception have not been handled
	
	public boolean closePre(){
		return !isClosed();
	}
	
	public boolean connectPre(SocketAddress endpoint){
		return connectPre(endpoint,0);
	}
	
	public boolean connectPre(SocketAddress endpoint, int timeout){		
		Field oldImplField;
		boolean oldImpl = false;
		
		try {
			oldImplField = Socket.class.getDeclaredField("oldImpl");
			oldImplField.setAccessible(true);
			oldImpl = (Boolean)oldImplField.get(this);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
        if (endpoint == null)
        	return false;

        if (timeout < 0)
        	return false;

        if (isClosed())
        	return false;

        if (!oldImpl && isConnected())
        	return false;

        if (!(endpoint instanceof InetSocketAddress))
        	return false;
        
        if (oldImpl && timeout != 0)
            return false;
		
		return true;
	}
	
	public boolean getInputStreamPre(){
        if (isClosed())
            return false;
        
        if (!isConnected())
        	return false;
        
        if (isInputShutdown())
        	return false;
        
        return true;
	}
	
	public boolean getOutputStreamPre(){
		
        if (isClosed())
            return false;
        
        if (!isConnected())
        	return false;
        
        if (isOutputShutdown())
        	return false;
		
		return true;
	}
	
	public boolean shutdownInputPre(){
		
        if (isClosed())
            return false;
        
        if (!isConnected())
        	return false;
        
        if (isInputShutdown())
        	return false;
        		
		return true;
	}
	
	public boolean shutdownOutputPre(){
		
        if (isClosed())
            return false;
        
        if (!isConnected())
        	return false;
        
        if (isOutputShutdown())
        	return false;
       		
		return true;
	}
	
	public boolean bindPre(SocketAddress bindpoint){
		
		Field oldImplField;
		boolean oldImpl = false;
		
		try {
			oldImplField = Socket.class.getDeclaredField("oldImpl");
			oldImplField.setAccessible(true);
			oldImpl = (Boolean)oldImplField.get(this);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
        if (isClosed())
            return false;
        
        if (!oldImpl && isBound())
        	return false;

        if (bindpoint != null && (!(bindpoint instanceof InetSocketAddress)))
        	return false;
        
        InetSocketAddress epoint = (InetSocketAddress) bindpoint;
        if (epoint != null && epoint.isUnresolved())
        	return false;
		
		return true;
	}
	
}