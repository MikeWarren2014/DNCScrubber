package com.mikebuyshouses.dncscrubber.utils

public final class NumberUtils { 
	public static int ParseInt(String string) { 
		if ((string == null) || (string.length() == 0))
			return 0;
		return Integer.parseInt(string);
	}

	public static int ExtractNumber(String string) {
		return string
			.split('_')[0]
			.replaceAll(/[^0-9]/, '')
			.toInteger();
	}
}