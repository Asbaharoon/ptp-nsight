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
package org.eclipse.ptp.debug.external.commands;

import org.eclipse.ptp.core.IPJob;
import org.eclipse.ptp.debug.core.cdi.PCDIException;
import org.eclipse.ptp.debug.external.IAbstractDebugger;


/**
 * @author Clement chu
 * 
 */
public class StartDebuggerCommand extends AbstractDebugCommand {
	private IPJob job = null;
	
	public StartDebuggerCommand(IPJob job) {
		super(null, false, true);
		this.job = job;
	}
	public void execCommand(IAbstractDebugger debugger) throws PCDIException {
		debugger.startDebugger(job);
		waitFnish();
	}
	
	public void waitFnish() throws PCDIException {
		if (waitForReturn()) {
			if (result.equals(OK))
				return;
		}
		throw new PCDIException("Cannot start debugger");		
	}
}

