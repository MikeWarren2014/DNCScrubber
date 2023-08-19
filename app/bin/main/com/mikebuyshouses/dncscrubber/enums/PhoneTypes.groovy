package com.mikebuyshouses.dncscrubber.enums

public enum PhoneTypes {
	LandLine("Land Line"),
	Mobile("Mobile");
	
	final String textValue;

	private PhoneTypes(String textValue) { 
		this.textValue = textValue;
	}

	public static PhoneTypes FromTextValue(String textValue) { 
		for (PhoneTypes phoneType : PhoneTypes.values()) {
			if (phoneType.textValue.equals(textValue)) {
				return phoneType;
            }
		}

		throw new IllegalArgumentException("PhoneType for String '${textValue}' not supported");
	}
}