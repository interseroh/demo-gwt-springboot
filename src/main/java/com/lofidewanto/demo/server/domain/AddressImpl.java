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
import java.util.Collection;

@Entity
public class AddressImpl implements Address {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String street;

	@ManyToOne
	private PersonImpl person;

	private AddressType addressType;

	private boolean isOld;

	@Transient
	private Collection<String> newAddresses;

	@Override
	public Collection<String> getNewAddresses() {
		return newAddresses;
	}

	@Override
	public String getStreet() {
		return street;
	}

	@Override
	public void setStreet(String street) {
		this.street = street;
	}

	@Override
	public Person getPerson() {
		return person;
	}

	@Override
	public void setPerson(Person person) {
		this.person = (PersonImpl) person;
	}

	@Override
	public AddressType getAddressType() {
		return addressType;
	}

	@Override
	public void setAddressType(AddressType addressType) {
		this.addressType = addressType;
	}

	@Override
	public boolean isOld() {
		return isOld;
	}

	@Override
	public void setOld(boolean isOld) {
		this.isOld = isOld;
	}

}
