package com.company.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que gestiona las tareas multihilo
 */
public class TaskManager {
    /**
     * Hilos que ejecutarán tareas
     */
    private static List<Thread> workerThreads;
    /**
     * Tareas pendientes
     */
    private final static BlockingQueue<Task> pendingTasks;
    /**
     * Logger que imprimirá cosas que sucedan en el servidor
     */
    private static Logger taskManagerLogger;
    /**
     * Número minimo de hilos
     */
    private static final int MIN_WORKER_THREADS = 8;

    static {
        taskManagerLogger = Logger.getLogger("TaskManager");
        workerThreads = new ArrayList<>();
        pendingTasks = new LinkedBlockingQueue<>();
        int numberOfProcessors = Runtime.getRuntime().availableProcessors();

        for (int i = 0; i < (numberOfProcessors < MIN_WORKER_THREADS ? MIN_WORKER_THREADS : numberOfProcessors * 2); i++) {
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

    /**
     * Encola una tarea
     * @param task tarea que se ejecutará en un hilo
     */
    public static void enqueue(Task task) {
        try {
            pendingTasks.put(task);
        } catch (InterruptedException e) {
            taskManagerLogger.log(Level.SEVERE, e.getMessage());
        }
    }
}
