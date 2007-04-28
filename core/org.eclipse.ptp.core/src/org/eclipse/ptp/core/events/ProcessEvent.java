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
package org.eclipse.ptp.core.events;

import org.eclipse.ptp.core.elements.IPProcess;
import org.eclipse.ptp.core.elements.attributes.ProcessAttributes;
import org.eclipse.ptp.core.elements.attributes.ProcessAttributes.State;


/**
 * @author Clement chu
 *
 */
public class ProcessEvent implements IProcessEvent {
	private int type;
	private IPProcess p;
	private String input = null;
	private int val = 0;
	private ProcessAttributes.State state = State.ERROR;
	
	public ProcessEvent(IPProcess p, int type) {
		this.p = p;
		this.type = type;
	}
	public ProcessEvent(IPProcess p, int type, String input) {
		this(p, type);
		this.input = input;
	}
	public ProcessEvent(IPProcess p, int type, ProcessAttributes.State state) {
		this(p, type);
		this.state = state;
	}
	public ProcessEvent(IPProcess p, int type, int val) {
		this(p, type);
		this.val = val;
	}
	public IPProcess getProcess() {
		return p;
	}
	public String getInput() {
		return input;
	}
	public int getType() {
		return type;
	}
	public int getValue() {
		return val;
	}
	public ProcessAttributes.State getState() {
		return state;
	}
}
