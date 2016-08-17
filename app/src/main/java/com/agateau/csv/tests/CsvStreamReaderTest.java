package com.agateau.csv.tests;

import com.agateau.csv.CsvStreamReader;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CsvStreamReaderTest {
    @Test
    public void readSingleRow() throws IOException {
        String bla = "a;bc;;def\n";
        InputStream in = new ByteArrayInputStream(bla.getBytes());
        CsvStreamReader reader = new CsvStreamReader(in);

        assertThat(reader.atDocumentEnd(), is(false));

        String c1 = reader.readCell();
        String c2 = reader.readCell();
        String c3 = reader.readCell();
        String c4 = reader.readCell();

        assertThat(c1, is("a"));
        assertThat(c2, is("bc"));
        assertThat(c3, is(""));
        assertThat(c4, is("def"));

        reader.readNextRow();
        assertThat(reader.atDocumentEnd(), is(true));
    }

    @Test
    public void readTwoRows() throws IOException {
        String bla = "a;bc\nd;ef\n";
        InputStream in = new ByteArrayInputStream(bla.getBytes());
        CsvStreamReader reader = new CsvStreamReader(in);
        assertThat(reader.getRow(), is(0));

        assertThat(reader.readCell(), is("a"));
        assertThat(reader.atRowEnd(), is(false));

        assertThat(reader.readCell(), is("bc"));
        assertThat(reader.atRowEnd(), is(true));

        reader.readNextRow();
        assertThat(reader.getRow(), is(1));

        assertThat(reader.readCell(), is("d"));
        assertThat(reader.atRowEnd(), is(false));

        assertThat(reader.readCell(), is("ef"));
        assertThat(reader.atRowEnd(), is(true));
    }

    @Test
    public void readIntCell() throws IOException {
        String bla = "0;1.0;-2\n";
        InputStream in = new ByteArrayInputStream(bla.getBytes());
        CsvStreamReader reader = new CsvStreamReader(in);

        assertThat(reader.readIntCell(), is(0));
        assertThat(reader.readIntCell(), is(1));
        assertThat(reader.readIntCell(), is(-2));
    }

    @Test
    public void readFloatCell() throws IOException {
        String bla = "0.1;10;-3.14\n";
        InputStream in = new ByteArrayInputStream(bla.getBytes());
        CsvStreamReader reader = new CsvStreamReader(in);

        assertThat(reader.readFloatCell(), is(0.1f));
        assertThat(reader.readFloatCell(), is(10f));
        assertThat(reader.readFloatCell(), is(-3.14f));
    }
}
