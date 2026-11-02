import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.Dimension;

public class SimulationFrame extends JFrame {

    private World world;
    private SimulationPanel panel;

    public SimulationFrame(World world) {

        this.world = world;

        panel = new SimulationPanel(world);

        setTitle(
                "Ant Colony Foraging Simulation"
        );

        panel.setPreferredSize(new Dimension(world.getWidth(), world.getHeight()));

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        add(panel);
        setResizable(false);
        pack();

        setLocationRelativeTo(null);

        Timer timer =
                new Timer(
                        40,
                        e -> {

                            world.update();

                            panel.repaint();
                        }
                );

        timer.start();
    }
}
