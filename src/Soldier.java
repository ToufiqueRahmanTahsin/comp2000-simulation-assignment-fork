public class Soldier extends Ant {
    public Soldier(double x, double y, World world) {
        super(x, y, 3.0, world, Movement::random);
    }

    @Override
    public void update() {
        if (!returningHome) {
            strategy.chooseAndMove(this, world);
            world.addPheromone(x, y, 0.2);
        } else {
            super.update();
        }
    }
}
