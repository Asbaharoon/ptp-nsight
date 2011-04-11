/*******************************************************************************
 * Copyright (c) 2011 University of Illinois All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html 
 * 	
 * Contributors: 
 * 	Albert L. Rossi - design and implementation
 ******************************************************************************/
package org.eclipse.ptp.rm.jaxb.ui.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ptp.remote.core.IRemoteConnection;
import org.eclipse.ptp.remote.ui.IRemoteUIConnectionManager;
import org.eclipse.ptp.remote.ui.IRemoteUIConstants;
import org.eclipse.ptp.remote.ui.IRemoteUIFileManager;
import org.eclipse.ptp.remote.ui.IRemoteUIServices;
import org.eclipse.ptp.remote.ui.PTPRemoteUIPlugin;
import org.eclipse.ptp.remote.ui.widgets.RemoteConnectionWidget;
import org.eclipse.ptp.rm.jaxb.core.utils.RemoteServicesDelegate;
import org.eclipse.ptp.rm.jaxb.ui.IJAXBUINonNLSConstants;
import org.eclipse.ptp.rm.jaxb.ui.messages.Messages;
import org.eclipse.swt.widgets.Shell;

/**
 * Convenience methods for handling various actions involving IRemoteUIServices.
 * 
 * @see org.eclipse.ptp.remote.ui.IRemoteUIServices
 * @see org.eclipse.ptp.remote.ui.IRemoteUIFileManager
 * @see org.eclipse.ptp.remote.ui.IRemoteUIConnectionManager
 * @see org.eclipse.ptp.rm.jaxb.core.utils.RemoteServicesDelegate
 * 
 * @author arossi
 * 
 */
public class RemoteUIServicesUtils implements IJAXBUINonNLSConstants {

	/**
	 * Opens a browse dialog using the indicated remote or local service and
	 * connection.
	 * 
	 * @param shell
	 *            for the dialog
	 * @param current
	 *            initial uri to display
	 * @param delegate
	 *            containing remote services data
	 * @param remote
	 *            whether to use the remote or the local connection and service
	 *            provided by the delegate
	 * @param readOnly
	 *            whether to disallow the user to type in a path (default is
	 *            <code>true</code>)
	 * @return the selected file path as URI
	 * @throws URISyntaxException
	 */
	public static URI browse(Shell shell, URI current, RemoteServicesDelegate delegate, boolean remote, boolean readOnly)
			throws URISyntaxException {
		IRemoteUIServices uIServices = null;
		IRemoteUIFileManager uiFileManager = null;
		IRemoteConnection conn = null;
		URI home = null;
		String path = null;
		int type = readOnly ? IRemoteUIConstants.OPEN : IRemoteUIConstants.SAVE;

		if (!remote) {
			uIServices = PTPRemoteUIPlugin.getDefault().getRemoteUIServices(delegate.getLocalServices());
			uiFileManager = uIServices.getUIFileManager();
			conn = delegate.getLocalConnection();
			home = delegate.getLocalHome();
		} else {
			uIServices = PTPRemoteUIPlugin.getDefault().getRemoteUIServices(delegate.getRemoteServices());
			uiFileManager = uIServices.getUIFileManager();
			conn = delegate.getRemoteConnection();
			home = delegate.getRemoteHome();
		}

		path = current == null ? home.getPath() : current.getPath();

		try {
			uiFileManager.setConnection(conn);
			uiFileManager.showConnections(remote);
			path = uiFileManager.browseFile(shell, Messages.JAXBRMConfigurationSelectionWizardPage_0, path, type);
		} catch (Throwable t) {
			t.printStackTrace();
		}

		if (path == null) {
			return current;
		}

		return new URI(home.getScheme(), home.getUserInfo(), home.getHost(), home.getPort(), path, home.getQuery(),
				home.getFragment());
	}

	/**
	 * Used to configure the default host and port in the wizard used for
	 * choosing a resource manager connection.
	 * 
	 * @see org.eclipse.ptp.remote.ui.widgets.RemoteConnectionWidget
	 * 
	 * @param connectionWidget
	 *            the widget allowing the user to choose the connection
	 * @param connection
	 *            name of the connection
	 * @throws URISyntaxException
	 */
	public static void setConnectionHints(RemoteConnectionWidget connectionWidget, String connection) throws URISyntaxException {
		String host = null;
		String port = null;
		if (connection != null) {
			if (connection != null) {
				URI uri = new URI(connection);
				host = uri.getHost();
				int p = uri.getPort();
				if (p != UNDEFINED) {
					port = String.valueOf(p);
				}
			}
		}
		Map<String, String> result = new HashMap<String, String>();
		if (host != null && !ZEROSTR.equals(host)) {
			result.put(IRemoteUIConnectionManager.CONNECTION_ADDRESS_HINT, host);
		}
		if (port != null && !ZEROSTR.equals(port)) {
			result.put(IRemoteUIConnectionManager.CONNECTION_PORT_HINT, port);
		}
		String[] hints = new String[result.size()];
		String[] defaults = new String[hints.length];
		int i = 0;
		for (String s : result.keySet()) {
			hints[i] = s;
			defaults[i++] = result.get(s);
		}
		connectionWidget.setHints(hints, defaults);
	}
}
