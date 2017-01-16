package de.solugo.matter.core;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public final class NodeMapper {

    public void serialize(final NodeWriter writer, final Node node) {
        if (node instanceof Node.Object) {
            final Node.Object object = (Node.Object) node;
            writer.write(new NodeToken.ContainerBegin(Node.Object.class));
            for (final Map.Entry<String, Node> entry : object.entrySet()) {
                writer.write(new NodeToken.Value(new Node.String(entry.getKey())));
                this.serialize(writer, entry.getValue());
            }
            writer.write(new NodeToken.ContainerEnd(Node.Object.class));
        } else if (node instanceof Node.Array) {
            final Node.Array array = (Node.Array) node;
            writer.write(new NodeToken.ContainerBegin(Node.Array.class));
            for (final Node entry : array) {
                this.serialize(writer, entry);
            }
            writer.write(new NodeToken.ContainerEnd(Node.Array.class));
        } else {
            writer.write(new NodeToken.Value((Node.Value) node));
        }
    }

    public Node deserialize(final NodeReader reader) {
        if (reader.next()) {
            if (reader.current() instanceof NodeToken.ContainerBegin) {
                if (reader.current().get() == Node.Object.class) {
                    final Node.Object object = new Node.Object();
                    while (reader.next()) {
                        if (reader.current() instanceof NodeToken.ContainerEnd) {
                            return object;
                        } else {
                            final Node.String property = (Node.String) reader.current().get();
                            final Node value = this.deserialize(reader);
                            object.put(property.get(), value);
                        }
                    }
                    throw new RuntimeException(String.format("Unfinished container type %s", object.getClass()));
                } else if (reader.current().get() == Node.Array.class) {
                    final Node.Array array = new Node.Array();
                    while (reader.next()) {
                        if (reader.current() instanceof NodeToken.ContainerEnd) {
                            return array;
                        } else {
                            array.add(this.deserialize(reader));
                        }
                    }
                    throw new RuntimeException(String.format("Unfinished container type %s", array.getClass()));
                } else {
                    throw new RuntimeException(String.format("Unknown container type %s found", reader.current().get()));
                }
            } else if (reader.current() instanceof NodeToken.Value) {
                return (Node) reader.current().get();
            } else {
                throw new RuntimeException(String.format("Unknown container type %s found", reader.current().get()));
            }
        } else {
            return null;
        }
    }

}
