package piwords;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Test;
@RunWith(Parameterized.class)
public class BaseTranslatorTest {
    private int baseA, baseB, precisionB;
    private int[] input, output;
    @Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][]{
                {2, 10, 2, new int[]{0, 1}, new int[]{2, 5}},
                {16, 10, 3, new int[]{1, 10}, new int[]{1, 0, 1}},
        });
    }
    public BaseTranslatorTest(int baseA, int baseB, int precisionB, int[] input, int[] output) {
        this.baseA = baseA;
        this.baseB = baseB;
        this.precisionB = precisionB;
        this.input = input;
        this.output = output;
    }
    @Test
    public void basicBaseTranslatorTest() {
        // Expect that .01 in base-2 is .25 in base-10
        // (0 * 1/2^1 + 1 * 1/2^2 = .25)
        assertArrayEquals(output,
                          BaseTranslator.convertBase(input, baseA, baseB, precisionB));
    }

    // TODO: Write more tests (Problem 2.a)
}
