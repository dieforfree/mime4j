package com.hzflk.mime4j;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;

public class OfflineContentHandler extends ContentHandlerDecorator {

    public OfflineContentHandler(ContentHandler handler) {
        super(handler);
    }

    /**
     * Returns an empty stream. This will make an XML parser silently
     * ignore any external entities.
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId) {
        return new InputSource(new ClosedInputStream());
    }

}