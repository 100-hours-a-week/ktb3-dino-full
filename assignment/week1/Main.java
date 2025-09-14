package org.example;


import java.util.ArrayList;
import java.util.Scanner;

class Unit {
    int n;
    int x,y;
    int[] dx = {n-1,n-1,n-1,0,1,1,1,0};
    int[] dy = {n-1,0,1,1,1,0,n-1,n-1};
    int dir;
    public Unit(int x, int y, int n){
        this.x = x;
        this.y = y;
        this.n = n;
    }
    public void move(int direction){
        x += dx[direction];
        y += dy[direction];
        x%=n;
        y%=n;
    }
    void move(){

    }
}

class Player extends Unit {
    public Player(int x, int y, int n) {
        super(x, y, n);
    }
    void move(char C){
        String dirstring = "?U?R?D?L";
        move(dirstring.indexOf(C));
    }
}

class Invader extends Unit {
    public Invader(int x, int y, int n) {
        super(x, y, n);
    }
}

class VInvader extends Invader{
    public VInvader(int x, int y, int n){
        super(x,y,n);
        dir = 1;
    }
    void move(){
        if(x==0) dir=5;
        else if(x==n-1) dir= 1;
        move(dir);
    }
}

class HInvader extends Invader{
    public HInvader(int x, int y, int n){
        super(x,y,n);
        dir = 7;
    }
    void move(){
        if(y==0) dir =3;
        else if(y==n-1) dir = 7;
        move(dir);
    }
}
class DInvader extends Invader{
    public DInvader(int x, int y, int n){
        super(x,y,n);
        dir = 0;
    }
    void move(){
        if(x==0){
            if(dir == 0) dir =6;
            else dir = 4;
        }
        else if(x==n-1){
            if(dir == 4) dir = 2;
            else dir = 0;
        }
        if(y==0){
            if(dir == 0) dir = 2;
            else dir = 4;
        }
        else if(y==n-1){
            if(dir == 2) dir = 0;
            else dir = 6;
        }
        move(dir);
    }
}


class Map{
    int n;
    String name;
    ArrayList<Invader> Invaders;
    Player P; Unit G;
    public Map(String name, int n){
        this.n =n;
        this.name = name;
        Invaders = new ArrayList<Invader>();
    }
    public Map(Map map){
        this.n = map.n;
        this.name = map.name;
        Invaders = map.Invaders;
        P = map.P;
        G = map.G;
    }
    public boolean set(String init){
        int Pcnt = 0; int Gcnt = 0;
        for(int i = 0; i< n; i++){
            for(int j = 0; j< n; j++){
                int idx = i* n +j;
                char x = init.charAt(idx);
                if(x=='P'){
                    Pcnt +=1;
                    P = new Player(i,j, n);
                }
                else if(x=='V'){
                    Invaders.add(new VInvader(i,j, n));
                }
                else if(x=='H'){
                    Invaders.add(new HInvader(i,j, n));
                }
                else if(x=='D'){
                    Invaders.add(new DInvader(i,j, n));
                }
                else if(x=='O'){
                    continue;
                }
                else if(x=='G'){
                    G = new Unit(i,j, n);
                    Gcnt+=1;
                }
                else return false;
            }
        }
        return Pcnt == 1 && Gcnt == 1;
    }
}




public class Main {
    public static int getint(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Input: ");
        return sc.nextInt();
    }
    public static int getint(String s){
        Scanner sc = new Scanner(System.in);
        System.out.print(s + " Input: ");
        return sc.nextInt();
    }
    public static String getstring(String s){
        Scanner sc = new Scanner(System.in);
        System.out.print(s + " Input: ");
        return sc.next();
    }
    public static char getchar(String s){
        Scanner sc = new Scanner(System.in);
        System.out.print(s + " Input: ");
        return sc.next().charAt(0);
    }

    public static void Play(Map map){
        while(true){
            int n = map.n;
            char[][] board = new char[n][n];
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    board[i][j] = 'O';
                }
            }
            board[map.P.x][map.P.y] = 'P';
            board[map.G.x][map.G.y] = 'G';
            if(map.P.x == map.G.x && map.P.y == map.G.y){
                System.out.println("STAGE CLEAR!");
                return;
            }

