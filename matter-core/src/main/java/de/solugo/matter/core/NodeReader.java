package de.solugo.matter.core;

import java.io.InputStream;

public abstract class NodeReader {

    protected final InputStream inputStream;
    private NodeToken current;

    protected NodeReader(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public final boolean next() {
        return (this.current = this.read()) != null;
    }

    public final NodeToken current() {
        return this.current;
    }

    protected abstract NodeToken read();

}
