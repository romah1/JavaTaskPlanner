package taskplanner;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractTaskPlanner implements TaskPlanner {
    protected final ArrayDeque<Task> tasks;
    private final ScheduledExecutorService schedule = Executors.newSingleThreadScheduledExecutor();
    public AbstractTaskPlanner(final Collection<Task> tasks) {
        this.tasks = new ArrayDeque<>(Objects.requireNonNull(tasks));
    }
    public AbstractTaskPlanner() {
        this.tasks = new ArrayDeque<>();
    }

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
    abstract protected int countTasks(boolean isCompleted);

    @Override
    public int amountOfTasks() {
        return tasks.size();
    }

    @Override
    public void clear() {
        tasks.clear();
    }

    @Override
    public void repeat(int ms) {
        schedule.scheduleAtFixedRate(new ScheduledExecution(), 0, ms, TimeUnit.MILLISECONDS);
    }
    @Override
    public void stop() {
        schedule.shutdown();
    }
    @Override
    public void stopNow() {
        schedule.shutdownNow();
    }
    @Override
    public void executeWithDelay(int ms) {
        schedule.schedule(new ScheduledExecution(), ms, TimeUnit.MILLISECONDS);
        stop();
    }
    private class ScheduledExecution implements Runnable {
        @Override
        public void run() {
            if (inProgress() == 0) {
                System.out.println(completed());
                execute();
            }
        }
    }
}
