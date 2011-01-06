/**
 * Copyright (c) 2009 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - Initial Implementation
 *
 */
package org.eclipse.ptp.ui;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ptp.core.elements.IPResourceManager;

public interface IRMSelectionListener {
	/**
	 * Notify the listener that a resource manager has been selected as the
	 * default
	 * 
	 * @param rm
	 *            selected resource manager
	 * @since 5.0
	 */
	public void setDefault(IPResourceManager rm);

	/**
	 * Notify the listener that the selection has changed in the RM view
	 * 
	 * @param selection
	 *            new selection
	 */
	public void selectionChanged(ISelection selection);
}
