package com.hzflk.mime4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;

class MagicMatch implements Clause {

    private final MediaType mediaType;

    private final String type;

    private final String offset;

    private final String value;

    private final String mask;

    private MagicDetector detector = null;

    MagicMatch(
            MediaType mediaType,
            String type, String offset, String value, String mask) {
        this.mediaType = mediaType;
        this.type = type;
        this.offset = offset;
        this.value = value;
        this.mask = mask;
    }

    private synchronized MagicDetector getDetector() {
        if (detector == null) {
            detector = MagicDetector.parse(mediaType, type, offset, value, mask);
        }
        return detector;
    }

    public boolean eval(byte[] data) {
        try {
            return getDetector().detect(
                    new ByteArrayInputStream(data), new Metadata())
                    != MediaType.OCTET_STREAM;
        } catch (IOException e) {
            // Should never happen with a ByteArrayInputStream
            return false;
        }
    }

    public int size() {
        return getDetector().getLength();
    }

    public String toString() {
        return mediaType.toString()
                + " " + type + " " + offset + " " +  value + " " + mask;
    }

}