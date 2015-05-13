package com.hzflk.mime4j;

import java.io.InputStream;

public class CloseShieldInputStream extends ProxyInputStream {

    /**
     * Creates a proxy that shields the given input stream from being
     * closed.
     *
     * @param in underlying input stream
     */
    public CloseShieldInputStream(InputStream in) {
        super(in);
    }

    /**
     * Replaces the underlying input stream with a {@link ClosedInputStream}
     * sentinel. The original input stream will remain open, but this proxy
     * will appear closed.
     */
    @Override
    public void close() {
        in = new ClosedInputStream();
    }

}