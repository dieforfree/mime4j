package com.hzflk.mime4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public interface Detector extends Serializable {

    /**
     * Detects the content type of the given input document. Returns
     * <code>application/octet-stream</code> if the type of the document
     * can not be detected.
     * <p>
     * If the document input stream is not available, then the first
     * argument may be <code>null</code>. Otherwise the detector may
     * read bytes from the start of the stream to help in type detection.
     * The given stream is guaranteed to support the
     * {@link InputStream#markSupported() mark feature} and the detector
     * is expected to {@link InputStream#mark(int) mark} the stream before
     * reading any bytes from it, and to {@link InputStream#reset() reset}
     * the stream before returning. The stream must not be closed by the
     * detector.
     * <p>
     * The given input metadata is only read, not modified, by the detector.
     *
     * @param input document input stream, or <code>null</code>
     * @param metadata input metadata for the document
     * @return detected media type, or <code>application/octet-stream</code>
     * @throws IOException if the document input stream could not be read
     */
    MediaType detect(InputStream input, Metadata metadata) throws IOException;

}