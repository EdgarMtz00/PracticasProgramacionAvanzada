package com.company.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskManager {
    private static List<Thread> workerThreads;
    private final static BlockingQueue<Task> pendingTasks;
    private static Logger taskManagerLogger;

    static {
        taskManagerLogger = Logger.getLogger("TaskManager");
        workerThreads = new ArrayList<>();
        pendingTasks = new LinkedBlockingQueue<>();

        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            Thread t = new Thread(() -> {
                while (true) {
                    Task task = null;
                    try {
                        task = pendingTasks.take();
                    } catch (InterruptedException e) {
                        taskManagerLogger.log(Level.SEVERE, e.getMessage());
                    }
                    if (task != null) {
                        task.execute();
                    }
                }
            });
            workerThreads.add(t);
            t.start();
        }
    }

    public static void enqueue(Task task) {
        try {
            pendingTasks.put(task);
        } catch (InterruptedException e) {
            taskManagerLogger.log(Level.SEVERE, e.getMessage());
        }
    }
}
