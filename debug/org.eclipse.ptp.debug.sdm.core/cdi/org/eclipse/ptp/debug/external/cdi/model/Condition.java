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
package org.eclipse.ptp.debug.external.cdi.model;

import org.eclipse.cdt.debug.core.cdi.ICDICondition;

/**
 * @author Clement chu
 * 
 */
public class Condition implements ICDICondition {
	int ignoreCount;
	String expression;
	String[] tids;

	public Condition(int ignore, String exp, String[] ids) {
		ignoreCount = ignore;
		expression = (exp == null) ? new String() : exp;
		tids = (ids == null) ? new String[0] : ids;
	}

	/**
	 * @see org.eclipse.cdt.debug.core.cdi.ICDICondition#getIgnoreCount()
	 */
	public int getIgnoreCount() {
		return ignoreCount;
	}

	/**
	 * @see org.eclipse.cdt.debug.core.cdi.ICDICondition#getExpression()
	 */
	public String getExpression() {
		return expression;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.debug.core.cdi.ICDICondition#getThreadId()
	 */
	public String[] getThreadIds() {
		return tids;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(ICDICondition obj) {
		if (obj instanceof Condition) {
			Condition cond = (Condition)obj;
			if (cond.getIgnoreCount() != this.getIgnoreCount())
				return false;
			if (cond.getExpression().compareTo(this.getExpression()) != 0)
				return false;
			if (cond.getThreadIds().length != this.getThreadIds().length)
				return false;
			for (int i = 0; i < cond.getThreadIds().length; ++i) {
				if (cond.getThreadIds()[i].compareTo(this.getThreadIds()[i]) != 0) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
