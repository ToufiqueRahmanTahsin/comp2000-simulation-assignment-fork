import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Colony<T extends Ant> {
    private final List<T> ants;

    public Colony() {
        ants = new ArrayList<>();
    }

    public void addAnt(T ant) {
        if (ant != null) {
            ants.add(ant);
        }
    }

    public void updateAll() {
        for (T ant : ants) {
            ant.update();
        }
    }

    public List<T> getAnts() {
        return Collections.unmodifiableList(ants);
    }

    public int size() {
        return ants.size();
    }

    public long countCarryingFood() {
        return ants.stream().filter(Ant::isCarryingFood).count();
    }

    public long countCarryingFoodParallel() {
        return ants.parallelStream().filter(Ant::isCarryingFood).count();
    }

    public Map<String, Long> countByRole() {
        return ants.stream()
                .collect(Collectors.groupingBy(Ant::roleName, Collectors.counting()));
    }
}
