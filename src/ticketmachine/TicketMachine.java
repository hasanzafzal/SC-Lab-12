package ticketmachine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TicketMachine {

    public enum TicketType {
        STANDARD, executive, STUDENT
    }

    private TicketType currentType = TicketType.STANDARD;
    private int currentZone = 1; // 1, 2 or 3

    private int balance;
    private int totalCollected;

    private final Random random = new Random();

    public TicketMachine() {
        this.balance = 0;
        this.totalCollected = 0;
    }

    public void setTicketType(TicketType type) {
        this.currentType = type;
    }

    public TicketType getTicketType() {
        return currentType;
    }

    public void setZone(int zone) {
        if (zone < 1) zone = 1;
        if (zone > 3) zone = 3;
        this.currentZone = zone;
    }

    public int getZone() {
        return currentZone;
    }

    public int getBalance() {
        return balance;
    }

    public int getTotalCollected() {
        return totalCollected;
    }

    /**
     * Calculate current ticket price based on type and zone.
     * Zone base prices: 1 -> 80, 2 -> 120, 3 -> 160
     * executive: +40
     * STUDENT: 50% discount on base
     */
    public int getCurrentTicketPrice() {
        int base;
        switch (currentZone) {
            case 2 -> base = 120;
            case 3 -> base = 160;
            default -> base = 80;
        }

        switch (currentType) {
            case executive -> base += 40;
            case STUDENT -> base = (int) Math.round(base * 0.5);
            default -> { /* STANDARD, keep base */ }
        }
        return base;
    }

    public void insertMoney(int amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean canPrintTicket() {
        return balance >= getCurrentTicketPrice();
    }

    public String printTicket() {
        int price = getCurrentTicketPrice();
        if (!canPrintTicket()) {
            int shortfall = price - balance;
            return "Need " + shortfall + " more to print this ticket.";
        }

        balance -= price;
        totalCollected += price;

        // build a receipt text
        String id = String.format("%06d", random.nextInt(1_000_000));
        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        String typeStr = switch (currentType) {
            case STANDARD -> "Standard";
            case executive -> "Executive";
            case STUDENT -> "Student";
        };

        return """
                -------------------------------
                      SMART TICKET
                -------------------------------
                Ticket ID : %s
                Type      : %s
                Zone      : %d
                Price     : %d
                Date/Time : %s
                -------------------------------
                Enjoy your journey!
                """.formatted(id, typeStr, currentZone, price, dateTime);
    }

    public int refund() {
        int r = balance;
        balance = 0;
        return r;
    }
}