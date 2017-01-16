package de.solugo.matter.core;

import lombok.Data;

public abstract class NodeToken<T> {
    private final T value;

    public NodeToken(final T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    @Data
    public static final class ContainerBegin<T extends Node.Container> extends NodeToken<Class<T>> {
        public ContainerBegin(final Class<T> value) {
            super(value);
        }
    }

    @Data
    public static final class ContainerEnd<T extends Node.Container> extends NodeToken<Class<T>> {
        public ContainerEnd(final Class<T> value) {
            super(value);
        }
    }

    @Data
    public static final class Value<T extends Node.Value> extends NodeToken<T> {
        public Value(final T value) {
            super(value);
        }
    }

}
