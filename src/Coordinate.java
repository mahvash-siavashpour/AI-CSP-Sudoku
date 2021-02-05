import java.util.Objects;

public class Coordinate {
    private int x;
    private int y;
    public Coordinate(int x, int y){
        this.x =x ;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Coordinate that = (Coordinate) o;
        return x == that.x &&
                y == that.y;

    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
