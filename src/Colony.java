import java.util.ArrayList;
import java.util.List;

public class Colony<T extends Ant> {

    private List<T> ants;

    public Colony() {

        ants = new ArrayList<T>();
    }

    public void addAnt(T ant) {

        ants.add(ant);
    }

    public void updateAll() {

        for (T ant : ants) {

            ant.update();
        }
    }

    public List<T> getAnts() {

        return ants;
    }

    public int size() {

        return ants.size();
    }
}