            char[] arrows = {'↖','↑','↗','→','↘','↓','↙','←'};
            for(int i=0; i<map.Invaders.size();i++){
                board[map.Invaders.get(i).x][map.Invaders.get(i).y] = arrows[map.Invaders.get(i).dir];
                if(map.Invaders.get(i).x == map.P.x && map.Invaders.get(i).y == map.P.y){
                    System.out.println("GAME OVER.");
                    return;
                }
            }
            for(int i=0; i<n;i++){
                for(int j=0; j<n; j++){
                    System.out.print(board[i][j]);
                }
                System.out.println();
            }
            char C = getchar("Move");
            map.P.move(C);
            for(int i=0; i<map.Invaders.size();i++){
                map.Invaders.get(i).move();
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<String> maplist = new ArrayList<String>();
        ArrayList<String> mapnamelist = new ArrayList<String>();
        ArrayList<Integer> mapsz = new ArrayList<Integer>();
        maplist.add("POVG");
        mapnamelist.add("Sample Map");
        mapsz.add(2);
        System.out.println("Welcome!");
        Scanner sc = new Scanner(System.in);
        int state = 0;
        while(true){
            if(state==0){ // Main
                System.out.println("1. Play");
                System.out.println("2. Build a Map");
                System.out.println("3. How to Play");
                System.out.println("4. EXIT");
                state = getint();
            }
            else if(state==1){ // Map Select
                for(int i=0; i<maplist.size(); i++){
                    System.out.println((i+1)+". " + mapnamelist.get(i));
                }
                System.out.println((maplist.size()+1)+". " + "Back to Main");
                int num = getint();
                if(num< 1 || num>maplist.size()+1){
                    System.out.println("Invalid input");
                    continue;
                }
                if(num==maplist.size()+1){
                    state = 0;
                }
                else{
                    Map map = new Map("adsffs", mapsz.get(num-1));
                    map.set(maplist.get(num-1));
                    Play(map);
                }
            }
            else if(state==2){ // Build a Map
                state = 0;
                int n = getint("Map size");
                System.out.println("Map input("+n+"x"+n+") :");
                if(n>10 || n<2){
                    System.out.println("Invalid input");
                    continue;
                }
                String init = "";
                boolean flag = true;
                for(int i=0; i<n; i++){
                    String s = sc.next();
                    init += s;
                    flag &= (s.length()==n);
                }

                String name = getstring("Map name");
                Map map = new Map(name, n);

                flag &= map.set(init);
                if(flag){
                    System.out.println("Map build Success!");
                    maplist.add(init);
                    mapnamelist.add(name);
                    mapsz.add(n);
                }
                else{
                    System.out.println("Map build Failed!");
                }
            }
            else if(state==3){
                state = 0;
                System.out.println();
                System.out.println("HOW TO PLAY");
                System.out.println("Your are at 'P'.");
                System.out.println("Arrows represent Invaders and their moving directions");
                System.out.println("Initially each Invaders is in independent position.");
                System.out.println("If two or more Invaders are in same position, you'll be able to see only one Invader in the position.");
                System.out.println("Invaders bounce when they encounter end of the map.");
                System.out.println("However You will come out from the opposite side.");
                System.out.println("Input L,R,U or D to move 1 block toward the direction.");
                System.out.println("Avoid Invaders and reach the Goal(G) to win ");
                System.out.println();
                System.out.println("HOW TO BUILD A MAP");
                System.out.println("P: Player's starting position\nG: Goal\n" +
                        "V: Invader moving upward initially\n" +
                        "H: Invader moving left initially\n"+
                        "D: Invader moving left-upward initially\n" +
                        "O: empty space");
                System.out.println();
            }
            else if(state==4){
                break;
            }
            else{
                state= 0;
                continue;
            }
        }
        System.out.println("Thank you for playing!");
    }
}