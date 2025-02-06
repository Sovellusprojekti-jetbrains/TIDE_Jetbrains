import com.example.Main;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//simple test class to test functionality of JUnit5
public class MainTest {

    @Test
    void testAdd() {
        Main main = new Main();
        int result = main.add(2, 3);
        assertEquals(5, result, "2 + 3 should equal 5");
        result = main.add(2, 4);
        assertNotEquals(5, result, "2 + 4 should not equal 5");

    }
}
