package org.example;


import java.util.ArrayList;
import java.util.Scanner;

import static org.example.Main.getchar;

class Unit {
    int n;
    int x,y;

    public enum Direction{
        UP('↑'), DOWN('↓'), LEFT('←'), RIGHT('→'),
        UPLEFT('↖'), UPRIGHT('↗'), DOWNLEFT('↙'), DOWNRIGHT('↘');

        private final char dirChar;
        Direction(char dirChar) {
            this.dirChar = dirChar;
        }

        public char getDirChar() {
            return dirChar;
        }


    }

    public Unit(int x, int y, int n){
        this.x = x;
        this.y = y;
        this.n = n;
    }

    public void move(Direction direction){
        switch(direction){
            case UP:
                x--; break;
            case DOWN:
                x++; break;
            case LEFT:
                y--; break;
            case RIGHT:
                y++; break;
            case UPLEFT:
                x--; y--; break;
            case UPRIGHT:
                x--;y++; break;
            case DOWNLEFT:
                x++; y--; break;
            case DOWNRIGHT:
                x++; y++; break;
        }
        x = (x+n)%n;
        y = (y+n)%n;
    }

}

class Player extends Unit {
    public Player(int x, int y, int n) {
        super(x, y, n);
    }
    void move(char C){
        if(C=='U') move(Direction.UP);
        else if(C=='D') move(Direction.DOWN);
        else if(C=='L') move(Direction.LEFT);
        else if(C=='R') move(Direction.RIGHT);
    }
}

class Invader extends Unit {
    Direction direction;
    public Invader(int x, int y, int n) {
        super(x, y, n);
    }

    void move(){}
}

class VInvader extends Invader{
    public VInvader(int x, int y, int n){
        super(x,y,n);
        direction = Direction.UP;
    }

    void move(){
        if(x==0) direction = Direction.DOWN;
        else if(x==n-1) direction = Direction.UP;
        move(direction);
    }
}
class HInvader extends Invader{
    public HInvader(int x, int y, int n){
        super(x,y,n);
        direction = Direction.LEFT;
    }
    void move(){
        if(y==0) direction = Direction.RIGHT;
        else if(y==n-1) direction = Direction.LEFT;
        move(direction);
    }
}
class DInvader extends Invader{
    public DInvader(int x, int y, int n){
        super(x,y,n);
        direction = Direction.UPLEFT;
    }
    void move(){
        if(x==0){
            if(direction == Direction.UPLEFT){
                direction = Direction.DOWNLEFT;
            }
            else if(direction == Direction.UPRIGHT){
                direction = Direction.DOWNRIGHT;
            }
        }
        else if(x==n-1){
            if(direction == Direction.DOWNLEFT){
                direction = Direction.UPLEFT;
            }
            else if(direction == Direction.DOWNRIGHT){
                direction = Direction.UPRIGHT;
            }
        }

        if(y==0){
            if(direction == Direction.DOWNLEFT){
                direction = Direction.DOWNRIGHT;
            }
            else if(direction == Direction.UPLEFT){
                direction = Direction.UPRIGHT;
            }
        }
        else if(y==n-1) {
            if (direction == Direction.DOWNRIGHT) {
                direction = Direction.DOWNLEFT;
            } else if (direction == Direction.UPRIGHT) {
                direction = Direction.UPLEFT;
            }
        }
        move(direction);
    }
}

class Time{
    private static long seconds = 0;
    private static Thread timer;

    public static void start(){
        if(timer != null && timer.isAlive()) return;
        timer = new Thread(()->{
           try{
               while(true){
                   Thread.sleep(1000);
                   seconds++;
               }
           } catch(InterruptedException e){

           }
        });
        timer.setDaemon(true);
        timer.start();
    }

    public static long getSeconds(){
        return seconds;
    }

}

class Map{

    int n; String name; String init;
    int Highscore; long bestTime;

    ArrayList<Invader> Invaders = new ArrayList<>();
    Player P; Unit G; int score; long startTime;

    public Map(String name, int n, String init){
        this.n = n;
        this.name = name;
        this.init = init;
        this.Highscore = - 1;
    }

    public void set(){
        score = 0;
        Invaders.clear();
        startTime = Time.getSeconds();
        for(int i = 0; i< n; i++){
            for(int j = 0; j< n; j++){
                int idx = i* n +j;
                char x = init.charAt(idx);
                if(x=='P'){
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
                else if(x=='G'){
                    G = new Unit(i,j, n);
                }
            }
        }
    }

    public boolean executeTurn(){
        char[][] board = new char[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                board[i][j] = 'O';
            }
        }
        board[P.x][P.y] = 'P';
        board[G.x][G.y] = 'G';

        if(P.x == G.x && P.y == G.y){
            long endTime = Time.getSeconds();
            System.out.println("STAGE CLEAR!");
            System.out.println("Turn: " + score +" Time: "+ (endTime-startTime));
            if(Highscore == -1 || (score < Highscore)){
                Highscore = score;
                bestTime = endTime - startTime;
            }
            else if(score == Highscore && endTime - startTime  < bestTime){
                bestTime = endTime - startTime;
            }
            return false;
        }
        score+=1;

        for (Invader value : Invaders) {
            board[value.x][value.y] = value.direction.getDirChar();
            if (value.x == P.x && value.y == P.y) {
                System.out.println("GAME OVER.");
                return false;
            }
        }
        for(int i=0; i<n;i++){
            for(int j=0; j<n; j++){
                System.out.print(board[i][j]);
            }
            System.out.println();
        }

        char C = getchar("Move");

        while(C!='L' && C!='R' && C!='U' && C!='D'){
            System.out.println("Wrong input");
            C = getchar("Move");
        }
        P.move(C);
        for (Invader invader : Invaders) {
            invader.move();
        }

        return true;
    }
}


