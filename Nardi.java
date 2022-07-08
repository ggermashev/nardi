package com.company;



import java.util.LinkedList;

class Point
{
    int i;
    int j;
    int count;
    int sum;

    Point()
    {
        i = 0;
        j = 0;
        count = 0;
        sum = 0;
    }

    Point(int _i, int _j)
    {
        i = _i;
        j = _j;
        count = 0;
        sum = 0;
    }

    Point(int _i, int _j, int _count)
    {
        i = _i;
        j = _j;
        count = _count;
        sum = 0;
    }

    Point (int _i, int _j, int _count, int _sum)
    {
        i = _i;
        j = _j;
        count = _count;
        sum = _sum;
    }

    void set(int _i, int _j)
    {
        i = _i;
        j = _j;
        count = 0;
    }

    void set(int _i, int _j, int _sum)
    {
        i = _i;
        j = _j;
        sum = _sum;
    }

    int getI()
    {
       return i;
    }

    int getJ()
    {
        return j;
    }

    int getCount()
    {
        return count;
    }

    Point max(Point p)
    {
        if (this.sum > p.sum) return this;
        else return p;
    }

    Point min(Point p)
    {
        if (this.sum < p.sum) return this;
        else return p;
    }

    Point copy()
    {
        Point newpoint = new Point(i,j,count,sum);
        return newpoint;
    }

}

class Cell
{
    int player;
    LinkedList<Cheker> chekers;
    int costb;
    int costw;

    Cell()
    {
        player = 0;
        chekers = new LinkedList<Cheker>();
    }

    Cell(int p)
    {
        player = p;
        chekers = new LinkedList<Cheker>();
    }

    Cell(int p, int i, int j) {
        player = p;
        chekers = new LinkedList<Cheker>();

        if (i == 0) costb = 11 - j;
        if (i == 1) costb = 12 + j;

        if (i == 1) costw = j;
        if (i == 0) costw = 23 - j;
    }

    Cell(Cell c)
    {
        player = c.player;
        chekers = new LinkedList<Cheker>();
        for (Cheker ch: c.chekers)
        {
            chekers.add(ch);
        }
        costb = c.costb;
        costw = c.costw;
    }

}


class Field
{
    Cell[][] field;

    Field()
    {
        field = new Cell[2][12];
        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 12; j++)
            {
                field[i][j] = new Cell(0,i,j);
            }
        }
        field[1][0].player = 1;
        field[0][11].player = -1;
    }

    Field(Field _field)
    {
        field = new Cell[2][12];
        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                field[i][j] = new Cell(_field.field[i][j]);
            }
        }

    }

    void setPlayer(int i, int j, int player)
    {
        field[i][j].player = player; //0 или 1
    }

    void addCheker(int i, int j,Cheker _cheker)
    {
        field[i][j].chekers.add(_cheker);
    }


    int getField(int i, int j)
    {
        return field[i][j].player;
    }

    void print()
    {
        if (field == null)
        {
            System.out.println("field is null");
        }
        else {
            for (int i = 0; i < 2; i++)
            {
                for (int j = 0; j < 12; j++)
                {
                    System.out.print(field[i][j]);
                }
                System.out.println();
            }
        }
    }

    boolean isEmpty(int i, int j)
    {
        if (field[i][j].chekers.isEmpty()) return true;
        else return false;
    }

    boolean isBot(int i, int j)
    {
        if (field[i][j].player == -1) return true;
        return false;
    }

    void dltCheker(int i, int j)
    {
        if (!isEmpty(i,j))
            field[i][j].chekers.pop();
    }

    int getCost(int i,int j,int p)
    {
        if (p == 1) return field[i][j].costw;
        if (p == -1) return field[i][j].costb;
        return 0;
    }

    boolean hasMove(int i, int j, int count)
    {
        for (Cheker ch : field[i][j].chekers)
        {
            for (Point m: ch.moves)
            {
                if (m.count == count) return true;
            }
        }
        return false;
    }

    Cheker getChekerToMove(int i, int j, int count)
    {
        for (Cheker ch : field[i][j].chekers)
        {
            for (Point m: ch.moves)
            {
                if (m.count == count) return ch;
            }
        }
        return null;
    }

    Cheker getCheker(Point p)
    {
        if (field[p.getI()][p.getJ()].chekers.isEmpty()) return null;
        return field[p.getI()][p.getJ()].chekers.getFirst();
    }

    Cheker getCheker(int i, int j)
    {
        return getCheker(new Point(i,j));
    }

    Point getNewPos(int i, int j, int count)
    {
        for (Cheker ch : field[i][j].chekers)
        {
            for (Point m: ch.moves)
            {
                if (m.count == count) return new Point(m.getI(),m.getJ(),0);
            }
        }
        return null;
    }

    void setField(Field _field)
    {
        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                field[i][j] = new Cell(_field.field[i][j]);
            }
        }
    }
}


