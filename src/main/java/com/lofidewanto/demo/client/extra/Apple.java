package com.lofidewanto.demo.client.extra;

import javax.inject.Singleton;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@Singleton
@JsType(namespace = JsPackage.GLOBAL, name = "Apple", isNative = true)
public class Apple {

	public int x;

	public int y;

	public native int sum();
}
