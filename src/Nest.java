public class Nest {

    private int x;
    private int y;
    private int storedFood;

    public Nest(int x, int y) {
        this.x = x;
        this.y = y;
        this.storedFood = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getStoredFood() {
        return storedFood;
    }

    public void storeFood(int amount) {

        if (amount > 0) {
            storedFood += amount;
        }
    }
}
