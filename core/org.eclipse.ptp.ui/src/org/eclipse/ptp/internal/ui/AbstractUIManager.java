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
package org.eclipse.ptp.internal.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.ptp.core.IModelManager;
import org.eclipse.ptp.ui.IManager;
import org.eclipse.ptp.ui.listeners.IPaintListener;
import org.eclipse.ptp.ui.listeners.ISetListener;
import org.eclipse.ptp.ui.model.IElement;
import org.eclipse.ptp.ui.model.IElementHandler;
import org.eclipse.ptp.ui.model.IElementSet;
import org.eclipse.ptp.ui.model.internal.ElementSet;

/**
 * @author Clement chu
 *
 */
public abstract class AbstractUIManager implements IManager {
	protected IModelManager modelManager = null;
	protected String cur_set_id = EMPTY_ID;
	protected List pListeners = new ArrayList(0);
	protected List setListeners = new ArrayList(0);
	
	public void shutdown() {
		pListeners.clear();
		pListeners = null;
	}
	
	public void addPaintListener(IPaintListener pListener) {
		if (!pListeners.contains(pListener))
			pListeners.add(pListener);
	}
	
	public void removePaintListener(IPaintListener pListener) {
		if (pListeners.contains(pListener))
			pListeners.remove(pListener);
	}

	protected void firePaintListener(Object condition) {
		for (Iterator i=pListeners.iterator(); i.hasNext();) {
			((IPaintListener)i.next()).repaint(condition);
		}
	}
	
	public void addSetListener(ISetListener setListener) {
		if (!setListeners.contains(setListener))
			setListeners.add(setListener);
	}
	public void removeSetListener(ISetListener setListener) {
		if (setListeners.contains(setListener))
			setListeners.remove(setListener);
	}
	
	public void fireEvent(int eventType, IElement[] elements, IElementSet cur_set, IElementSet pre_set) {
		for (Iterator i=setListeners.iterator(); i.hasNext();) {
			ISetListener setListener = (ISetListener)i.next();
			switch (eventType) {
			case CREATE_SET_TYPE:
				setListener.createSetEvent(cur_set, elements);
				break;
			case DELETE_SET_TYPE:
				setListener.deleteSetEvent(cur_set);
				break;
			case CHANGE_SET_TYPE:
				setListener.changeSetEvent(cur_set, pre_set);
				break;
			case ADD_ELEMENT_TYPE:
				setListener.addElementsEvent(cur_set, elements);
				break;
			case REMOVE_ELEMENT_TYPE:
				setListener.removeElementsEvent(cur_set, elements);
				break;
			}
		}
	}
	
	public void addToSet(IElement[] elements, IElementSet set) {
		for (int i=0; i<elements.length; i++) {
			set.add(elements[i]);
		}
	}
	public void addToSet(IElement[] elements, String setID, IElementHandler elementHandler) {
		IElementSet set = elementHandler.getSet(setID);
		addToSet(elements, set);
		updateMatchElementSets(set, elementHandler);
		fireEvent(ADD_ELEMENT_TYPE, elements, set, null);
	}
	public String createSet(IElement[] elements, String setID, String setName, IElementHandler elementHandler) {
		IElementSet set = new ElementSet(elementHandler, setID, setName, true);
		addToSet(elements, set);
		elementHandler.add(set);
		updateMatchElementSets(set, elementHandler);
		fireEvent(CREATE_SET_TYPE, elements, set, null);
		return set.getID();
	}
	public void removeSet(String setID, IElementHandler elementHandler) {
		IElementSet set = elementHandler.getSet(setID);
		String[] sets = set.getMatchSets();
		for (int i=0; i<sets.length; i++) {
			elementHandler.getSet(sets[i]).removeMatchSet(setID);
		}
		elementHandler.remove(setID);
		fireEvent(DELETE_SET_TYPE, null, set, null);
	}
	public void removeFromSet(IElement[] elements, String setID, IElementHandler elementHandler) {
		IElementSet set = elementHandler.getSet(setID);
		for (int i=0; i<elements.length; i++) {
			set.remove(elements[i]);
		}
		updateMatchElementSets(set, elementHandler);
		fireEvent(REMOVE_ELEMENT_TYPE, elements, set, null);
	}
	public void updateMatchElementSets(IElementSet targetSet, IElementHandler elementHandler) {
		IElementSet[] sets = elementHandler.getSortedSets();
		for (int i=0; i<sets.length; i++) {
			if (sets[i].getID().equals(targetSet.getID()))
				continue;
			
			IElement[] elements = sets[i].getElements();
			for (int j=0; j<elements.length; j++) {
				if (targetSet.contains(elements[j].getID())) {
					targetSet.addMatchSet(sets[i].getID());
					sets[i].addMatchSet(targetSet.getID());
					break;
				}
			}
		}
	}	
}
