import java.util.*;

public class CSP {

    static HashMap<Coordinate, Cell> table = new HashMap<>();
    private static char[] colors;
    Stack<HashMap> stack = new Stack<>();
    int colorCount;
    int size;

    public CSP(int n) {
        size = n;
    }

    public HashMap Run() {
        while (true) {
            Coordinate coordinate = heuristic();
            if (coordinate.getY() == -1 && coordinate.getX() == -1) return table;
            Cell cell = table.get(coordinate);
            if (!cell.hasNumber()) {
                boolean empty = false;
                Integer n = -1;
                if(cell.numberDomain.size() == 0) empty = true;
                else{
                    n = cell.numberDomain.get(0);
                    cell.numberDomain.remove(n);
                    if (cell.numberDomain.size() != 0) {
                        stack.push(copy());
                    }
                    cell.setNumber(n);
                }

                if (empty || !forwardCheck(n, '0', coordinate)) {
                    if (stack.size() == 0) return new HashMap();
                    table = stack.pop();
                }
            } else if (!cell.hasColor()) {
                boolean empty = false;
                char c = '0';
                if(cell.colorDomain.size() == 0) empty = true;
                else {
                    c = cell.colorDomain.get(0);
                    cell.colorDomain.remove(0);
                    if (cell.colorDomain.size() != 0) {
                        stack.push(copy());
                    }
                    cell.setColor(c);
                }
                if (empty || !forwardCheck(-1, c, coordinate)) {
                    if (stack.size() == 0) return new HashMap();
                    table = stack.pop();
                }
            }


        }
    }

