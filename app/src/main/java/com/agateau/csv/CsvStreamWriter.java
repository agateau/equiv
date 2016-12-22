/*
Copyright 2016 Aurélien Gâteau

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
