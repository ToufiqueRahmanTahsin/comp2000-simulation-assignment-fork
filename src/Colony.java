import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Colony {
    private final List<Ant> ants;
    private final List<Ant> scouts;
    private final List<Ant> foragers;
    private final Nest nest;

    public Colony(Nest nest) {
        if (nest == null) {
            throw new IllegalArgumentException("Colony requires a nest.");
        }
        this.nest = nest;
        this.ants = new ArrayList<>();
        this.scouts = new ArrayList<>();
        this.foragers = new ArrayList<>();
    }

    public void addAnt(Ant ant) {
        addAnt(ant, ant instanceof Scout ? "Scout" : "Forager");
    }

    public void addAnt(Ant ant, String antType) {
        if (ant != null) {
            ants.add(ant);
            if ("Scout".equals(antType) || ant instanceof Scout) {
                scouts.add(ant);
            } else if ("Forager".equals(antType) || ant instanceof Forager) {
                foragers.add(ant);
            }
        }
    }

    public List<Ant> getAnts() {
        return Collections.unmodifiableList(ants);
    }

    public List<Ant> getScouts() {
        return Collections.unmodifiableList(scouts);
    }

    public List<Ant> getForagers() {
        return Collections.unmodifiableList(foragers);
    }

    public Nest getNest() {
        return nest;
    }

    public long countCarryingFood() {
        return ants.stream().filter(Ant::isCarryingFood).count();
    }

    public long countScouting() {
        return ants.stream().filter(Ant::isScouting).count();
    }

    // Week 11: group ants by role
    public Map<String, Long> countByRole() {
        return ants.stream()
                .collect(Collectors.groupingBy(
                        a -> a.isScouting() ? "Scout" : "Forager",
                        Collectors.counting()));
    }

    // Week 12: read-only parallel count. Do not parallelise the update.
    public long countCarryingFoodParallel() {
        return ants.parallelStream().filter(Ant::isCarryingFood).count();
    }

    public void update(World world) {
        Map map = world.getMap();
        MoveStrategy goHome = Movement.closestTo(nest.getPosition());

        for (Ant ant : ants) {
            Cell nextCell = ant.isReturningHome()
                    ? goHome.choose(ant, map)
                    : ant.chooseNextCell(map);

            ant.move(nextCell);

            if (ant.isCarryingFood()) {
                ant.getPosition().addPheromone(10.0);
            }

            if (ant.isScouting()) {
                ant.getPosition().addPheromone(1.0);
            }

            world.getFoodSources().stream()
                    .filter(source -> sameCell(source.getPosition(), ant.getPosition()))
                    .findFirst()
                    .ifPresent(source -> source.interact(ant));

            if (sameCell(nest.getPosition(), ant.getPosition())) {
                nest.interact(ant);
            }
        }
    }

    private boolean sameCell(Cell a, Cell b) {
        return a != null && b != null && a.getX() == b.getX() && a.getY() == b.getY();
    }
}
