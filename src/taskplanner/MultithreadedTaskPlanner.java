package taskplanner;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultithreadedTaskPlanner extends AbstractTaskPlanner {
    private final LinkedList<Future<String>> invokedTasks = new LinkedList<>();

    public MultithreadedTaskPlanner(final Collection<Task> tasks) {
        super(tasks);
    }

    public MultithreadedTaskPlanner() {
        super();
    }

    @Override
    public void execute() {
        ExecutorService mainExecutor = Executors.newCachedThreadPool();
        for (Task t: this.tasks) {
            invokedTasks.addLast(mainExecutor.submit(new TaskExecutor(t)));
        }
        this.tasks.clear();
        mainExecutor.shutdown();
    }

    @Override
    public int countTasks(boolean isCompleted) {
        int c = 0;
        for (Future<String> t: invokedTasks) {
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
                ", waiting=" + waiting() +
                '}';
    }


    private static class TaskExecutor implements Callable<String> {
        private final Task mainTask;

        public TaskExecutor(final Task t) {
            this.mainTask = Objects.requireNonNull(t);
        }

        @Override
        public String call() throws Exception {
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
            return Thread.currentThread().getName();
        }
    }
}
