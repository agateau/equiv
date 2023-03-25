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
        assertThat(reader.loadNextRow(), is(true));

        String c1 = reader.readCell();
        String c2 = reader.readCell();
        String c3 = reader.readCell();
        String c4 = reader.readCell();

        assertThat(c1, is("a"));
        assertThat(c2, is("bc"));
        assertThat(c3, is(""));
        assertThat(c4, is("def"));

        assertThat(reader.loadNextRow(), is(false));
    }

    @Test
    public void readTwoRows() throws IOException {
        String bla = "a;bc\nd;ef\n";
        InputStream in = new ByteArrayInputStream(bla.getBytes());
        CsvStreamReader reader = new CsvStreamReader(in);
        assertThat(reader.loadNextRow(), is(true));

        assertThat(reader.getRow(), is(0));

        assertThat(reader.readCell(), is("a"));
        assertThat(reader.atRowEnd(), is(false));

        assertThat(reader.readCell(), is("bc"));
        assertThat(reader.atRowEnd(), is(true));

        assertThat(reader.loadNextRow(), is(true));
        assertThat(reader.getRow(), is(1));

        assertThat(reader.readCell(), is("d"));
        assertThat(reader.atRowEnd(), is(false));

        assertThat(reader.readCell(), is("ef"));
        assertThat(reader.atRowEnd(), is(true));

        assertThat(reader.loadNextRow(), is(false));
    }

    @Test
    public void readIntCell() throws IOException {
        String bla = "0;1.0;-2\n";
        InputStream in = new ByteArrayInputStream(bla.getBytes());
        CsvStreamReader reader = new CsvStreamReader(in);
        assertThat(reader.loadNextRow(), is(true));

        assertThat(reader.readIntCell(), is(0));
        assertThat(reader.readIntCell(), is(1));
        assertThat(reader.readIntCell(), is(-2));
    }

    @Test
    public void readFloatCell() throws IOException {
        String bla = "0.1;10;-3.14\n";
        InputStream in = new ByteArrayInputStream(bla.getBytes());
        CsvStreamReader reader = new CsvStreamReader(in);
        assertThat(reader.loadNextRow(), is(true));

        assertThat(reader.readFloatCell(), is(0.1f));
        assertThat(reader.readFloatCell(), is(10f));
        assertThat(reader.readFloatCell(), is(-3.14f));
    }
}
