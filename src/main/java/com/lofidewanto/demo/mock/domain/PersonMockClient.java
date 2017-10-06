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
package com.lofidewanto.demo.mock.domain;

import com.lofidewanto.demo.shared.DemoGwtServiceEndpoint;
import com.lofidewanto.demo.shared.PersonDto;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@Singleton
public class PersonMockClient {

	public void getPersons(Integer start,
                    Integer length,
                    MethodCallback<List<PersonDto>> callback) {

    }

	public void filterPerson(String personName,
                      Date fromDate,
                      Date toDate,
                      MethodCallback<List<PersonDto>> callback) {

    }
}
