public class Pheromone {

    private double x;
    private double y;
    private double strength;

    public Pheromone(double x, double y, double strength) {
        this.x = x;
        this.y = y;
        this.strength = strength;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getStrength() {
        return strength;
    }

    public void decay() {
        strength -= 0.02;

        if (strength < 0) {
            strength = 0;
        }
    }

    public boolean isActive() {
        return strength > 0;
    }
}
