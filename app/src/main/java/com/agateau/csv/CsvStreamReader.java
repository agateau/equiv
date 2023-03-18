/*
Copyright 2016 Aurélien Gâteau

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.agateau.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Stream through a Csv file
 */
public class CsvStreamReader {
    private static final char CELL_SEPARATOR = ';';

    private final BufferedReader mReader;

    private int mRow = -1;
    private int mColumn = 0;
    private String mCurrentLine;
    // Current position inside mCurrentLine
    private int mIdx = 0;

    public CsvStreamReader(InputStream in) throws IOException {
        mReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
    }

    public boolean atRowEnd() {
        return mIdx == mCurrentLine.length();
    }

    /**
     * Loads the next row, returns true on success
     * @return True if there was a row to read
     * @throws IOException
     */
    public boolean loadNextRow() throws IOException {
        mCurrentLine = mReader.readLine();
        mIdx = 0;
        mColumn = 0;
        ++mRow;
        return mCurrentLine != null;
    }

    public String readCell() {
        int start = mIdx;
        int end = -1;
        while (mIdx < mCurrentLine.length()) {
            if (mCurrentLine.charAt(mIdx) == CELL_SEPARATOR) {
                end = mIdx;
                ++mColumn;
                ++mIdx; // Move to skip the separator
                break;
            } else {
                ++mIdx;
            }
        }
        if (end == -1) {
            end = mIdx;
        }
        return mCurrentLine.substring(start, end);
    }

    public float readFloatCell() {
        return Float.parseFloat(readCell());
    }

    public int readIntCell() {
        // Go through readFloatCell so that 1.0 can be read as 1
        return (int) readFloatCell();
    }

    public int getRow() {
        return mRow;
    }

    public int getColumn() {
        return mColumn;
    }
}
