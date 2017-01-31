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
package com.lofidewanto.demo.client.extra;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.gwtbootstrap3.extras.bootbox.client.Bootbox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.lofidewanto.demo.client.ui.event.FilterEvent;
import com.lofidewanto.demo.client.ui.event.SearchEvent;

@Singleton
public class PersonUtil {

	private static Logger logger = Logger.getLogger(PersonUtil.class.getName());

	interface PersonUtilEventBinder extends EventBinder<PersonUtil> {
	}

	private final PersonUtilEventBinder eventBinder = GWT.create(PersonUtilEventBinder.class);

	@Inject
	public PersonUtil(EventBus eventBus) {
		logger.info("PersonUtil created...");
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@EventHandler
	public void onEvent(FilterEvent event) {
		logger.info("Get Event:" + event);
		Bootbox.alert("FilterEvent is received in PersonUtil...");
	}

	@EventHandler
	public void onEvent(SearchEvent event) {
		logger.info("Get Event:" + event);
		Bootbox.alert("SearchEvent is received in PersonUtil...");
	}

	public String sayHello() {
		return "Hello from PersonUtil";
	}

}
