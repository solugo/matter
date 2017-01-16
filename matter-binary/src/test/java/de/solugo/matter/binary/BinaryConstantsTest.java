package de.solugo.matter.binary;

import org.junit.Test;

public class BinaryConstantsTest {
    @Test
    public void setType() {
        final int result = BinaryConstants.TYPE_CONTAINER | BinaryConstants.CONTAINER_FLAG_BEGIN | BinaryConstants.CONTAINER_VALUE_OBJECT;
    }
}
