public class Pheromone {
    public static final double DECAY_RATE = 0.02;
    private double x;
    private double y;
    private double strength;
    private final FoodSource foodSource;

    public Pheromone(double x, double y, double strength) {
        this(x, y, strength, null);
    }

    public Pheromone(double x, double y, double strength, FoodSource foodSource) {
        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(strength) || strength < 0) {
            throw new IllegalArgumentException("Invalid pheromone position or strength.");
        }
        this.x = x;
        this.y = y;
        this.strength = strength;
        this.foodSource = foodSource;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getStrength() { return strength; }
    public FoodSource getFoodSource() { return foodSource; }

    public void decay() {
        strength -= DECAY_RATE;
        if (strength < 0) {
            strength = 0;
        }
    }

    public boolean isActive() {
        return strength > 0 && (foodSource == null || foodSource.hasFood());
    }
}
