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
package org.eclipse.ptp.rm.lml.monitor.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.ptp.core.jobs.IJobListener;
import org.eclipse.ptp.core.jobs.IJobStatus;
import org.eclipse.ptp.core.jobs.JobManager;
import org.eclipse.ptp.core.util.CoreExceptionUtils;
import org.eclipse.ptp.core.util.LaunchUtils;
import org.eclipse.ptp.remote.core.IRemoteConnection;
import org.eclipse.ptp.remote.core.IRemoteConnectionManager;
import org.eclipse.ptp.remote.core.IRemoteServices;
import org.eclipse.ptp.remote.core.PTPRemoteCorePlugin;
import org.eclipse.ptp.remote.core.exception.RemoteConnectionException;
import org.eclipse.ptp.remote.core.server.RemoteServerManager;
import org.eclipse.ptp.rm.lml.core.JobStatusData;
import org.eclipse.ptp.rm.lml.core.LMLManager;
import org.eclipse.ptp.rm.lml.core.model.IPattern;
import org.eclipse.ptp.rm.lml.da.server.core.LMLDAServer;
import org.eclipse.ptp.rm.lml.internal.core.elements.DriverType;
import org.eclipse.ptp.rm.lml.internal.core.elements.RequestType;
import org.eclipse.ptp.rm.lml.internal.core.model.Pattern;
import org.eclipse.ptp.rm.lml.monitor.LMLMonitorCorePlugin;
import org.eclipse.ptp.rm.lml.monitor.core.messages.Messages;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;

/**
 * LML JAXB resource manager monitor
 */
@SuppressWarnings("restriction")
public class MonitorControl implements IMonitorControl {
	private class JobListener implements IJobListener {
		public void jobAdded(IJobStatus status) {
			addJob(status);
		}

		public void jobChanged(IJobStatus status) {
			updateJob(status);
		}
	}

	/**
	 * Job for running the LML DA server. This job gets run periodically based on the JOB_SCHEDULE_FREQUENCY.
	 */
	private class MonitorJob extends Job {
		private final LMLDAServer fServer;

		public MonitorJob(IRemoteConnection conn) {
			super(Messages.LMLResourceManagerMonitor_LMLMonitorJob);
			setSystem(true);
			fServer = (LMLDAServer) RemoteServerManager.getServer(LMLDAServer.SERVER_ID, conn);
			fServer.setWorkDir(new Path(conn.getWorkingDirectory()).append(".eclipsesettings").toString()); //$NON-NLS-1$
		}

		/**
		 * Schedule an immediate refresh
		 */
		public void refresh() {
			wakeUp();
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			final SubMonitor subMon = SubMonitor.convert(monitor, 100);
			try {
				try {
					fServer.startServer(subMon.newChild(20));
					if (!subMon.isCanceled()) {
						fServer.waitForServerStart(subMon.newChild(20));
						if (!subMon.isCanceled()) {
							LMLManager.getInstance().update(getMonitorId(), fServer.getInputStream(), fServer.getOutputStream());
						}
					}
				} catch (final Exception e) {
					fActive = false;
					MonitorControlManager.getInstance().fireMonitorUpdated(new IMonitorControl[] { MonitorControl.this });
					return new Status(IStatus.ERROR, LMLMonitorCorePlugin.PLUGIN_ID, e.getLocalizedMessage());
				}
				IStatus status = fServer.waitForServerFinish(subMon.newChild(40));
				if (status == Status.OK_STATUS) {
					schedule(JOB_SCHEDULE_FREQUENCY);
				} else {
					fActive = false;
					MonitorControlManager.getInstance().fireMonitorUpdated(new IMonitorControl[] { MonitorControl.this });
				}
				return status;
			} finally {
				if (monitor != null) {
					monitor.done();
				}
			}
		}
	}

	/*
	 * needs to be parameter
	 */
	private static final int JOB_SCHEDULE_FREQUENCY = 60000;

	private MonitorJob fMonitorJob;

	private final String fMonitorId;
	private final LMLManager fLMLManager = LMLManager.getInstance();
	private final JobListener fJobListener = new JobListener();
	private final StringBuffer fSavedLayout = new StringBuffer();
	private final List<JobStatusData> fSavedJobs = new ArrayList<JobStatusData>();
	private final Map<String, List<IPattern>> fSavedPattern = new HashMap<String, List<IPattern>>();
	private String fSystemType;
	private boolean fActive;
	private String fRemoteServicesId;
	private String fConnectionName;

