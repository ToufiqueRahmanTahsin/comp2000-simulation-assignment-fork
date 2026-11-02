public class Forager extends Ant {

    public Forager(
            double x,
            double y,
            World world) {

        super(x, y, 2.0, world);
    }

    @Override
    public void update() {

        if (!returningHome) {

            FoodSource food =
                    world.findNearbyFood(
                            x,
                            y,
                            15
                    );

            if (food != null && food.hasFood()) {

                int collected =
                        food.collectFood(5);

                if (collected > 0) {

                    carriedFood = collected;

                    carryingFood = true;

                    returningHome = true;

                    world.addPheromone(
                            x,
                            y,
                            1.5
                    );
                }
            }
        }

        if (returningHome) {

            world.addPheromone(
                    x,
                    y,
                    0.5
            );
        }

        super.update();
    }
}
