/*******************************************************************************
 * Copyright (c) 2011 University of Illinois All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html 
 * 	
 * Contributors: 
 * 	Albert L. Rossi - design and implementation
 ******************************************************************************/
package org.eclipse.ptp.rm.jaxb.core.rm;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.ptp.rm.core.rmsystem.AbstractRemoteResourceManagerConfiguration;
import org.eclipse.ptp.rm.jaxb.core.IJAXBNonNLSConstants;
import org.eclipse.ptp.rm.jaxb.core.IJAXBResourceManagerConfiguration;
import org.eclipse.ptp.rm.jaxb.core.data.ResourceManagerData;
import org.eclipse.ptp.rm.jaxb.core.messages.Messages;
import org.eclipse.ptp.rm.jaxb.core.utils.JAXBInitializationUtils;
import org.eclipse.ptp.rm.jaxb.core.variables.RMVariableMap;
import org.eclipse.ptp.services.core.IServiceProvider;

/**
 * Configuration object used for persisting values between sessions. Also
 * contains (in memory) the JAXB data object tree and the active instance of the
 * environment map.<br>
 * <br>
 * There are actually three such providers associated with a JAXB resource
 * manager instance: the base configuration, and the control and monitor
 * configurations. The latter two contain references to their parent base
 * provider.
 * 
 * @author arossi
 * 
 */
public class JAXBServiceProvider extends AbstractRemoteResourceManagerConfiguration implements IJAXBResourceManagerConfiguration,
		IJAXBNonNLSConstants {

	private ResourceManagerData rmdata;
	private RMVariableMap map;

	public JAXBServiceProvider() {
	}

	/**
	 * @param namespace
	 *            base, control or monitor
	 * @param provider
	 *            base provider configuration
	 */
	public JAXBServiceProvider(String namespace, IServiceProvider provider) {
		super(namespace, provider);
		setDescription(Messages.JAXBServiceProvider_defaultDescription);
	}

	/**
	 * clears in-memory objects
	 */
	public void clearReferences() {
		map.clear();
		map = null;
		rmdata = null;
	}

	public ResourceManagerData getResourceManagerData() {
		return rmdata;
	}

	@Override
	public String getResourceManagerId() {
		return getId();
	}

	public URL getRMConfigurationURL() {
		String loc = getString(RM_XSD_URL, ZEROSTR);
		if (ZEROSTR.equals(loc)) {
			return null;
		}
		try {
			return new URL(loc);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public void realizeRMDataFromXML() throws Throwable {
		URL location = getRMConfigurationURL();
		if (location == null) {
			rmdata = null;
		} else {
			rmdata = JAXBInitializationUtils.initializeRMData(location);
		}
	}

	public void setActive() throws Throwable {
		map = RMVariableMap.setActiveInstance(map);
		if (!map.isInitialized()) {
			if (rmdata == null) {
				realizeRMDataFromXML();
			}
			if (rmdata == null) {
				throw new InstantiationError(Messages.FailedToCreateRmData);
			}
			JAXBInitializationUtils.initializeMap(rmdata, map);
		}
	}

	public void setDefaultNameAndDesc() {
		String name = getName();
		if (name == null) {
			name = JAXB;
		}
		String conn = getConnectionName();
		if (conn != null && !conn.equals(ZEROSTR)) {
			name += AMP + conn;
		}
		setName(name);
		setDescription(Messages.JAXBServiceProvider_defaultDescription);
	}

	public void setRMConfigurationURL(URL location) {
		URL current = getRMConfigurationURL();
		if (location != null && current != location) {
			String url = location.toExternalForm();
			putString(RM_XSD_URL, url);
			clearRMData();
		}
	}

	// @Override
	protected void clearRMData() {
		rmdata = null;
		setRemoteServicesId(null);
		setConnectionName(null);
		setInvocationOptions(ZEROSTR);
		setLocalAddress(ZEROSTR);
	}
}
