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
import com.lofidewanto.demo.server.repository.AddressRepository;
import com.lofidewanto.demo.server.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class PersonServiceImpl implements PersonService {

	private static final Logger logger = LoggerFactory
			.getLogger(PersonServiceImpl.class);

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Override
	public Person createAddressFromPerson(Address address, Person person)
			throws CreatePersonException {
		// Create a Person and add an Address to it
		AddressImpl addressImplSaved = addressRepository
				.save((AddressImpl) address);
		
		logger.info(
				"Following address created: " + addressImplSaved.getStreet());
		
		person.addAddress(address);
		
		try {
			PersonImpl personImplSaved = personRepository.save((PersonImpl) person);

			logger.info("Following person created: " + personImplSaved.getName());

			return personImplSaved;
		} catch (Exception e) {
			logger.error(
					"Error saving the person and address - exception: " + e);
			throw new CreatePersonException();
		}
	}

	@Override
	public Collection<Person> findAllPersons(Integer start, Integer length) {
		Collection<Person> personsAsCollection = new ArrayList<>();
		Pageable pageable = new PageRequest(start, length);
		Iterable<PersonImpl> persons = personRepository.findAll(pageable);

		persons.forEach(person -> {
			personsAsCollection.add(person);
		});
		
		logger.info("Find all persons with start and length amount: "
				+ personsAsCollection.size());
		
		return personsAsCollection;
	}

}
