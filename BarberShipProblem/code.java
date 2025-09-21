import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

public class BarberShop {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final int[] waiting = new int[3];
    private int waitingCount = 0;
    private static final int NUM_CHAIRS = 3;
    private Random random = new Random();

    public void barber() {
        while (true) {
            lock.lock();
            try {
                while (waitingCount == 0) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int cust = waiting[0];
                for (int i = 0; i < waitingCount - 1; ++i) {
                    waiting[i] = waiting[i + 1];
                }
                waitingCount--;
                System.out.println("Barber cutting hair of customer " + cust);
            } finally {
                lock.unlock();
            }
            try {
                Thread.sleep(random.nextInt(3) + 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Barber finished with customer " + cust);
            lock.lock();
            try {
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    public void customer(int i) {
        try {
            Thread.sleep(random.nextInt(4) + 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.lock();
        try {
            if (waitingCount < NUM_CHAIRS) {
                waiting[waitingCount++] = i;
                System.out.println("Customer " + i + " waiting");
                condition.signal();
                while (waiting[0] != i) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Customer " + i + " got haircut");
            } else {
                System.out.println("Customer " + i + " left (no seat)");
            }
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        BarberShop barberShop = new BarberShop();
        Thread barberThread = new Thread(() -> barberShop.barber());
        barberThread.start();
        for (int i = 0; i < 5; ++i) {
            new Thread(() -> barberShop.customer(i)).start();
        }
        try {
            barberThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
