import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.Dimension;

public class SimulationFrame extends JFrame {
    private World world;
    private SimulationPanel panel;
    private final Timer timer;

    public SimulationFrame(World world) {
        this.world = world;
        panel = new SimulationPanel(world);
        setTitle("Ant Colony Foraging Simulation");
        panel.setPreferredSize(new Dimension(world.getWidth(), world.getHeight()));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        timer = new Timer(40, e -> {
            world.update();
            panel.repaint();
        });
        timer.start();
    }

    @Override
    public void dispose() {
        timer.stop();
        super.dispose();
    }
}
