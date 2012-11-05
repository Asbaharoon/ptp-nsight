/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - Initial API and implementation
 *******************************************************************************/
package org.eclipse.ptp.rm.jaxb.control;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ptp.core.ModelManager;
import org.eclipse.ptp.core.Preferences;
import org.eclipse.ptp.rm.jaxb.core.JAXBCorePlugin;
import org.eclipse.ptp.rm.jaxb.core.JAXBExtensionUtils;
import org.eclipse.ptp.rm.jaxb.core.JAXBRMPreferenceConstants;

public class LaunchControllerManager {

	private static final LaunchControllerManager fInstance = new LaunchControllerManager();
	private static final Map<String, ILaunchController> fControllers = new HashMap<String, ILaunchController>();

	public static String generateControlId(String remoteServicesId, String connectionName, String configName) {
		String controlBytes = remoteServicesId + "/" + connectionName + "/" + configName; //$NON-NLS-1$ //$NON-NLS-2$
		return UUID.nameUUIDFromBytes(controlBytes.getBytes()).toString();
	}

	public static LaunchControllerManager getInstance() {
		return fInstance;
	}

	private LaunchControllerManager() {
	}

	/**
	 * @param name
	 *            configuration name
	 * @param remoteServicesId
	 *            remote services Id
	 * @param connectionName
	 *            connection name
	 * @return launch controller or null if the launch controller can't be created or initialized
	 * @throws CoreException
	 */
	public ILaunchController getLaunchController(String remoteServicesId, String connectionName, String configName)
			throws CoreException {
		if (remoteServicesId != null && connectionName != null && configName != null) {
			String controlId = generateControlId(remoteServicesId, connectionName, configName);
			ILaunchController controller = fControllers.get(controlId);
			if (controller == null) {
				controller = new LaunchController();
				controller.setRMConfigurationURL(JAXBExtensionUtils.getConfigurationURL(configName));
				if (connectionName != null && remoteServicesId != null) {
					controller.setConnectionName(connectionName);
					controller.setRemoteServicesId(remoteServicesId);
				}
				controller.initialize();
				fControllers.put(controlId, controller);
				ModelManager.getInstance().getUniverse().addResourceManager(configName, controlId);
			}
			boolean reload = Preferences.getBoolean(JAXBCorePlugin.getUniqueIdentifier(),
					JAXBRMPreferenceConstants.FORCE_XML_RELOAD);
			if (!controller.isInitialized() || reload) {
				controller.initialize();
			}
			return controller;
		}
		return null;
	}
}
