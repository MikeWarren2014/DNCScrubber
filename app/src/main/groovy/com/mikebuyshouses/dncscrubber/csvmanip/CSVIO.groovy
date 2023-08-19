package com.mikebuyshouses.dncscrubber.csvmanip

import com.mikebuyshouses.dncscrubber.models.BaseDataRowModel

class CSVIO {
    protected Class clazz;

    public CSVIO() {
        this.clazz = BaseDataRowModel.class;
    }

    public CSVIO(Class clazz) {
        this.clazz = clazz;
    }
}