	private static final String XML = "xml";//$NON-NLS-1$ 
	private static final String JOBS_ATTR = "jobs";//$NON-NLS-1$ 
	private static final String JOB_ATTR = "job";//$NON-NLS-1$ 
	private static final String LAYOUT_ATTR = "layout";//$NON-NLS-1$
	private static final String LAYOUT_STRING_ATTR = "layoutString";//$NON-NLS-1$
	private static final String PATTERNS_ATTR = "patterns";//$NON-NLS-1$
	private static final String PATTERN_GID_ATTR = "gid";//$NON-NLS-1$
	private static final String FILTER_TITLE_ATTR = "columnTitle";//$NON-NLS-1$
	private static final String FILTER_TYPE_ATTR = "type";//$NON-NLS-1$
	private static final String FILTER_RANGE_ATTR = "range";//$NON-NLS-1$
	private static final String FILTER_RELATION_ATTR = "relation";//$NON-NLS-1$
	private static final String FILTER_MAX_VALUE_RANGE_ATTR = "maxValueRange";//$NON-NLS-1$
	private static final String FILTER_MIN_VALUE_RANGE_ATTR = "minValueRange";//$NON-NLS-1$
	private static final String FILTER_RELATION_OPERATOR_ATTR = "relationOperartor";//$NON-NLS-1$
	private static final String FILTER_RELATION_VALUE_ATTR = "relationValue";//$NON-NLS-1$
	private static final String SYSTEM_TYPE_ATTR = "systemType";//$NON-NLS-1$;
	private static final String MONITOR_STATE = "monitorState";//$NON-NLS-1$;
	private static final String REMOTE_SERVICES_ID_ATTR = "remoteServicesId";//$NON-NLS-1$;
	private static final String CONNECTION_NAME_ATTR = "connectionName";//$NON-NLS-1$;
	private static final String MONITOR_ATTR = "monitor";//$NON-NLS-1$

