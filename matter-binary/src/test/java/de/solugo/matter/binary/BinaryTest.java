package de.solugo.matter.binary;

import de.solugo.matter.core.Node;
import de.solugo.matter.core.NodeMapper;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class BinaryTest {

    @Test
    public void test() {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final Node.Object object = new Node.Object();
        object.put("string", new Node.String("Value"));
        object.put("long", new Node.Number(Long.MAX_VALUE));
        object.put("double", new Node.Number(Double.MAX_VALUE));

        final NodeMapper mapper = new NodeMapper();
        mapper.serialize( new BinaryWriter(outputStream), object);

        final InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        final Node node = mapper.deserialize(new BinaryReader(inputStream));

        System.out.println(node);
    }

}
