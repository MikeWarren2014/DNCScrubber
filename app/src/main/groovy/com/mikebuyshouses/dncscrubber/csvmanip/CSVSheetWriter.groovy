package com.mikebuyshouses.dncscrubber.csvmanip


import com.mikebuyshouses.dncscrubber.filemanip.DataWriter
import com.mikebuyshouses.dncscrubber.models.BaseDataRowModel
import com.opencsv.CSVWriter
import com.opencsv.bean.ColumnPositionMappingStrategy
import com.opencsv.bean.StatefulBeanToCsv
import com.opencsv.bean.StatefulBeanToCsvBuilder
import groovy.transform.InheritConstructors

@InheritConstructors
public class CSVSheetWriter extends CSVIO implements DataWriter {
    @Override
     public void writeToFile(List<BaseDataRowModel> dataRowModels, File file) {
        new CSVWriter(new FileWriter(file)).withCloseable { CSVWriter csvWriter ->
            // Create StatefulBeanToCsv
            StatefulBeanToCsv<BaseDataRowModel> beanToCsv = new StatefulBeanToCsvBuilder<BaseDataRowModel>(csvWriter)
//                .withMappingStrategy(new ColumnPositionMappingStrategy<BaseDataRowModel>())
                .build();

            // Write data to CSV file
            beanToCsv.write(dataRowModels);
        }
    }


}
