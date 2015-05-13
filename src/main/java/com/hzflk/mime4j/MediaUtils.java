package com.hzflk.mime4j;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypes;

import java.io.*;

/**
 * Created by free6om on 5/12/15.
 */
public class MediaUtils {
    private static final MimeTypes detector = MimeTypes.getDefaultMimeTypes();

    public static String getMimeType(String filename) {
        File file = new File(filename);
        Metadata metadata = new Metadata();
        metadata.add(Metadata.RESOURCE_NAME_KEY, file.getName());

        // Automatically detect the MIME type of the document
        try {
            MediaType type = detector.detect(new BufferedInputStream(new FileInputStream(file)), metadata);

            return type.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
