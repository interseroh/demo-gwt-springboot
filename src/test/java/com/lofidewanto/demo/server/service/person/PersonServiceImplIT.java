/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.lofidewanto.demo.server.service.person;

import com.lofidewanto.demo.server.domain.Address;
import com.lofidewanto.demo.server.domain.AddressImpl;
import com.lofidewanto.demo.server.domain.Person;
import com.lofidewanto.demo.server.domain.PersonImpl;
import com.lofidewanto.demo.server.exception.CreatePersonException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PersonServiceImplIT {

	@Autowired
	private PersonService personService;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCreateAddressFromPerson() throws CreatePersonException {
		// Prepare
		Person person = new PersonImpl();
		Address address = new AddressImpl();

		// CUT
		Person createAddressFromPerson = personService.createAddressFromPerson(address, person);
		
		// Asserts
		assertNotNull(createAddressFromPerson);
	}

	@Test
	public void testFindAllPersons() throws CreatePersonException {
		// Prepare
		Person person = new PersonImpl();
		Address address = new AddressImpl();

		Person createAddressFromPerson = personService
				.createAddressFromPerson(address, person);
		assertNotNull(createAddressFromPerson);

		// CUT
		Collection<Person> persons = personService.findAllPersons(0, 10);
		
		// Asserts
		assertEquals(1, persons.size());
	}

}
