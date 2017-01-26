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

import java.util.*;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.vaadin.client.widget.grid.CellReference;
import com.vaadin.client.widget.grid.CellStyleGenerator;
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

import com.lofidewanto.demo.client.utils.ColumnFactory;
import com.lofidewanto.demo.client.utils.SortableStaticDataSource;
import com.lofidewanto.demo.shared.PersonDto;
import com.vaadin.client.data.DataSource;
import com.vaadin.client.widget.grid.datasources.ListDataSource;
import com.vaadin.client.widget.grid.sort.SortEvent;
import com.vaadin.client.widget.grid.sort.SortHandler;
import com.vaadin.client.widget.grid.sort.SortOrder;
import com.vaadin.client.widgets.Grid;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.gwt.DataGrid;
import org.gwtbootstrap3.extras.bootbox.client.Bootbox;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.lofidewanto.demo.client.ui.person.PersonPanelView.Cols.AGE;


@Singleton
public class PersonPanelView extends Composite implements Startable {

    public static enum Cols{
        ID("Id"),
        NAME("Name"),
        NICKNAME("Nickname"),
        AGE("Age"),
        RETIRED("Retired");

        //EnumSet not supported by GWT js runtime.
        private static Set<Cols> numeric =
				new HashSet<Cols>(Arrays.asList(new Cols[] {ID, AGE}));

        private String name;
        private Cols(String name) {
            this.name = name;
        }

        public static boolean isNumericColumn(Cols col) {
			return col != null && numeric.contains(col);
		}

        public boolean equalsIgnoreCase(String s) {
            return s != null && this.name.toUpperCase().equals(s.toUpperCase());
        }

        public String toString() {
            return name;
        }

        public static Cols getFromString(String s) {
            for(Cols c :Cols.values()) {
                if (c.equalsIgnoreCase(s)) {
                    return c;
                }
            }
            return null;
        }
    }

    private static Logger logger = Logger
			.getLogger(PersonPanelView.class.getName());

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
    TabPane listTabPane;

	@UiField
    TabPane searchTabPane;

	@UiField
	DataGrid dataGrid1;


	@UiField
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

		initTableColumns(dataGrid1);
		initListDataProvider(dataGrid1);
		//initTableColumns(dataGrid2);
		//initFilterDataProvider( dataGrid2);
        initVGrid(vGrid);
		getPersons();

		logger.info("PersonPanelView created...");
	}

    private void initVGrid(Grid<VPerson> vGrid) {
	    vGrid.setVisible(false);
	    vGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        vGrid.setColumnResizeMode(ColumnResizeMode.ANIMATED);
        vGrid.setColumnReorderingAllowed(true);
        //vGrid.setFooterVisible(true);
        //vGrid.setHeaderVisible(true);
        vGrid.appendHeaderRow();
        vGrid.addFooterRowAt(0);

        vGrid.addColumn(ColumnFactory.createIntColumn(Cols.ID.toString(), 80, VPerson::getId));
        vGrid.addColumn(ColumnFactory.createStringColumn(Cols.NAME.toString(), 250, VPerson::getName));
        vGrid.addColumn(ColumnFactory.createIntColumn(AGE.toString(), 80, VPerson::getAge));
        vGrid.addColumn(ColumnFactory.createBooleanColumn(Cols.RETIRED.toString(), 100, VPerson::isRetired));
        vGrid.addColumn(ColumnFactory.createStringColumn(Cols.NICKNAME.toString(), 200, VPerson::getNickName));

        vGrid.addSortHandler(new SortHandler<VPerson>() {
            @Override
            public void sort(SortEvent<VPerson> sortEvent) {
                DataSource<VPerson> ds = vGrid.getDataSource();
                List<SortOrder> order = sortEvent.getOrder();
                if (!order.isEmpty()) {
                    SortOrder sortOrder = order.get(0);
                    SortOrder opposite = sortOrder.getOpposite();
                    String headerCaption = sortOrder.getColumn().getHeaderCaption();
                    List<VPerson> sortedData =
							SortableStaticDataSource.getSortedData(Cols.getFromString(headerCaption), sortOrder.getDirection());
                    ds = new ListDataSource<VPerson>(sortedData);
                    vGrid.setDataSource(ds);
                }
                logger.info("Vaadin Grid Sort Event");
            }
        });

        //Set text-align right for numeric colums via CellReference to column.
		vGrid.setCellStyleGenerator(new CellStyleGenerator() {
			@Override
			public String getStyle(CellReference cellReference) {
				Cols col = Cols.getFromString(cellReference.getColumn().getHeaderCaption());
				if (Cols.isNumericColumn(col)) {
					return "align-right";
				}
				return null;
			}
		});

        vGrid.setDataSource(new ListDataSource<VPerson>(SortableStaticDataSource.getSortedData(Cols.ID, SortDirection.ASCENDING)));
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
