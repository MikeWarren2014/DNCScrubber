package com.mikebuyshouses.dncscrubber.datamanip

import com.mikebuyshouses.dncscrubber.models.AddressModel
import com.mikebuyshouses.dncscrubber.models.BaseDataRowModel
import org.apache.commons.collections4.MultiValuedMap

abstract class BaseChildAddressModelBuilder<ParentModel extends BaseDataRowModel> {
    protected ParentModel model;

    AddressModel propertyAddressModel,mailingAddressModel;

    BaseChildAddressModelBuilder(ParentModel model) {
        this.model = model
    }

    public abstract BaseChildAddressModelBuilder buildPropertyAddressModel();
    public abstract BaseChildAddressModelBuilder buildMailingAddressModel();

    public ParentModel build() {
        model.propertyAddressModel = propertyAddressModel;
        model.mailingAddressModel = mailingAddressModel;

        return model;
    }

}
