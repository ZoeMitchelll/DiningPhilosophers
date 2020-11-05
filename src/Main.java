import java.util.concurrent.Semaphore;  //Required
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static Semaphore[] fork = new Semaphore[5];
    static boolean[] endOfWhile = new boolean[5];

    static public void Sleep(int ms)
    {
        try{
            Thread.sleep(ms);
        }
        catch (Exception e) {}
    }
    static public class Philosopher extends Thread {
        //Represents reader process
        private static final AtomicInteger philosopherCounter = new AtomicInteger(0);
        private final int ID;
        private static int fullCounter = 0;

        public Philosopher() {
            ID = philosopherCounter.incrementAndGet();
        }

        public void run() {
            //Read from array and print its contents to the screen
            do {
                if (fork[ID % fork.length].tryAcquire()) {
                    fork[ID % fork.length].release();
                    try {
                        fork[ID % fork.length].acquire();
                        System.out.println("Philosopher " + ID + " picked up fork " + ID % fork.length+".");
                        if (fork[(ID + 1) % fork.length].tryAcquire()) {
                            fork[(ID + 1) % fork.length].release();
                            try {
                                fork[(ID + 1) % fork.length].acquire();
                                System.out.println("Philosopher " + ID + " picked up fork " + (ID + 1) % fork.length+".");
                                System.out.println("Philosopher " + ID + " is now eating.");
                                fullCounter++;
                                Sleep(500);
                                System.out.println("Philosopher " + ID + " put down fork " + (ID + 1) % fork.length+".");
                            } catch (InterruptedException e) { }
                        } else {
                            Sleep(500);
                        }
                    } catch (InterruptedException e) { }
                    finally {
                        fork[ID % fork.length].release();
                        fork[(ID + 1) % fork.length].release();
                        System.out.println("Philosopher " + ID + " put down fork " + ID % fork.length+".");
                        System.out.println("Philosopher " + ID + " is now thinking.");
                    }
                } else {
                    System.out.println("Philosopher " + ID + " is now thinking.");
                    Sleep(500);
                }
            } while (fullCounter<5);

        }
    }
    public static void main(String[] args)
    {
        fork[0] = new Semaphore(1);
        fork[1] = new Semaphore(1);
        fork[2] = new Semaphore(1);
        fork[3] = new Semaphore(1);
        fork[4] = new Semaphore(1);

        endOfWhile[0] = false;
        endOfWhile[1] = false;
        endOfWhile[2] = false;
        endOfWhile[3] = false;
        endOfWhile[4] = false;

        Philosopher p1 = new Philosopher();
        Philosopher p2 = new Philosopher();
        Philosopher p3 = new Philosopher();
        Philosopher p4 = new Philosopher();
        Philosopher p5 = new Philosopher();

        p1.start();
        p2.start();
        p3.start();
        p4.start();
        p5.start();

    }
}
