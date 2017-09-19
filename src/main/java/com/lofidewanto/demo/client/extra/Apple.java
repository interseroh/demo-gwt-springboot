package com.lofidewanto.demo.client.extra;

import javax.inject.Singleton;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@Singleton
@JsType(namespace = JsPackage.GLOBAL, isNative = true)
public class Apple {

	public Apple() {
	}

	public Apple(String type) {
	}

	public native String getType();

	public native String getColor();

	public native String getInfo();
}
