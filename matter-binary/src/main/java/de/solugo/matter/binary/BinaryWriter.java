package de.solugo.matter.binary;

import de.solugo.matter.core.Node;
import de.solugo.matter.core.NodeToken;
import de.solugo.matter.core.NodeWriter;
import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BinaryWriter extends NodeWriter {

    private static final BigInteger CONTINUE = BigInteger.valueOf(BinaryConstants.MASK_CONTINUE);
    private final List<Node.Value> constants = new ArrayList<>();

    public BinaryWriter(final OutputStream outputStream) {
        super(outputStream);
    }

    public void defineConstant(final Node.Value value) throws IOException {
        /*if (!constants.contains(value)) {
            outputStream.write(BinaryConstants.CONSTANT_DEFINITION);
            this.encodeInt(constants.size());
            constants.add(value);
        }*/
    }

    @Override
    public void write(final NodeToken event) {
       /* try {
            if (event instanceof NodeToken.ContainerBegin) {
                final NodeToken.ContainerBegin begin = (NodeToken.ContainerBegin) event;
                if (begin.get() == Node.Object.class) {
                    outputStream.write(BinaryConstants.CONTAINER_BEGIN_OBJECT);
                } else if (begin.get() == Node.Array.class) {
                    outputStream.write(BinaryConstants.CONTAINER_BEGIN_ARRAY);
                } else {
                    throw new RuntimeException("Unknown container type found");
                }
            } else if (event instanceof NodeToken.ContainerEnd) {
                final NodeToken.ContainerEnd end = (NodeToken.ContainerEnd) event;
                if (end.get() == Node.Object.class) {
                    outputStream.write(BinaryConstants.CONTAINER_END_OBJECT);
                } else if (end.get() == Node.Array.class) {
                    outputStream.write(BinaryConstants.CONTAINER_END_ARRAY);
                } else {
                    throw new RuntimeException("Unknown container type found");
                }
            } else if (event instanceof NodeToken.Value) {
                final NodeToken.Value value = (NodeToken.Value) event;
                final int constant = this.constants.indexOf(value);
                if (constant != -1) {
                    this.outputStream.write(BinaryConstants.CONSTANT_INSTANCE);
                    this.encodeInt(constant);
                } else {
                    if (value.get() instanceof Node.Binary) {
                        outputStream.write(BinaryConstants.VALUE_BINARY);
                        final Node.Binary content = (Node.Binary) value.get();
                        this.encodeInt(content.get().length);
                        outputStream.write(content.get());
                    } else if (value.get() instanceof Node.String) {
                        outputStream.write(BinaryConstants.VALUE_STRING);
                        final Node.String content = (Node.String) value.get();
                        this.encodeInt(content.get().length());
                        outputStream.write(content.get().getBytes(StandardCharsets.UTF_8));
                    } else if (value.get() instanceof Node.Number) {
                        final Node.Number number = (Node.Number) value.get();
                        if (number.scale() > 0) {
                            if (number.signum() != -1) {
                                outputStream.write(BinaryConstants.VALUE_DECIMAL_POSITIVE);
                            } else {
                                outputStream.write(BinaryConstants.VALUE_DECIMAL_NEGATIVE);
                            }
                            this.encodeLong(Math.abs(number.unscaled()));
                            this.encodeInt(number.scale());
                        } else {
                            if (number.signum() != -1) {
                                outputStream.write(BinaryConstants.VALUE_INTEGER_POSITIVE);
                            } else {
                                outputStream.write(BinaryConstants.VALUE_INTEGER_NEGATIVE);
                            }
                            this.encodeLong(Math.abs(number.unscaled()));
                        }
                    } else {
                        throw new RuntimeException("Unknown value type found");
                    }
                }
            }
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }*/
    }

    private void encodeInt(@NonNull final int value) throws IOException {
        int rest = value;
        while (true) {
            final int part = value % BinaryConstants.MASK_CONTINUE;
            rest = rest / BinaryConstants.MASK_CONTINUE;
            if (rest > 0) {
                this.outputStream.write(part | BinaryConstants.MASK_CONTINUE);
            } else {
                this.outputStream.write(part);
                break;
            }
        }
    }

    private void encodeLong(@NonNull final long value) throws IOException {
        long rest = value;
        while (true) {
            final int part = (int) (value % BinaryConstants.MASK_CONTINUE);
            rest = rest / BinaryConstants.MASK_CONTINUE;
            if (rest > 0) {
                this.outputStream.write(part | BinaryConstants.MASK_CONTINUE);
            } else {
                this.outputStream.write(part);
                break;
            }
        }
    }

}
