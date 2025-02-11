import com.Main;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * simple test class to test functionality of JUnit5.
 */
public class MainTest {

    /**
     * Adds a basic test.
     */
    @Test
    void testAdd() {
        final int[] res = {2, 3};
        final int expect = 5;
        final int[] res2 = {2, 4};
        final int unexpect = 5;
        Main main = new Main();
        int result = main.add(res[0], res[1]);
        assertEquals(expect, result, "2 + 3 should equal 5");
        result = main.add(res2[0], res2[1]);
        assertNotEquals(unexpect, result, "2 + 4 should not equal 5");

    }
}
