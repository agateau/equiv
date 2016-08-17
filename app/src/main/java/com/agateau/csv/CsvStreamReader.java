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

    private int mRow = 0;
    private int mColumn = 0;
    private String mCurrentLine;
    // Current position inside mCurrentLine
    private int mIdx = 0;

    public CsvStreamReader(InputStream in) throws IOException {
        mReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        readLine();
    }

    public boolean atDocumentEnd() {
        return mCurrentLine == null;
    }

    public boolean atRowEnd() {
        return mIdx == mCurrentLine.length();
    }

    public void readNextRow() throws IOException {
        readLine();
        ++mRow;
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

    private void readLine() throws IOException {
        mCurrentLine = mReader.readLine();
        mIdx = 0;
        mColumn = 0;
    }
}
