package com.hzflk.mime4j;

import java.io.InputStream;

public class ClosedInputStream extends InputStream {

    /**
     * Returns -1 to indicate that the stream is closed.
     *
     * @return always -1
     */
    @Override
    public int read() {
        return -1;
    }

}