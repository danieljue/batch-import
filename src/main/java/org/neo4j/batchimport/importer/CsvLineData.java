package org.neo4j.batchimport.importer;

import java.io.IOException;
import java.io.Reader;

import org.neo4j.batchimport.utils.Config;

import au.com.bytecode.opencsv.CSVReader;

public class CsvLineData extends AbstractLineData {
    private final CSVReader csvReader;

    public CsvLineData(Reader reader, char delim, int offset, Config config) {
        super(offset, config);
        this.csvReader = new CSVReader(reader, delim,'"','\\',0,false,false);
        initHeaders(createHeaders(readRawRow()));
        createMapData(lineSize, offset);
    }

    @Override
    protected String[] readRawRow() {
        try {
            return csvReader.readNext();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean readLine() {
        final String[] row = readRawRow();
        if (row==null || row.length==0) return false;
        for (int i = 0; i < row.length && i < lineSize; i++) {
            String value = row[i];
            lineData[i] = value == null || value.isEmpty() ? null : convert(i, value);
        }
        return true;
    }

}
