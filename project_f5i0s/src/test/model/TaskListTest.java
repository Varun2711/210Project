package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TaskListTest {


    @Test
    void testAddTaskMethodEmptyList() {
        TaskList taskList = new TaskList("TodoList");
        boolean taskAdded = taskList.addTask("Math", "Math220");
        Assertions.assertTrue(taskAdded);
    }

    @Test
    void testAddTaskMethodSomeInList() {
        TaskList taskList = new TaskList("TodoList");
        taskList.addTask("Something", "Doing Something");
        boolean taskAdded = taskList.addTask("Study", "Finish CPSC210 Lab");
        Assertions.assertTrue(taskAdded);
    }

    @Test
    void testAddTaskMethodTaskInListWithSameName() {
        TaskList taskList = new TaskList("TodoList");
        taskList.addTask("something", "do something");
        boolean addedTask = taskList.addTask("something","doing something");
        Assertions.assertFalse(addedTask);
    }

    @Test
    void testDeleteTaskMethodTaskWithNameInList() {
        TaskList taskList = new TaskList("TodoList");
        taskList.addTask("Task 1", "do something");
        taskList.addTask("Task 2", "do something else");
        boolean deletedTask = taskList.deleteTask("Task 2");
        Assertions.assertTrue(deletedTask);
    }

    @Test
    void testDeleteTaskMethodTaskWithNameNotInList() {
        TaskList taskList = new TaskList("TodoList");
        taskList.addTask("Task 1", "do something");
        taskList.addTask("Task 2", "do something else");
        boolean deletedTask = taskList.deleteTask("Task 3");
        Assertions.assertFalse(deletedTask);
    }

    @Test
    void testGetObjectiveOfTaskMethodTaskWithNameInList() {
        TaskList taskList = new TaskList("TodoList");
        String testString = "do something";
        taskList.addTask("Task1", "do something");
        taskList.addTask("Task2", "do something else");
        String result = taskList.getObjectiveOfTask("Task1");
        Assertions.assertEquals(testString, result);
    }

    @Test
    void testGetObjectiveOfTaskMethodTaskWithNameNotInList() {
        TaskList taskList = new TaskList("TodoList");
        String testString = "There is no task in the ToDo List with the name Task 2";
        taskList.addTask("Task 1", "do something");
        String result = taskList.getObjectiveOfTask("Task 2");
        Assertions.assertEquals(testString, result);
    }

    @Test
    void testUpdateTaskStatusMethodUpdateToComplete() {
        TaskList taskList = new TaskList("TodoList");
        taskList.addTask("Task1", "do something");
        taskList.addTask("Task2", "do something else");
        boolean updatedToComplete = taskList.updateTaskStatus("Task1");
        List<String> testList = new ArrayList<String>();
        testList.add("Task1");
        Assertions.assertTrue(updatedToComplete);
        Assertions.assertEquals(testList, taskList.getCompletedTasks());
    }

    @Test
    void testUpdateTaskStatusMethodUpdateToIncomplete() {
        TaskList taskList = new TaskList("TodoList");
        taskList.addTask("Task1", "do something");
        taskList.addTask("Task2", "do something else");
        taskList.addTask("Task3", "do something new");
        taskList.updateTaskStatus("Task1");

        List<String> checkList1 = new ArrayList<String>();
        List<String> checkList2 = new ArrayList<String>();

        checkList1.add("Task1");
        checkList2.add("Task2");
        checkList2.add("Task3");

        Assertions.assertEquals(checkList1, taskList.getCompletedTasks());
        Assertions.assertEquals(checkList2, taskList.getIncompleteTasks());

        taskList.updateTaskStatus("Task1");

        List<String> testList = new ArrayList<String>();
        testList.add("Task1");
        testList.add("Task2");
        testList.add("Task3");
        Assertions.assertEquals(testList, taskList.getIncompleteTasks());
    }

    @Test
    void testUpdateTaskStatusMethodTaskNotInList() {
        TaskList taskList = new TaskList("TodoList");
        taskList.addTask("Task 1", "do something");
        taskList.addTask("Task 2", "do something else");
        taskList.addTask("Task 3", "do something new");

        boolean taskUpdated = taskList.updateTaskStatus("Task 4");
        Assertions.assertFalse(taskUpdated);

    }

    @Test
    void testGetCompletedTasksMethodNoCompletedTasksInList() {
        TaskList taskList = new TaskList("TodoList");
        taskList.addTask("Task 1", "Do something");
        taskList.addTask("Task 2", "do something else");
        taskList.addTask("Task 3", "do something new");
        List<String> testList = new ArrayList<String>();
        Assertions.assertEquals(testList, taskList.getCompletedTasks());
    }

    @Test
    void testGetCompletedTasksMethodSomeCompletedTaskInList() {
        TaskList taskList = new TaskList("TodoList");

        taskList.addTask("Task1", "do something");
        taskList.addTask("Task2", "do something else");
        taskList.addTask("Task3", "do something new");

        taskList.updateTaskStatus("Task1");
        taskList.updateTaskStatus("Task2");

        List<String> testList = new ArrayList<String>();
        testList.add("Task1");
        testList.add("Task2");

        Assertions.assertEquals(testList, taskList.getCompletedTasks());
    }

    @Test
    void testGetIncompleteTasksMethodNoneInList() {
        TaskList taskList = new TaskList("TodoList");
        List<String> testList = new ArrayList<String>();
        Assertions.assertEquals(testList, taskList.getIncompleteTasks());
    }

    @Test
    void testGetIncompleTasksMethodSomeIncompleteInList ()  {
        TaskList taskList = new TaskList("TodoList");

        taskList.addTask("Task 1", "do something");
        taskList.addTask("Task 2", "do something else");
        taskList.addTask("Task 3", "do something new");

        taskList.updateTaskStatus("Task 1");
        taskList.updateTaskStatus("Task 2");

        List<String> testList = new ArrayList<String>();
        testList.add("Task 3");

        Assertions.assertEquals(testList, taskList.getIncompleteTasks());
    }

    @Test
    void testGetIncompleteTasksMethodAllTasksCompletedInList() {
        TaskList taskList = new TaskList("TodoList");

        taskList.addTask("Task 1", "do something");
        taskList.addTask("Task 2", "do something else");
        taskList.addTask("Task 3", "do something new");

        taskList.updateTaskStatus("Task 1");
        taskList.updateTaskStatus("Task 2");
        taskList.updateTaskStatus("Task 3");

        List<String> testList = new ArrayList<String>();
        Assertions.assertEquals(testList, taskList.getIncompleteTasks());

    }

    @Test
    void testGetListMethodEmpty() {
        TaskList taskList = new TaskList("TodoList");
        List<String> testList= new ArrayList<String>();
        Assertions.assertEquals(testList, taskList.getList());
    }

    @Test
    void testGetListMethodSomeInList() {
        TaskList taskList = new TaskList("TodoList");
        List<String> testList = new ArrayList<String>();
        for (int i = 0; i<5; i++) {
            taskList.addTask(Integer.toString(i), "have fun!");
            testList.add(Integer.toString(i));
        }
        Assertions.assertEquals(testList, taskList.getList());
        System.out.println("Hello world!");
    }

    @Test
    void testGetName() {
        TaskList taskList = new TaskList("taskList");
        Assertions.assertEquals("taskList", taskList.getName());
    }

    @Test
    void testGetSizeNoTasksInTaskList() {
        TaskList taskList = new TaskList("taskList");
        Assertions.assertEquals(0, taskList.getSize());
    }

    @Test
    void testGetSizeOneTaskInTaskList() {
        TaskList taskList = new TaskList("taskList");
        taskList.addTask("Task 1", "do something");
        Assertions.assertEquals(1, taskList.getSize());
    }

    @Test
    void testGetSizeManyTasksInTaskList() {
        TaskList taskList = new TaskList("taskList");
        taskList.addTask("Task 1", "do something");
        taskList.addTask("Task 2", "do something else");
        taskList.addTask("Task 3", "do something new");
        Assertions.assertEquals(3, taskList.getSize());
    }

    @Test
    void testGetTasksEmptyTaskList() {
        TaskList taskList = new TaskList("taskList");
        List<Task> tasks = new ArrayList<Task>();
        Assertions.assertEquals(tasks, taskList.getTasks());
    }

    @Test
    void testGetTaskNonEmptyTaskList() {
        TaskList taskList = new TaskList("taskList");
        List<Task> tasks = new ArrayList<Task>();
        for (int i = 0; i < 5; i++) {
            taskList.addTask(Integer.toString(i), Integer.toString(i));
            tasks.add(new Task((Integer.toString(i)), Integer.toString(i)));
            if (i%2 ==1) {
                tasks.get(i).setCompleted(true);
                taskList.updateTaskStatus(Integer.toString(i));
            }
        }

        List<Task> result = taskList.getTasks();

        for (int i = 0; i < taskList.getSize(); i++) {
            Assertions.assertEquals(tasks.get(i).getName(), result.get(i).getName());
            Assertions.assertEquals(tasks.get(i).getObjective(), result.get(i).getObjective());
            Assertions.assertEquals(tasks.get(i).getCompleted(), result.get(i).getCompleted());
        }
    }

    @Test
    void testGetTaskStatus() {
        TaskList tasklist = new TaskList("taskList");
        tasklist.addTask("task1", "do something");
        tasklist.addTask("task2", "do something else");
        tasklist.updateTaskStatus("task1");
        Assertions.assertTrue(tasklist.getTaskStatus("task1"));
        Assertions.assertFalse(tasklist.getTaskStatus("task2"));
        Assertions.assertFalse(tasklist.getTaskStatus("task3"));

    }
}
