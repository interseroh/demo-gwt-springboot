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

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractMethodCallbackTest {

	@Spy
	private AbstractMethodCallback abstractMethodCallback;

	private LoadingMessagePopupPanel loadingMessagePopupPanel;

	private ErrorFormatter errorFormatter;

	@Before
	public void setUp() throws Exception {
		abstractMethodCallback = new AbstractMethodCallback() {
			@Override
			protected void callService(MethodCallback methodCallback) {
			}

			@Override
			public void onSuccess(Method method, Object response) {
			}
		};
	}

	@Test
	public void callService() throws Exception {
	}

	@Test
	public void checkTimeoutError() throws Exception {
		abstractMethodCallback.checkTimeoutError(new Exception(
				AbstractMethodCallback.ERROR_TEXT_RESPONSE_WAS_NOT_A_VALID_JSON));
	}

}