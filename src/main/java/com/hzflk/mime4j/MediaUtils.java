package com.hzflk.mime4j;

import java.io.*;

/**
 * Created by free6om on 5/12/15.
 */
public class MediaUtils {

    public static String getMimeType(String filename) {
        File file = new File(filename);
        Metadata metadata = new Metadata();
        metadata.add("resourceName", file.getName());

        // Automatically detect the MIME type of the document
        try {
            MimeTypes detector = MimeTypes.getDefaultMimeTypes();
            MediaType type = detector.detect(new BufferedInputStream(new FileInputStream(file)), metadata);

            return type.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
