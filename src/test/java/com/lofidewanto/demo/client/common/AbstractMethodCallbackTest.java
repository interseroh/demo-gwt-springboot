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
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AbstractMethodCallbackTest {

	private static final Logger logger = Logger
			.getLogger(AbstractMethodCallbackTest.class.getName());

	@Spy
	private AbstractMethodCallback abstractMethodCallback;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private Exception exception;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void checkTimeoutErrorWithJsonErrorText() throws Exception {
		final boolean isTimeout = abstractMethodCallback.checkTimeoutError(
				new Exception(
						AbstractMethodCallback.ERROR_TEXT_RESPONSE_WAS_NOT_A_VALID_JSON));

		assertEquals(false, isTimeout);
	}

	@Test
	public void checkTimeoutErrorWithTimeoutError() throws Exception {
		doNothing().when(abstractMethodCallback).redirectToLogin();

		final boolean isTimeout = abstractMethodCallback.checkTimeoutError(
				new Exception(AbstractMethodCallback.ERROR_TEXT_LOGIN_FORM));

		assertEquals(true, isTimeout);
	}

	@Test
	public void execute() {
		doNothing().when(abstractMethodCallback).showLoadingMessage();

		abstractMethodCallback.execute();

		verify(abstractMethodCallback, times(1)).executeCallService(any());
	}

	@Ignore
	@Test
	public void executeCallService() {
		doAnswer(invocationOnMock -> {
			logger.info(invocationOnMock.getMock().toString());

			((MethodCallback) invocationOnMock.getArguments()[0]).
					onSuccess(any(Method.class), anyObject());

			return null;
		}).when(abstractMethodCallback)
				.executeCallService(any(MethodCallback.class));

		abstractMethodCallback.executeCallService(any(MethodCallback.class));

		verify(abstractMethodCallback, times(1)).
				onSuccess(any(Method.class), anyObject());
	}

	@Test
	public void executeOnFailure() {
		doReturn(false).when(abstractMethodCallback)
				.checkTimeoutError(exception);
		doReturn(
				AbstractMethodCallback.ERROR_TEXT_RESPONSE_WAS_NOT_A_VALID_JSON)
				.when(exception).getMessage();
		doNothing().when(abstractMethodCallback).onFailure(any(), any());
		doNothing().when(abstractMethodCallback)
				.hideLoadingMessage(anyBoolean());

		abstractMethodCallback.executeOnFailure(null, exception);

		verify(abstractMethodCallback, times(1)).onFailure(any(), any());
	}
}