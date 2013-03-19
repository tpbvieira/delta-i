package drivers.socket;

import gov.nasa.jpf.jvm.Verify;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import nativemap.InterfaceGenerator;

public class SocketDriver {
	
	private static final boolean EA = false;	
	private static final int SEQ_BOUND = 2;
	private static final String hostName = "localhost";
	private static final int port = 5432;//port of postgresql of my machine

	public static void main(String[] args) {
		/*
		 * InetAddres and port for a local service available.
		 */
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(hostName);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	    SocketAddress sockaddr = new InetSocketAddress(addr, port);		
		SocketSubject socket = new SocketSubject();
				
		/*
		 * i denotes the i_th method call
		 * against one socketSubject object
		 */
		for (int i = 1; i <= SEQ_BOUND; i++) {
			
			boolean raisedException = false;			
			int index;
			int method;
			
			/*
			 * verify what transitions(method) are enabled previously 
			 */
			int[] optsPre = getEnableOperations(socket);
			if(optsPre.length > 0){		
				index = Verify.getInt(0, optsPre.length - 1);
				method = optsPre[index];
			}else{
				continue;
			}			
			String strPre = socket.toString();
			
			/*
			 * execute each enabled methods 
			 */	
			try {
				switch (method) {
				
				case 0:
					socket.bind(sockaddr);
					break;
				case 3:
					socket.close();
					break;
				case 4:
					socket.connect(sockaddr);
					break;					
				case 5:
					socket.connect(sockaddr, 50000);
					break;						
				case 10:
					socket.getInputStream();
					break;								
				case 16:
					socket.getOutputStream();
					break;								
				case 48:
					socket.shutdownInput();
					break;				
				case 49:
					socket.shutdownOutput();
					break;
				default:
					break;
				}
			} catch (RuntimeException _) {
				raisedException = true;
				_.printStackTrace();
			} catch (Exception _) {
				raisedException = true;
				_.printStackTrace();
			}

			/*
			 * verify what transitions(method) are enabled after method execution and update saved states
			 */
			if (EA) {
				int[] optsPost = getEnableOperations(socket);
				InterfaceGenerator.updateAbstractFSM(SocketSubject.class.getName(), optsPre, method, optsPost, raisedException, EA);
//				Verify.ignoreIf(!updated);
			} else {
				String strPost = socket.toString();
				InterfaceGenerator.updateConcreteFSM(SocketSubject.class.getName(), strPre, method, strPost, raisedException, EA);
			}

		}
		
	}
	
	private static int[] getEnableOperations(SocketSubject state) {
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(hostName);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	    SocketAddress sockaddr = new InetSocketAddress(addr, port);
		List<Integer> opts = new ArrayList<Integer>();
		
		//0:bind
		if (state.bindPre(sockaddr)) {			
			opts.add(new Integer(0));
		}
		
		//3:close
		if (state.closePre()) {
			opts.add(new Integer(3));
		}
		
		//4:connect
		if (state.connectPre(sockaddr)) {
			opts.add(new Integer(4));
		}
		
		//5:connect
		if (state.connectPre(sockaddr, 50000)) {			
			opts.add(new Integer(5));
		}
		
		//10:getInputStream
		if (state.getInputStreamPre()) {
			opts.add(new Integer(10));
		}
		
		//16:getOutputStream
		if (state.getOutputStreamPre()) {
			opts.add(new Integer(16));
		}
		
		//48:shutdownInput
		if (state.shutdownInputPre()) {
			opts.add(new Integer(48));
		}
		
		//49:shutdownOutput
		if (state.shutdownOutputPre()) {
			opts.add(new Integer(49));
		}		
		
		/**
		 * mount array of enabled operations
		 */
		int[] optsEnabled = new int[opts.size()];
		for (int k = 0; k < optsEnabled.length; k++) {
			optsEnabled[k] = opts.get(k).intValue();
		}
		return optsEnabled;
	}

}

//0:bind
//1:checkAddress
//2:checkOldImpl
//3:close
//4:connect
//5:connect
//6:createImpl
//7:getChannel
//8:getImpl
//9:getInetAddress
//10:getInputStream
//11:getKeepAlive
//12:getLocalAddress
//13:getLocalPort
//14:getLocalSocketAddress
//15:getOOBInline
//16:getOutputStream
//17:getPort
//18:getReceiveBufferSize
//19:getRemoteSocketAddress
//20:getReuseAddress
//21:getSendBufferSize
//22:getSoLinger
//23:getSoTimeout
//24:getTcpNoDelay
//25:getTrafficClass
//26:isBound
//27:isClosed
//28:isConnected
//29:isInputShutdown
//30:isOutputShutdown
//31:postAccept
//32:sendUrgentData
//33:setBound
//34:setConnected
//35:setCreated
//36:setImpl
//37:setKeepAlive
//38:setOOBInline
//39:setPerformancePreferences
//40:setReceiveBufferSize
//41:setReuseAddress
//42:setSendBufferSize
//43:setSoLinger
//44:setSoTimeout
//45:setSocketImplFactory
//46:setTcpNoDelay
//47:setTrafficClass
//48:shutdownInput
//49:shutdownOutput
//50:toString
