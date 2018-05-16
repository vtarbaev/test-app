package test.app.cft2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        final int groupCount = 6;
        final int processCount = 4;
        final Queue sourceQueue = new Queue(-1);

        ExecutorService producerService = Executors.newSingleThreadExecutor();
        ExecutorService routerService = Executors.newSingleThreadExecutor();

        producerService.submit(new Producer(sourceQueue, groupCount));
        routerService.submit(new Router(sourceQueue, groupCount, processCount));

        producerService.shutdown();
        routerService.shutdown();
    }

}
