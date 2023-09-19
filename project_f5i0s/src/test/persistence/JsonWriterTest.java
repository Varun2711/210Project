package persistence;

import model.Task;
import model.TaskList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest{

    @Test
    void testWriterWritingtoInvalidFile() {
        try {
            TaskList taskList = new TaskList("My ToDo List");
            JsonWriter writer = new JsonWriter("./data/mynon\\sensenamefile.json");
            writer.open();
            fail("IOException was expected");
        } catch (FileNotFoundException e) {
            //pass
        }
    }

    @Test
    void testWriterSavingAnEmptyToDoList() {
        try {
            TaskList taskList = new TaskList("My ToDo List");
            JsonWriter writer = new JsonWriter("./data/writerTestSavingEmptyTodoList.json");
            writer.open();
            writer.write(taskList);
            writer.close();

            JsonReader reader = new JsonReader("./data/writerTestSavingEmptyTodoList.json");
            taskList = reader.read();
            Assertions.assertEquals("My ToDo List", taskList.getName());
            Assertions.assertEquals(0, taskList.getSize());
        } catch (IOException e) {
            fail("IOException was not expected");
        }
    }

    @Test
    void testWriterSavingANonEmptyToDoList() {
        try {
            TaskList taskList = new TaskList("My ToDo List");
            taskList.addTask("Task 1", "do something");
            taskList.addTask("Task 2", "do something else");
            taskList.updateTaskStatus("Task 1");
            JsonWriter writer = new JsonWriter("./data/writerTestSavingANonEmptyTaskList.json");
            writer.open();
            writer.write(taskList);
            writer.close();

            JsonReader reader = new JsonReader("./data/writerTestSavingANonEmptyTaskList.json");
            taskList = reader.read();
            assertEquals("My ToDo List", taskList.getName());
            List<Task> tasks = taskList.getTasks();
            checkTask("Task 1", "do something", true, tasks.get(0));
            checkTask("Task 2", "do something else", false, tasks.get(1));
        } catch (IOException e) {
            fail("IOException was not expected");
        }
    }

}
