package de.solugo.matter.binary;

import de.solugo.matter.core.Node;
import de.solugo.matter.core.NodeReader;
import de.solugo.matter.core.NodeToken;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class BinaryReader extends NodeReader {

    public BinaryReader(final InputStream inputStream) {
        super(inputStream);
    }

    @Override
    protected NodeToken read() {
        try {
            final int token = inputStream.read();
            if (token != -1) {
                switch (token & BinaryConstants.MASK_TYPE) {
                    case BinaryConstants.TYPE_NULL: {
                        return new NodeToken.Value(new Node.Null());
                    }
                    case BinaryConstants.TYPE_CONTAINER: {
                        final Class<? extends Node> type;
                        final int typeValue = BinaryConstants.getValue(token, 3);
                        switch (typeValue) {
                            case BinaryConstants.CONTAINER_VALUE_OBJECT: {
                                type = Node.Object.class;
                                break;
                            }
                            case BinaryConstants.CONTAINER_VALUE_ARRAY: {
                                type = Node.Array.class;
                                break;
                            }
                            default: {
                                throw new RuntimeException(String.format("Unknown container type %s", typeValue));
                            }
                        }
                        if (BinaryConstants.hasFlag(token, BinaryConstants.CONTAINER_FLAG_BEGIN)) {
                            return new NodeToken.ContainerBegin(type);
                        } else {
                            return new NodeToken.ContainerEnd(type);
                        }
                    }
                    case BinaryConstants.TYPE_BOOLEAN: {
                        return new NodeToken.Value(new Node.Boolean(
                            BinaryConstants.hasFlag(token, BinaryConstants.BOOLEAN_FLAG_TRUE)
                        ));
                    }
                    case BinaryConstants.TYPE_BINARY: {
                        final int length = this.decodeInt();
                        final byte[] content = new byte[length];
                        this.inputStream.read(content);
                        final int typeValue = BinaryConstants.getValue(token, 4)
                        switch (typeValue) {
                            case BinaryConstants.BINARY_VALUE_PLAIN: {
                                return new NodeToken.Value(new Node.Binary(content));
                            }
                            case BinaryConstants.BINARY_VALUE_STRING: {
                                return new NodeToken.Value(new Node.String(new String(content, StandardCharsets.UTF_8)));
                            }
                            default: {
                                throw new RuntimeException(String.format("Unknown binary type %s", typeValue));
                            }
                        }
                    }
                    case BinaryConstants.TYPE_NUMBER: {
                        final long value = this.decodeLong();
                        final int sig = BinaryConstants.hasFlag(token, BinaryConstants.NUMBER_FLAG_POSITIVE) ? 1 : -1;
                        final int scale = BinaryConstants.hasFlag(token, BinaryConstants.NUMBER_FLAG_SCALE) ? this.decodeInt() : 0;
                        final int exp = BinaryConstants.hasFlag(token, BinaryConstants.NUMBER_FLAG_EXP) ? this.decodeInt() : 0;

                        return new NodeToken.Value(new Node.Number(value * sig * Math.pow(10, exp)))
                    }
                }
                if ((token & BinaryConstants.MASK_TYPE) == BinaryConstants.TYPE_NULL) {

                } else if ((token & BinaryConstants.MASK_TYPE) == BinaryConstants.TYPE_BINARY) {

                } else if ((token & BinaryConstants.MASK_TYPE) == BinaryConstants.TYPE_BOOLEAN) {

                } else if ((token & BinaryConstants.MASK_TYPE) == BinaryConstants.TYPE_BINARY) {

                }
                switch (token) {
                    case BinaryConstants.CONTAINER_BEGIN_OBJECT: {
                        return new NodeToken.ContainerBegin(Node.Object.class);
                    }
                    case BinaryConstants.CONTAINER_END_OBJECT: {
                        return new NodeToken.ContainerEnd(Node.Object.class);
                    }
                    case BinaryConstants.CONTAINER_BEGIN_ARRAY: {
                        return new NodeToken.ContainerBegin(Node.Array.class);
                    }
                    case BinaryConstants.CONTAINER_END_ARRAY: {
                        return new NodeToken.ContainerEnd(Node.Array.class);
                    }
                    case BinaryConstants.VALUE_NULL: {
                        return new NodeToken.Value(new Node.Null());
                    }
                    case BinaryConstants.VALUE_STRING: {
                        final int size = this.decodeInt();
                        final byte[] content = new byte[size];
                        inputStream.read(content);
                        return new NodeToken.Value(new Node.String(new String(content, StandardCharsets.UTF_8)));
                    }
                    case BinaryConstants.VALUE_BINARY: {
                        final byte[] content = new byte[this.decodeInt()];
                        inputStream.read(content);
                        return new NodeToken.Value(new Node.Binary(content));
                    }
                    case BinaryConstants.VALUE_INTEGER_POSITIVE: {
                        return new NodeToken.Value(new Node.Number(this.decodeLong(), 0));
                    }
                    case BinaryConstants.VALUE_INTEGER_NEGATIVE: {
                        return new NodeToken.Value(new Node.Number(this.decodeLong() * -1, 0));
                    }
                    case BinaryConstants.VALUE_DECIMAL_POSITIVE: {
                        return new NodeToken.Value(new Node.Number(this.decodeLong(), this.decodeInt()));
                    }
                    case BinaryConstants.VALUE_DECIMAL_NEGATIVE: {
                        final int scale = this.decodeInt();
                        final long unscaled = this.decodeLong();
                        return new NodeToken.Value(new Node.Number(unscaled * -1, scale));
                    }
                    case BinaryConstants.VALUE_BOOLEAN_TRUE: {
                        return new NodeToken.Value(new Node.Boolean(true));
                    }
                    case BinaryConstants.VALUE_BOOLEAN_FALSE: {
                        return new NodeToken.Value(new Node.Boolean(false));
                    }
                    default: {
                        throw new RuntimeException(String.format("Unknown value type %s found", token));
                    }
                }
            } else {
                return null;
            }
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    private long decodeLong() throws IOException {
        long value = 0;
        int step;

        do {
            step = inputStream.read();
            value += (step | BinaryConstants.MASK_CONTINUE) - BinaryConstants.MASK_CONTINUE;
        } while ((step & BinaryConstants.MASK_CONTINUE) == BinaryConstants.MASK_CONTINUE);

        return value;
    }

    private int decodeInt() throws IOException {
        int value = 0;
        int step;

        do {
            step = inputStream.read();
            value += (step | BinaryConstants.MASK_CONTINUE) - BinaryConstants.MASK_CONTINUE;
        } while ((step & BinaryConstants.MASK_CONTINUE) == BinaryConstants.MASK_CONTINUE);

        return value;
    }

}
