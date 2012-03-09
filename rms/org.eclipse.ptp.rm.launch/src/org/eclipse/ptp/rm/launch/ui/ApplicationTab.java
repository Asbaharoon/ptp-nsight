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
package org.eclipse.ptp.rm.launch.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ptp.core.IPTPLaunchConfigurationConstants;
import org.eclipse.ptp.launch.ui.LaunchConfigurationTab;
import org.eclipse.ptp.launch.ui.LaunchImages;
import org.eclipse.ptp.remote.core.IRemoteConnection;
import org.eclipse.ptp.remote.core.IRemoteServices;
import org.eclipse.ptp.remote.ui.IRemoteUIFileManager;
import org.eclipse.ptp.remote.ui.IRemoteUIServices;
import org.eclipse.ptp.remote.ui.PTPRemoteUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The Main tab is used to specify the resource manager for the launch, select the project and executable to launch, and specify the
 * location of the executable if it is a remote launch.
 */
public class ApplicationTab extends LaunchConfigurationTab {
	protected class WidgetListener extends SelectionAdapter implements ModifyListener {
		@Override
		public void modifyText(ModifyEvent e) {
			updateLaunchConfigurationDialog();
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			Object source = e.getSource();
			if (source == appButton) {
				handleApplicationButtonSelected();
			} else if (source == consoleButton) {
				updateLaunchConfigurationDialog();
			}
		}
	}

	/**
	 * @since 4.0
	 */
	public static final String TAB_ID = "org.eclipse.ptp.launch.applicationLaunch.applicationTab"; //$NON-NLS-1$

	protected Text projText = null;

	protected Text appText = null;
	protected Button appButton = null;
	protected Button browseAppButton = null;
	protected Button consoleButton = null;
	protected WidgetListener listener = new WidgetListener();
	protected final boolean combinedOutputDefault = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse .swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);

		comp.setLayout(new GridLayout());

		Composite mainComp = new Composite(comp, SWT.NONE);
		mainComp.setLayout(createGridLayout(2, false, 0, 0));
		mainComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label appLabel = new Label(mainComp, SWT.NONE);
		appLabel.setText("Application executable");
		appLabel.setLayoutData(spanGridData(-1, 2));

		appText = new Text(mainComp, SWT.SINGLE | SWT.BORDER);
		appText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		appText.addModifyListener(listener);

		appButton = createPushButton(mainComp, "Browse", null);
		appButton.addSelectionListener(listener);

		consoleButton = createCheckButton(mainComp, "Display output from all processes in a console view");
		consoleButton.setSelection(combinedOutputDefault);
		consoleButton.addSelectionListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getId()
	 */
	@Override
	public String getId() {
		return TAB_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getImage()
	 */
	@Override
	public Image getImage() {
		return LaunchImages.getImage(LaunchImages.IMG_MAIN_TAB);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	@Override
	public String getName() {
		return "Application";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse .debug.core.ILaunchConfiguration)
	 */
	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		super.initializeFrom(configuration);

		try {
			appText.setText(configuration.getAttribute(IPTPLaunchConfigurationConstants.ATTR_EXECUTABLE_PATH, EMPTY_STRING));
			consoleButton.setSelection(configuration.getAttribute(IPTPLaunchConfigurationConstants.ATTR_CONSOLE, false));
		} catch (CoreException e) {
			setErrorMessage("Cannot read configuation");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#isValid(org.eclipse .debug.core.ILaunchConfiguration)
	 */
	@Override
	public boolean isValid(ILaunchConfiguration config) {
		setErrorMessage(null);
		setMessage(null);

		String name = getFieldContent(appText.getText());
		if (name == null) {
			setErrorMessage("Application executable is not specified");
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse .debug.core.ILaunchConfigurationWorkingCopy)
	 */
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(IPTPLaunchConfigurationConstants.ATTR_EXECUTABLE_PATH, getFieldContent(appText.getText()));
		configuration.setAttribute(IPTPLaunchConfigurationConstants.ATTR_CONSOLE, consoleButton.getSelection());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse. debug.core.ILaunchConfigurationWorkingCopy)
	 */
	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(IPTPLaunchConfigurationConstants.ATTR_EXECUTABLE_PATH, (String) null);
		configuration.setAttribute(IPTPLaunchConfigurationConstants.ATTR_CONSOLE, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.debug.ui.AbstractLaunchConfigurationTab# setLaunchConfigurationDialog
	 * (org.eclipse.debug.ui.ILaunchConfigurationDialog)
	 */
	@Override
	public void setLaunchConfigurationDialog(ILaunchConfigurationDialog dialog) {
		super.setLaunchConfigurationDialog(dialog);
	}

	/**
	 * Allow the user to choose the application to execute
	 * 
	 * Initial path does not work on MacOS X: see bug #153365
	 */
	protected void handleApplicationButtonSelected() {
		String initPath = appText.getText();

		IRemoteConnection conn = getRemoteConnection(getLaunchConfiguration());
		if (conn != null) {
			IRemoteUIServices remoteUIServices = PTPRemoteUIPlugin.getDefault().getRemoteUIServices(conn.getRemoteServices());
			if (remoteUIServices != null) {
				IRemoteUIFileManager fileManager = remoteUIServices.getUIFileManager();
				if (fileManager != null) {
					fileManager.setConnection(conn);
					fileManager.showConnections(false);
					String path = fileManager.browseFile(getShell(), "Select application to execute", initPath, 0);
					if (path != null) {
						appText.setText(path.toString());
					}
				} else {
					MessageDialog.openInformation(getShell(), "Please fix error first", "Unable to obtain file manager");
				}
			} else {
				MessageDialog.openInformation(getShell(), "Please fix error first", "Unable to obtain remote UI services");
			}
		} else {
			MessageDialog.openInformation(getShell(), "Please fix error first", "No connection available");
		}
	}

	protected IRemoteConnection getRemoteConnection(ILaunchConfiguration config) {
		String id = getRemoteServicesId(config);
		if (id != null) {
			IRemoteServices services = PTPRemoteUIPlugin.getDefault().getRemoteServices(id, getLaunchConfigurationDialog());
			if (services != null) {
				String name = getConnectionName(config);
				if (name != null) {
					return services.getConnectionManager().getConnection(name);
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.debug.ui.AbstractLaunchConfigurationTab# updateLaunchConfigurationDialog()
	 */
	@Override
	protected void updateLaunchConfigurationDialog() {
		super.updateLaunchConfigurationDialog();
	}
}
