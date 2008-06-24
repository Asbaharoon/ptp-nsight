/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.ptp.rm.mpi.openmpi.ui.preferences;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.ptp.rm.mpi.openmpi.core.OpenMpiPreferenceManager;
import org.eclipse.ptp.rm.mpi.openmpi.core.rmsystem.OpenMpiResourceManagerConfiguration;
import org.eclipse.ptp.rm.ui.preferences.AbstractToolsPreferencePage;

public class OpenMpiPreferencePage extends AbstractToolsPreferencePage {

	public OpenMpiPreferencePage() {
		super(OpenMpiResourceManagerConfiguration.OPENMPI_CAPABILITIES, "Open MPI preferences");
	}

	@Override
	public Preferences getPreferences() {
		return OpenMpiPreferenceManager.getPreferences();
	}

	@Override
	public void savePreferences() {
		OpenMpiPreferenceManager.savePreferences();
	}



}
