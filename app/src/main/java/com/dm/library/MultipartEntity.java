package com.dm.library;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Created by dataman on 12/2/2016.
 */
public abstract class MultipartEntity {

    public MultipartEntity(AndroidMultiPartEntity.HttpMultipartMode mode) {
    }

    public MultipartEntity(AndroidMultiPartEntity.HttpMultipartMode mode, String boundary, Charset charset) {

    }

    public MultipartEntity() {

    }

    public void writeTo(AndroidMultiPartEntity.CountingOutputStream countingOutputStream) {
    }

    public abstract void writeTo(OutputStream outstream) throws IOException;
}
