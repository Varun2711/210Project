package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

//This is the TaskList class. It has one field that is a List of type Task. This is where all tasks will be stored.
//It has methods to add and delete tasks from the list, and methods for viewing all tasks and for
//filtering the completed and incomplete tasks. This class also has methods to update the status of any class and to
//view the objective of any class.
public class TaskList implements Writable {
    List<Task> taskList;
    String name;

    //EFFECTS: create a new TaskList object with an empty list
    public TaskList(String name) {
        this.name = name;
        taskList = new ArrayList<Task>();
    }

    //EFFECTS: returns a List of strings with the names of all tasks in the task list
    public List getList() {
        List<String> names = new ArrayList<String>();
        for (Task t: taskList) {
            names.add(t.getName());
        }
        return names;
    }

    //MODIFIES: this
    //EFFECTS: removes the task from the task list and returns true else returns false
    public boolean deleteTask(String nameOfTask) {
        Task t = getTask(nameOfTask);
        if (t == null) {
            return false;
        }
        taskList.remove(t);
        EventLog.getInstance().logEvent(new Event(nameOfTask + " was removed from the ToDo List"));
        return true;
    }

    //MODIFIES: this
    //EFFECTS: adds a new task object to the task list and returns true else returns false
    public boolean addTask(String name, String objective) {
        Task t = getTask(name);
        if (t == null) {
            taskList.add(new Task(name, objective));
            EventLog.getInstance().logEvent(new Event(name + " was added to the ToDo List"));
            return true;
        }
        return false;
    }


    //EFFECTS: returns the objective of a task if there is a task in the list with the name nameOfTask
    //         else returns a string stating there is no such task in the task list
    public String getObjectiveOfTask(String nameOfTask) {
        Task t = getTask(nameOfTask);
        if (!(t == null)) {
            return t.getObjective();
        }
        return "There is no task in the ToDo List with the name " + nameOfTask;
    }


    //MODIFIES: this
    //EFFECTS: if there is a task with name nameOfTask then update its status and return true else return false
    public Boolean updateTaskStatus(String nameOfTask) {
        Task t = getTask(nameOfTask);
        if (t == null) {
            return false;
        }
        t.setCompleted(!t.getCompleted());
        EventLog.getInstance().logEvent(new Event(nameOfTask + "'s status was updated"));
        return true;
    }

    //EFFECTS: returns a List of strings with the names of all tasks that are completed
    public List getCompletedTasks() {
        List<String> completedNames = new ArrayList<String>();
        for (Task t: taskList) {
            if (t.getCompleted()) {
                completedNames.add(t.getName());
            }
        }
        return completedNames;
    }

    //EFFECTS: returns a List of strings with the names of all tasks that are incomplete
    public List getIncompleteTasks() {
        List<String> incompleteNames = new ArrayList<String>();
        for (Task t: taskList) {
            if (!t.getCompleted()) {
                incompleteNames.add(t.getName());
            }
        }
        return incompleteNames;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return taskList.size();
    }

    public List getTasks() {
        List<Task> tasks = new ArrayList<Task>();
        for (Task t: taskList) {
            tasks.add(t);
        }
        return tasks;
    }

    //EFFECTS: returns true if the task is completed false otherwise
    public boolean getTaskStatus(String name) {
        Task t = getTask(name);
        if (t != null) {
            return t.getCompleted();
        }
        return false;
    }

    //EFFECTS: returns a Task from the task list with the name *name* else returns null
    private Task getTask(String name) {
        for (Task t: taskList) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }

    //MODIFIES: EventLog
    //EFFECTS: logs Events of all tasks from the previous save being added to the todolist.
    public void logLoad() {
        EventLog.getInstance().logEvent(new Event("A previous save has been loaded"));
        List<String> names = getList();
        for (String name: names) {
            EventLog.getInstance().logEvent(new Event(name + " added to the ToDo List"));
        }
    }


    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("tasks", tasksToJson());
        return json;
    }

    private JSONArray tasksToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Task t : taskList) {
            jsonArray.put(t.toJson());
        }

        return jsonArray;
    }
}
