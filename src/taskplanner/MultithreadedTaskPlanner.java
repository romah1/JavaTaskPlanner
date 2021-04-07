package taskplanner;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.*;

public class MultithreadedTaskPlanner extends AbstractTaskPlanner {
    protected final LinkedList<Future<Task>> invokedTasks = new LinkedList<>();
    public MultithreadedTaskPlanner(final Collection<Task> tasks) {
        super(tasks);
    }

    public MultithreadedTaskPlanner() {
        super();
    }


    @Override
    public void execute() {
        ExecutorService mainExecutor = Executors.newCachedThreadPool();
        for (Task t: tasks){
            invokedTasks.addLast(mainExecutor.submit(new TaskExecutor(t)));
        }
        mainExecutor.shutdown();
    }

    @Override
    public int countTasks(boolean isCompleted) {
        int c = 0;
        for (Future<Task> t: invokedTasks) {
            if (t.isDone() == isCompleted) {
                c++;
            }
        }
        return c;
    }

    @Override
    public String toString() {
        return "MultithreadedTaskPlanner{" +
                "completed=" + completed() +
                ", in progress=" + inProgress() +
                '}';
    }


    private static class TaskExecutor implements Callable<Task> {
        private final Task mainTask;

        public TaskExecutor(final Task t) {
            this.mainTask = Objects.requireNonNull(t);
        }

        @Override
        public Task call() throws Exception {
            if (mainTask.dependencies() != null) {
                ExecutorService executor = Executors.newCachedThreadPool();
                LinkedList<TaskExecutor> dependencies = new LinkedList<>();
                for (Task t: mainTask.dependencies()) {
                    dependencies.add(new TaskExecutor(t));
                }
                executor.invokeAll(dependencies);
                executor.shutdown();
            }
            mainTask.execute();
            return mainTask;
        }
    }
}
