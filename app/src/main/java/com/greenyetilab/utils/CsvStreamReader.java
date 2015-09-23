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

    public interface Listener {
        void onCell(int row, int column, String value);
        void onStartRow();
        void onEndRow();
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
            mListener.onStartRow();
            String[] cells = line.split(";");
            for (int col = 0; col < cells.length; ++col) {
                mListener.onCell(row, col, cells[col]);
            }
            mListener.onEndRow();
            ++row;
        }
    }
}
