/*******************************************************************************
 * Copyright (c) 2011 University of Illinois All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html 
 * 	
 * Contributors: 
 * 	Albert L. Rossi - design and implementation
 ******************************************************************************/

package org.eclipse.ptp.rm.jaxb.tests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.ptp.remote.core.RemoteServicesDelegate;
import org.eclipse.ptp.rm.jaxb.control.JAXBControlConstants;
import org.eclipse.ptp.rm.jaxb.control.internal.runnable.ManagedFilesJob;
import org.eclipse.ptp.rm.jaxb.control.internal.variables.RMVariableMap;
import org.eclipse.ptp.rm.jaxb.control.runnable.ScriptHandler;
import org.eclipse.ptp.rm.jaxb.core.IJAXBResourceManagerControl;
import org.eclipse.ptp.rm.jaxb.core.IVariableMap;
import org.eclipse.ptp.rm.jaxb.core.JAXBInitializationUtils;
import org.eclipse.ptp.rm.jaxb.core.data.AttributeType;
import org.eclipse.ptp.rm.jaxb.core.data.ControlType;
import org.eclipse.ptp.rm.jaxb.core.data.ManagedFileType;
import org.eclipse.ptp.rm.jaxb.core.data.ManagedFilesType;
import org.eclipse.ptp.rm.jaxb.core.data.PropertyType;
import org.eclipse.ptp.rm.jaxb.core.data.ResourceManagerData;
import org.eclipse.ptp.rm.jaxb.core.data.ScriptType;
import org.eclipse.ptp.rmsystem.IJobStatus;
import org.eclipse.ptp.rmsystem.IResourceManagerComponentConfiguration;

public class ManagedFilesTest extends TestCase implements IJAXBResourceManagerControl {

