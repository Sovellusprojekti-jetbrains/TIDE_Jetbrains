import com.api.JsonHandler;
import com.course.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;

/**
 * Tests for Json handling.
 * Test data on the tail end of this file.
 */
public class JsonHandlerTest {
    JsonHandler handler;

    /**
     * Create a JsonHandler for tests
     */
    @BeforeEach
    public void setUp() {
        handler = new JsonHandler();
    }


    /**
     * Input data should map to objects correctly.
     */
    @Test
    @DisplayName("Json maps to Course objects correctly")
    public void handleValidData() {
        List<Course> courseList = handler.jsonToCourses(validJsonData);
        assertEquals(2, courseList.size());
        assertEquals(3, courseList
                .get(1)
                .getTasks()
                .size());
        assertEquals("ITKP101, ohjelmointi 1", courseList
                .get(0)
                .getName());
        assertEquals("Demo2", courseList
                .get(1)
                .getTasks()
                .get(1)
                .getName());
    }


    /**
     * Invalid Json objects should be omitted
     */
    @Test
    @DisplayName("An array of invalid Json data results in an empty list")
    public void inputInvalidObjects() {
        List<Course> emptyList = handler.jsonToCourses(arrayOfInvalidObjects);
        assertEquals(0, emptyList.size());
        assertEquals(Collections.emptyList(), emptyList);
    }


    /**
     * Json input that is not an array
     * of objects returns an empty list.
     */
    @Test
    @DisplayName("Bare Json object input results in an empty list")
    public void bareObjectInput() {
        List<Course> emptyList = handler.jsonToCourses(bareJsonObject);
        assertEquals(0, emptyList.size());
        assertEquals(Collections.emptyList(), emptyList);
    }


    /**
     * Json data that correctly maps to Course objects.
     * The format is an array of Json objects.
     */
    private final String validJsonData = "[\n" +
            "  {\n" +
            "      \"name\": \"ITKP101, ohjelmointi 1\",\n" +
            "      \"id\": 11203,\n" +
            "      \"path\": \"kurssit/tie/ohj1/2025k/demot\",\n" +
            "      \"tasks\": [\n" +
            "          {\n" +
            "              \"name\": \"Demo1\",\n" +
            "              \"doc_id\": 401648,\n" +
            "              \"path\": \"kurssit/tie/ohj1/2025k/demot/Demo1\"\n" +
            "          },\n" +
            "          {\n" +
            "              \"name\": \"Demo2\",\n" +
            "              \"doc_id\": 401649,\n" +
            "              \"path\": \"kurssit/tie/ohj1/2025k/demot/Demo2\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"name\": \"Demo3\",\n" +
            "            \"doc_id\": 401650,\n" +
            "            \"path\": \"kurssit/tie/ohj1/2025k/demot/Demo3\"\n" +
            "        }\n" +
            "      ]\n" +
            "      \n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"ITKP102, ohjelmointi 2\",\n" +
            "    \"id\": 16103,\n" +
            "    \"path\": \"kurssit/tie/ohj2/2025k/demot\",\n" +
            "    \"tasks\": [\n" +
            "        {\n" +
            "            \"name\": \"Demo1\",\n" +
            "            \"doc_id\": 501370,\n" +
            "            \"path\": \"kurssit/tie/ohj2/2025k/demot/Demo1\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"Demo2\",\n" +
            "            \"doc_id\":  501372,\n" +
            "            \"path\": \"kurssit/tie/ohj2/2025k/demot/Demo2\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"Demo3\",\n" +
            "          \"doc_id\":  501374,\n" +
            "          \"path\": \"kurssit/tie/ohj2/2025k/demot/Demo3\"\n" +
            "      }\n" +
            "    ]\n" +
            "    }\n" +
            "]";

    /**
     * A bare Json object
     */
    private final String bareJsonObject = "{\"field\": \"value\"}";
    /**
     * An array of invalid objects
     */
    private final String arrayOfInvalidObjects = "[{\"field\": \"value\"}]";
}
