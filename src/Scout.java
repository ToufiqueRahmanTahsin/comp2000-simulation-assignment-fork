public class Scout extends Ant {
    public Scout(double x, double y, World world) {
        super(x, y, 7.0, world, Movement::random);
    }

    @Override
    public void update() {
        if (returningHome) {
            world.addFoodPheromone(x, y, preferredSource);
            super.update();
            return;
        }
        FoodSource found = Movement.nearestFood(this, world, 30);
        if (found == null) {
            strategy.chooseAndMove(this, world);
            return;
        }
        preferredSource = found;
        moveTowards(found.getX(), found.getY());
        if (distanceTo(found.getX(), found.getY()) <= 5) {
            returningHome = true;
            world.addFoodPheromone(x, y, found);
        }
    }
}
