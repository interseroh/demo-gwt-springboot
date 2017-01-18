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
package com.lofidewanto.demo.server.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lofidewanto.demo.server.domain.Person;
import com.lofidewanto.demo.server.service.person.PersonService;
import com.lofidewanto.demo.shared.DemoGwtServiceEndpoint;
import com.lofidewanto.demo.shared.PersonDto;

import javax.ws.rs.QueryParam;

@Controller
@CrossOrigin
public class PersonController {

	private static final Logger logger = LoggerFactory
			.getLogger(PersonController.class);

	@Autowired
	private PersonService personService;




	@RequestMapping(value = DemoGwtServiceEndpoint.PERSON_LIST, method = RequestMethod.GET)
	public @ResponseBody List<PersonDto> getPersons(
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length) {
		logger.info("Method getPersons begins...");
		ArrayList<PersonDto> persons = new ArrayList<>();

//		Collection<Person> findAllPersons = personService.findAllPersons(start,
//				length);
//		for (Person person : findAllPersons) {
//			PersonDto personDto = buildPerson(person);
//			persons.add(personDto);
//		}

		PersonDto dto= new PersonDto();
		dto.setName("PersonName_0");
		dto.setNickname("PersonName_0_Nickname");

		persons.add(dto);


		return persons;
	}

	@RequestMapping(value = DemoGwtServiceEndpoint.PERSON_FILTER, method = RequestMethod.GET)
	public @ResponseBody List<PersonDto> filterPersons(
			@RequestParam("nameSuggestBox") String personName,
			@RequestParam("fromDateTimePicker") Date fromDate,
			@RequestParam("untilDateTimePicker") Date toDate) {
		logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++Method filterPersons begins...");
		ArrayList<PersonDto> persons = new ArrayList<>();
		PersonDto dto= new PersonDto();
		dto.setName("PersonName_1");
		dto.setNickname("PersonName_1_Nickname");
		persons.add(dto);
		return persons;
	}

	private PersonDto buildPerson(Person person) {
		PersonDto personDto = new PersonDto();
		personDto.setName(person.getName());
		personDto.setNickname(person.getNickname());
		return personDto;
	}

}
