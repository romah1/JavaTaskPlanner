package taskplanner;
import java.util.Collection;


public interface TaskPlanner {
    // executes all tasks in the queue
    public void execute();

    // adds one task to the queue
    public void add(final Task t);

    // repeat with interval
    public void repeat(int ms);

    // remove all tasks
    public void clear();

    // get amount of tasks
    public int amountOfTasks();

    // stop repeating (finishes all tasks)
    public void stop();

    // stop now
    public void stopNow();

    // execute after ms
    public void executeWithDelay(int ms);

    // adds a Collection of tasks in the queue
    public void add(final Collection<Task> tasks);

    // counts completed tasks
    public int completed();

    // counts tasks which are in progress
    public int inProgress();
}
