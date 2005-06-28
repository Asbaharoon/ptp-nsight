package org.eclipse.ptp.debug.core.cdi.model;

import org.eclipse.cdt.debug.core.cdi.model.ICDITarget;

public interface IPCDITarget extends ICDITarget {
	public Process[] getProcesses();
	public Process getProcess(int num);
}
