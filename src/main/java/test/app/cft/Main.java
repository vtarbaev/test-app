package test.app.cft;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        PriorityBlockingQueue<Integer> q = new PriorityBlockingQueue<>();
        q.add(200);
        q.add(201);
        q.add(202);
        q.add(203);
        q.add(204);
        q.add(205);
        q.add(100);


        try {
            while (true) {
                Integer v = q.poll(10, TimeUnit.MILLISECONDS);
                if (v == null) {
                    break;
                }
                System.out.println(v);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        int groupCount = 6;
        int processorCount = 4;

        ExecutorService service1 = Executors.newSingleThreadExecutor();
        ExecutorService service2 = Executors.newSingleThreadExecutor();

        ItemQueue queue = new ItemQueue();

        service1.submit(new Producer(queue, groupCount));
        service2.submit(new MainProcessor(queue, groupCount, processorCount));

        service1.shutdown();
        service2.shutdown();


        System.out.println("Waiting ...");

    }

}
