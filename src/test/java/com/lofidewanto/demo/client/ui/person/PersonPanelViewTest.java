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

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.lofidewanto.demo.client.common.ErrorFormatter;
import com.lofidewanto.demo.client.common.LoadingMessagePopupPanel;
import com.lofidewanto.demo.client.domain.PersonClient;
import com.lofidewanto.demo.client.ui.main.MainPanelView;
import org.gwtbootstrap3.extras.select.client.ui.MultipleSelect;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit test with GwtMockito.
 * <p>
 * Created by dewanto on 29.05.2017.
 */
@RunWith(GwtMockitoTestRunner.class)
public class PersonPanelViewTest {

	private PersonPanelView view;

	@Mock
	private ErrorFormatter errorFormatter;

	@Mock
	private LoadingMessagePopupPanel loadingMessagePopupPanel;

	@Mock
	private EventBus eventBus;

	@Mock
	private PersonClient personClient;

	@Mock
	private MultipleSelect foodMultipleSelect;

	@Mock
	private Option mustardOption;

	@Before
	public void setUp() throws Exception {
		view = new PersonPanelView(eventBus, errorFormatter,
				loadingMessagePopupPanel, personClient);

		view = spy(view);
	}
	@Test
	public void testOnButtonClickNoSelectedFoodRuntimerTrue() throws Exception {
		// Prepare
		// Empty list no food selected
		List<Option> mockItems = new ArrayList<Option>();
		doReturn(mockItems).when(view.foodMultipleSelect).getSelectedItems();
		doReturn(true).when(view).runTimer();

		// CUT
		view.onButtonClick(null);

		// Asserts
		verify(view.filterButton, times(0)).setText(anyString());
		verify(view.filterButton, times(1)).setEnabled(true);
	}

	@Test
	public void testOnButtonClickNoSelectedFoodRuntimerFalse() throws Exception {
		// Prepare
		// Empty list no food selected
		List<Option> mockItems = new ArrayList<Option>();
		doReturn(mockItems).when(view.foodMultipleSelect).getSelectedItems();
		doReturn(false).when(view).runTimer();

		// CUT
		view.onButtonClick(null);

		// Asserts
		verify(view.filterButton, times(0)).setText(anyString());
		verify(view.filterButton, times(1)).setEnabled(false);
	}

	@Test
	public void testOnButtonClickMustardSelectedFood() throws Exception {
		// Prepare
		// Empty list no food selected
		List<Option> mockItems = new ArrayList<Option>();
		mockItems.add(mustardOption);

		doReturn(mockItems).when(view.foodMultipleSelect).getSelectedItems();
		doReturn("Mustard").when(mustardOption).getValue();

		// CUT
		view.onButtonClick(null);

		// Asserts
		verify(view.filterButton, times(1)).setText("Mustard");
	}

	@Test
	public void testOnButtonClickNotMustardSelectedFood() throws Exception {
		// Prepare
		// Empty list no food selected
		List<Option> mockItems = new ArrayList<Option>();
		mockItems.add(mustardOption);

		doReturn(mockItems).when(view.foodMultipleSelect).getSelectedItems();
		doReturn("Ketchup").when(mustardOption).getValue();

		// CUT
		view.onButtonClick(null);

		// Asserts
		verify(view.filterButton, times(0)).setText(anyString());
	}

}