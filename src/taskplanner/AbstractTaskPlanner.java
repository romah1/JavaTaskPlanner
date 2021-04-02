package taskplanner;

import java.util.*;

public abstract class AbstractTaskPlanner implements TaskPlanner {
    protected final ArrayDeque<Task> tasks;
    public AbstractTaskPlanner(final Collection<Task> tasks) {
        this.tasks = new ArrayDeque<>(Objects.requireNonNull(tasks));
    }
    public AbstractTaskPlanner() {
        this.tasks = new ArrayDeque<>();
    }
    @Override
    abstract public void execute();

    @Override
    public void add(final Task t) {
        this.tasks.addLast(Objects.requireNonNull(t));
    }

    @Override
    public void add(final Collection<Task> tasks) {
        for (Task t: Objects.requireNonNull(tasks)) {
            add(t);
        }
    }

    @Override
    public int inProgress() {
        return countTasks(false);
    }
    @Override
    public int completed() {
        return countTasks(true);
    }
    @Override
    public int waiting() {
        return tasks.size();
    }
    abstract public int countTasks(boolean isCompleted);
}