class Cheker
{
    int player;
    Point pos;
    static Field field;
    LinkedList<Point> moves;
    LinkedList<Integer> allowedCubes;

    static {
        field = new Field();
    }

    Cheker()
    {
        pos = new Point();
        moves = new LinkedList<Point>();
        allowedCubes = new LinkedList<Integer>();
        player = 0;
    }

    Cheker(Cheker ch)
    {
        pos = new Point(ch.pos.getI(), ch.pos.getJ());
        moves = ch.copyMoves();
        allowedCubes = ch.copyAllowedCubes();
        player = ch.player;
    }

    LinkedList<Point> copyMoves()
    {
        LinkedList<Point> res = new LinkedList<Point>();
        for (Point m : moves)
        {
            res.add(m);
        }
        return res;
    }

    LinkedList<Integer> copyAllowedCubes()
    {
        LinkedList<Integer> res = new LinkedList<Integer>();
        for (Integer a : allowedCubes)
        {
            res.add(a);
        }
        return res;
    }

    void setPos(int i, int j)
    {
        pos.set(i,j);
    }

    void mayGo()
    {

    }

    void printMoves()
    {
        System.out.println("-----------------------------");
        for ( Point m : moves)
        {
            System.out.println(m.getI() + " " + m.getJ());
        }
        System.out.println("-----------------------------");
    }

    boolean inMoves(int i, int j)
    {
        for (Point m: moves)
        {
            if ((m.getI() == i) && (m.getJ() == j)) return true;
        }
        return false;
    }


    void go(int i, int j)
    {
        if (inMoves(i,j))
        {
            field.dltCheker(pos.getI(), pos.getJ());
            if (field.isEmpty(pos.getI(), pos.getJ()))
            {
                field.setPlayer(pos.getI(), pos.getJ(),0);
            }
            pos.set(i,j);
            field.addCheker(i,j,this);
            field.setPlayer(i,j,player);
        }
        //else System.out.println("cant go " + i + " " + j);
    }

    void go(Point to)
    {
        go(to.getI(), to.getJ());
    }

    void goBack(int i, int j)
    {
        field.dltCheker(pos.getI(), pos.getJ());
        if (field.isEmpty(pos.getI(), pos.getJ()))
        {
            field.setPlayer(pos.getI(), pos.getJ(),0);
        }
        pos.set(i,j);
        field.addCheker(i,j,this);
        field.setPlayer(i,j,player);
    }
}

class Wcheker extends Cheker
{
    Wcheker()
    {
        super();
        player = 1;
    }

    Wcheker(Cheker ch)
    {
        super(ch);
    }

    void mayGo()
    {
        moves.clear();
        allowedCubes.clear();
        int i = pos.getI();
        int j = pos.getJ();
        for (int k = 1; k <7; k++)
        {
            if (i == 1)
            {
                if (j != 11) j++;
                else if (j == 11)
                {
                    i = 0;
                    j = 11;
                }
                if (field.getField(i,j) != -1) {
                    moves.add(new Point(i, j,k));
                    allowedCubes.add(k);
                }
            }
            else if (i == 0)
            {
                if (j != 0)
                {
                    j--;
                    if (field.getField(i,j) != -1)
                        moves.add(new Point(i,j,k));
                }
            }
        }
    }

}

class Bcheker extends Cheker
{
    Bcheker()
    {
        super();
        player = -1;
    }

    Bcheker(Cheker ch)
    {
        super(ch);
    }

    void mayGo()
    {
        moves.clear();
        int i = pos.getI();
        int j = pos.getJ();
        for (int k = 1; k < 7; k++)
        {
            if (i == 0)
            {
                if (j != 0) {
                    j--;
                }
                else if (j == 0)
                {
                    i = 1;
                    j = 0;
                }
                if (field.getField(i,j) != 1)
                    moves.add(new Point(i,j,k));

            }
            else if (i == 1)
            {
                if (j != 11)
                {
                    j++;
                    if (field.getField(i,j) != 1)
                        moves.add(new Point(i,j,k));
                }
            }
        }

    }

}

class Move
{
    Point from;
    Point to;
    Move(int i1, int j1, int i2, int j2, int count, int sum)
    {
        from = new Point(i1,j1,count,sum);
        to = new Point(i2,j2,count,sum);
    }

    Move (Point _from, Point _to, int _sum)
    {
        from = new Point(_from.getI(), _from.getJ(), _sum);
        to = new Point(_to.getI(), _to.getJ(), _sum);
    }

    Move(Move mv)
    {
        from = new Point(mv.from.i, mv.from.j, mv.from.count, mv.from.sum);
        to = new Point(mv.to.i, mv.to.j, mv.to.count, mv.to.sum);
    }

    Move()
    {
        from = new Point();
        to = new Point();
    }

