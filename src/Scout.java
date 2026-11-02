public class Scout extends Ant {

    public Scout(
            double x,
            double y,
            World world) {

        super(x, y, 3.0, world);
    }

    @Override
    public void update() {

        if (!returningHome) {

            FoodSource food =
                    world.findNearbyFood(
                            x,
                            y,
                            30
                    );

            if (food != null && food.hasFood()) {

                returningHome = true;

                world.addPheromone(
                        x,
                        y,
                        1.0
                );
            }
        }

        super.update();
    }
}