	private static final String xml = JAXBControlConstants.DATA + "test-pbs.xml"; //$NON-NLS-1$
	private static ControlType controlData;
	private static Map<String, Object> env;
	private static Map<String, String> live;
	private static boolean verbose = false;
	private RMVariableMap rmVarMap;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rmsystem.IResourceManagerControl#control(java.lang.String
	 * , java.lang.String, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void control(String jobId, String operation, IProgressMonitor monitor) throws CoreException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rmsystem.IResourceManagerControl#dispose()
	 */
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rm.jaxb.core.IJAXBResourceManagerControl#getAppendEnv()
	 */
	public boolean getAppendEnv() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rmsystem.IResourceManagerControl#getControlConfiguration
	 * ()
	 */
	public IResourceManagerComponentConfiguration getControlConfiguration() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rm.jaxb.core.IJAXBResourceManagerControl#getEnvironment()
	 */
	public IVariableMap getEnvironment() {
		return rmVarMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rmsystem.IResourceManagerControl#getJobStatus(java.lang
	 * .String, boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IJobStatus getJobStatus(String jobId, boolean force, IProgressMonitor monitor) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rmsystem.IResourceManagerControl#getJobStatus(java.lang
	 * .String, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IJobStatus getJobStatus(String jobId, IProgressMonitor monitor) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rm.jaxb.core.IJAXBResourceManagerControl#getLaunchEnv()
	 */
	public Map<String, String> getLaunchEnv() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rm.jaxb.core.IJAXBResourceManagerControl#
	 * getRemoteServicesDelegate(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public RemoteServicesDelegate getRemoteServicesDelegate(IProgressMonitor monitor) throws CoreException {
		RemoteServicesDelegate d = new RemoteServicesDelegate(null, null);
		d.initialize(monitor);
		return d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rm.jaxb.core.IJAXBResourceManagerControl#getState()
	 */
	public String getState() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rm.jaxb.core.IJAXBResourceManagerControl#jobStateChanged
	 * (java.lang.String, org.eclipse.ptp.rmsystem.IJobStatus)
	 */
	public void jobStateChanged(String jobId, IJobStatus status) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rm.jaxb.core.IJAXBResourceManagerControl#runActionCommand
	 * (java.lang.String, java.lang.String,
	 * org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public Object runActionCommand(String action, String resetValue, ILaunchConfiguration configuration) throws CoreException {
		return null;
	}

	@Override
	public void setUp() {
		try {
			JAXBTestsPlugin.validate(xml);
			ResourceManagerData rmdata = JAXBInitializationUtils.initializeRMData(JAXBTestsPlugin.getURL(xml));
			controlData = rmdata.getControlData();
			rmVarMap = new RMVariableMap();
			JAXBInitializationUtils.initializeMap(rmdata, rmVarMap);
			env = rmVarMap.getVariables();
			live = new HashMap<String, String>();
			live.put("FOO_VAR_1", "FOO_VALUE_1"); //$NON-NLS-1$ //$NON-NLS-2$
			live.put("FOO_VAR_2", "FOO_VALUE_2"); //$NON-NLS-1$ //$NON-NLS-2$
			live.put("FOO_VAR_3", "FOO_VALUE_3"); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Throwable t) {
			t.printStackTrace();
			assertNotNull(t);
		}
		setTestValues();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rmsystem.IResourceManagerControl#start(org.eclipse.core
	 * .runtime.IProgressMonitor)
	 */
	public void start(IProgressMonitor monitor) throws CoreException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rmsystem.IResourceManagerControl#stop()
	 */
	public void stop() throws CoreException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rmsystem.IResourceManagerControl#submitJob(org.eclipse
	 * .debug.core.ILaunchConfiguration, java.lang.String,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	public String submitJob(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor) throws CoreException {
		return null;
	}

	@Override
	public void tearDown() {
		controlData = null;
	}

	public void testManagedFiles() {
		composeScript();
		if (verbose) {
			PropertyType contents = (PropertyType) env.get(JAXBControlConstants.SCRIPT);
			if (contents != null) {
				System.out.println(contents.getValue());
			}
		}
		ManagedFilesType files = controlData.getManagedFiles().get(0);
		files = maybeAddManagedFileForScript(files);
		assertNotNull(files);
		try {
			ManagedFilesJob job = new ManagedFilesJob(null, files, this);
			job.schedule();
			try {
				job.join();
			} catch (InterruptedException t) {
				t.printStackTrace();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			assertNotNull(t);
		}
	}

	private void composeScript() {
		ScriptType script = controlData.getScript();
		assertNotNull(script);
		ScriptHandler job = new ScriptHandler(null, script, rmVarMap, live, false);
		job.schedule();
		try {
			job.join();
		} catch (InterruptedException t) {
			t.printStackTrace();
		}

		PropertyType contents = (PropertyType) env.get(JAXBControlConstants.SCRIPT);
		assertNotNull(contents);
	}

	private ManagedFilesType maybeAddManagedFileForScript(ManagedFilesType files) {
		PropertyType scriptVar = (PropertyType) rmVarMap.get(JAXBControlConstants.SCRIPT);
		PropertyType scriptPathVar = (PropertyType) rmVarMap.get(JAXBControlConstants.SCRIPT_PATH);
		if (scriptVar != null || scriptPathVar != null) {
			if (files == null) {
				files = new ManagedFilesType();
				files.setFileStagingLocation(JAXBControlConstants.ECLIPSESETTINGS);
			}
			List<ManagedFileType> fileList = files.getFile();
			ManagedFileType scriptFile = null;
			if (!fileList.isEmpty()) {
				for (ManagedFileType f : fileList) {
					if (f.getName().equals(JAXBControlConstants.SCRIPT_FILE)) {
						scriptFile = f;
						break;
					}
				}
			}
			if (scriptFile == null) {
				scriptFile = new ManagedFileType();
				scriptFile.setName(JAXBControlConstants.SCRIPT_FILE);
				fileList.add(scriptFile);
			}
			scriptFile.setResolveContents(false);
			scriptFile.setUniqueIdPrefix(true);
			if (scriptPathVar != null) {
				scriptFile.setPath(String.valueOf(scriptPathVar.getValue()));
				scriptFile.setDeleteSourceAfterUse(false);
			} else {
				scriptFile.setContents(JAXBControlConstants.OPENVRM + JAXBControlConstants.SCRIPT + JAXBControlConstants.PD
						+ JAXBControlConstants.VALUE + JAXBControlConstants.CLOSV);
				scriptFile.setDeleteSourceAfterUse(true);
			}
		}
		return files;
	}

	private void putValue(String name, String value) {
		PropertyType p = new PropertyType();
		p.setName(name);
		p.setValue(value);
		env.put(name, p);
	}

	private void setTestValues() {
		for (String key : env.keySet()) {
			Object target = env.get(key);
			String value = key + "_TEST_VALUE"; //$NON-NLS-1$
			if (target instanceof PropertyType) {
				((PropertyType) target).setValue(value);
			} else if (target instanceof AttributeType) {
				((AttributeType) target).setValue(value);
			}
		}
		putValue(JAXBControlConstants.CONTROL_USER_VAR, "fooUser"); //$NON-NLS-1$
		putValue(JAXBControlConstants.CONTROL_ADDRESS_VAR, "abe.ncsa.uiuc.edu"); //$NON-NLS-1$
		putValue(JAXBControlConstants.DIRECTORY, "/u/ncsa/arossi/test"); //$NON-NLS-1$ 
		putValue(JAXBControlConstants.MPI_CMD, "mpiexec"); //$NON-NLS-1$ 
		putValue(JAXBControlConstants.MPI_ARGS, "-np 8"); //$NON-NLS-1$ 
		putValue(JAXBControlConstants.EXEC_PATH, "/u/ncsa/arossi/test/foo"); //$NON-NLS-1$ 
		if (verbose) {
			RMDataTest.print(rmVarMap);
		}
	}
}
