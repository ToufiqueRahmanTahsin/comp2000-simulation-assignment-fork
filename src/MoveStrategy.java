@FunctionalInterface
public interface MoveStrategy {
    void chooseAndMove(Ant ant, World world);
}
