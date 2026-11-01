import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SimulationPanel extends JPanel {
    private static final int CELL_SIZE = 14;
    private final World world;
    private final Timer timer;

    public SimulationPanel(World world) {
        this.world = world;
        Map map = world.getMap();
        setPreferredSize(new Dimension(map.getWidth() * CELL_SIZE, map.getHeight() * CELL_SIZE + 30));

        timer = new Timer(60, e -> {
            world.update();
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Map map = world.getMap();

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell c = map.getCell(x, y);
                double p = Math.min(c.getPheromone(), 20.0) / 20.0;
                int shade = 255 - (int)(180 * p);
                g.setColor(new Color(255, shade, shade));
                g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(new Color(235, 235, 235));
                g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        Nest nest = world.getColony().getNest();
        g.setColor(Color.BLUE);
        g.fillRect(nest.getPosition().getX() * CELL_SIZE, nest.getPosition().getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        for (FoodSource source : world.getFoodSources()) {
            if (source.isAvailable()) {
                g.setColor(new Color(0, (int) Math.round(140 * source.getPercent()), 0));
                g.fillRect(source.getPosition().getX() * CELL_SIZE, source.getPosition().getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        for (Ant ant : world.getColony().getAnts()) {
            if (ant.isScouting()) {
                g.setColor(ant.isCarryingFood() ? Color.GREEN : Color.BLUE);
            } else {
                g.setColor(ant.isCarryingFood() ? Color.ORANGE : Color.BLACK);
            }
            int px = ant.getX() * CELL_SIZE + 4;
            int py = ant.getY() * CELL_SIZE + 4;
            g.fillOval(px, py, 6, 6);
        }

        g.setColor(Color.DARK_GRAY);
        Colony colony = world.getColony();
        java.util.Map<String, Long> roles = colony.countByRole();
        g.drawString("Food in nest: " + nest.getStoredFood(), 8, map.getHeight() * CELL_SIZE + 20);
        g.drawString("Ants: " + colony.getAnts().size()
                + " Scout=" + roles.getOrDefault("Scout", 0L)
                + " Forager=" + roles.getOrDefault("Forager", 0L)
                + " carrying=" + colony.countCarryingFood(),
                128, map.getHeight() * CELL_SIZE + 20);
    }
}
