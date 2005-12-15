/*******************************************************************************
 * Copyright (c) 2000, 2004 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *******************************************************************************/

#include <stdlib.h>
#include <string.h>

#include "list.h"
#include "MIValue.h"
#include "MIResult.h"
#include "MIResultRecord.h"

MIResultRecord *
MIResultRecordNew(void)
{
	MIResultRecord *	rr;
	
	rr = (MIResultRecord *)malloc(sizeof(MIResultRecord));
	rr->results = NewList();
	rr->resultClass = MIResultRecordINVALID;
	rr->token = -1;
	return rr;
}

MIString *
MIResultRecordToString(MIResultRecord *rr)
{
	char *		class;
	MIString *	str;
	MIResult *	r;
	
	switch (rr->resultClass) {
	case MIResultRecordDONE:
		class = "done";
		break;
	case MIResultRecordRUNNING:
		class = "running";
		break;
	case MIResultRecordCONNECTED:
		class = "connected";
		break;
	case MIResultRecordERROR:
		class = "error";
		break;
	case MIResultRecordEXIT:
		class = "exit";
		break;
	default:
		class = "<invalid>";
		break;
	}
	
	if (rr->token != -1)
		str = MIStringNew("%d^%s", rr->token, class);
	else
		str = MIStringNew("^%s", class);
	
	for (SetList(rr->results); (r = (MIResult *)GetListElement(rr->results)) != NULL;) {
			MIStringAppend(str, MIStringNew(","));
			MIStringAppend(str, MIResultToString(r));
	}
	
	return str;
}

void
MIResultRecordFree(MIResultRecord *rr)
{
	if (rr->results != NULL)
		DestroyList(rr->results, MIResultFree);
	free(rr);
}
