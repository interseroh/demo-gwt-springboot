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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Pagination;
import org.gwtbootstrap3.client.ui.SuggestBox;
import org.gwtbootstrap3.client.ui.TabListItem;
import org.gwtbootstrap3.client.ui.gwt.DataGrid;
import org.gwtbootstrap3.extras.bootbox.client.Bootbox;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.lofidewanto.demo.client.common.ErrorFormatter;
import com.lofidewanto.demo.client.common.LoadingMessagePopupPanel;
import com.lofidewanto.demo.client.common.Startable;
import com.lofidewanto.demo.client.domain.PersonClient;
import com.lofidewanto.demo.client.extra.PersonUtil;
import com.lofidewanto.demo.client.ui.event.FilterEvent;
import com.lofidewanto.demo.client.ui.event.SearchEvent;
import com.lofidewanto.demo.shared.PersonDto;
import org.gwtbootstrap3.extras.select.client.ui.MultipleSelect;
import org.gwtbootstrap3.extras.select.client.ui.Option;

@Singleton
public class PersonPanelView extends Composite implements Startable {

	private static Logger logger = Logger.getLogger(PersonPanelView.class.getName());

	interface PersonPanelViewUiBinder extends UiBinder<Widget, PersonPanelView> {
	}

	private static PersonPanelViewUiBinder uiBinder = GWT.create(PersonPanelViewUiBinder.class);

	interface PersonPanelEventBinder extends EventBinder<PersonPanelView> {
	}

	private final PersonPanelEventBinder eventBinder = GWT.create(PersonPanelEventBinder.class);

	private final EventBus eventBus;

	private final PersonClient personClient;

	private ListDataProvider<PersonDto> dataProviderList;

	private ListDataProvider<PersonDto> dataProviderFilter;

	@Inject
	private PersonUtil personUtil;

	@UiField
	Button refreshButton;

	@UiField
	Button searchButton;

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
	DataGrid<PersonDto> dataGrid1;

	@UiField
	DataGrid<PersonDto> dataGrid2;

	@UiField
	Pagination dataGridPagination1;

	@UiField
	Pagination dataGridPagination2;

	@UiField
	MultipleSelect foodMultipleSelect;

	@Inject
	public PersonPanelView(EventBus eventbus, ErrorFormatter errorFormatter,
			LoadingMessagePopupPanel loadingMessagePopupPanel, PersonClient personClient) {
		initWidget(uiBinder.createAndBindUi(this));
		this.eventBus = eventbus;
		eventBinder.bindEventHandlers(this, eventBus);

		this.personClient = personClient;

		init();
	}

