public class Forager extends Ant {

    private FoodSource targetFood;

    public Forager(
            double x,
            double y,
            World world) {

        super(x, y, 2.0, world);
    }

    @Override
    public void update() {

        if (!returningHome) {

            if (targetFood != null && !targetFood.hasFood()) {

                targetFood = null;
            }

            FoodSource food =
                    world.findNearbyFood(
                            x,
                            y,
                            15
                    );

            if (food != null && food.hasFood()) {

                targetFood = food;

                int collected =
                        food.collectFood(5);

                if (collected > 0) {

                    carriedFood = collected;

                    carryingFood = true;

                    returningHome = true;

                    if (food.hasFood()) {

                        world.addFoodPheromone(
                                x,
                                y,
                                1.5,
                                food
                        );
                    }
                }
            }

            if (!returningHome) {

                if (targetFood == null) {

                    Pheromone pheromone =
                            world.findNearbyFoodPheromone(
                                    x,
                                    y,
                                    30
                            );

                    if (pheromone != null) {

                        targetFood = pheromone.getFoodSource();
                    }
                }

                if (targetFood != null && targetFood.hasFood()) {

                    // A linked trail provides a hint to a known food source.
                    moveTowards(targetFood.getX(), targetFood.getY());
                    return;
                }
            }
        }

        if (returningHome && targetFood != null && targetFood.hasFood()) {

            world.addFoodPheromone(
                    x,
                    y,
                    0.5,
                    targetFood
            );
        }

        super.update();
    }

    @Override
    protected void returnFoodToNest() {

        super.returnFoodToNest();

        if (targetFood != null && !targetFood.hasFood()) {

            targetFood = null;
        }
    }
}
