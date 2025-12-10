package ticketmachine;

import javax.swing.SwingUtilities;

public class TicketMachineApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TicketMachineFrame frame = new TicketMachineFrame();
            frame.setVisible(true);
        });
    }
}
