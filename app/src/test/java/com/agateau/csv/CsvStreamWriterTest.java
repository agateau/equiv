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
