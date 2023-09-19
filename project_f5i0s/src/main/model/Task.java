package model;

import org.json.JSONObject;
import persistence.Writable;

import java.nio.file.Watchable;

// This is a Task class, it has 3 fields: name, objective, and completed. name and objective are Strings that
// represents the name of the Task and the description of things to be completed to complete the task respectively.
// The completed field is a boolean that represents the status of the task, with status here being either: completed
// or incomplete.
public class Task implements Writable {

    private String name;
    private String objective;
    private Boolean completed;

    //EFFECTS: Constructs as new Task object with a name and objective and an incomplete status.
    public Task(String name, String objective) {
        this.name = name;
        this.objective = objective;
        completed = false;
    }

    //EFFECTS: returns the name of the task
    public String getName() {
        return name;
    }

    //EFFECTS: returns the objective of the task
    public String getObjective() {
        return objective;
    }

    //REQUIRES: isComplete is true or isComplete is false
    //MODIFIES: this
    //EFFECTS: sets the value of the completed field to isComplete
    public void setCompleted(Boolean isComplete) {
        completed = isComplete;
    }

    //EFFECTS: returns the value of the completed field
    public boolean getCompleted() {
        return completed;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("objective", objective);
        json.put("status", completed);
        return json;
    }
}
