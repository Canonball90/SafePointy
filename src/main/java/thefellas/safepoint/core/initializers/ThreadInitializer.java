package thefellas.safepoint.core.initializers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadInitializer {
    protected ExecutorService executorService = Executors.newFixedThreadPool(2);

    public void run(Runnable command) {
        try {
            executorService.execute(command);
        } catch (Exception ignored){
        }
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}
