/*******************************************************************************
 * Copyright (c) 2005 The Regents of the University of California. 
 * This material was produced under U.S. Government contract W-7405-ENG-36 
 * for Los Alamos National Laboratory, which is operated by the University 
 * of California for the U.S. Department of Energy. The U.S. Government has 
 * rights to use, reproduce, and distribute this software. NEITHER THE 
 * GOVERNMENT NOR THE UNIVERSITY MAKES ANY WARRANTY, EXPRESS OR IMPLIED, OR 
 * ASSUMES ANY LIABILITY FOR THE USE OF THIS SOFTWARE. If software is modified 
 * to produce derivative works, such modified software should be clearly marked, 
 * so as not to confuse it with the version available from LANL.
 * 
 * Additionally, this program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * LA-CC 04-115
 *******************************************************************************/

package org.eclipse.ptp.core.proxy;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.ptp.core.proxy.event.IProxyEvent;
import org.eclipse.ptp.core.proxy.event.IProxyEventListener;
import org.eclipse.ptp.core.proxy.event.ProxyConnectedEvent;
import org.eclipse.ptp.core.proxy.event.ProxyEvent;
import org.eclipse.ptp.core.util.BitList;

public abstract class AbstractProxyClient {
	private String				sessHost = null;
	private int					sessPort = 0;
	private ServerSocket			sessSvrSock = null;
	private Socket				sessSock = null;
	private OutputStreamWriter	sessOut;
	private InputStreamReader		sessIn;
	private boolean				sessConnected = false;
	private boolean				exitThread;
	private Thread				eventThread;
	private Thread				acceptThread;
	protected List 				listeners = Collections.synchronizedList(new ArrayList());
	
	private String encodeLength(int val) {
		char[] res = new char[8];
		String str = Integer.toHexString(val);
		int rem = 8 - str.length();
		
		for (int i = 0 ; i < 8 ; i++) {
			if (i < rem)
				res[i] = '0';
			else
				res[i] = str.charAt(i - rem);
		}
		return String.valueOf(res);
	}
	
	protected void sendCommand(String cmd) throws IOException {
		if (sessConnected) {
			String buf = encodeLength(cmd.length()) + " " + cmd;
			System.out.println("<" + buf + ">");
			sessOut.write(buf);
			sessOut.flush();
		}
	}

	protected String encodeBitSet(BitList set) {
		String lenStr = Integer.toHexString(set.size());
		return lenStr + ":" + set.toString();
	}
	
	protected void sendCommand(String cmd, String args) throws IOException {
		this.sendCommand(cmd + " " + args);
	}
	
	public void addEventListener(IProxyEventListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	public void removeEventListener(IProxyEventListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
	public int sessionConnect() {
		return 0;
	}

	public void sessionCreate() throws IOException {
		sessionCreate(0);
	}
	
	public void sessionCreate(int port) throws IOException {
		System.out.println("sessionCreate()");
		sessSvrSock = new ServerSocket(port);
		sessPort = sessSvrSock.getLocalPort();
		sessHost = sessSvrSock.getLocalSocketAddress().toString();
		System.out.println("port=" + sessPort);
		acceptThread = new Thread("Proxy Client Accept Thread") {
			public void run() {
				try {
					System.out.println("accept thread starting...");
					sessSock = sessSvrSock.accept();
					sessOut = new OutputStreamWriter(sessSock.getOutputStream());
					sessIn = new InputStreamReader(sessSock.getInputStream());
					sessConnected = true;
					fireProxyEvent(new ProxyConnectedEvent());
					startEventThread();
				} catch (IOException e) {
					try {
						sessIn.close();
					} catch (IOException e1) {
					}
					try {
						sessOut.close();
					} catch (IOException e1) {
					}
					try {
						sessSock.close();
					} catch (IOException e1) {
					} 
				}
				try {
					sessSvrSock.close();
				} catch (IOException e) {
				}
				System.out.println("accept thread exiting...");
			}
		};
		acceptThread.start();
	}

	public int getSessionPort() {
		return sessPort;
	}
	
	public String getSessionHost() {
		return sessHost;
	}

	private void startEventThread() throws IOException {
		eventThread = new Thread("Proxy Client Event Thread") {
			public void run() {
				System.out.println("event thread starting...");
				try {
					exitThread = false;
					while (!exitThread) {
						sessionProgress();
					}
				} catch (IOException e) {
				} 
				sessConnected = false;
				try {
					sessIn.close();
				} catch (IOException e) {
				}
				try {
					sessOut.close();
				} catch (IOException e) {
				}
				try {
					sessSock.close();
				} catch (IOException e) {
				} 
				System.out.println("event thread exiting...");
			}
		};
		eventThread.start();
	}
	
	protected void fireProxyEvent(IProxyEvent event) {
		if (listeners == null)
			return;
		synchronized (listeners) {
			Iterator i = listeners.iterator();
			while (i.hasNext()) {
				IProxyEventListener listener = (IProxyEventListener) i.next();
				listener.handleEvent(event);
			}
		}
	}
	
	private boolean fullRead(char[] buf, int len) throws IOException {
		int offset = 0;
		while (len > 0) {
			int n = sessIn.read(buf, offset, len);
			if (n <= 0)
				return false;
			offset += n;
			len -= n;
		}
		return true;
	}
	
	private void sessionProgress() throws IOException {
		char[] len_bytes = new char[9];
		
		if (!fullRead(len_bytes, 9)) {
			exitThread = true;
			return;
		}
		
		String len_str = new String(len_bytes, 0, 8);
		int len = Integer.parseInt(len_str, 16);
		
		char[] event_bytes = new char[len];

		if (!fullRead(event_bytes, len)) {
			exitThread = true;
			return;
		}

		String event_str = new String(event_bytes);
		fireProxyEvent(ProxyEvent.toEvent(event_str));
	}

	public void sessionFinish() throws IOException {
		this.sendCommand("QUI");
		if (acceptThread.isAlive())
			acceptThread.interrupt();
	}
}
