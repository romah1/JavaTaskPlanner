package taskplanner;

import java.util.Collection;

interface Task {
    void execute();

    Collection<Task> dependencies();
}