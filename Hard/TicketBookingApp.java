package Hard;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class TicketBookingSystem {
    private int availableSeats;
    private final Lock lock = new ReentrantLock();

    public TicketBookingSystem(int seats) {
        this.availableSeats = seats;
    }

    public void bookTicket(String customerName, int numSeats) {
        lock.lock();
        try {
            if (numSeats <= availableSeats) {
                System.out.println(customerName + " successfully booked " + numSeats + " seat(s).");
                availableSeats -= numSeats;
            } else {
                System.out.println("Sorry, " + customerName + ", not enough seats available.");
            }
        } finally {
            lock.unlock();
        }
    }
}

class BookingThread extends Thread {
    private TicketBookingSystem system;
    private String customerName;
    private int numSeats;

    public BookingThread(TicketBookingSystem system, String customerName, int numSeats, int priority) {
        this.system = system;
        this.customerName = customerName;
        this.numSeats = numSeats;
        this.setPriority(priority);
    }

    @Override
    public void run() {
        system.bookTicket(customerName, numSeats);
    }
}

public class TicketBookingApp {
    public static void main(String[] args) {
        TicketBookingSystem system = new TicketBookingSystem(10);

        BookingThread vip1 = new BookingThread(system, "VIP_Alice", 2, Thread.MAX_PRIORITY);
        BookingThread vip2 = new BookingThread(system, "VIP_Bob", 3, Thread.MAX_PRIORITY);
        BookingThread user1 = new BookingThread(system, "User_Charlie", 2, Thread.NORM_PRIORITY);
        BookingThread user2 = new BookingThread(system, "User_David", 4, Thread.MIN_PRIORITY);

        vip1.start();
        vip2.start();
        user1.start();
        user2.start();
    }
}