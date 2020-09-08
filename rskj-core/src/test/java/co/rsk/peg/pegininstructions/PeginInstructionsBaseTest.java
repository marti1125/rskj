package co.rsk.peg.pegininstructions;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;

public class PeginInstructionsBaseTest {

    @Test
    public void extractProtocolVersion() {
        int protocolVersion = PeginInstructionsBase.extractProtocolVersion(
                Hex.decode("00010e537aad84447a2c2a7590d5f2665ef5cf9b667a"));

        Assert.assertEquals(1, protocolVersion);
    }
}