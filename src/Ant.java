public class Ant {
    protected double x;
    protected double y;
    protected double speed;
    protected boolean carryingFood;
    protected int carriedFood;
    protected boolean returningHome;
    protected World world;
    protected MoveStrategy strategy;
    protected FoodSource preferredSource;

    public Ant(double x, double y, double speed, World world, MoveStrategy strategy) {
        if (world == null || strategy == null) {
            throw new IllegalArgumentException("World and strategy are required.");
        }
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.world = world;
        this.strategy = strategy;
        this.carryingFood = false;
        this.carriedFood = 0;
        this.returningHome = false;
    }

    public void update() {
        if (returningHome) {
            Movement.goHome(this, world);
            if (distanceTo(world.getNest().getX(), world.getNest().getY()) < 10) {
                returnFoodToNest();
            }
            return;
        }
        strategy.chooseAndMove(this, world);
    }

    protected void randomMove() {
        double angle = Math.random() * Math.PI * 2;
        double newX = x + Math.cos(angle) * speed;
        double newY = y + Math.sin(angle) * speed;
        try {
            world.validatePosition(newX, newY);
            x = newX;
            y = newY;
        } catch (InvalidPositionException e) {
            // ignore invalid step
        }
    }

    protected void moveTowards(double targetX, double targetY) {
        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance == 0) {
            return;
        }
        double step = Math.min(speed, distance);
        double newX = x + (dx / distance) * step;
        double newY = y + (dy / distance) * step;
        try {
            world.validatePosition(newX, newY);
            x = newX;
            y = newY;
        } catch (InvalidPositionException e) {
            // ignore invalid step
        }
    }

    public double distanceTo(double targetX, double targetY) {
        double dx = targetX - x;
        double dy = targetY - y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    protected void returnFoodToNest() {
        if (carriedFood > 0) {
            world.getNest().storeFood(carriedFood);
        }
        carriedFood = 0;
        carryingFood = false;
        returningHome = false;
        preferredSource = null;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public boolean isCarryingFood() { return carryingFood; }
    public boolean isReturningHome() { return returningHome; }
    public FoodSource getPreferredSource() { return preferredSource; }
    public void setPreferredSource(FoodSource source) { preferredSource = source; }
    public void setReturningHome(boolean value) { returningHome = value; }
    public void setCarriedFood(int amount) { carriedFood = amount; carryingFood = amount > 0; }
    public String roleName() { return getClass().getSimpleName(); }
}
