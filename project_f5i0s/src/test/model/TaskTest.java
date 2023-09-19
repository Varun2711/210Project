package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class TaskTest {

    @Test
    void testGetNameMethod() {
        Task t1 = new Task("Task 1", "do something");
        Assertions.assertEquals("Task 1", t1.getName());
    }

    @Test
    void testGetObjectiveMethod() {
        Task t2 = new Task("Task 2", "do something else");
        Assertions.assertEquals("do something else", t2.getObjective());
    }

    @Test
    void testGetCompletedMethodFalse() {
        Task t3 = new Task("Task 3", "do something new");
        Assertions.assertFalse(t3.getCompleted());
    }

    @Test
    void testGetCompletedMethodTrue() {
        Task t4 = new Task("Task 4", "I'm doing something new!");
        t4.setCompleted(true);
        Assertions.assertTrue(t4.getCompleted());
    }

    @Test
    void testSetCompletedMethodTrue() {
        Task t5 = new Task("Task 5", "I'm bored now");
        t5.setCompleted(true);
        Assertions.assertTrue(t5.getCompleted());
    }

    @Test
    void testSetCompletedMethodFalse() {
        Task t6 = new Task(" Task 6", "study");
        t6.setCompleted(true);
        t6.setCompleted(false);
        Assertions.assertFalse(t6.getCompleted());
    }
}
