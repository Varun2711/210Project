package ui;


import model.Event;
import model.EventLog;
import model.TaskList;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

//This is the ToDoListApp class. It is the user interface for the app. Its purpose is to acquire input from the end user
//and process it and then give them an output, for example user wants to add a task, so they enter "a" in the terminal
//then the scanner in the class picks it up, and then the class processes the command, and then asks the user for
//further necessary inputs (name and objective) and then calls methods from the toDoList class to add a new Task with
// name and objective specified by the user to the task list.
public class ToDoListApp extends JFrame implements ActionListener {
    private TaskList toDoList;
    private static final String[] COMMANDS = {"a", "r", "s", "c", "i", "l", "o", "save", "load", "x"};
    private static final String PATH = "./data/ToDoList.json";
    private JsonReader reader = new JsonReader(PATH);
    private JsonWriter writer = new JsonWriter(PATH);

    private JPanel listPanel;
    private JPanel interactivePanel;

    private String selectedTask;

    private JButton addButton;
    private JButton removeButton;
    private JButton updateButton;
    private JButton saveButton;

    private JLabel titleLabel;

    private JTextArea descriptionArea;


    private JRadioButton viewAllButton;
    private JRadioButton viewCompletedButton;
    private JRadioButton viewIncompleteButton;

    private JList list;
    private DefaultListModel dm = new DefaultListModel();


    //EFFECTS: instantiates the ToDoListApp
    public ToDoListApp() {
        init();
    }


    //EFFECTS: initializes the toDoList as an empty TaskList object and prints the commands
    public void init() {
        toDoList = new TaskList("My ToDo List");
        System.out.println("ToDo List App\n");
        String message = "Would you like to load your previously saved todo list?";
        int choice = JOptionPane.showConfirmDialog(this, message, message, JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            loadList();
        }
        selectedTask = "";
        initGui();
    }

