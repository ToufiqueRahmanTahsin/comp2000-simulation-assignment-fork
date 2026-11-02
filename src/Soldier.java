public class Soldier extends Ant {

    public Soldier(
            double x,
            double y,
            World world) {

        super(x, y, 1.5, world);
    }

    @Override
    public void update() {

        if (!returningHome) {

            randomMove();

            world.addPheromone(
                    x,
                    y,
                    0.2
            );

        } else {

            super.update();
        }
    }
}
