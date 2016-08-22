package com.agateau.csv.tests;

import com.agateau.csv.CsvStreamWriter;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CsvStreamWriterTest {
    @Test
    public void writeSingleRow() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CsvStreamWriter writer = new CsvStreamWriter(out);
        writer.writeCell("a");
        writer.writeCell(1);
        writer.writeCell(1.2f);
        writer.endRow();
        out.close();

        String str = out.toString();
        assertThat(str, is("a;1;1.2\n"));
    }
}
