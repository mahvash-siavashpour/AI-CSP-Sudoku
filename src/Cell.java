import javax.crypto.Cipher;
import java.util.ArrayList;

public class Cell {
    private int number;
    private char color;
    ArrayList<Integer> numberDomain = new ArrayList<>();
    ArrayList<Character> colorDomain = new ArrayList<>();
    private Coordinate coordinate;

    public Cell(int x, int y, int n, char[] c) {
        coordinate = new Coordinate(x, y);
        number = -1;
        color = '0';
        for (int i = 0; i < n; i++) {
            numberDomain.add(i + 1);
        }
        for (int i = 0; i < c.length; i++) {
            colorDomain.add(c[i]);
        }

    }

    public Cell(Integer n, Character c, Coordinate coordinate, ArrayList<Integer> numbers, ArrayList<Character> characters) {
        this.number = n;
        this.color = c;
        this.coordinate = coordinate;
        this.numberDomain.addAll(numbers);
        this.colorDomain.addAll(characters);
    }

    public boolean isComplete() {
        if (color != '0' && number != -1) {
            return true;
        }
        return false;
    }

    public boolean hasColor() {
        if (color != '0')
            return true;
        return false;
    }

    public boolean hasNumber() {
        if (number != -1)
            return true;
        return false;
    }


    public void setNumber(int number) {

        this.number = number;
        if (this.numberDomain.contains(Integer.valueOf(number)))
            this.numberDomain.remove(Integer.valueOf(number));
    }

    public void setColor(char color) {
        this.color = color;
        if (this.colorDomain.contains(Character.valueOf(color)))
            this.colorDomain.remove(Character.valueOf(color));
    }

    public int getNumber() {
        return number;
    }

    public char getColor() {
        return color;
    }


    public Coordinate getCoordinate() {
        return coordinate;
    }


    public ArrayList getNumberDomain() {
        return numberDomain;
    }

    public ArrayList getColorDomain() {
        return colorDomain;
    }

    public int getMRV() {
        return numberDomain.size() + colorDomain.size();
    }

}
