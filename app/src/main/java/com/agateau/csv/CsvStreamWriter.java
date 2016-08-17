package com.agateau.csv;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Write tabular data as a CSV file
 */
public class CsvStreamWriter {
    private static final char CELL_SEPARATOR = ';';

    private final OutputStream mOut;
    private boolean mFirstCell = true;

    public CsvStreamWriter(OutputStream out) {
        mOut = out;
    }

    public void writeCell(String value) throws IOException {
        if (mFirstCell) {
            mFirstCell = false;
        } else {
            mOut.write(CELL_SEPARATOR);
        }
        mOut.write(value.getBytes());
    }

    public void writeCell(int value) throws IOException {
        writeCell(Integer.toString(value));
    }

    public void writeCell(float value) throws IOException {
        writeCell(Float.toString(value));
    }

    public void endRow() throws IOException {
        mOut.write("\n".getBytes());
        mFirstCell = true;
    }
}
