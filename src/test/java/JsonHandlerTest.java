import com.api.JsonHandler;
import com.course.Course;
import com.course.DemoTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Json handling.
 * Test data on the tail end of this file.
 */
public class JsonHandlerTest {
    /**
     * JsonHandler object to be used in tests.
     */
    private JsonHandler handler;

    /**
     * Create a JsonHandler for tests.
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
        final int expectedTaskNumber = 3;
        assertEquals(2, courseList.size());
        assertEquals(expectedTaskNumber, courseList
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
     * Invalid Json objects should be omitted.
     */
    @Test
    @DisplayName("An array of invalid Json data results in an empty list")
    public void inputInvalidObjects() {
        List<Course> emptyList = handler.jsonToCourses(arrayOfInvalidObjects);
        assertEquals(0, emptyList.size());
        assertEquals(Collections.emptyList(), emptyList);
    }


    /**
     * Json input that is not an array of objects returns an empty list.
     */
    @Test
    @DisplayName("Bare Json object input results in an empty list")
    public void bareObjectInput() {
        List<Course> emptyList = handler.jsonToCourses(bareJsonObject);
        assertEquals(0, emptyList.size());
        assertEquals(Collections.emptyList(), emptyList);
    }


    /**
     * Parse DemoTask objects from .timdata Json.
     */
    @Test
    @DisplayName("DemoTasks correctly parsed from .timdata")
    public void parseDemotaskFromJson() {
        List<DemoTask> demoTasks = handler.jsonToDemotask(timdata);
        final int taskCount = 3;
        assertEquals(taskCount, demoTasks.size());
        // test first demo task in the test data
        DemoTask demoTask = demoTasks.get(0);
        assertEquals("testidemo1.java", demoTask.getTaskFiles().get(0).getFileName());
        assertEquals("view/demo1/path", demoTask.getPath());
        assertEquals("t1", demoTask.getIdeTaskId());
        assertNull(demoTask.getTaskDirectory());
        // test last demo task in the test data
        demoTask = demoTasks.get(demoTasks.size() - 1);
        assertEquals("testidemo4.java", demoTask.getTaskFiles().get(0).getFileName());
        assertEquals("view/demo1/anotherpath", demoTask.getPath());
        assertEquals("t4", demoTask.getIdeTaskId());
        assertEquals("hasTaskDirectory", demoTask.getTaskDirectory());
    }


    /**
     * Test handling incorrect demo task data.
     */
    @Test
    @DisplayName("Parsing Json data with no demo tasks returns an empty list")
    public void jsonToDemoTaskHandlesInvalidJson() {
        List<DemoTask> demotaskList = handler.jsonToDemotask(validJsonData);
        assertEquals(0, demotaskList.size());
        demotaskList = handler.jsonToDemotask(arrayOfInvalidObjects);
        assertEquals(0, demotaskList.size());
        demotaskList = handler.jsonToDemotask(bareJsonObject);
        assertEquals(0, demotaskList.size());
    }


    /**
     * Json data that correctly maps to Course objects.
     * The format is an array of Json objects.
     */
    private final String validJsonData = "[\n"
            + "  {\n"
            + "      \"name\": \"ITKP101, ohjelmointi 1\",\n"
            + "      \"id\": 11203,\n"
            + "      \"path\": \"kurssit/tie/ohj1/2025k/demot\",\n"
            + "      \"tasks\": [\n"
            + "          {\n"
            + "              \"name\": \"Demo1\",\n"
            + "              \"doc_id\": 401648,\n"
            + "              \"path\": \"kurssit/tie/ohj1/2025k/demot/Demo1\"\n"
            + "          },\n"
            + "          {\n"
            + "              \"name\": \"Demo2\",\n"
            + "              \"doc_id\": 401649,\n"
            + "              \"path\": \"kurssit/tie/ohj1/2025k/demot/Demo2\"\n"
            + "          },\n"
            + "          {\n"
            + "            \"name\": \"Demo3\",\n"
            + "            \"doc_id\": 401650,\n"
            + "            \"path\": \"kurssit/tie/ohj1/2025k/demot/Demo3\"\n"
            + "        }\n"
            + "      ]\n"
            + "      \n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"ITKP102, ohjelmointi 2\",\n"
            + "    \"id\": 16103,\n"
            + "    \"path\": \"kurssit/tie/ohj2/2025k/demot\",\n"
            + "    \"tasks\": [\n"
            + "        {\n"
            + "            \"name\": \"Demo1\",\n"
            + "            \"doc_id\": 501370,\n"
            + "            \"path\": \"kurssit/tie/ohj2/2025k/demot/Demo1\"\n"
            + "        },\n"
            + "        {\n"
            + "            \"name\": \"Demo2\",\n"
            + "            \"doc_id\":  501372,\n"
            + "            \"path\": \"kurssit/tie/ohj2/2025k/demot/Demo2\"\n"
            + "        },\n"
            + "        {\n"
            + "          \"name\": \"Demo3\",\n"
            + "          \"doc_id\":  501374,\n"
            + "          \"path\": \"kurssit/tie/ohj2/2025k/demot/Demo3\"\n"
            + "      }\n"
            + "    ]\n"
            + "    }\n"
            +
            "]";

