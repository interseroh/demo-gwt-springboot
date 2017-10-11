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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lofidewanto.demo.server.domain.Address;
import com.lofidewanto.demo.server.domain.AddressImpl;
import com.lofidewanto.demo.server.domain.Person;
import com.lofidewanto.demo.server.domain.PersonImpl;
import com.lofidewanto.demo.server.exception.CreatePersonException;
import com.lofidewanto.demo.server.repository.PersonRepository;
import com.lofidewanto.demo.server.service.person.PersonService;
import com.lofidewanto.demo.shared.AddressDto;
import com.lofidewanto.demo.shared.DemoGwtServiceEndpoint;
import com.lofidewanto.demo.shared.PersonDto;

@Controller
@CrossOrigin
public class PersonController {

    private static final Logger logger = LoggerFactory
            .getLogger(PersonController.class);

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    /**
     * Using simple @{@link ResponseBody}.
     *
     * @param start
     * @param length
     * @return
     */
    @RequestMapping(value = DemoGwtServiceEndpoint.PERSON_LIST, method = RequestMethod.GET)
    public @ResponseBody
    List<PersonDto> getPersons(
            @RequestParam("start") Integer start,
            @RequestParam("length") Integer length) {
        addTestDateToDb();
        logger.info("Method getPersons begins...");
        ArrayList<PersonDto> persons = new ArrayList<>();
        Collection<Person> findAllPersons = personService.findAllPersons(start,
                length);
        for (Person person : findAllPersons) {
            PersonDto personDto = buildPerson(person);
            persons.add(personDto);
        }

        return persons;
    }

    /**
     * Using {@link ResponseEntity}.
     *
     * @param personName
     * @param fromDate
     * @param toDate
     * @return
     */
    @RequestMapping(value = DemoGwtServiceEndpoint.PERSON_FILTER, method = RequestMethod.GET)
    public ResponseEntity<List<PersonDto>> filterPersons(
            @RequestParam("nameSuggestBox") String personName,
            @RequestParam(value = "fromDateTimePicker", required = false) Date fromDate,
            @RequestParam(value = "untilDateTimePicker", required = false) Date toDate) {
        logger.info("Method filterPersons begins...");

        List<PersonDto> persons = new ArrayList<>();
        PersonDto dto = new PersonDto();

        if (personName == null || personName.equals("")) {
            personName = ".. Empty ..";
        }

        dto.setName(personName);
        dto.setNickname("muster");
        persons.add(dto);

        dto = new PersonDto();
        dto.setName("Bauer_Filter");
        dto.setNickname("baur");

        persons.add(dto);

        return new ResponseEntity<List<PersonDto>>(persons, HttpStatus.OK);
    }

    public PersonDto createAddressFromPerson(PersonDto personDto, AddressDto addressDto) {
        // Mapping to entity
        Person person = new PersonImpl("muster");
        person.setName("Mustermann");

        Address address = new AddressImpl();

        Person createdPerson = null;
        try {
            createdPerson = personService.createAddressFromPerson(address, person);
        } catch (CreatePersonException e) {
            logger.error("Error: ", e);
        }

        // Mapping to DTO
        PersonDto createdPersonDto = buildPerson(createdPerson);

        return createdPersonDto;
    }

    private PersonDto buildPerson(Person person) {
        PersonDto personDto = new PersonDto();
        personDto.setName(person.getName());
        personDto.setNickname(person.getNickname());
        return personDto;
    }

    private void addTestDateToDb() {
        PersonImpl personImpl = new PersonImpl("muster");
        personImpl.setName("Mustermann");
        personRepository.save(personImpl);

        personImpl = new PersonImpl("baur");
        personImpl.setName("Bauer");
        personRepository.save(personImpl);
    }

}
