public class FoodSource {
    private int x;
    private int y;
    private int foodAmount;

    public FoodSource(int x, int y, int foodAmount) {
        if (foodAmount < 0) {
            throw new IllegalArgumentException("Food amount cannot be negative.");
        }
        this.x = x;
        this.y = y;
        this.foodAmount = foodAmount;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getFoodAmount() { return foodAmount; }
    public boolean hasFood() { return foodAmount > 0; }

    public int collectFood(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Collection amount cannot be negative.");
        }
        int collected = Math.min(amount, foodAmount);
        foodAmount -= collected;
        return collected;
    }
}
