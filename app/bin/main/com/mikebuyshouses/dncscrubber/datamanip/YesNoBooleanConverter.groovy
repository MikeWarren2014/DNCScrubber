package com.mikebuyshouses.dncscrubber.datamanip

import com.opencsv.bean.customconverter.ConverterLanguageToBoolean

class YesNoBooleanConverter extends ConverterLanguageToBoolean {
    @Override
    protected String getLocalizedTrue() {
        return "YES"
    }

    @Override
    protected String getLocalizedFalse() {
        return "NO"
    }

    @Override
    protected String[] getAllLocalizedTrueValues() {
        return [this.getLocalizedTrue(), 'Yes'] as String[];
    }

    @Override
    protected String[] getAllLocalizedFalseValues() {
        return [this.getLocalizedFalse(), 'No'] as String[];
    }

    public boolean convertStringToBoolean(String string) {
        return this.convert(string);
    }

    public String convertBooleanToString(boolean flag) {
        return this.convertToWrite(flag);
    }
}