	private void init() {
		// Standard event handling
		filterButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				logger.info("Click Detected by Simple Click Event");
				logger.info("Button Filter is clicked!!!" + clickEvent.getNativeEvent().getString());
				eventBus.fireEvent(new SearchEvent());
				filterPerson();
			}
		});
		logger.info("PersonPanelView created...");

		initTableColumns(dataGrid1);
		initTableColumns(dataGrid2);
		initListDataProvider(dataGrid1);
		initFilterDataProvider(dataGrid2);

		getPersons();

		// Event handling with Lambda
		searchButton.addClickHandler(clickHandler -> searchButtonClick("Click Detected by Lambda Listener"));
	}

	private void searchButtonClick(String message) {
		logger.info(message);
		searchButton.state().loading();

		new Timer() {
			@Override
			public void run() {
				searchButton.state().reset();
			}
		}.schedule(5000);
	}

	@UiHandler("refreshButton")
	public void onButtonClick(final ClickEvent event) {
		// Event handling in GWT UiBinder
		logger.info("Click Detected by GWT UiBinder");

		refreshButton.setEnabled(false);

		List<Option> items = foodMultipleSelect.getSelectedItems();

		if (items.size() != 0 && items.get(0).getValue().equalsIgnoreCase("Mustard")) {
			logger.info("We have mustard...");
			filterButton.setText("Mustard");
		}

		boolean result = runTimer();
		filterButton.setEnabled(result);
	}

	boolean runTimer() {
		// This is a GWT timer implementation so you won't see this running in Java!
		new Timer() {
			@Override
			public void run() {
				refreshButton.setEnabled(true);
				logger.info("Enable the button again...");
				filterButton.setText("Filter");
			}
		}.schedule(5000);

		return true;
	}

	private void initTableColumns(DataGrid<PersonDto> dataGrid) {
		dataGrid.setWidth("100%");
		dataGrid.setHeight("300px");
		dataGrid.setAutoHeaderRefreshDisabled(true);
		// Nick name.
		Column<PersonDto, String> nicknameColumn = new Column<PersonDto, String>(new TextCell()) {
			@Override
			public String getValue(PersonDto object) {
				return object.getNickname();
			}
		};
		dataGrid.addColumn(nicknameColumn, "Nickname");
		dataGrid.setColumnWidth(nicknameColumn, 40, Style.Unit.PCT);

		// Nick name.
		Column<PersonDto, String> nameColumn = new Column<PersonDto, String>(new TextCell()) {
			@Override
			public String getValue(PersonDto object) {
				return object.getName();
			}
		};
		dataGrid.addColumn(nameColumn, "Name");
		dataGrid.setColumnWidth(nameColumn, 40, Style.Unit.PCT);

		// Retired
		Column<PersonDto, Boolean> isRetiredColumn = new Column<PersonDto, Boolean>(new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(PersonDto object) {
				if (object.isInRetirement() == null) {
					return false;
				} else {
					return object.isInRetirement();
				}
			}
		};
		dataGrid.addColumn(isRetiredColumn, "Retired");
		dataGrid.setColumnWidth(isRetiredColumn, 20, Style.Unit.PCT);
	}

	private void initListDataProvider(DataGrid<PersonDto> dataGrid) {
		dataProviderList = new ListDataProvider<>(new ArrayList<PersonDto>(0));
		dataProviderList.addDataDisplay(dataGrid);

		// Set the message to display when the table is empty.
		dataGrid.setEmptyTableWidget(new Label("No Data"));

	}

	private void initFilterDataProvider(DataGrid<PersonDto> dataGrid) {
		dataProviderFilter = new ListDataProvider<>(new ArrayList<PersonDto>(0));
		dataProviderFilter.addDataDisplay(dataGrid);

		// Set the message to display when the table is empty.
		dataGrid.setEmptyTableWidget(new Label("No Data"));

	}

	private void filterPerson() {
		MethodCallback<List<PersonDto>> filterCallBack = new MethodCallback<List<PersonDto>>() {
			@Override
			public void onFailure(Method method, Throwable throwable) {
				Bootbox.alert("Method call back has ERROR:" + throwable.getLocalizedMessage());
				throwable.printStackTrace();
			}

			@Override
			public void onSuccess(Method method, List<PersonDto> persons) {
				Bootbox.alert("Method call back is OK .:" + persons.get(0));
				listTab.setActive(false);
				searchTab.setActive(true);
				refreshGrid(persons, dataProviderFilter);
			}
		};

		personClient.filterPerson(nameSuggestBox.getValue(), fromDateTimePicker.getValue(),
				untilDateTimePicker.getValue(), filterCallBack);
	}

	private void getPersons() {
		MethodCallback<List<PersonDto>> callback = new MethodCallback<List<PersonDto>>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				logger.info("Error: " + exception);
				Bootbox.alert("Error: " + exception);
			}

			@Override
			public void onSuccess(Method method, List<PersonDto> persons) {
				logger.info("The result is ok");
				Bootbox.alert("The result is ok");
				searchTab.setActive(false);
				listTab.setActive(true);

				refreshGrid(persons, dataProviderList);
			}
		};

		logger.info("Get persons begins...");

		personClient.getPersons(0, 100, callback);

		logger.info("Get persons ends...");
	}

	private void refreshGrid(List<PersonDto> personDtos, ListDataProvider<PersonDto> dataProvider) {
		for (PersonDto p : personDtos) {
			logger.info(p.getNickname() + " " + p.isInRetirement());
		}

		dataProvider.setList(personDtos);
	}

	@Override
	public void start() {
		personUtil.sayHello();
	}

	@EventHandler
	public void onEvent(FilterEvent event) {
		logger.info("Get Event:" + event);
		Bootbox.alert("FilterEvent is received in PersonPanelView...");
	}
}
