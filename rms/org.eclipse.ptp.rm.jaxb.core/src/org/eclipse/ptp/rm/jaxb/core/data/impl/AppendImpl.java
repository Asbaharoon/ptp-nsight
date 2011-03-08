/*******************************************************************************
 * Copyright (c) 2011 University of Illinois All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html 
 * 	
 * Contributors: 
 * 	Albert L. Rossi - design and implementation
 ******************************************************************************/
package org.eclipse.ptp.rm.jaxb.core.data.impl;

import java.util.List;

import org.eclipse.ptp.rm.jaxb.core.data.Append;

public class AppendImpl extends AbstractRangeAssign {

	private final String separator;
	private final String endTag;
	private final String startTag;
	private final List<String> values;

	public AppendImpl(String uuid, Append append) {
		this.uuid = uuid;
		this.field = append.getField();
		separator = append.getSeparator();
		startTag = append.getStartTag();
		endTag = append.getEndTag();
		String rString = append.getGroups();
		if (rString == null) {
			rString = append.getIndices();
		}
		range = new Range(rString);
		this.values = append.getValue();
	}

	@Override
	protected Object[] getValue(Object previous, String[] values) throws Throwable {
		if (!this.values.isEmpty()) {
			StringBuffer norm = new StringBuffer();
			if (startTag != null) {
				norm.append(startTag);
			}
			norm.append((String) normalizedValue(target, uuid, this.values.get(0)));
			int len = this.values.size();
			for (int i = 1; i < len; i++) {
				if (separator != null) {
					norm.append(separator);
				}
				norm.append((String) normalizedValue(target, uuid, this.values.get(i)));
			}
			if (endTag != null) {
				norm.append(endTag);
			}
			return new Object[] { norm.toString() };
		}

		if (values == null) {
			return new Object[] { previous };
		}

		range.setLen(values.length);
		List<Object> found = range.findInRange(values);

		if (found.isEmpty()) {
			return new Object[] { previous };
		}
		StringBuffer buffer = new StringBuffer();
		if (previous != null && previous instanceof String) {
			buffer.append(previous);
			if (null != startTag) {
				buffer.append(startTag);
			} else if (null != separator) {
				buffer.append(separator);
			}
		} else if (null != startTag) {
			buffer.append(startTag);
		}
		buffer.append(found.get(0).toString());
		int sz = found.size();
		for (int i = 1; i < sz; i++) {
			if (separator != null) {
				buffer.append(separator);
			}
			buffer.append(found.get(i));
		}
		if (null != endTag) {
			buffer.append(endTag);
		}
		return new Object[] { buffer.toString() };
	}
}
