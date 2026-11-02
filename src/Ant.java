public class Ant {

    protected double x;
    protected double y;
    protected double speed;

    protected boolean carryingFood;
    protected int carriedFood;
    protected boolean returningHome;

    protected World world;

    public Ant(double x, double y, double speed, World world) {

        this.x = x;
        this.y = y;
        this.speed = speed;
        this.world = world;

        this.carryingFood = false;
        this.carriedFood = 0;
        this.returningHome = false;
    }

    public void update() {

        if (returningHome) {

            moveTowards(
                    world.getNest().getX(),
                    world.getNest().getY()
            );

            if (distanceTo(
                    world.getNest().getX(),
                    world.getNest().getY()) < 10) {

                returnFoodToNest();
            }

        } else {

            randomMove();
        }
    }

    protected void randomMove() {

        double angle = Math.random() * Math.PI * 2;

        double newX =
                x + Math.cos(angle) * speed;

        double newY =
                y + Math.sin(angle) * speed;

        try {

            world.validatePosition(newX, newY);

            x = newX;
            y = newY;

        } catch (InvalidPositionException e) {

            // Invalid movement is ignored.
        }
    }

    protected void moveTowards(
            double targetX,
            double targetY) {

        double dx = targetX - x;
        double dy = targetY - y;

        double distance =
                Math.sqrt(dx * dx + dy * dy);

        if (distance == 0) {
            return;
        }

        double step = Math.min(speed, distance);

        double newX = x + (dx / distance) * step;
        double newY = y + (dy / distance) * step;

        try {

            world.validatePosition(newX, newY);

            x = newX;
            y = newY;

        } catch (InvalidPositionException e) {

            // Invalid movement is ignored.
        }
    }

    protected double distanceTo(
            double targetX,
            double targetY) {

        double dx = targetX - x;
        double dy = targetY - y;

        return Math.sqrt(dx * dx + dy * dy);
    }

    protected void returnFoodToNest() {

        if (carriedFood > 0) {

            world.getNest().storeFood(carriedFood);
        }

        carriedFood = 0;
        carryingFood = false;
        returningHome = false;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isCarryingFood() {
        return carryingFood;
    }

    public boolean isReturningHome() {
        return returningHome;
    }
}
