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
package com.lofidewanto.demo.client.ui.person;

import java.util.logging.Logger;

import javax.inject.Singleton;

import org.gwtbootstrap3.client.ui.Button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lofidewanto.demo.client.common.ErrorFormatter;
import com.lofidewanto.demo.client.common.LoadingMessagePopupPanel;
import com.lofidewanto.demo.client.common.Startable;

@Singleton
public class PersonPanelView extends Composite implements Startable {

	private static Logger logger = Logger
			.getLogger(PersonPanelView.class.getName());

	private static PersonPanelViewUiBinder uiBinder = GWT
			.create(PersonPanelViewUiBinder.class);

	interface PersonPanelViewUiBinder
			extends UiBinder<Widget, PersonPanelView> {
	}

	private final EventBus eventBus;

	@UiField
	Button refreshButton;

	@Inject
	public PersonPanelView(EventBus eventbus, ErrorFormatter errorFormatter,
			LoadingMessagePopupPanel loadingMessagePopupPanel) {
		initWidget(uiBinder.createAndBindUi(this));
		this.eventBus = eventbus;

		logger.info("PersonPanelView created...");
	}

	@Override
	public void start() {

	}

}
