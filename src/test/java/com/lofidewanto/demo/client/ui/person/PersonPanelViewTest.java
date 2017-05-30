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

import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import com.google.gwt.event.shared.EventBus;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.lofidewanto.demo.client.common.ErrorFormatter;
import com.lofidewanto.demo.client.common.LoadingMessagePopupPanel;
import com.lofidewanto.demo.client.domain.PersonClient;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit test with GwtMockito.
 * <p>
 * Created by dewanto on 29.05.2017.
 */
@RunWith(GwtMockitoTestRunner.class)
public class PersonPanelViewTest {

	// CUT: Class Under Test
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
	private Option mustardOption;

	@Before
	public void setUp() throws Exception {
		// CUT create
		view = new PersonPanelView(eventBus, errorFormatter,
				loadingMessagePopupPanel, personClient);

		// Create a spy on the view to mock view.runtimer()
		view = spy(view);
	}

	/**
	 * Partial Mocking using Spy in the method: runTimerRefreshButton.
	 *
	 * @throws Exception
	 */
	@Test
	public void testOnButtonClickNoSelectedFoodRuntimerTrue() throws Exception {
		// Prepare
		// Empty list no food selected
		List<Option> mockItems = new ArrayList<Option>();
		doReturn(mockItems).when(view.foodMultipleSelect).getSelectedItems();
		// Partial mocking with spy
		doReturn(true).when(view).runTimerRefreshButton();

		// CUT
		view.onButtonClick(null);

		// Asserts for filterButton
		verify(view.filterButton, times(1)).setEnabled(false);
		verify(view.filterButton, times(0)).setText(anyString());
	}

	/**
	 * Partial Mocking using Spy in the method: runTimerRefreshButton.
	 *
	 * @throws Exception
	 */
	@Test
	public void testOnButtonClickNoSelectedFoodRuntimerFalse()
			throws Exception {
		// Prepare
		// Empty list no food selected
		List<Option> mockItems = new ArrayList<Option>();
		doReturn(mockItems).when(view.foodMultipleSelect).getSelectedItems();
		// Partial mocking with spy
		doReturn(false).when(view).runTimerRefreshButton();

		// CUT
		view.onButtonClick(null);

		// Asserts for filterButton
		verify(view.filterButton, times(2)).setEnabled(false);
		verify(view.filterButton, times(0)).setText(anyString());
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

	/**
	 * How to simulate GWT Timer class. Important: the GWT Timer class won't be
	 * started as a timer since it just run the timer in JavaScript.
	 *
	 * @throws Exception
	 */
	@Test
	public void testOnButtonClickNotMustardSelectedFoodWithRuntimerRefreshButton()
			throws Exception {
		// Prepare
		// Empty list no food selected
		List<Option> mockItems = new ArrayList<Option>();
		mockItems.add(mustardOption);

		doReturn(mockItems).when(view.foodMultipleSelect).getSelectedItems();
		doReturn("Ketchup").when(mustardOption).getValue();

		// CUT
		view.onButtonClick(null);
		// We simulate that the timer has run
		view.runTimerRefreshButtonExecutor();

		// Asserts
		verify(view.filterButton, times(1)).setEnabled(false);
		verify(view.filterButton, times(1)).setText("Filter");
		verify(view.refreshButton, times(1)).setEnabled(true);
	}

	/**
	 * How to simulate GWT Timer class. Important: the GWT Timer class won't be
	 * started as a timer since it just run the timer in JavaScript.
	 *
	 * @throws Exception
	 */
	@Test
	public void testOnButtonClickMustardSelectedFoodWithRuntimerRefreshButton()
			throws Exception {
		// Prepare
		// Empty list no food selected
		List<Option> mockItems = new ArrayList<Option>();
		mockItems.add(mustardOption);

		doReturn(mockItems).when(view.foodMultipleSelect).getSelectedItems();
		doReturn("Mustard").when(mustardOption).getValue();

		// CUT
		view.onButtonClick(null);
		// We simulate that the timer has run
		view.runTimerRefreshButtonExecutor();

		// Asserts
		verify(view.filterButton, times(1)).setEnabled(false);
		verify(view.refreshButton, times(1)).setEnabled(false);
		verify(view.filterButton, times(1)).setText("Mustard");
		verify(view.filterButton, times(1)).setText("Filter");
		verify(view.refreshButton, times(1)).setEnabled(true);
		verify(view.filterButton, times(1)).setEnabled(true);
	}
}