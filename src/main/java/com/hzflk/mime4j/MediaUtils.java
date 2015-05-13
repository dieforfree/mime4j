package com.hzflk.mime4j;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.mime.MimeTypesFactory;

import java.io.*;

/**
 * Created by free6om on 5/12/15.
 */
public class MediaUtils {
    private static MimeTypes detector = null;

    public static synchronized String getMimeType(String filename) {
        if(detector == null) {
            try {
                detector =  MimeTypesFactory.create(new FileInputStream("../mime4j/src/main/resources/tika-mimetypes.xml"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (MimeTypeException e) {
                e.printStackTrace();
            }
        }

        if(detector == null) {
            return null;
        }

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
