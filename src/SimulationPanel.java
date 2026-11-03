import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Map;

public class SimulationPanel extends JPanel {
    private World world;

    public SimulationPanel(World world) {
        this.world = world;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        drawPheromones(graphics);
        drawFood(graphics);
        drawNest(graphics);
        drawAnts(graphics);
        drawInformation(graphics);
    }

    private void drawPheromones(Graphics2D g) {
        for (Pheromone pheromone : world.getPheromones()) {
            int alpha = (int) Math.min(180, pheromone.getStrength() * 80);
            if (alpha < 20) {
                alpha = 20;
            }
            g.setColor(new Color(150, 80, 200, alpha));
            int x = (int) pheromone.getX();
            int y = (int) pheromone.getY();
            g.fillRect(x, y, 4, 4);
        }
    }

    private void drawFood(Graphics2D g) {
        for (FoodSource food : world.getFoodSources()) {
            if (food.hasFood()) {
                g.setColor(new Color(40, 170, 70));
                int size = 20;
                g.fillOval(food.getX() - size / 2, food.getY() - size / 2, size, size);
            }
        }
    }

    private void drawNest(Graphics2D g) {
        int x = world.getNest().getX();
        int y = world.getNest().getY();
        g.setColor(new Color(120, 70, 30));
        g.fillOval(x - 25, y - 25, 50, 50);
        g.setColor(Color.WHITE);
        g.drawString("NEST", x - 17, y + 5);
    }

    private void drawAnts(Graphics2D g) {
        for (Ant ant : world.getColony().getAnts()) {
            if (ant instanceof Scout) {
                g.setColor(Color.BLUE);
            } else if (ant instanceof Forager) {
                g.setColor(Color.RED);
            } else if (ant instanceof Soldier) {
                g.setColor(Color.BLACK);
            }
            int size = ant.isCarryingFood() ? 14 : 10;
            int x = (int) ant.getX();
            int y = (int) ant.getY();
            g.fillOval(x - size / 2, y - size / 2, size, size);
        }
    }

    private void drawInformation(Graphics2D g) {
        g.setColor(Color.BLACK);
        int totalFood = world.getFoodSources().stream().mapToInt(FoodSource::getFoodAmount).sum();
        Map<String, Long> roles = world.getColony().countByRole();
        g.drawString("Ants: " + world.getColony().size()
                + " Scout=" + roles.getOrDefault("Scout", 0L)
                + " Forager=" + roles.getOrDefault("Forager", 0L)
                + " Soldier=" + roles.getOrDefault("Soldier", 0L), 10, 20);
        g.drawString("Carrying: " + world.getColony().countCarryingFood(), 10, 40);
        g.drawString("Food remaining: " + totalFood, 10, 60);
        g.drawString("Food in nest: " + world.getNest().getStoredFood(), 10, 80);
        g.drawString("Blue=Scout  Red=Forager  Black=Soldier", 10, 100);
    }
}
