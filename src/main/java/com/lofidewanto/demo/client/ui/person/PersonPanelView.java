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
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
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
import com.lofidewanto.demo.shared.PersonDto;
import com.vaadin.client.widget.grid.datasources.ListDataSource;
import com.vaadin.client.widgets.Grid;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.themes.valoutil.BodyStyleName;
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
	//DataGrid dataGrid2;
    Grid<VPerson> vGrid;

	@UiField
	Pagination dataGridPagination1;

//	@UiField
//	Pagination dataGridPagination2;

	@Inject
	public PersonPanelView(EventBus eventbus, ErrorFormatter errorFormatter,
			LoadingMessagePopupPanel loadingMessagePopupPanel, PersonClient personClient) {
		initWidget(uiBinder.createAndBindUi(this));
		this.eventBus = eventbus;
		eventBinder.bindEventHandlers(this, eventBus);

		this.personClient = personClient;
		filterButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				logger.info("Button Filter is clicked!!!" + clickEvent.getNativeEvent().getString());
				eventBus.fireEvent(new FilterEvent());
				filterPerson();
			}
		});
		logger.info("PersonPanelView created...");

		initTableColumns(dataGrid1);
		initListDataProvider(dataGrid1);
		//initTableColumns(dataGrid2);
		//initFilterDataProvider( dataGrid2);
        initVGrid(vGrid);
		getPersons();
	}

    private void initVGrid(Grid<VPerson> vGrid) {
	    vGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        vGrid.addColumn(new Grid.Column<String, VPerson>("Name") {
            @Override
            public String getValue(VPerson row) {
                return row.getName();
            }
        });

        // A simple String column for the add-on summary/description
        vGrid.addColumn(new Grid.Column<Integer, VPerson>("Age") {
            @Override
            public Integer getValue(VPerson row) {
                return row.getAge();
            }
        });
        vGrid.getColumn(0).setWidth(150);
        vGrid.getColumn(1).setWidth(150);

        vGrid.setColumnResizeMode(ColumnResizeMode.ANIMATED);

        // Some dummy data
        ArrayList<VPerson> people = new ArrayList<VPerson>();
        for (int i = 0; i < 2; i++) {
            people.add(new VPerson("John", 12));
            people.add(new VPerson("Emma", 18));
            people.add(new VPerson("Jeff", 44));
            people.add(new VPerson("George", 78));
            people.add(new VPerson("Abraham", 114));
            people.add(new VPerson("Henrik", 32));
            people.add(new VPerson("Paul", 56));
            people.add(new VPerson("Biff", 34));
            people.add(new VPerson("Leo", 88));
        }
        vGrid.setDataSource(new ListDataSource<VPerson>(people));
        vGrid.setVisible(true);
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
			};
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
				//Bootbox.alert("The result is ok");
				//searchTab.setActive(false);
				//listTab.setActive(true);

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
