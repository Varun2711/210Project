package persistence;

import model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkTask(String name, String objective, Boolean status, Task task) {
        assertEquals(name, task.getName());
        assertEquals(objective, task.getObjective());
        assertEquals(status, task.getCompleted());
    }
}
