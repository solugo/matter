package de.solugo.matter.core;

import java.text.NumberFormat;
import java.util.*;

public abstract class Node {

    private static final NumberFormat NUMBER_FORMAT;

    static {
        NUMBER_FORMAT = NumberFormat.getInstance(Locale.US);
        NUMBER_FORMAT.setMaximumFractionDigits(100);
        NUMBER_FORMAT.setGroupingUsed(false);
    }

    private Node() {
        super();
    }

    public abstract static class Container extends Node {
        private Container() {
            super();
        }
    }

    public final static class Object extends Container implements Map<java.lang.String, Node> {
        private final Map<java.lang.String, Node> properties = new HashMap<>();

        @Override
        public int size() {
            return this.properties.size();
        }

        @Override
        public boolean isEmpty() {
            return this.properties.isEmpty();
        }

        @Override
        public boolean containsKey(final java.lang.Object key) {
            return this.properties.containsKey(key);
        }

        @Override
        public boolean containsValue(final java.lang.Object value) {
            return this.properties.containsValue(value);
        }

        @Override
        public Node get(final java.lang.Object key) {
            return this.properties.get(key);
        }

        @Override
        public Node put(final java.lang.String key, final Node value) {
            return this.properties.put(key, value);
        }

        @Override
        public Node remove(final java.lang.Object key) {
            return this.properties.remove(key);
        }

        @Override
        public void putAll(final Map<? extends java.lang.String, ? extends Node> m) {
            this.properties.putAll(m);
        }

        @Override
        public void clear() {
            this.properties.clear();
        }

        @Override
        public Set<java.lang.String> keySet() {
            return this.properties.keySet();
        }

        @Override
        public Collection<Node> values() {
            return this.properties.values();
        }

        @Override
        public Set<Entry<java.lang.String, Node>> entrySet() {
            return this.properties.entrySet();
        }

        @Override
        public java.lang.String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append('{');
            for (final Map.Entry<java.lang.String, Node> entry : this.entrySet()) {
                if (builder.length() > 1) {
                    builder.append(", ");
                }
                builder.append(java.lang.String.format("\"%s\"", entry.getKey()));
                builder.append(": ");
                builder.append(entry.getValue().toString());
            }
            builder.append('}');
            return builder.toString();
        }
    }

    public final static class Array extends Container implements List<Node> {
        private final List<Node> values = new ArrayList<>();

        @Override
        public int size() {
            return this.values.size();
        }

        @Override
        public boolean isEmpty() {
            return this.values.isEmpty();
        }

        @Override
        public boolean contains(final java.lang.Object o) {
            return this.values.contains(o);
        }

        @Override
        public Iterator<Node> iterator() {
            return this.values.iterator();
        }

        @Override
        public java.lang.Object[] toArray() {
            return this.values.toArray();
        }

        @Override
        public <T> T[] toArray(final T[] a) {
            return this.values.toArray(a);
        }

        @Override
        public boolean add(final Node node) {
            return this.values.add(node);
        }

        @Override
        public boolean remove(final java.lang.Object o) {
            return this.values.remove(o);
        }

        @Override
        public boolean containsAll(final Collection<?> c) {
            return this.values.containsAll(c);
        }

        @Override
        public boolean addAll(final Collection<? extends Node> c) {
            return this.values.addAll(c);
        }

        @Override
        public boolean addAll(final int index, final Collection<? extends Node> c) {
            return this.values.addAll(index, c);
        }

        @Override
        public boolean removeAll(final Collection<?> c) {
            return this.values.removeAll(c);
        }

        @Override
        public boolean retainAll(final Collection<?> c) {
            return this.values.retainAll(c);
        }

        @Override
        public void clear() {
            this.values.clear();
        }

        @Override
        public Node get(final int index) {
            return this.values.get(index);
        }

        @Override
        public Node set(final int index, final Node element) {
            return this.values.set(index, element);
        }

        @Override
        public void add(final int index, final Node element) {
            this.values.add(index, element);
        }

        @Override
        public Node remove(final int index) {
            return this.values.remove(index);
        }

        @Override
        public int indexOf(final java.lang.Object o) {
            return this.values.indexOf(o);
        }

        @Override
        public int lastIndexOf(final java.lang.Object o) {
            return this.lastIndexOf(o);
        }

        @Override
        public ListIterator<Node> listIterator() {
            return this.values.listIterator();
        }

        @Override
        public ListIterator<Node> listIterator(final int index) {
            return null;
        }

        @Override
        public List<Node> subList(final int fromIndex, final int toIndex) {
            return null;
        }

        @Override
        public java.lang.String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append('[');
            for (final Node entry : this) {
                if (builder.length() > 1) {
                    builder.append(", ");
                }
                builder.append(entry.toString());
            }
            builder.append(']');
            return builder.toString();
        }
    }

    public abstract static class Value extends Node {
        private Value() {
            super();
        }
    }

    public final static class Binary extends Value {
        private final byte[] value;

        public Binary(final byte[] value) {
            this.value = value;
        }

        public byte[] get() {
            return this.value;
        }
    }

    public final static class String extends Value {
        private final java.lang.String value;

        public String(final java.lang.String value) {
            this.value = value;
        }

        public java.lang.String get() {
            return this.value;
        }

        @Override
        public java.lang.String toString() {
            return java.lang.String.format("\"%s\"", this.value);
        }
    }

    public final static class Boolean extends Value {
        private final boolean value;

        public Boolean(final boolean value) {
            this.value = value;
        }

        public boolean get() {
            return value;
        }

        @Override
        public java.lang.String toString() {
            return java.lang.String.valueOf(this.value);
        }
    }

    public final static class Number extends Value {
        private final long value;
        private final int scale;

        public Number(final long unscaled, final int scale) {
            this.value = unscaled;
            this.scale = scale;
        }

        public Number(final int value) {
            this(value, 0);
        }

        public Number(final long value) {
            this(value, 0);
        }

        public Number(final short value) {
            this(value, 0);
        }

        public Number(final byte value) {
            this(value, 0);
        }

        public Number(final java.lang.String value) {
            if (value.indexOf('E') != -1) {
                throw new RuntimeException("Unsupported number format");
            }

            final int point = value.indexOf('.');
            if (point != -1) {
                final long decimal = Long.valueOf(value.substring(point + 1));
                this.value = Long.valueOf(value.substring(0, point));
                this.scale
            }

        }

        public Number(final float value) {
            this(NUMBER_FORMAT.format(value));
        }

        public Number(final double value) {
            this(NUMBER_FORMAT.format(value));
        }

        public int intValue() {
            return (int) this.value / (10 ^ this.scale);
        }

        public long longValue() {
            return this.value / (10 ^ this.scale);
        }

        public float floatValue() {
            return 1f * this.value / (10 ^ this.scale);
        }

        public double doubleValue() {
            return 1d * this.value / (10 ^ this.scale);
        }

        public int scale() {
            return this.scale;
        }

        public long unscaled() {
            return this.value;
        }

        public int signum() {
            if (value > 0) {
                return 1;
            } else if (value < 0) {
                return -1;
            } else {
                return 0;
            }
        }

        @Override
        public java.lang.String toString() {
            return Double.toString(this.doubleValue());
        }
    }

    public final static class Null extends Value {
        @Override
        public java.lang.String toString() {
            return "null";
        }
    }

    public final static class Undefined extends Value {
        @Override
        public java.lang.String toString() {
            return "undefined";
        }
    }

}
