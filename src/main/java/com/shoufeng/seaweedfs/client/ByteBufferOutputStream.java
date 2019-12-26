package com.shoufeng.seaweedfs.client;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @author shoufeng
 */
public class ByteBufferOutputStream extends OutputStream {
    private final ByteBuffer buf;

    public ByteBufferOutputStream(ByteBuffer buf) {
        this.buf = buf;
    }

    @Override
    public void write(int b) throws IOException {
        this.buf.put((byte)b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.buf.put(b, off, len);
    }
}
