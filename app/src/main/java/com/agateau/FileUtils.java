package com.agateau;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Various file utilities
 */
public class FileUtils {
    public static String readUtf8(final InputStream in) throws IOException {
        InputStreamReader reader = new InputStreamReader(in, "UTF-8");
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[1024];
        while (true) {
            int length = reader.read(buffer);
            if (length == -1) {
                break;
            }
            sb.append(buffer);
        }
        return sb.toString();
    }

    public static void writeUtf8(final OutputStream out, String text) throws IOException {
        byte[] buffer = text.getBytes("UTF-8");
        out.write(buffer);
        out.flush();
    }
}