	public MonitorControl(String monitorId) {
		fMonitorId = monitorId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rm.lml.monitor.core.IMonitorControl#getConnectionName()
	 */
	public String getConnectionName() {
		return fConnectionName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rm.lml.monitor.core.IMonitorControl#getMonitorId()
	 */
	public String getMonitorId() {
		return fMonitorId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rm.lml.monitor.core.IMonitorControl#getRemoteServicesId()
	 */
	public String getRemoteServicesId() {
		return fRemoteServicesId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rm.lml.monitor.core.IMonitorControl#getSystemType()
	 */
	public String getSystemType() {
		return fSystemType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rm.lml.monitor.core.IMonitorControl#isActive()
	 */
	public boolean isActive() {
		return fActive;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rm.lml.monitor.core.IMonitorControl#load()
	 */
	public boolean load() {
		try {
			fSavedLayout.setLength(0);
			fSavedJobs.clear();
			fSavedPattern.clear();

			FileReader reader = new FileReader(getSaveLocation());
			IMemento memento = XMLMemento.createReadRoot(reader);

			boolean active = loadState(memento);

			IMemento childLayout = memento.getChild(LAYOUT_ATTR);
			if (childLayout != null) {
				fSavedLayout.append(childLayout.getString(LAYOUT_STRING_ATTR));
			}

			childLayout = memento.getChild(JOBS_ATTR);
			loadJobs(childLayout, fSavedJobs);

			childLayout = memento.getChild(PATTERNS_ATTR);
			loadPattern(childLayout, fSavedPattern);

			return active;
		} catch (FileNotFoundException e) {
			LMLMonitorCorePlugin.log(e.getLocalizedMessage());
		} catch (WorkbenchException e) {
			LMLMonitorCorePlugin.log(e.getLocalizedMessage());
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rm.lml.monitor.core.IMonitorControl#refresh()
	 */
	public void refresh() {
		fMonitorJob.refresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rm.lml.monitor.core.IMonitorControl#save()
	 */
	public void save() {
		final XMLMemento memento = XMLMemento.createWriteRoot(MONITOR_ATTR);

		final String layout = fLMLManager.getCurrentLayout(getMonitorId());
		final JobStatusData[] jobs = fLMLManager.getUserJobs(getMonitorId());
		final Map<String, List<IPattern>> patternMap = fLMLManager.getCurrentPattern(getMonitorId());

		saveState(memento);

		if (layout != null) {
			final IMemento layoutMemento = memento.createChild(LAYOUT_ATTR);
			layoutMemento.putString(LAYOUT_STRING_ATTR, layout);
		}

		if (jobs != null && jobs.length > 0) {
			final IMemento jobsMemento = memento.createChild(JOBS_ATTR);
			for (final JobStatusData status : jobs) {
				if (!status.isRemoved()) {
					saveJob(status, jobsMemento);
				}
			}
		}

		if (patternMap != null && patternMap.keySet().size() > 0) {
			final IMemento patternMemento = memento.createChild(PATTERNS_ATTR);
			for (final Entry<String, List<IPattern>> pattern : patternMap.entrySet()) {
				savePattern(pattern.getKey(), pattern.getValue(), patternMemento);
			}
		}

		try {
			FileWriter writer = new FileWriter(getSaveLocation());
			memento.save(writer);
		} catch (final IOException e) {
			LMLMonitorCorePlugin.log(e.getLocalizedMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rm.lml.monitor.core.IMonitorControl#setConnectionName(java.lang.String)
	 */
	public void setConnectionName(String connName) {
		fConnectionName = connName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rm.lml.monitor.core.IMonitorControl#setRemoteServicesId(java.lang.String)
	 */
	public void setRemoteServicesId(String id) {
		fRemoteServicesId = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rm.lml.monitor.core.IMonitorControl#setSystemType(java.lang.String)
	 */
	public void setSystemType(String type) {
		fSystemType = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.core.monitors.IMonitorControl#start(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void start(IProgressMonitor monitor) throws CoreException {
		if (!isActive()) {
			SubMonitor progress = SubMonitor.convert(monitor, 30);
			try {
				final IRemoteConnection conn = getRemoteConnection(progress.newChild(10));
				if (progress.isCanceled()) {
					return;
				}

				if (!conn.isOpen()) {
					try {
						conn.open(progress.newChild(10));
					} catch (final RemoteConnectionException e) {
						throw new CoreException(new Status(IStatus.ERROR, LMLMonitorCorePlugin.getUniqueIdentifier(),
								e.getMessage()));
					}
					if (!conn.isOpen()) {
						throw new CoreException(new Status(IStatus.ERROR, LMLMonitorCorePlugin.getUniqueIdentifier(),
								Messages.LMLResourceManagerMonitor_unableToOpenConnection));
					}
				}

				load();

				/*
				 * Initialize LML classes
				 */
				fLMLManager.openLgui(getMonitorId(), conn.getUsername(), getMonitorConfigurationRequestType(),
						fSavedLayout.toString(), fSavedJobs.toArray(new JobStatusData[0]), fSavedPattern);

				fActive = true;

				MonitorControlManager.getInstance().fireMonitorUpdated(new IMonitorControl[] { this });

				/*
				 * Start monitoring job. Note that the monitoring job can fail, in which case the monitor is considered to be
				 * stopped and the active flag set appropriately.
				 */
				synchronized (this) {
					if (fMonitorJob == null) {
						fMonitorJob = new MonitorJob(conn);
					}
					fMonitorJob.schedule();
				}

				JobManager.getInstance().addListener(fJobListener);
			} finally {
				if (monitor != null) {
					monitor.done();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.core.monitors.IMonitorControl#stop()
	 */
	public void stop() throws CoreException {
		if (isActive()) {
			JobManager.getInstance().removeListener(fJobListener);

			save();

			fLMLManager.closeLgui(getMonitorId());

			synchronized (this) {
				if (fMonitorJob != null) {
					fMonitorJob.cancel();
					fMonitorJob = null;
				}
			}

			fActive = false;

			MonitorControlManager.getInstance().fireMonitorUpdated(new IMonitorControl[] { this });
		}
	}

	private void addJob(IJobStatus status) {
		String monitorId = getMonitorId(status);
		if (monitorId != null && monitorId.equals(getMonitorId())) {
			ILaunchConfiguration configuration = status.getLaunchConfiguration();
			String controlName = LaunchUtils.getTemplateName(configuration);
			String[][] attrs = { { JobStatusData.JOB_ID_ATTR, status.getJobId() },
					{ JobStatusData.REMOTE_SERVICES_ID_ATTR, getRemoteServicesId() },
					{ JobStatusData.CONNECTION_NAME_ATTR, getConnectionName() }, { JobStatusData.CONTROL_TYPE_ATTR, controlName },
					{ JobStatusData.MONITOR_TYPE_ATTR, getSystemType() }, { JobStatusData.QUEUE_NAME_ATTR, status.getQueueName() },
					{ JobStatusData.OWNER_ATTR, status.getOwner() },
					{ JobStatusData.STDOUT_REMOTE_FILE_ATTR, status.getOutputPath() },
					{ JobStatusData.STDERR_REMOTE_FILE_ATTR, status.getErrorPath() },
					{ JobStatusData.INTERACTIVE_ATTR, Boolean.toString(status.isInteractive()) } };
			final JobStatusData data = new JobStatusData(attrs);
			data.setState(status.getState());
			data.setStateDetail(status.getStateDetail());
			fLMLManager.addUserJob(getMonitorId(), status.getJobId(), data);
		}
	}

	private RequestType getMonitorConfigurationRequestType() {
		RequestType request = new RequestType();
		final DriverType driver = new DriverType();
		driver.setName(getSystemType());
		request.getDriver().add(driver);
		return request;
	}

	private String getMonitorId(IJobStatus status) {
		ILaunchConfiguration configuration = status.getLaunchConfiguration();
		if (configuration != null) {
			String connectionName = LaunchUtils.getConnectionName(configuration);
			String remoteServicesId = LaunchUtils.getRemoteServicesId(configuration);
			String monitorType = LaunchUtils.getSystemType(configuration);
			if (connectionName != null && remoteServicesId != null && monitorType != null) {
				return MonitorControlManager.generateMonitorId(remoteServicesId, connectionName, monitorType);
			}
		}
		return null;
	}

	/**
	 * Get the remote connection specified by the monitor configuration.
	 * 
	 * @param monitor
	 *            progress monitor
	 * @return connection for the monitor
	 */
	private IRemoteConnection getRemoteConnection(IProgressMonitor monitor) throws CoreException {
		final IRemoteServices services = PTPRemoteCorePlugin.getDefault().getRemoteServices(getRemoteServicesId(), monitor);
		if (services != null) {
			final IRemoteConnectionManager connMgr = services.getConnectionManager();
			return connMgr.getConnection(getConnectionName());
		}
		throw CoreExceptionUtils.newException(Messages.MonitorControl_unableToOpenRemoteConnection, null);
	}

	private File getSaveLocation() {
		return LMLMonitorCorePlugin.getDefault().getStateLocation().append(getMonitorId()).addFileExtension(XML).toFile();
	}

	private void loadJobs(IMemento memento, List<JobStatusData> jobs) {
		if (memento != null) {
			final IMemento[] children = memento.getChildren(JOB_ATTR);
			for (final IMemento child : children) {
				String[][] attrs = { { JobStatusData.JOB_ID_ATTR, child.getID() },
						{ JobStatusData.REMOTE_SERVICES_ID_ATTR, getRemoteServicesId() },
						{ JobStatusData.CONNECTION_NAME_ATTR, getConnectionName() },
						{ JobStatusData.CONTROL_TYPE_ATTR, child.getString(JobStatusData.CONTROL_TYPE_ATTR) },
						{ JobStatusData.MONITOR_TYPE_ATTR, getSystemType() },
						{ JobStatusData.STATE_ATTR, child.getString(JobStatusData.STATE_ATTR) },
						{ JobStatusData.STATE_DETAIL_ATTR, child.getString(JobStatusData.STATE_DETAIL_ATTR) },
						{ JobStatusData.STDOUT_REMOTE_FILE_ATTR, child.getString(JobStatusData.STDOUT_REMOTE_FILE_ATTR) },
						{ JobStatusData.STDERR_REMOTE_FILE_ATTR, child.getString(JobStatusData.STDERR_REMOTE_FILE_ATTR) },
						{ JobStatusData.INTERACTIVE_ATTR, Boolean.toString(child.getBoolean(JobStatusData.INTERACTIVE_ATTR)) },
						{ JobStatusData.QUEUE_NAME_ATTR, child.getString(JobStatusData.QUEUE_NAME_ATTR) },
						{ JobStatusData.OWNER_ATTR, child.getString(JobStatusData.OWNER_ATTR) },
						{ JobStatusData.OID_ATTR, child.getString(JobStatusData.OID_ATTR) } };
				jobs.add(new JobStatusData(attrs));
			}
		}
	}

	private void loadPattern(IMemento memento, Map<String, List<IPattern>> pattern) {
		if (memento != null) {
			final IMemento[] childrenPattern = memento.getChildren(PATTERN_GID_ATTR);
			for (final IMemento childPattern : childrenPattern) {
				final List<IPattern> filters = new LinkedList<IPattern>();
				final IMemento[] childrenFilter = childPattern.getChildren(FILTER_TITLE_ATTR);
				for (final IMemento childFilter : childrenFilter) {
					final IPattern filter = new Pattern(childFilter.getID(), childFilter.getString(FILTER_TYPE_ATTR));
					if (childFilter.getBoolean(FILTER_RANGE_ATTR)) {
						filter.setRange(childFilter.getString(FILTER_MIN_VALUE_RANGE_ATTR),
								childFilter.getString(FILTER_MAX_VALUE_RANGE_ATTR));
					} else if (childFilter.getBoolean(FILTER_RELATION_ATTR)) {
						filter.setRelation(childFilter.getString(FILTER_RELATION_OPERATOR_ATTR),
								childFilter.getString(FILTER_RELATION_VALUE_ATTR));
					}
					filters.add(filter);
				}

				if (filters.size() > 0) {
					pattern.put(childPattern.getID(), filters);
				}
			}
		}
	}

	private boolean loadState(IMemento memento) {
		setRemoteServicesId(memento.getString(JobStatusData.REMOTE_SERVICES_ID_ATTR));
		setConnectionName(memento.getString(JobStatusData.CONNECTION_NAME_ATTR));
		fSystemType = memento.getString(SYSTEM_TYPE_ATTR);
		return memento.getBoolean(MONITOR_STATE);
	}

	private void saveJob(JobStatusData job, IMemento memento) {
		final IMemento jobMemento = memento.createChild(JOB_ATTR, job.getJobId());
		jobMemento.putString(JobStatusData.CONTROL_TYPE_ATTR, job.getControlType());
		jobMemento.putString(JobStatusData.STATE_ATTR, job.getState());
		jobMemento.putString(JobStatusData.STATE_DETAIL_ATTR, job.getStateDetail());
		jobMemento.putString(JobStatusData.STDOUT_REMOTE_FILE_ATTR, job.getOutputPath());
		jobMemento.putString(JobStatusData.STDERR_REMOTE_FILE_ATTR, job.getErrorPath());
		jobMemento.putBoolean(JobStatusData.INTERACTIVE_ATTR, job.isInteractive());
		jobMemento.putString(JobStatusData.QUEUE_NAME_ATTR, job.getQueueName());
		jobMemento.putString(JobStatusData.OWNER_ATTR, job.getOwner());
		jobMemento.putString(JobStatusData.OID_ATTR, job.getOid());
	}

	private void savePattern(String key, List<IPattern> value, IMemento memento) {
		final IMemento patternMemento = memento.createChild(PATTERN_GID_ATTR, key);
		for (final IPattern filterValue : value) {
			final IMemento filterMemento = patternMemento.createChild(FILTER_TITLE_ATTR, filterValue.getColumnTitle());
			filterMemento.putString(FILTER_TYPE_ATTR, filterValue.getType());
			filterMemento.putBoolean(FILTER_RANGE_ATTR, filterValue.isRange());
			filterMemento.putBoolean(FILTER_RELATION_ATTR, filterValue.isRelation());
			if (filterValue.isRange()) {
				filterMemento.putString(FILTER_MIN_VALUE_RANGE_ATTR, filterValue.getMinValueRange());
				filterMemento.putString(FILTER_MAX_VALUE_RANGE_ATTR, filterValue.getMaxValueRange());
			} else if (filterValue.isRelation()) {
				filterMemento.putString(FILTER_RELATION_OPERATOR_ATTR, filterValue.getRelationOperator());
				filterMemento.putString(FILTER_RELATION_VALUE_ATTR, filterValue.getRelationValue());
			}
		}
	}

	private void saveState(IMemento memento) {
		memento.putString(REMOTE_SERVICES_ID_ATTR, getRemoteServicesId());
		memento.putString(CONNECTION_NAME_ATTR, getConnectionName());
		memento.putString(SYSTEM_TYPE_ATTR, getSystemType());
		memento.putBoolean(MONITOR_STATE, isActive());
	}

	private void updateJob(IJobStatus status) {
		String monitorId = getMonitorId(status);
		if (monitorId != null && monitorId.equals(getMonitorId())) {
			fLMLManager.updateUserJob(getMonitorId(), status.getJobId(), status.getState(), status.getStateDetail());
		}
	}
}