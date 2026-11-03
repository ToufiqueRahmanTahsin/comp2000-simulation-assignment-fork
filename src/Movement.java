import java.util.Comparator;
import java.util.List;

public final class Movement {
    private Movement() {
    }

    public static void random(Ant ant, World world) {
        ant.randomMove();
    }

    public static void followFoodTrail(Ant ant, World world) {
        FoodSource preferred = ant.getPreferredSource();
        Pheromone next = world.findFoodTrail(ant.getX(), ant.getY(), 30, preferred);
        if (next != null) {
            ant.setPreferredSource(next.getFoodSource());
            ant.moveTowards(next.getX(), next.getY());
        } else {
            ant.randomMove();
        }
    }

    public static void goHome(Ant ant, World world) {
        ant.moveTowards(world.getNest().getX(), world.getNest().getY());
    }

    public static FoodSource nearestFood(Ant ant, World world, double range) {
        return world.getFoodSources().stream()
                .filter(FoodSource::hasFood)
                .filter(food -> ant.distanceTo(food.getX(), food.getY()) <= range)
                .min(Comparator.comparingDouble(food -> ant.distanceTo(food.getX(), food.getY())))
                .orElse(null);
    }

    public static double averagePheromoneStrength(List<Pheromone> pheromones) {
        return pheromones.parallelStream()
                .filter(Pheromone::isActive)
                .mapToDouble(Pheromone::getStrength)
                .average()
                .orElse(0.0);
    }
}
