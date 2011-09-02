/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.api.handler;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.api.context.Context;
import org.openmrs.api.handler.ExistingOrNewVisitAssignmentHandler;
import org.openmrs.api.handler.ExistingVisitAssignmentHandler;
import org.openmrs.test.BaseContextSensitiveTest;
import org.openmrs.test.Verifies;

/**
 * Tests methods in the {@link ExistingOrNewVisitAssignmentHandler}
 */
public class ExistingOrNewVisitAssignmentHandlerTest extends BaseContextSensitiveTest {

	protected static final String ENC_INITIAL_DATA_XML = "org/openmrs/api/include/EncounterServiceTest-initialData.xml";
	
	/**
	 * This method is run before all of the tests in this class because it has the @Before
	 * annotation on it. This will add the contents of {@link #ENC_INITIAL_DATA_XML} to the current
	 * database
	 * 
	 * @see BaseContextSensitiveTest#runBeforeAllUnitTests()
	 * @throws Exception
	 */
	@Before
	public void runBeforeEachTest() throws Exception {
		executeDataSet(ENC_INITIAL_DATA_XML);
	}
	
	/**
	 * @see {@link ExistingVisitAssignmentHandler#beforeCreateEncounter(Encounter)}
	 */
	@Test
	@Verifies(value = "should assign existing visit if match found", method = "beforeCreateEncounter(Encounter)")
	public void beforeCreateEncounter_shouldAssignExistingVisitIfMatchFound() throws Exception {
		Encounter encounter = Context.getEncounterService().getEncounter(1);
		Assert.assertNull(encounter.getVisit());
		
		new ExistingOrNewVisitAssignmentHandler().beforeCreateEncounter(encounter);
		
		Assert.assertNotNull(encounter.getVisit());
	}
	
	/**
	 * @see {@link ExistingVisitAssignmentHandler#beforeCreateEncounter(Encounter)}
	 */
	@Test
	@Verifies(value = "should assign new visit if no match found", method = "beforeCreateEncounter(Encounter)")
	public void beforeCreateEncounter_shouldAssignNewVisitIfNoMatchFound() throws Exception {
		Encounter encounter = Context.getEncounterService().getEncounter(1);
		Assert.assertNull(encounter.getVisit());
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(encounter.getEncounterDatetime());
		calendar.set(Calendar.YEAR, 1900);
		
		encounter.setEncounterDatetime(calendar.getTime());
		
		new ExistingOrNewVisitAssignmentHandler().beforeCreateEncounter(encounter);
		
		Assert.assertNotNull(encounter.getVisit());
	}
}