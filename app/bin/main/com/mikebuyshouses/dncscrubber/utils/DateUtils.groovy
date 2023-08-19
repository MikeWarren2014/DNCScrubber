package com.mikebuyshouses.dncscrubber.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

public final class DateUtils {
    // SOURCE: Phind AI
    public static ZonedDateTime ParseDateTime(String dateTimeString) {
        List<String> formatStrings = [
            "yyyy-MM-dd'T'HH:mm:ss.SSSX",
            "yyyy-MM-dd'T'HH:mm:ssX",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd",
        ];

        for (String formatString : formatStrings) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatString);
                if (!dateTimeString.contains("T")) {
                    dateTimeString += "T00:00:00Z"; // Add default time and time zone
                }
                return ZonedDateTime.parse(dateTimeString, formatter);
            } catch (DateTimeParseException e) {
                // Ignore the exception and try the next format
            }
        }

        throw new IllegalArgumentException("Could not parse date time string: " + dateTimeString);
    }
}
