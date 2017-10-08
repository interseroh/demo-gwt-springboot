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
package com.lofidewanto.demo.server.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Entity
public class PersonImpl implements Person {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	private String nickname;

	@OneToMany
	private Collection<AddressImpl> addresses = new ArrayList<>();

	private Boolean isInRetirement;

	public PersonImpl() {
	}

	public PersonImpl(String nickname) {
		super();
		this.nickname = nickname;
	}

	@Override
	public Integer calculateAge() {
		// The age depends on the nickname
		if (nickname.equalsIgnoreCase("Oldie")) {
			return 80;
		} else {
			return 10;
		}
	}

	@Override
	public Date[] run(byte[] content) {
		Date[] dates = { getDateFromString("25/11/2009"),
				getDateFromString("24/12/2009") };
		return dates;
	}

	private Date getDateFromString(String string) {
		return new Date();
	}

	@Override
	public void changeLastAddress(Address address, Boolean isLastOne) {
		if (!isLastOne) {
			Optional<AddressImpl> findFirst = addresses.stream().findFirst();
			Address addressFound = findFirst.get();
			addressFound.setStreet("New Street on the Blocks");
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Collection<Address> getAddresses() {
		ArrayList<Address> copyOfAddresses = new ArrayList<>(addresses);
		return copyOfAddresses;
	}

	@Override
	public void addAddress(Address address) {
		this.addresses.add((AddressImpl) address);
	}

	@Override
	public String getNickname() {
		return nickname;
	}

	@Override
	public Boolean isInRetirement() {
		return isInRetirement;
	}

}
