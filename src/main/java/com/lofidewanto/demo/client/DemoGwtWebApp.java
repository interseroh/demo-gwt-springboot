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
package com.lofidewanto.demo.client;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.gwtbootstrap3.extras.bootbox.client.Bootbox;
import org.gwtbootstrap3.extras.bootbox.client.options.BootboxLocale;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.MetaElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.lofidewanto.demo.client.common.WidgetName;
import com.lofidewanto.demo.client.ui.main.MainPanelView;
import com.lofidewanto.demo.client.ui.person.PersonPanelView;

@Singleton
public class DemoGwtWebApp {

    private static Logger logger = Logger
			.getLogger(DemoGwtWebApp.class.getName());

	private static final String JQUERY_UI_URL = "https://code.jquery.com/ui/1.11.4/jquery-ui.js";

    private static final String MYFUNCTION_URL = "myfunction.js";

	private static final String HOST_LOADING_IMAGE = "loadingImage";

	private static final String HOST_MAIN_PANEL = "mainPanel";

	private static final String HISTORY_MAIN = "main";

	private static final String LOCALE = "de_DE";

    private final MainPanelView mainPanelView;

    private final PersonPanelView personPanelView;

    @Inject
	public DemoGwtWebApp(MainPanelView mainPanelView,
			PersonPanelView personPanelView) {
		logger.info("DemoGwtWebApp create...");

        this.mainPanelView = mainPanelView;
		this.personPanelView = personPanelView;

        injectJqueryScript();
        injectMyFunctionScript();
    }

    private void injectMyFunctionScript() {
        ScriptInjector.fromUrl(MYFUNCTION_URL).setCallback(new Callback<Void, Exception>() {
            @Override
            public void onFailure(Exception reason) {
                logger.info("Script load failed Info: " + reason);
            }

            @Override
            public void onSuccess(Void result) {
                logger.info("MyFunction loaded successful!");
            }

        }).setRemoveTag(true).setWindow(ScriptInjector.TOP_WINDOW).inject();
    }

    private void injectJqueryScript() {
        // Workaround: https://goo.gl/1OrFqj
        ScriptInjector.fromUrl(JQUERY_UI_URL).setCallback(new Callback<Void, Exception>() {
            @Override
            public void onFailure(Exception reason) {
                logger.info("Script load failed Info: " + reason);
            }

            @Override
            public void onSuccess(Void result) {
                logger.info("JQuery for Select loaded successful!");

                init();
            }

        }).setRemoveTag(true).setWindow(ScriptInjector.TOP_WINDOW).inject();
    }

    private void init() {
        logger.info("Init...");

		addMetaElements();

		// Disable Back Button
		setupHistory();
		setupBootbox();

		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				logger.info("Error on Async!");
			}

			@Override
			public void onSuccess() {
				createViews();
				removeLoadingImage();

                // Example calling JavaScript with JSNI - old style
                // alert("Lofi Old Style...");
			}
		});
	}

	// @formatter:off
	public native void alert(String msg) /*-{
	    $wnd.alert(msg);

        var myJavaScriptHello = new MyJavaScriptHello("Lofi");
        console.log(myJavaScriptHello.click());
	}-*/;
	// @formatter:on

	private void addMetaElements() {
		logger.info("Add viewport");
		MetaElement element = Document.get().createMetaElement();
		element.setName("viewport");
		element.setContent("width=device-width, initial-scale=1.0");

		NodeList<Element> node = Document.get().getElementsByTagName("head");
		Element elementHead = node.getItem(0);
		elementHead.appendChild(element);
	}

	private void removeLoadingImage() {
		// Remove loadingImage from Host HTML page
        Element body = Document.get().getBody();
        Element loading = Document.get().getElementById(HOST_LOADING_IMAGE);

        if(body.isOrHasChild(loading)){
            loading.removeFromParent();
        }
	}

	private void setupHistory() {
		History.newItem(HISTORY_MAIN);

		// Add history listener
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String token = event.getValue();
				if (!token.equals(HISTORY_MAIN)) {
					History.newItem(HISTORY_MAIN);
				}
			}
		});
	}

	private void createViews() {
		// Views
		logger.info("Create Views begins...");

		mainPanelView.setContentAreaVisible(false);
		mainPanelView.addWidget(WidgetName.PERSONLIST,
				personPanelView);

		mainPanelView.setContentAreaVisible(true);
		mainPanelView.updatePersonPanelView();

		RootPanel.get(HOST_MAIN_PANEL).add(mainPanelView);

		logger.info("Create Views ends...");
	}

	private void setupBootbox() {
		if (LocaleInfo.getCurrentLocale().getLocaleName().equals(LOCALE)) {
			logger.info(
					"Locale: " + LocaleInfo.getCurrentLocale().getLocaleName());
			Bootbox.setLocale(BootboxLocale.DE);
		}
	}

}
