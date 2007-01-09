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
package org.eclipse.ptp.debug.core.cdi;

import org.eclipse.ptp.core.util.BitList;

/**
 * @author Clement chu
 * 
 */
public interface ICommonActions {
	/** Stop debugger of given tasks
	 * @param tasks
	 * @throws PCDIException
	 */
	public void stop(BitList tasks) throws PCDIException;
	/** Resume debugger of given tasks
	 * @param tasks
	 * @throws PCDIException
	 */
	public void resume(BitList tasks) throws PCDIException;
	/** Suspend debugger of given tasks
	 * @param tasks
	 * @throws PCDIException
	 */
	public void suspend(BitList tasks) throws PCDIException;
	/** Step into debugger of given tasks
	 * @param tasks
	 * @throws PCDIException
	 */
	public void steppingInto(BitList tasks) throws PCDIException;
	/** step over debugger of given tasks
	 * @param tasks
	 * @throws PCDIException
	 */
	public void steppingOver(BitList tasks) throws PCDIException;
	/** step return debugger of given tasks
	 * @param tasks
	 * @throws PCDIException
	 */
	public void steppingReturn(BitList tasks) throws PCDIException;
	
	/** get step return tasks
	 * @param tasks test tasks for step return
	 * @return BitList[] with 2 elements - 0: add tasks, 1: delete tasks
	 * @throws PCDIException
	 */
	public BitList[] getStepReturnTasks(BitList tasks) throws PCDIException;
	/** Get expression value of given tasks and given variable name
	 * @param tasks
	 * @param variable
	 * @return
	 * @throws PCDIException
	 */
	public ICommandResult getExpressionValue(BitList tasks, String variable) throws PCDIException;
}

