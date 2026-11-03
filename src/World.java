import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class World {
    private int width;
    private int height;
    private Nest nest;
    private List<FoodSource> foodSources;
    private List<Pheromone> pheromones;
    private Colony<Ant> colony;

    public World(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("World dimensions must be positive.");
        }
        this.width = width;
        this.height = height;
        foodSources = new ArrayList<>();
        pheromones = new ArrayList<>();
        nest = new Nest(width / 2, height / 2);
        colony = new Colony<>();
    }

    public void validatePosition(double x, double y) throws InvalidPositionException {
        if (!Double.isFinite(x) || !Double.isFinite(y) || x < 0 || x > width || y < 0 || y > height) {
            throw new InvalidPositionException("Position is outside the simulation.");
        }
    }

    public void addPheromone(Pheromone pheromone) {
        if (pheromone == null) {
            throw new IllegalArgumentException("Pheromone cannot be null.");
        }
        requireValidPosition(pheromone.getX(), pheromone.getY());
        pheromones.add(pheromone);
    }

    public void addPheromone(double x, double y) {
        addPheromone(new Pheromone(x, y, 1.0));
    }

    public void addPheromone(double x, double y, double strength) {
        addPheromone(new Pheromone(x, y, strength));
    }

    public void addFoodSource(FoodSource food) {
        if (food == null) {
            throw new IllegalArgumentException("Food source cannot be null.");
        }
        requireValidPosition(food.getX(), food.getY());
        foodSources.add(food);
    }

    private void requireValidPosition(double x, double y) {
        try {
            validatePosition(x, y);
        } catch (InvalidPositionException e) {
            throw new IllegalArgumentException("Position must be inside the world.", e);
        }
    }

    public void addFoodPheromone(double x, double y, FoodSource source) {
        if (source == null || !source.hasFood()) {
            return;
        }
        double routeLength = Math.hypot(source.getX() - nest.getX(), source.getY() - nest.getY());
        double lifetime = 100 + routeLength * 1.5;
        addPheromone(new Pheromone(x, y, lifetime * Pheromone.DECAY_RATE, source));
    }

    public Pheromone findFoodTrail(double x, double y, double range, FoodSource preferredSource) {
        double distanceFromNest = Math.hypot(x - nest.getX(), y - nest.getY());
        return pheromones.stream()
                .filter(Pheromone::isActive)
                .filter(p -> p.getFoodSource() != null)
                .filter(p -> Math.hypot(p.getX() - x, p.getY() - y) <= range)
                .filter(p -> {
                    double progress = Math.hypot(p.getX() - nest.getX(), p.getY() - nest.getY()) - distanceFromNest;
                    return progress > 0.000001;
                })
                .max(Comparator
                        .comparing((Pheromone p) -> p.getFoodSource() == preferredSource)
                        .thenComparingDouble(p -> Math.hypot(p.getX() - nest.getX(), p.getY() - nest.getY()) - distanceFromNest)
                        .thenComparingDouble(Pheromone::getStrength))
                .orElse(null);
    }

    public FoodSource findNearbyFood(double x, double y, double range) {
        return foodSources.stream()
                .filter(FoodSource::hasFood)
                .filter(food -> Math.hypot(food.getX() - x, food.getY() - y) <= range)
                .min(Comparator.comparingDouble(food -> Math.hypot(food.getX() - x, food.getY() - y)))
                .orElse(null);
    }

    public void update() {
        colony.updateAll();
        pheromones.forEach(Pheromone::decay);
        pheromones = pheromones.stream().filter(Pheromone::isActive).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public void createInitialColony() {
        for (int i = 0; i < 3; i++) {
            colony.addAnt(new Scout(nest.getX(), nest.getY(), this));
        }
        for (int i = 0; i < 8; i++) {
            colony.addAnt(new Forager(nest.getX(), nest.getY(), this));
        }
        for (int i = 0; i < 2; i++) {
            colony.addAnt(new Soldier(nest.getX(), nest.getY(), this));
        }
    }

    public void createFoodSources() {
        addFoodSource(new FoodSource((int) (width * 0.30), (int) (height * 0.30), 200));
        addFoodSource(new FoodSource((int) (width * 0.70), (int) (height * 0.65), 200));
        addFoodSource(new FoodSource((int) (width * 0.65), (int) (height * 0.25), 200));
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Nest getNest() { return nest; }
    public List<FoodSource> getFoodSources() { return foodSources; }
    public List<Pheromone> getPheromones() { return pheromones; }
    public Colony<Ant> getColony() { return colony; }
}
