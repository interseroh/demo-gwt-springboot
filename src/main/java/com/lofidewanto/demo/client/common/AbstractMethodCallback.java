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
package com.lofidewanto.demo.client.common;

import java.util.logging.Logger;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;

public abstract class AbstractMethodCallback<T> implements MethodCallback<T> {

	private static final Logger logger = Logger
			.getLogger(AbstractMethodCallback.class.getName());

	private static final int Z_INDEX = 101;

	public static final String AUTH_ERROR_URL = "login?autherror";

	private final ErrorFormatter errorFormatter;

	private final LoadingMessagePopupPanel loadingMessagePopupPanel;

	private boolean isHidingMessagePopupPanel = true;

	private boolean isPopupCentered = true;

	protected abstract void callService(MethodCallback<T> methodCallback);

	public AbstractMethodCallback(ErrorFormatter errorFormatter,
			LoadingMessagePopupPanel loadingMessagePopupPanel) {
		super();
		this.errorFormatter = errorFormatter;
		this.loadingMessagePopupPanel = loadingMessagePopupPanel;

		setZindex(loadingMessagePopupPanel);
	}

	private void setZindex(LoadingMessagePopupPanel loadingMessagePopupPanel) {
		// This brings the popup panel to front
		// http://stackoverflow.com/questions/10068633/popuppanel-show-up-beneath-the-widget
		DOM.setIntStyleAttribute(loadingMessagePopupPanel.getElement(),
				"zIndex", Z_INDEX);
	}

	public AbstractMethodCallback(ErrorFormatter errorFormatter,
			LoadingMessagePopupPanel loadingMessagePopupPanel,
			boolean isHidingMessagePopupPanel) {
		this(errorFormatter, loadingMessagePopupPanel);
		this.isHidingMessagePopupPanel = isHidingMessagePopupPanel;
	}

	public AbstractMethodCallback(ErrorFormatter errorFormatter,
			LoadingMessagePopupPanel loadingMessagePopupPanel,
			boolean isHidingMessagePopupPanel, int popupPosLeft,
			int popupPosTop) {
		this(errorFormatter, loadingMessagePopupPanel,
				isHidingMessagePopupPanel);

		loadingMessagePopupPanel.setPopupPosition(popupPosLeft, popupPosTop);
		this.isPopupCentered = false;
	}

	@Override
	public void onFailure(Method method, Throwable exception) {
		String className = method.getClass().getSimpleName();
		String methodName = method.toString();
		errorFormatter.showError(exception,
				className + "." + methodName + ": onFailure: ");
	}

	private void hideLoadingMessage(boolean isHiding) {
		if (isHiding) {
			loadingMessagePopupPanel.setGlassEnabled(false);
			loadingMessagePopupPanel.hide();
		}
	}

	private void showLoadingMessage() {
		loadingMessagePopupPanel.setGlassEnabled(true);

		if (isPopupCentered) {
			loadingMessagePopupPanel.center();
		}

		loadingMessagePopupPanel.show();
	}

	public void execute() {
		showLoadingMessage();

		// Execute
		callService(new MethodCallback<T>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				boolean isTimeoutError = checkTimeoutError(exception);
				if (!isTimeoutError) {
					AbstractMethodCallback.this.onFailure(method, exception);
				}
				hideLoadingMessage(true);
			}

			@Override
			public void onSuccess(Method method, T response) {
				hideLoadingMessage(isHidingMessagePopupPanel);
				AbstractMethodCallback.this.onSuccess(method, response);
			}
		});
	}

	boolean checkTimeoutError(Throwable exception) {
		// Check if we found timeout error
		String exceptionText = exception.getMessage();
		logger.info("Check login error: " + exceptionText);

		if (exceptionText.contains("loginForm")
				|| exceptionText.contains("Response was NOT a valid JSON")) {
			// Yes, the result is a login form, so the timeout
			logger.info("Session timeout, neu einloggen.");
			redirectToLogin();
			return true;
		}

		return false;
	}

	void redirectToLogin() {
		Window.Location.assign(AUTH_ERROR_URL);
	}
}
