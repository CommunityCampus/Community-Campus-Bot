package core;

import java.util.HashMap;
import java.util.Scanner;

public class Console {

    private static final HashMap<String, ConsoleTask> tasks = new HashMap<>();

    static {
        registerTasks();
    }

    public static void start() {
        Thread t = new Thread(Console::manageConsole, "Console");
        t.setDaemon(true);
        t.start();
    }

    private static void registerTasks() {
        tasks.put("help", Console::onHelp);

        tasks.put("quit", Console::onQuit);
        tasks.put("stats", Console::onStats);
        tasks.put("memory", Console::onMemory);
        tasks.put("threads", Console::onThreads);
        tasks.put("threads_interrupt", Console::onThreadsInterrupt);
        tasks.put("disconnect", Console::onDisconnect);
    }

    private static void onThreadsInterrupt(String[] args) {
        int stopped = 0;

        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (args.length < 2 || t.getName().matches(args[1])) {
                t.interrupt();
                stopped++;
            }
        }

        MainLogger.get().info("{} thread/s interrupted", stopped);
    }

    private static void onThreads(String[] args) {
        StringBuilder sb = new StringBuilder();

        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (args.length < 2 || t.getName().matches(args[1])) {
                sb.append(t.getName()).append(", ");
            }
        }

        String str = sb.toString();
        if (str.length() >= 2) str = str.substring(0, str.length() - 2);

        MainLogger.get().info("\n--- THREADS ({}) ---\n{}\n", Thread.getAllStackTraces().size(), str);
    }

    private static void onStats(String[] args) {
        MainLogger.get().info(getStats());
    }

    private static void onMemory(String[] args) {
        MainLogger.get().info(getMemory());
    }

    private static void onQuit(String[] args) {
        MainLogger.get().info("EXIT - Stopping Program");
        System.exit(0);
    }

    private static void onDisconnect(String[] args) {
        MainLogger.get().info("Disconnecting...");
        Main.getJda().shutdownNow();
        // DBMain.getInstance().disconnect();
        System.exit(0);
    }

    private static void onHelp(String[] args) {
        tasks.keySet().stream()
                .filter(key -> !key.equals("help"))
                .sorted()
                .forEach(key -> System.out.println("- " + key));
    }

    private static void manageConsole() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.length() > 0) {
                    processInput(line);
                }
            }
        }
    }

    public static void processInput(String input) {
        String[] args = input.split(" ");
        ConsoleTask task = tasks.get(args[0]);
        if (task != null) {
            GlobalThreadPool.getExecutorService().submit(() -> {
                try {
                    task.process(args);
                } catch (Throwable throwable) {
                    MainLogger.get().error("Console task {} ended with exception", args[0], throwable);
                }
            });
        } else {
            System.err.printf("No result for \"%s\"\n", args[0]);
        }
    }

    public static String getMemory() {
        StringBuilder sb = new StringBuilder();
        double memoryTotal = Runtime.getRuntime().totalMemory() / (1024.0 * 1024.0);
        double memoryUsed = memoryTotal - (Runtime.getRuntime().freeMemory() / (1024.0 * 1024.0));
        sb.append("Memory: ")
                .append(String.format("%1$.2f", memoryUsed))
                .append(" / ")
                .append(String.format("%1$.2f", memoryTotal))
                .append(" MB");

        return sb.toString();
    }

    public static String getStats() {
        String header = "--- STATS Program ---";
        StringBuilder sb = new StringBuilder("\n" + header + "\n");

        // heap memory
        double memoryTotal = Runtime.getRuntime().totalMemory() / (1024.0 * 1024.0);
        double memoryUsed = memoryTotal - (Runtime.getRuntime().freeMemory() / (1024.0 * 1024.0));
        sb.append("Heap Memory Memory: ")
                .append(String.format("%1$.2f", memoryUsed))
                .append(" / ")
                .append(String.format("%1$.2f", memoryTotal))
                .append(" MB\n");

        // threads
        sb.append("Threads: ")
                .append(Thread.getAllStackTraces().keySet().size())
                .append("\n");

        sb.append("-".repeat(header.length()))
                .append("\n");
        return sb.toString();
    }

    public interface ConsoleTask {

        void process(String[] args) throws Throwable;

    }

}