    void set(int i1, int j1, int i2, int j2, int sum)
    {
        from.set(i1,j1,sum);
        to.set(i2,j2,sum);
    }

    void set(Point _from, Point _to, int _sum)
    {
        set(_from.getI(), _from.getJ(), _to.getI(), _to.getJ(), _sum );
    }

    Move copy()
    {
        Move mv = new Move(this);
        return mv;
    }

    Move max(Move mv)
    {
        if (mv.to.sum > this.to.sum)
            return mv;
        return this;
    }

    Move min(Move mv)
    {
        if (mv.to.sum < this.to.sum)
            return mv;
        return this;
    }

    void print()
    {
        System.out.println("from: " + from.getI() + " " + from.getJ() + " to: " + to.getI() + " " + to.getJ());
    }
}


public class Nardi{
    Wcheker [] wchekers;
    Bcheker [] bchekers;
    int [] cube;

    Nardi()
    {
        wchekers = new Wcheker[15];
        bchekers = new Bcheker[15];

            for (int j = 0; j < 15; j++)
            {
                wchekers[j] = new Wcheker();
                bchekers[j] = new Bcheker();

                bchekers[j].setPos(0, 11);
                Cheker.field.addCheker(0, 11, bchekers[j]);

                wchekers[j].setPos(1, 0);
                Cheker.field.addCheker(1, 0, wchekers[j]);
            }

        cube = new int[2];

    }

    void throwCube()
    {
        for (int i = 0; i < 2; i++)
        {
            cube[i] = (int)(Math.random() * 6) + 1;
            if (cube[i] > 6) cube[i] = 6;
        }
    }

    void printCube()
    {
        System.out.println(cube[0] + " " + cube[1]);
    }

    void printPoses() {
        for (int j = 0; j < 15; j++) {
            System.out.println(bchekers[j].pos.getI() + " " + bchekers[j].pos.getJ());
        }
        for (int j = 0; j < 15; j++) {
            System.out.println(wchekers[j].pos.getI() + " " + wchekers[j].pos.getJ());
        }
    }

    void printField()
    {
        Cheker.field.print();
    }

    void printMayGoW()
    {
        for (int j = 0; j < 15; j++)
        {
            wchekers[j].printMoves();
        }
    }

    void refreshMays()
    {
        for (int i = 0; i < 15; i++)
        {
            wchekers[i].mayGo();
            bchekers[i].mayGo();
        }
    }

    void test()
    {
    }

    Move getBestMove(int sum , Move best, int player, int count, Point from, Point to)//sum = 0 player = -1 count = 0 from = null  to = null
    {
        if (count == 2)
        {
            best.set(from,to,sum);
        }
        else {
            if (count == 0) throwCube();
            if (count == 0) printCube();
            Field field = null;
            if (Cheker.field != null) field = Cheker.field;
            if (field == null) return null;
            Field saveField = new Field(Cheker.field);

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 12; j++) {
                    if (count % 2 == 0) {
                        if (!field.isEmpty(i, j) && field.isBot(i, j)) {
                            Cheker cheker = field.getChekerToMove(i, j, cube[0]);
                            Point newpos = field.getNewPos(i, j, cube[0]);
                            if (newpos == null) continue;
                            cheker.go(newpos.getI(), newpos.getJ());
                            refreshMays();
                            sum += field.getCost(newpos.getI(), newpos.getJ(), player);
                            if (from == null) from = new Point(i, j);
                            if (to == null) to = new Point(newpos.getI(), newpos.getJ());
                            if (best == null) best = new Move(from,to,sum);
                            Move newbest = best.copy();
                            best = best.max(getBestMove(sum, newbest, -1 * player, count + 1, from, to));
                            sum -= field.getCost(newpos.getI(), newpos.getJ(), player);
                            cheker.goBack(i, j);
                            field.setField(saveField);
                            refreshMays();
                        }
                    }
                    else {
                        if (!field.isEmpty(i, j) && !field.isBot(i, j)) {
                            Cheker cheker = field.field[i][j].chekers.getFirst();
                            LinkedList<Point> saveMoves = cheker.copyMoves();
                            for (Point newpos : saveMoves) {
                                cheker.go(newpos.getI(), newpos.getJ());
                                refreshMays();
                                sum -= field.getCost(newpos.getI(), newpos.getJ(), player);
                                Move newbest = best.copy();
                                best = best.min(getBestMove(sum, newbest, -1 * player, count + 1, from, to));
                                sum += field.getCost(newpos.getI(), newpos.getJ(), player);
                                cheker.goBack(i, j);
                                field.setField(saveField);
                                refreshMays();
                            }

                        }
                    }
                }
            }
        }
        return best;
    }

    void go(Point from, Point to, int count)
    {
        //refreshMays();
        Cheker cheker = Cheker.field.getCheker(from);
        cheker.go(to);
        refreshMays();
    }

}
