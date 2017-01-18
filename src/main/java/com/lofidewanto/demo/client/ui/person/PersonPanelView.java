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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.lofidewanto.demo.client.common.ErrorFormatter;
import com.lofidewanto.demo.client.common.LoadingMessagePopupPanel;
import com.lofidewanto.demo.client.common.Startable;
import com.lofidewanto.demo.client.domain.PersonClient;
import com.lofidewanto.demo.shared.PersonDto;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.SuggestBox;
import org.gwtbootstrap3.client.ui.TabListItem;
import org.gwtbootstrap3.client.ui.gwt.DataGrid;
import org.gwtbootstrap3.extras.bootbox.client.Bootbox;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.logging.Logger;

@Singleton
public class PersonPanelView extends Composite implements Startable {

	private static Logger logger = Logger
			.getLogger(PersonPanelView.class.getName());
	private static PersonPanelViewUiBinder uiBinder = GWT
			.create(PersonPanelViewUiBinder.class);
	@SuppressWarnings("unused")
	private final EventBus eventBus;
	private final PersonClient personClient;
	@UiField
	Button refreshButton;

	@UiField
	Button filterButton;

	@UiField
	SuggestBox nameSuggestBox;

	@UiField
	DateTimePicker fromDateTimePicker;

	@UiField
	DateTimePicker untilDateTimePicker;

	@UiField
	TabListItem listTab;

	@UiField
	TabListItem searchTab;

	@UiField
	DataGrid dataGrid1;

	@UiField
	DataGrid dataGrid2;


	@Inject
	public PersonPanelView(EventBus eventbus, ErrorFormatter errorFormatter,
			LoadingMessagePopupPanel loadingMessagePopupPanel,
			PersonClient personClient) {
		initWidget(uiBinder.createAndBindUi(this));
		this.eventBus = eventbus;
		this.personClient = personClient;
		filterButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				Bootbox.alert("Button Filter is Clicked!!!"+clickEvent.getNativeEvent().getString());
				filterPerson();
			}
		});
		logger.info("PersonPanelView created...");

		getPersons();
	}


	private void filterPerson(){
		MethodCallback<List<PersonDto>> filterCallBack=new
				MethodCallback<List<PersonDto> >(){

					@Override
					public void onFailure(Method method, Throwable throwable) {

						Bootbox.alert("Method call cack is ERROR :"+nameSuggestBox.getValue());
						throwable.printStackTrace();
						Bootbox.alert("ERROR :"+throwable.getLocalizedMessage());
					}

					@Override
					public void onSuccess(Method method, List<PersonDto>  persons) {
						Bootbox.alert("Method call back is OK :"+persons);
						listTab.setActive(true);
						searchTab.setActive(false);

						dataGrid2.setRowData(persons);
						dataGrid2.setVisible(true);
					}
				};

		personClient.filterPerson(nameSuggestBox.getValue(),
				fromDateTimePicker.getValue(),
				untilDateTimePicker.getValue(),
				filterCallBack );
	}

	private void getPersons() {
		MethodCallback<List<PersonDto>> callback = new MethodCallback<List<PersonDto>>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				logger.info("Error: " + exception);
				Bootbox.alert("Error: " + exception);
			}

			@Override
			public void onSuccess(Method method, List<PersonDto> response) {
				logger.info("The result is ok");
				Bootbox.alert("The result is ok");
				searchTab.setActive(true);
				listTab.setActive(false);
				dataGrid1.setRowData(response);
				dataGrid2.setVisible(true);
			}
		};

		logger.info("Get persons begins...");

		personClient.getPersons(0, 100, callback);

		logger.info("Get persons ends...");
	}

	@Override
	public void start() {

	}

	interface PersonPanelViewUiBinder
			extends UiBinder<Widget, PersonPanelView> {
	}

}
