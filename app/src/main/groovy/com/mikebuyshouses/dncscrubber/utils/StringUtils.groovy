package com.mikebuyshouses.dncscrubber.utils

public final class StringUtils {
	public static final boolean IsNullOrEmpty(String string) {
		return string == null || string.length() == 0;
	}
}