public class Main {

    public static boolean isValid(int n, String init){
        if(init.length() != n*n) return false;
        int pcnt = 0; int gcnt= 0;
        for(char c : init.toCharArray()){
            if(c=='P') pcnt+=1;
            else if(c=='G') gcnt+=1;
            if(c!='P' && c!='G' && c!='O' && c!='V' && c!='H' && c!='D') return false;
        }
        return pcnt==1 && gcnt==1;
    }

    public enum gameState{
        MAIN_MENU, MAP_SELECT, MAP_BUILD, HOW_TO_PLAY, GAME_EXIT
    }

    public static gameState state=  gameState.MAIN_MENU;

    static ArrayList<Map> maplist = new ArrayList<>();

    static Scanner sc = new Scanner(System.in);
    public static int getint(){
        while(true) {
            System.out.print("Input: ");
            if (sc.hasNextInt()) {
                return sc.nextInt();
            } else {
                System.out.println("Invalid input");
            }
        }
    }
    public static int getint(String s){
        while(true) {
            System.out.print(s + " Input: ");
            if (sc.hasNextInt()) {
                return sc.nextInt();
            } else {
                System.out.println("Invalid input");
                sc.next();
            }
        }
    }
    public static String getstring(String s){
        System.out.print(s + " Input: ");
        return sc.next();
    }
    public static char getchar(String s){
        System.out.print(s + " Input: ");
        return sc.next().charAt(0);
    }

    public static void Play(Map map){
        map.set();
        while(map.executeTurn()) {}

    }

    public static void MAIN_MENU(){
        System.out.println("1. Play");
        System.out.println("2. Build a Map");
        System.out.println("3. How to Play");
        System.out.println("4. EXIT");
        int d = getint();
        switch(d){
            case 1:
                state = gameState.MAP_SELECT;
                break;
            case 2:
                state = gameState.MAP_BUILD;
                break;
            case 3:
                state = gameState.HOW_TO_PLAY;
                break;
            case 4:
                state = gameState.GAME_EXIT;
                break;
            default:
                System.out.println("Wrong input");
                break;
        }
    }

    public static void MAP_SELECT(){
        for(int i=0; i<maplist.size(); i++){
            Map map = maplist.get(i);
            System.out.print((i+1)+". " + map.name);
            if(map.Highscore!=-1) System.out.println(" | Highscore: "+map.Highscore + " Turn(s), " +
                    map.bestTime +" Second(s)");
            else System.out.println();
        }
        System.out.println((maplist.size()+1)+". " + "Back to Main");
        int num = getint();
        if(num< 1 || num>maplist.size()+1){
            System.out.println("Invalid input");
            return;
        }
        if(num==maplist.size()+1){
            state = gameState.MAIN_MENU;
        }
        else{
            Map map = maplist.get(num-1);
            Play(map);
        }
    }

    public static void HOW_TO_PLAY(){
        System.out.println(""" 
                
                HOW TO PLAY
                Your are at 'P'.
                Arrows represent Invaders and their moving directions.
                Initially each Invaders is in independent position.
                If two or more Invaders are in same position, you'll be able to see only one Invader in the position.
                Invaders bounce when they encounter end of the map.
                However You will come out from the opposite side.
                Input L,R,U or D to move 1 block toward the direction.
                Avoid Invaders and reach the Goal(G) to win. 
                Position of each turn matters, swaping position with an Invader is possible.
                """);
        System.out.println("""
                HOW TO BUILD A MAP
                P: Player's starting position
                G: Goal
                V: Invader moving upward initially
                H: Invader moving left initially
                D: Invader moving left-upward initially
                O: empty space
                """);
        state = gameState.MAIN_MENU;
    }

    public static void MAP_BUILD(){
        int n = getint("Map size");
        System.out.println("Map input("+n+"x"+n+") :");
        if(n>10 || n<2){
            System.out.println("Invalid input");
            return;
        }
        String init = "";

        for(int i=0; i<n; i++){
            String s = sc.next();
            init += s;
        }

        String name = getstring("Map name");

        state=gameState.MAIN_MENU;
        if(!isValid(n,init)) {
            System.out.println("Map build Failed!");
        }
        else{
            System.out.println("Map build Success!");
            maplist.add(new Map(name,n,init));
        }
    }

    public static void main(String[] args) {

        Time.start();
        System.out.println("Welcome!");
        maplist.add(new Map("Sample Map", 2, "POVG"));

        while(true){
            switch(state){
                case MAIN_MENU:
                    MAIN_MENU();
                    break;
                case MAP_SELECT:
                    MAP_SELECT();
                    break;
                case MAP_BUILD:
                    MAP_BUILD();
                    break;
                case HOW_TO_PLAY:
                    HOW_TO_PLAY();
                    break;
                case GAME_EXIT:
                    System.out.println("Thank you for playing!");
                    return;
            }
        }

    }
}