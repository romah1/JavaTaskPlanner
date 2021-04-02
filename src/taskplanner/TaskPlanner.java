package taskplanner;
import java.util.Collection;


public interface TaskPlanner {
    // executes all tasks in the queue
    public void execute();

    // adds one task to the queue
    public void add(final Task t);

    // adds a Collection os tasks in the queue
    public void add(final Collection<Task> tasks);

    // counts completed tasks
    public int completed();

    // counts tasks which are in progress
    public int inProgress();

    // counts tasks that are waiting in the queue
    public int waiting();
}
