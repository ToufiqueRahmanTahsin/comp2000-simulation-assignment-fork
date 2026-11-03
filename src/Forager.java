public class Forager extends Ant {
    public Forager(double x, double y, World world) {
        super(x, y, 4.0, world, Movement::followFoodTrail);
    }

    @Override
    public void update() {
        if (returningHome) {
            world.addFoodPheromone(x, y, preferredSource);
            super.update();
            return;
        }
        if (preferredSource != null && !preferredSource.hasFood()) {
            preferredSource = null;
        }
        FoodSource food = Movement.nearestFood(this, world, 15);
        if (food != null) {
            int collected = food.collectFood(5);
            if (collected > 0) {
                setCarriedFood(collected);
                preferredSource = food;
                returningHome = true;
                world.addFoodPheromone(x, y, food);
                super.update();
                return;
            }
        }
        strategy.chooseAndMove(this, world);
    }
}