    /**
     * A bare Json object.
     */
    private final String bareJsonObject = "{\"field\": \"value\"}";
    /**
     * An array of invalid objects.
     */
    private final String arrayOfInvalidObjects = "[{\"field\": \"value\"}]";
    /**
     * A timdata file. Four tasks, one of them missing ide_task_id and thus
     * should not be included in the list returned by JsonHandler.
     * TODO: Apparently JSON field order not guaranteed. Make test data reflect this.
     */
    private final String timdata = "{\n"
        + "    \"course_parts\": {\n"
        + "        \"task/top/level/path\": {\n"
        + "            \"tasks\": {\n"
        + "                \"t1\": {\n"
        + "                    \"path\": \"view/demo1/path\",\n"
        + "                    \"type\": \"java\",\n"
        + "                    \"doc_id\": 135,\n"
        + "                    \"ide_task_id\": \"t1\",\n"
        + "                    \"task_files\": [\n"
        + "                        {\n"
        + "                            \"task_id_ext\": \"135.testidemo.Dp1JOmNgeSym\",\n"
        + "                            \"content\": \"testi = \\\"Tämä on testi\\\"\\nprint(testi)\",\n"
        + "                            \"file_name\": \"testidemo1.java\",\n"
        + "                            \"source\": \"editor\",\n"
        + "                            \"task_directory\": null,\n"
        + "                            \"task_type\": \"java\",\n"
        + "                            \"user_input\": \"\",\n"
        + "                            \"user_args\": \"\"\n"
        + "                        }\n"
        + "                    ],\n"
        + "                    \"supplementary_files\": [],\n"
        + "                    \"stem\": null,\n"
        + "                    \"task_directory\": null,\n"
        + "                    \"header\": null\n"
        + "                },\n"
        + "                \"t2\": {\n"
        + "                    \"path\": \"view/demo1/t2path\",\n"
        + "                    \"type\": \"java\",\n"
        + "                    \"doc_id\": 135,\n"
        + "                    \"ide_task_id\": \"t2\",\n"
        + "                    \"task_files\": [\n"
        + "                        {\n"
        + "                            \"task_id_ext\": \"135.testidemo2.cyftskDFkgmm\",\n"
        + "                            \"content\": \"testi = \\\"Tämä on testi useammalle tehtävälle\\\"\\nprint(testi)\",\n"
        + "                            \"file_name\": \"testidemo2.java\",\n"
        + "                            \"source\": \"editor\",\n"
        + "                            \"task_directory\": innerField,\n"
        + "                            \"task_type\": \"java\",\n"
        + "                            \"user_input\": \"\",\n"
        + "                            \"user_args\": \"\"\n"
        + "                        }\n"
        + "                    ],\n"
        + "                    \"supplementary_files\": [],\n"
        + "                    \"stem\": null,\n"
        + "                    \"task_directory\": outerField,\n"
        + "                    \"header\": null\n"
        + "                },\n"
        + "                \"t3\": {\n"
        + "                    \"path\": \"view/demo1/notaskid\",\n"
        + "                    \"type\": \"java\",\n"
        + "                    \"doc_id\": 135,\n"
        + "                    \"task_files\": [\n"
        + "                        {\n"
        + "                            \"task_id_ext\": \"135.testidemo2.cyfthsEFkgmm\",\n"
        + "                            \"content\": \"testi = \\\"Task with no task id should not get parsed into an object\",\n"
        + "                            \"file_name\": \"testidemo3.java\",\n"
        + "                            \"source\": \"editor\",\n"
        + "                            \"task_directory\": null,\n"
        + "                            \"task_type\": \"java\",\n"
        + "                            \"user_input\": \"\",\n"
        + "                            \"user_args\": \"\"\n"
        + "                        }\n"
        + "                    ],\n"
        + "                    \"supplementary_files\": [],\n"
        + "                    \"stem\": null,\n"
        + "                    \"task_directory\": null,\n"
        + "                    \"header\": null\n"
        + "                },\n"
        + "                \"t4\": {\n"
        + "                    \"path\": \"view/demo1/anotherpath\",\n"
        + "                    \"type\": \"java\",\n"
        + "                    \"doc_id\": 135,\n"
        + "                    \"ide_task_id\": \"t4\",\n"
        + "                    \"task_files\": [\n"
        + "                        {\n"
        + "                            \"task_id_ext\": \"135.testidemo4.YHKv5HpmFgP9\",\n"
        + "                            \"content\": \"testi = \\\"Tämä on testi kolmannelle tehtävälle\\\"\\nprint(testi)\",\n"
        + "                            \"file_name\": \"testidemo4.java\",\n"
        + "                            \"source\": \"editor\",\n"
        + "                            \"task_directory\": null,\n"
        + "                            \"task_type\": \"java\",\n"
        + "                            \"user_input\": \"\",\n"
        + "                            \"user_args\": \"\"\n"
        + "                        }\n"
        + "                    ],\n"
        + "                    \"supplementary_files\": [],\n"
        + "                    \"stem\": null,\n"
        + "                    \"task_directory\": \"hasTaskDirectory\",\n"
        + "                    \"header\": null\n"
        + "                }\n"
        + "            }\n"
        + "        }"
        + "    }"
        + "}";
}