    //EFFECTS: initializes the GUI
    private void initGui() {
        setTitle("ToDo List App");
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(1500, 1200));
        setupListPanel();
        setupInteractivePanel();
        this.setVisible(true);
        this.setResizable(true);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (Event event : EventLog.getInstance()) {
                    System.out.println(event.toString());
                }
                System.exit(0);
            }
        });
        this.pack();
        viewAllButton.doClick();
    }

    //EFFECTS: initializes the listPanel and add it to the jframe
    private void setupListPanel() {
        listPanel = new JPanel();
        listPanel.setPreferredSize(new Dimension(600, 1200));
        listPanel.setBackground(new Color(23,53,123));
        setupList();
        this.add(listPanel, BorderLayout.WEST);
    }

    //EFFECTS: initalizes sets up list;
    private void setupList() {
        list = new JList(toDoList.getList().toArray());
        list.setFont(new Font("Times Roman", Font.PLAIN, 50));
        list.setModel(dm);
        list.getSelectionModel().addListSelectionListener(e -> {
            int indexOfSelection = list.getSelectedIndex();

            if (indexOfSelection == -1) {
                indexOfSelection = 0;
            }

            selectedTask = dm.get(indexOfSelection).toString();
            updateFields();
        });


        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(580, 1200));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        listPanel.add(scrollPane, BorderLayout.CENTER);
    }

    //EFFECTS: initializes the interactivePanel and adds it to the jframe
    private void setupInteractivePanel() {
        interactivePanel = new JPanel();
        interactivePanel.setPreferredSize(new Dimension(900, 1200));
        interactivePanel.setBackground(new Color(234,53,23));
        setupTitleAndLogo();
        setupFields();
        setupButtons();
        this.add(interactivePanel, BorderLayout.EAST);
    }

    //EFFECTS: initializes the titleLabel and adds it to the interactive panel
    private void setupTitleAndLogo() {
        titleLabel = new JLabel("ToDo List App");
        ImageIcon image = new ImageIcon("./data/logo.png");

        titleLabel.setFont(new Font("Arial", Font.ITALIC, 80));
        titleLabel.setIcon(image);
        titleLabel.setPreferredSize(new Dimension(880, 290));
        JPanel logoPanel = new JPanel();
        logoPanel.setPreferredSize(new Dimension(900, 300));
        logoPanel.setBackground(new Color(245,53,123));
        logoPanel.add(titleLabel);
        interactivePanel.add(logoPanel, BorderLayout.NORTH);

    }

    //EFFECTS: initializes the descriptionArea and adds it to the interactive panel
    private void setupFields() {
        descriptionArea = new JTextArea(5,20);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setPreferredSize(new Dimension(880, 390));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        descriptionArea.setEditable(false);
        descriptionArea.setFont(new Font("Times Roman", Font.PLAIN, 50));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setText("Name: \n\nObjective: \n\nStatus: ");
        descriptionArea.setBackground(new Color(245,53,123));

        JPanel viewPanel = new JPanel();
        viewPanel.setPreferredSize(new Dimension(900, 400));
        viewPanel.add(scrollPane);

        interactivePanel.add(viewPanel, BorderLayout.SOUTH);
    }

    //EFFECTS: changes the information in the descriptionArea to the
    //         information of the selected task
    private void updateFields() {
        String name = selectedTask;
        String objective = getTaskObjective();
        String status;
        if (toDoList.getTaskStatus(selectedTask)) {
            status = "Completed";
        } else {
            status = "Incomplete";
        }
        if (name.equals("")) {
            descriptionArea.setText("Name: \n\nObjective: \n\nStatus: ");
        } else {
            descriptionArea.setText("Name: " + name + "\n\nObjective:" + objective + "\n\nStatus: " + status);
        }

    }

    //EFFECTS: initializes the required buttons for GUI and adds them to the interactive panel
    private void setupButtons() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setPreferredSize(new Dimension(900, 500));
        buttonsPanel.setLayout(new BorderLayout());

        JPanel viewButtonsPanel = new JPanel();
        viewButtonsPanel.setLayout(new GridLayout(3, 1, 0, 0));
        viewButtonsPanel.setPreferredSize(new Dimension(300, 500));

        JPanel changeButtonsPanel = new JPanel();
        changeButtonsPanel.setPreferredSize(new Dimension(600, 500));

        changeButtonsPanel.setBackground(new Color(235,235,123));

        addChangeButtons(changeButtonsPanel);
        addViewButtons(viewButtonsPanel);

        buttonsPanel.add(viewButtonsPanel, BorderLayout.WEST);
        buttonsPanel.add(changeButtonsPanel, BorderLayout.EAST);

        interactivePanel.add(buttonsPanel);
    }

    //MODIFIES: this
    //EFFECTS: initializes the radio buttons for the view filters
    private void addViewButtons(JPanel panel) {
        ButtonGroup group = new ButtonGroup();
        viewAllButton = new JRadioButton("View all");
        viewCompletedButton = new JRadioButton("View completed");
        viewIncompleteButton = new JRadioButton("View incomplete");

        Font font = new Font("Times Roman", Font.ITALIC, 30);
        viewAllButton.setFont(font);
        viewIncompleteButton.setFont(font);
        viewCompletedButton.setFont(font);

        viewAllButton.setBackground(new Color(235,235,123));
        viewIncompleteButton.setBackground(new Color(235,235,123));
        viewCompletedButton.setBackground(new Color(235,235,123));

        group.add(viewCompletedButton);
        group.add(viewAllButton);
        group.add(viewIncompleteButton);

        viewAllButton.addActionListener(this);
        viewCompletedButton.addActionListener(this);
        viewIncompleteButton.addActionListener(this);

        viewAllButton.setActionCommand("view all");
        viewCompletedButton.setActionCommand("view completed");
        viewIncompleteButton.setActionCommand("view incomplete");

        panel.add(viewAllButton);
        panel.add(viewCompletedButton);
        panel.add(viewIncompleteButton);
    }

    //MODIFIES: this
    //EFFECTS: initializes the buttons for saving, adding, removing and updating tasks
    @SuppressWarnings({"checkstyle:SuppressWarnings", "checkstyle:MethodLength"})
    private void addChangeButtons(JPanel panel) {
        addButton = new JButton("Add");
        removeButton = new JButton("Remove");
        updateButton = new JButton("Update");
        saveButton = new JButton("Save");
        Font font = new Font("Times Roman", Font.PLAIN, 40);

        addButton.setPreferredSize(new Dimension(200, 100));
        addButton.addActionListener(this);
        addButton.setFont(font);

        removeButton.setPreferredSize(new Dimension(200, 100));
        removeButton.addActionListener(this);
        removeButton.setFont(font);

        updateButton.setPreferredSize(new Dimension(200, 100));
        updateButton.addActionListener(this);
        updateButton.setFont(font);

        saveButton.setPreferredSize(new Dimension(200, 100));
        saveButton.addActionListener(this);
        saveButton.setFont(font);

        addButton.setActionCommand("add");
        removeButton.setActionCommand("remove");
        updateButton.setActionCommand("update");
        saveButton.setActionCommand("save");

        panel.add(saveButton, BorderLayout.NORTH);
        panel.add(addButton, BorderLayout.EAST);
        panel.add(removeButton, BorderLayout.WEST);
        panel.add(updateButton, BorderLayout.SOUTH);
    }

    //EFFECTS: makes the jlist display all tasks in the todolist
    private void changeListToViewAll() {
        list.clearSelection();
        selectedTask = "";
        List<String> names = toDoList.getList();
        for (String name: names) {
            if (!dm.contains(name)) {
                dm.addElement(name);
            }
        }
    }

    //EFFECTS: makes the jlist display all completed tasks in the todolist
    private void changeListToViewCompleted() {
        list.clearSelection();
        selectedTask = "";
        if (!(dm.size() == 0)) {
            List<String> names = toDoList.getList();
            List<String> completedNames = toDoList.getCompletedTasks();
            for (String name : names) {
                if (dm.contains(name) && !(completedNames.contains(name))) {
                    dm.removeElement(name);
                } else if (!dm.contains(name) && completedNames.contains(name)) {
                    dm.addElement(name);
                }
            }
        }
    }

    //EFFECTS: makes the jlist display all the incomplete tasks in the todolist
    private void changeListToViewIncomplete() {
        list.clearSelection();
        selectedTask = "";
        if (!(dm.size() == 0)) {
            List<String> names = toDoList.getList();
            List<String> incompleteNames = toDoList.getIncompleteTasks();
            for (String name : names) {
                if (dm.contains(name) && !(incompleteNames.contains(name))) {
                    dm.removeElement(name);
                } else if (!dm.contains(name) && incompleteNames.contains(name)) {
                    dm.addElement(name);
                }
            }
        }
    }

    //EFFECTS: save the todolist to a json file
    private void saveList() {
        try {
            writer.open();
            writer.write(toDoList);
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("File you are trying to save to does not exist");
        }
    }

    //MODIFIES: this
    //EFFECTS: retrieves the save data of the todolist from the json file and assigns it to todolist
    private void loadList() {
        try {
            toDoList = reader.read();
            toDoList.logLoad();
        } catch (IOException e) {
            System.out.println("An Error has occurred. The file you are trying to read from does not exist.");
        }
    }

    //MODIFIES: this
    //EFFECTS: if a Task with the name and objective of the user's choice is added to the task list successfully
    // let the user know else, let them know that a task with the name they have chosen already exists in the task list
    private void addTaskToTaskList() {
        String name = getNameFromUser();
        if (name.equals("")) {
            JOptionPane.showMessageDialog(this,"canceled opration");
        } else {
            String description = getObjective();
            boolean added = toDoList.addTask(name, description);
            if (added) {
                System.out.println("Task added successfully to the ToDo List!\n");
                dm.addElement(name);
            } else {
                System.out.println("There is already a task in the ToDo list with the same name "
                        + "please choose a different name.");
            }
        }

    }

    //MODIFIES: this
    //EFFECTS: if there is a task with the name specified by the user, remove it and let the user know, else
    //         let the user know that, no task with the name given exists in the task list.
    private void removeTaskFromTaskList() {
        boolean removed = toDoList.deleteTask(selectedTask);
        if (removed) {
            System.out.println("Task removed successfully!");
            list.clearSelection();
            dm.removeElement(selectedTask);
            selectedTask = "";
            updateFields();
        } else {
            System.out.println("Task not found. Please make sure that you have the right name of the task "
                    + "you wish to remove.\n");
        }
    }

    //EFFECTS: print to objective of the Task with the name specified by the user if such task exists in task list
    //         else let them know that no such task exists.
    private String getTaskObjective() {
        return toDoList.getObjectiveOfTask(selectedTask);
    }

    //MODIFIES: this
    //EFFECTS:  if a task with the name specified by the user exists in the task list, update its status and let the
    //          user know so, else let the user know that no such task with the given name exists in the task list.
    private void updateStatusOfTask() {
        String name = getNameFromUser();
        boolean updated = toDoList.updateTaskStatus(name);
        if (updated) {
            System.out.println("Task status was updated successfully!");
        } else {
            System.out.println("Task not found. Please make sure that you have the right name of the task "
                    + "you wish to to remove.\n");
        }
    }

    //EFFECT: acquire a proper name for a task from the user. proper here being not an empty string.
    private String getNameFromUser() {
        String name = JOptionPane.showInputDialog(this, "Please enter a name for your task: ");
        if (name != null) {
            while (name.length() == 0) {
                name = JOptionPane.showInputDialog(this, "Please enter a Valid name!: ");
            }
            return name;
        }
        return "";
    }

    //EFFECTS: acquire a proper objective for a task from the user. proper here being not an empty string.
    private String getObjective() {
        String description = JOptionPane.showInputDialog(this,
                "Please enter the objective of your task: ");
        if (description != null) {
            while (description.length() == 0) {
                description = JOptionPane.showInputDialog(this, "Please enter a valid objective: ");
            }
            return description;
        }
        return "";
    }


    //EFFECTS: performs actions as required by the user input
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("view all")) {
            changeListToViewAll();
        } else if (e.getActionCommand().equals("view completed")) {
            changeListToViewCompleted();
        } else if (e.getActionCommand().equals("view incomplete")) {
            changeListToViewIncomplete();
        } else if (e.getActionCommand().equals("add")) {
            addTaskToTaskList();
        } else if (e.getActionCommand().equals("remove")) {
            removeTaskFromTaskList();
        } else if (e.getActionCommand().equals("update")) {
            updateStatusOfTask();
        } else if (e.getActionCommand().equals("save")) {
            saveList();
        }
    }
}
