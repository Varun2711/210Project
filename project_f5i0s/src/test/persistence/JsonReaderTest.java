package persistence;

import model.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import model.TaskList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            TaskList tl  = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyTaskList() {
        JsonReader reader = new JsonReader("./data/readerTestEmptyTaskList.json");
        try {
            TaskList tl = reader.read();
            assertEquals("My ToDo List", tl.getName());
            assertEquals(0, tl.getSize());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderTaskListWithTasks() {
        JsonReader reader = new JsonReader("./data/readerTestTaskListWithTasks.json");
        try {
            TaskList tl = reader.read();
            assertEquals("My ToDo List", tl.getName());
            List<Task> tasks = tl.getTasks();
            assertEquals(2, tasks.size());
            checkTask("Task 1", "do something", false, tasks.get(0));
            checkTask("Task 2", "do something else", true, tasks.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
