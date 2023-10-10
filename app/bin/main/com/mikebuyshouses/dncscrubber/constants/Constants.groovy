package com.mikebuyshouses.dncscrubber.constants

public final class Constants {
	public static final String DncKeyPart = "DNC";
	public static final String ScoreKeyPart = "Score";
	public static final String TypeKeyPart = "Type";
	public static final String NumberKeyPart = "Number";
	public static final String DateKeyPart = "Last_Seen";

	public static final String AddressPart = "Address";
	public static final String CityPart = "City";
	public static final String StatePart = "State";
	public static final String ZipPart = "Zip";

	// TODO: these may need their own file
	public static final String InputMailingPart = "Input_Mailing";
	public static final String InputPropertyPart = "Input_Property";
	public static final String MailingPart = "Mailing";

	public static final String PhoneEntryRegex = "Phone\\d+_.+";
	public static final String AddressEntryRegex = ".*(Address|City|State|Zip)";

	// TODO: this should be in a properties file, that AWS Lambda can handle
	public static final String EfsMountPath = '/mnt/dncScrubber';
}