    public HashMap copy() {
        HashMap<Coordinate, Cell> copyTable = new HashMap<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Coordinate coordinate = new Coordinate(i, j);
                Cell current = table.get(coordinate);
                Cell cell = new Cell(current.getNumber(), current.getColor(), coordinate, current.numberDomain, current.colorDomain);
                copyTable.put(coordinate, cell);
            }
        }
        return copyTable;
    }

    public boolean forwardCheck(int num, char chr, Coordinate c) {
        Integer number = num;
        Character character = chr;
        if (number != -1) {
            ArrayList<Coordinate> neighbor = new ArrayList<>();
            if (c.getX() - 1 >= 0)
                neighbor.add(new Coordinate(c.getX() - 1, c.getY()));
            if (c.getX() + 1 < size)
                neighbor.add(new Coordinate(c.getX() + 1, c.getY()));
            if (c.getY() - 1 >= 0)
                neighbor.add(new Coordinate(c.getX(), c.getY() - 1));
            if (c.getY() + 1 < size)
                neighbor.add(new Coordinate(c.getX(), c.getY() + 1));

            for (Coordinate cord :
                    neighbor) {
                if (table.get(cord).hasColor() && table.get(cord).hasNumber() &&
                        !table.get(c).hasColor()) {
                    int master = table.get(c).getNumber();
                    int slave = table.get(cord).getNumber();
                    boolean flag;
                    if (master < slave) {
                        flag = true;
                    } else flag = false;
                    for (int i = 0; i < colors.length; i++) {
                        Character colorToRemove = colors[i];
                        if (flag) {
                            if (table.get(c).colorDomain.contains(colorToRemove)) {
                                table.get(c).colorDomain.remove(colorToRemove);
                            }
                        }
                        if (colorToRemove == table.get(cord).getColor())
                            flag = !flag;
                    }
                }
            }




            for (int i = 0; i < size; i++) {
                if (i != c.getY()) {
                    Coordinate temp = new Coordinate(c.getX(), i);
                    if (!table.get(temp).hasNumber() && table.get(temp).numberDomain.contains(number)) {
                        table.get(temp).numberDomain.remove(number);
                        if (table.get(temp).numberDomain.size() == 0) {
                            return false;
                        }
                    }
                }

                if (i != c.getX()) {
                    Coordinate temp = new Coordinate(i, c.getY());
//                    System.out.println(temp.getX() +" "+ temp.getY());
                    if (!table.get(temp).hasNumber() && table.get(temp).numberDomain.contains(number)) {
                        table.get(temp).numberDomain.remove(number);
                        if (table.get(temp).numberDomain.size() == 0) {
                            return false;
                        }
                    }
                }
            }
        } else {
            ArrayList<Coordinate> neighbor = new ArrayList<>();
            if (c.getX() - 1 >= 0)
                neighbor.add(new Coordinate(c.getX() - 1, c.getY()));
            if (c.getX() + 1 < size)
                neighbor.add(new Coordinate(c.getX() + 1, c.getY()));
            if (c.getY() - 1 >= 0)
                neighbor.add(new Coordinate(c.getX(), c.getY() - 1));
            if (c.getY() + 1 < size)
                neighbor.add(new Coordinate(c.getX(), c.getY() + 1));

            for (Coordinate cord :
                    neighbor) {
                if (!table.get(cord).hasColor() && table.get(cord).colorDomain.contains(character)) {
                    table.get(cord).colorDomain.remove(character);
                }
                if (table.get(c).hasNumber() ) {
                    if (table.get(cord).hasNumber() && !table.get(cord).hasColor()) {
                        int master = table.get(c).getNumber();
                        int slave = table.get(cord).getNumber();
                        boolean flag;
                        if (master < slave) {
                            flag = false;
                        } else flag = true;
                        for (int i = 0; i < colors.length; i++) {
                            Character colorToRemove = colors[i];
                            if (flag) {
                                if (table.get(cord).colorDomain.contains(colorToRemove)) {
                                    table.get(cord).colorDomain.remove(colorToRemove);
                                }
                            }
                            if (colorToRemove == character)
                                flag = !flag;
                        }
                        if (table.get(cord).colorDomain.size() == 0) {
                            return false;
                        }

                    }else if(!table.get(cord).hasNumber() && table.get(cord).hasColor())  {
                        int master=0, slave =0;
                        for (int i = 0; i <colors.length ; i++) {
                            if(colors[i] == table.get(c).getColor()) master = i;
                            if(colors[i] == table.get(cord).getColor()) slave = i;
                        }
                        boolean flag;
                        if (master < slave) {
                            flag = false;
                        } else flag = true;
                        for (Integer i = 1; i <= size; i++) {

                            if (flag) {
                                if (table.get(cord).numberDomain.contains(i)) {
                                    table.get(cord).numberDomain.remove(i);
                                }
                            }
                            if (i == table.get(c).getNumber())
                                flag = !flag;
                        }

                    }
                }

                if (table.get(cord).colorDomain.size() == 0) {
                    return false;
                }
            }


        }
        return true;
    }


    public Coordinate heuristic() {
        int min = Integer.MAX_VALUE;
        Coordinate c = new Coordinate(0, 0);
        int complete = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Coordinate coordinate = new Coordinate(i, j);
                if (!table.get(coordinate).isComplete()) {
                    Cell cell = table.get(coordinate);
                    if (cell.getMRV() < min) {
                        min = cell.getMRV();
                        c = cell.getCoordinate();
                    }
                    if (cell.getMRV() == min) {
                        if (degree(cell) > degree(table.get(c))) {
                            c = cell.getCoordinate();
                        }
                    }
                } else complete++;
            }

        }

        if (complete == table.size()) return new Coordinate(-1, -1);
        return c;
    }

    public int degree(Cell cell) {
        Coordinate c = cell.getCoordinate();
        int deg = 0;
        if (!cell.hasColor()) {
            if (!cell.hasColor()) {
                for (int i = 0; i < size; i++) {
                    if (i != c.getY()) {
                        Coordinate temp = new Coordinate(c.getX(), i);
                        if (!table.get(temp).hasColor() || !table.get(temp).hasNumber()) {
                            deg++;
                        }
                    }

                    if (i != c.getX()) {
                        Coordinate temp = new Coordinate(i, c.getY());
                        if (!table.get(temp).hasColor() || !table.get(temp).hasNumber()) {
                            deg++;
                        }
                    }
                }

            } else {
                if (c.getX() - 1 >= 0 && c.getX() - 1 < size && !table.get(new Coordinate(c.getX() - 1, c.getY())).hasColor())
                    deg++;
                if (c.getX() + 1 >= 0 && c.getX() + 1 < size && !table.get(new Coordinate(c.getX() + 1, c.getY())).hasColor())
                    deg++;
                if (c.getY() - 1 >= 0 && c.getY() - 1 < size && !table.get(new Coordinate(c.getX(), c.getY() - 1)).hasColor())
                    deg++;
                if (c.getY() + 1 >= 0 && c.getY() + 1 < size && !table.get(new Coordinate(c.getX(), c.getY() + 1)).hasColor())
                    deg++;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (i > c.getY() - 1 && i < c.getY() + 1) {
                    Coordinate temp = new Coordinate(c.getX(), i);
                    if (!table.get(temp).hasColor() || !table.get(temp).hasNumber()) {
                        deg++;
                    }
                }

                if (i != c.getX()) {
                    Coordinate temp = new Coordinate(i, c.getY());
                    if (!table.get(temp).hasColor() || !table.get(temp).hasNumber()) {
                        deg++;
                    }
                }
            }
        }

        return deg;
    }


    public void printTable(HashMap<Coordinate, Cell> map, int size) {

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Coordinate coordinate = new Coordinate(i, j);
                Cell cell = map.get(coordinate);
                if (!cell.hasNumber()) System.out.print('*');
                else System.out.print(cell.getNumber());
                if (!cell.hasColor()) System.out.print('#');
                else System.out.print(cell.getColor());
                System.out.print(' ');
            }
            System.out.println();
        }
        System.out.println("______________");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int colorCount = sc.nextInt();
        int size = sc.nextInt();
        sc.nextLine();
        colors = sc.nextLine().replace(" ", "").toCharArray();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                char[] next = sc.next().toCharArray();
                Cell cell = new Cell(i, j, size, colors);
                if (next[0] != '*')
                    cell.setNumber(Integer.parseInt(next[0] + ""));

                if (next[1] != '#')
                    cell.setColor(next[1]);
                Coordinate coordinate = new Coordinate(i, j);
                table.put(coordinate, cell);
            }
        }


        CSP csp = new CSP(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Coordinate coordinate = new Coordinate(i, j);
                Cell cell = table.get(coordinate);

                if (cell.hasNumber()) {
                    if (!csp.forwardCheck(cell.getNumber(), '0', coordinate)) {
                        System.out.println("No Solutions");
                        return;
                    }
                }
                if (cell.hasColor()) {
                    if (!csp.forwardCheck(-1, cell.getColor(), coordinate)) {
                        System.out.println("No Solutions");
                        return;
                    }


                }

            }
        }
        HashMap map = csp.Run();
        if (map.size() == 0) {
            System.out.println("No Solutions");
            return;
        }
        System.out.println("______________");
        csp.printTable(map, size);
    }
}
