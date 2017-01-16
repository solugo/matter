package de.solugo.matter.core;

import java.io.OutputStream;

public abstract class NodeWriter {

    protected final OutputStream outputStream;

    public NodeWriter(final OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public abstract void write(final NodeToken token);

}
