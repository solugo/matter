package de.solugo.matter.binary;

public class BinaryConstants {

    public static final int MASK_CONTINUE = 0x1000000;
    public static final int MASK_TYPE = 0xF0;
    public static final int MASK_VALUE = 0x0F;

    public static final int TYPE_NULL = 0 << 4;
    public static final int TYPE_CONTAINER = 1 << 4;
    public static final int TYPE_NUMBER = 2 << 4;
    public static final int TYPE_BOOLEAN = 3 << 4;
    public static final int TYPE_BINARY = 4 << 4;
    public static final int TYPE_REFERENCE = 5 << 4;
    public static final int CONTAINER_FLAG_BEGIN = 1 << 3;
    public static final int CONTAINER_VALUE_OBJECT = 1;
    public static final int CONTAINER_VALUE_ARRAY = 2;
    public static final int BOOLEAN_FLAG_TRUE = 1;
    public static final int BINARY_VALUE_PLAIN = 1;
    public static final int BINARY_VALUE_STRING = 2;
    public static final int NUMBER_FLAG_POSITIVE = 1 << 3;
    public static final int NUMBER_FLAG_SCALE = 1 << 2;
    public static final int NUMBER_FLAG_EXP = 1 << 1;

    public static boolean hasFlag(final int value, final int flag) {
        return (value & flag) == flag;
    }

    public static int getValue(final int value, final int bits) {
        int mask = 0;
        for (int bit = 0; bit < bits; bit++) {
            mask = mask | 1 << bit;
        }
        return value & mask;
    }

    public static String toString(final int value) {
        final StringBuilder builder = new StringBuilder();
        for (int pos = 7; pos >= 0; pos--) {
            if ((value & (1 << pos))  != 0) {
                builder.append('1');
            } else {
                builder.append('0');
            }
        }
        return builder.toString();
    }
}
