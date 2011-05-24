/*******************************************************************************
 * Copyright (c) 2005, 2006, 2007 Los Alamos National Security, LLC.
 * This material was produced under U.S. Government contract DE-AC52-06NA25396
 * for Los Alamos National Laboratory (LANL), which is operated by the Los Alamos
 * National Security, LLC (LANS) for the U.S. Department of Energy.  The U.S. Government has
 * rights to use, reproduce, and distribute this software. NEITHER THE
 * GOVERNMENT NOR LANS MAKES ANY WARRANTY, EXPRESS OR IMPLIED, OR
 * ASSUMES ANY LIABILITY FOR THE USE OF THIS SOFTWARE. If software is modified
 * to produce derivative works, such modified software should be clearly marked,
 * so as not to confuse it with the version available from LANL.
 *
 * Additionally, this program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Modified by:
 * 		Claudia Knobloch, Forschungszentrum Juelich GmbH
 *******************************************************************************/
package org.eclipse.ptp.rm.lml.internal.core.events;

import org.eclipse.ptp.rm.lml.core.LMLManager;
import org.eclipse.ptp.rm.lml.core.events.ILguiRemovedEvent;
import org.eclipse.ptp.rm.lml.core.model.ILguiItem;

/**
 * Class of the interface ILguiRemovedEvent.
 */
public class LguiRemovedEvent implements ILguiRemovedEvent{
	
	private final ILguiItem removedLguiItem;
	
	/*
	 * The associated LMLManager
	 */
	private final LMLManager lmlManager;
	
	/*
	 * The associated ILguiItem
	 */
	private final ILguiItem lguiItem;
	
	/**
	 * Constructor
	 * @param lmlManager the associated LMLManager
	 * @param lguiItem the associated ILguiItem
	 */
	public LguiRemovedEvent(LMLManager lmlManager, ILguiItem lguiItem, ILguiItem removedLguiItem) {
		this.lmlManager = lmlManager;
		this.lguiItem = lguiItem;
		this.removedLguiItem = removedLguiItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rm.lml.core.elements.ILguiRemovedEvent#getLgui()
	 */
	public ILguiItem getLguiItem() {
		return lguiItem;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rm.lml.core.elements.ILguiRemovedEvent#getLMLManager()
	 */
	public LMLManager getLMLManager() {
		return lmlManager;
	}

	public ILguiItem getRemovedLguiItem() {
		return removedLguiItem;
	}

}
