import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                new Runnable() {

                    @Override
                    public void run() {

                        World world =
                                new World(
                                        800,
                                        600
                                );

                        world.createFoodSources();

                        world.createInitialColony();

                        SimulationFrame frame =
                                new SimulationFrame(
                                        world
                                );

                        frame.setVisible(true);
                    }
                }
        );
    }
}
