package piwords;

import static org.junit.Assert.*;

import org.junit.Test;

public class PiGeneratorTest {
    @Test
    public void basicPowerModTest() {
        // 5^7 mod 23 = 17
        assertEquals(17, PiGenerator.powerMod(5, 7, 23));
    }

//     TODO: Write more tests (Problem 1.a, 1.c)
    @Test
    public void advancedPowerModTest() {
        assertEquals(0, PiGenerator.powerMod(0, 0, 1));
        assertEquals(-1, PiGenerator.powerMod(-1, 0, 1));
    }
    @Test
    public void computePiInHexTest() {
        assertArrayEquals(new int[]{2, 4, 3, 15, 6, 10}, PiGenerator.computePiInHex(6));
        assertArrayEquals(new int[]{}, PiGenerator.computePiInHex(0));
    }
}
