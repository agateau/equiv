package com.greenyetilab.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Stream through a Csv file
 */
public class CsvStreamReader {
    private final InputStream mStream;
    private final Listener mListener;
    private static final char CELL_SEPARATOR = ';';

    public interface Listener {
        void onCell(int row, int column, String value);
        void onStartRow(int row);
        void onEndRow(int row);
    }

    public CsvStreamReader(InputStream in, Listener listener) {
        mStream = in;
        mListener = listener;
    }

    public void read() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(mStream, "UTF-8"));
        int row = 0;
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            mListener.onStartRow(row);
            int start = 0;
            int idx = 0;
            int col = 0;
            while (idx < line.length()) {
                if (line.charAt(idx) == CELL_SEPARATOR) {
                    mListener.onCell(row, col, line.substring(start, idx));
                    start = idx + 1;
                    ++col;
                }
                ++idx;
            }
            // Emit last cell (which does not end with CELL_SEPARATOR)
            mListener.onCell(row, col, line.substring(start, idx));
            mListener.onEndRow(row);
            ++row;
        }
    }
}
