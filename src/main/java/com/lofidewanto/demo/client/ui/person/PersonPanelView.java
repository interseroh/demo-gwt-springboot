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

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.lofidewanto.demo.client.common.ErrorFormatter;
import com.lofidewanto.demo.client.common.LoadingMessagePopupPanel;
import com.lofidewanto.demo.client.common.Startable;
import com.lofidewanto.demo.client.domain.PersonClient;
import com.lofidewanto.demo.shared.PersonDto;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.gwt.DataGrid;
import org.gwtbootstrap3.extras.bootbox.client.Bootbox;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;

import javax.ws.rs.QueryParam;
import java.util.ArrayList;
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

	@UiField
	Pagination dataGridPagination1;

	@UiField
	Pagination dataGridPagination2;

	ListDataProvider<PersonDto> dataProviderList;

	ListDataProvider<PersonDto> dataProviderFilter;


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
				Bootbox.alert("Button Filter is clicked!!!"+clickEvent.getNativeEvent().getString());
				filterPerson();
			}
		});
		logger.info("PersonPanelView created...");


		initTableColumns(dataGrid1);
		initTableColumns(dataGrid2);
		initListDataProvider(dataGrid1);
		initFilterDataProvider( dataGrid2);
		getPersons();
	}


	private void initTableColumns(DataGrid dataGrid) {
		dataGrid.setWidth("100%");
		dataGrid.setHeight("300px");
		dataGrid.setAutoHeaderRefreshDisabled(true);
		// Nick name.
		Column<PersonDto, String> nicknameColumn =
				new Column<PersonDto, String>(new TextCell()) {
					@Override
					public String getValue(PersonDto object) {
						return object.getNickname();
					}
				};
		dataGrid.addColumn(nicknameColumn, "Nickname");
		dataGrid.setColumnWidth(nicknameColumn, 40, Style.Unit.PCT);

		// Nick name.
		Column<PersonDto, String> nameColumn =
				new Column<PersonDto, String>(new TextCell()) {
					@Override
					public String getValue(PersonDto object) {
						return object.getName();
					}
				};
		dataGrid.addColumn(nameColumn, "Name");
		dataGrid.setColumnWidth(nameColumn, 40, Style.Unit.PCT);

		// Retired
		Column<PersonDto, Boolean> isRetiredColumn =
				new Column<PersonDto, Boolean>(new CheckboxCell(true, false)) {
					@Override
					public Boolean getValue(PersonDto object) {
						if(object.isInRetirement()==null){
							return false;
						}else {
							return object.isInRetirement();
						}
					};
				} ;
		dataGrid.addColumn(isRetiredColumn, "Retired");
		dataGrid.setColumnWidth(isRetiredColumn, 20, Style.Unit.PCT);


	}

	/**
	 *
	 * @param dataGrid
	 */
	private void initListDataProvider(DataGrid dataGrid) {
		dataProviderList = new ListDataProvider<>(new ArrayList<PersonDto>(0));
		dataProviderList.addDataDisplay(dataGrid);

		// Set the message to display when the table is empty.
		dataGrid.setEmptyTableWidget(new Label("No Data"));

	}

	/**
	 *
	 * @param dataGrid
	 */
	private void initFilterDataProvider(DataGrid dataGrid) {
		dataProviderFilter = new ListDataProvider<>(new ArrayList<PersonDto>(0));
		dataProviderFilter.addDataDisplay(dataGrid);

		// Set the message to display when the table is empty.
		dataGrid.setEmptyTableWidget(new Label("No Data"));

	}

	private void filterPerson(){
		MethodCallback<List<PersonDto>> filterCallBack=new
				MethodCallback<List<PersonDto> >(){

					@Override
					public void onFailure(Method method, Throwable throwable) {
						Bootbox.alert("Method call back has ERROR:"+throwable.getLocalizedMessage());
						throwable.printStackTrace();
					}

					@Override
					public void onSuccess(Method method, List<PersonDto>  persons) {
						Bootbox.alert("Method call back is OK :"+persons.get(0));
						listTab.setActive(false);
						searchTab.setActive(true);
						refreshGrid(persons,dataProviderFilter);


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
			public void onSuccess(Method method, List<PersonDto> persons) {
				logger.info("The result is ok");
				Bootbox.alert("The result is ok");
				searchTab.setActive(false);
				listTab.setActive(true);

				refreshGrid(persons,dataProviderList);
			}
		};

		logger.info("Get persons begins...");

		personClient.getPersons(0, 100, callback);

		logger.info("Get persons ends...");
	}

	/**
	 *
	 * @param personDtos
	 * @param dataProvider
	 */
	private void refreshGrid(List<PersonDto> personDtos, ListDataProvider<PersonDto> dataProvider) {
		for(PersonDto p : personDtos) {
			logger.info(p.getNickname() + " " + p.isInRetirement());
		}
		dataProvider.setList(personDtos);

	}


	@Override
	public void start() {

	}

	interface PersonPanelViewUiBinder
			extends UiBinder<Widget, PersonPanelView> {
	}

}
