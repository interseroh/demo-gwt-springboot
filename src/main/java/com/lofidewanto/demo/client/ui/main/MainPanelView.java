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
package com.lofidewanto.demo.client.ui.main;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Singleton;

import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.NavbarLink;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.lofidewanto.demo.client.common.ErrorFormatter;
import com.lofidewanto.demo.client.common.LoadingMessagePopupPanel;
import com.lofidewanto.demo.client.common.Startable;
import com.lofidewanto.demo.client.common.WidgetName;
import com.lofidewanto.demo.client.ui.event.ChangeViewEvent;
import com.lofidewanto.demo.client.ui.person.PersonPanelView;

@Singleton
public class MainPanelView extends Composite {

	private static Logger logger = Logger
			.getLogger(MainPanelView.class.getName());

	interface MainPanelViewUiBinder extends UiBinder<Widget, MainPanelView> {
	}

	private static MainPanelViewUiBinder uiBinder = GWT
			.create(MainPanelViewUiBinder.class);

	interface MainPanelEventBinder extends EventBinder<MainPanelView> {
	}

	private final MainPanelEventBinder eventBinder = GWT
			.create(MainPanelEventBinder.class);

	final Map<WidgetName, Widget> widgets = new HashMap<>();

	private final LoadingMessagePopupPanel loadingMessagePopupPanel;

	private final ErrorFormatter errorFormatter;

	@UiField
	Column contentColumn;

	@UiField
	NavbarLink userNavbarLink;

	@UiField
	Image mainImage;

	@Inject
	public MainPanelView(EventBus eventBus, ErrorFormatter errorFormatter,
			LoadingMessagePopupPanel loadingMessagePopupPanel) {
		initWidget(uiBinder.createAndBindUi(this));
		eventBinder.bindEventHandlers(this, eventBus);
		this.errorFormatter = errorFormatter;
		this.loadingMessagePopupPanel = loadingMessagePopupPanel;

		logger.info("MainPanelView created...");
	}

	public void addWidget(WidgetName name, Widget widget) {
		this.widgets.put(name, widget);
		this.contentColumn.add(widget);
		widget.setVisible(false);
	}

	public void showWidget(WidgetName name) {
		hideAllWidgets();
		Widget widget = this.widgets.get(name);
		widget.setVisible(true);
	}

	private void hideAllWidgets() {
		final int count = this.contentColumn.getWidgetCount();
		for (int i = 0; i < count; i++) {
			this.contentColumn.getWidget(i).setVisible(false);
		}
	}

	void showAndStartWidget(WidgetName name) {
		hideAllWidgets();
		Widget widget = this.widgets.get(name);
		widget.setVisible(true);
		if (widget instanceof Startable) {
			Startable startable = (Startable) widget;
			startable.start();
		}
	}

	public void setContentAreaVisible(boolean visible) {
		this.contentColumn.setVisible(visible);
	}

	public void updatePersonPanelView() {
		showWidget(WidgetName.PERSONLIST);
		// Workaround for DataGrid, the same for GwtBootstrap3:
		// https://github.com/gwtbootstrap/gwt-bootstrap/issues/279
		// just call dataGrid.onResize() when the tab is clicked
		// dataGrid implements RequiresResize and must be placed on a
		// LayoutPanel or you must call onResize() yourself
		PersonPanelView personPanelView = (PersonPanelView) this.widgets
				.get(WidgetName.PERSONLIST);

		logger.info("Method updatePersonPanelView finished...");
	}

	@EventHandler
	void onChangeViewed(ChangeViewEvent event) {
		logger.info("ChangeViewEvent triggered: " + event.getWidgetName()
				+ " - Source: " + event.getSource());
		showAndStartWidget(event.getWidgetName());
	}

}
