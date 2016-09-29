import java.util.*;

/**
 * Created by Kshitij Gorde on 9/16/2016.
 This file contains the main class definition of PuzzleSolver and it's related methods
 The implementation of A* algorithm is carried out here.
 */


public class PuzzleSolver{

    public static void main(String args[]){
        Integer [][]initial = new Integer[3][3];
        Integer [][]goal = new Integer[3][3];
        List<State> closedList = new ArrayList<>();
        PriorityQueue<State> openList = new PriorityQueue<State>(1, new Comparator<State>() {   //Priority queue for holding unexpanded states
            @Override
            public int compare(State first, State second) {     //overridden compare to remove the element with lowest f(n) cost first
                if(first.getF()>second.getF()){
                    return 1;
                }
                else
                    return -1;
            }
        });
        System.out.println("Input the initial state(Enter 0 for blank)");
        initial = getInitialStates();                           //handles illegal character checking
        System.out.println("Input the Goal state");
        goal = getInitialStates();

        if(isSolvable(initial,goal)){                       // Check if instance is solvable based on parity of arranged tiles/blocks.
            State initialState = new State(initial);
            State goalState = new State(goal);
            initialState.setG(0);
            initialState.setF(initialState.calculateF(initialState.getG(),goalState.getTilePositions()));
            System.out.println();

            openList.add(initialState);                     //initial state added to OpenList
            while(!openList.isEmpty()) {
                State currentState = openList.poll();       //poll will return the state with lowest f(n)
                if (currentState.equals(goalState)) {
                    System.out.println("A* search successfully completed.");
                    closedList.add(currentState);
                    currentState.printFinalPath();
                    System.out.println("The Open List size :" + openList.size());
                    System.out.println("The Closed List size :" + closedList.size());
                    System.out.println("Total states generated(open list+closed list) :"+(openList.size()+closedList.size()));
                    break;
                }
                else {
                    closedList.add(currentState);       //add to expanded list i.e closedList
                    if (currentState.isMovableLeft()) {
                        State successor = new State(currentState);  //currentState will be assigned as the parent
                        successor = successor.moveLeft(currentState);
                        successor.setG(successor.getParentState().getG()+1);
                        successor.setF(successor.calculateF(successor.getG(), goalState.getTilePositions()));
                        if (!closedList.contains(successor)) {      //checking for already expanded states.
                            openList.add(successor);
                        }
                    }
                    if (currentState.isMovableRight()) {
                        State successor = new State(currentState);
                        successor = successor.moveRight(currentState);
                        successor.setG(successor.getParentState().getG() + 1);
                        successor.setF(successor.calculateF(successor.getG(), goalState.getTilePositions()));
                        if (!closedList.contains(successor)) {
                            openList.add(successor);
                        }
                    }
                    if (currentState.isMovableUpwards()) {
                        State successor = new State(currentState);
                        successor = successor.moveUp(currentState);
                        successor.setG(successor.getParentState().getG()+1);
                        successor.setF(successor.calculateF(successor.getG(), goalState.getTilePositions()));
                        if (!closedList.contains(successor)) {
                            openList.add(successor);
                        }
                    }
                    if (currentState.isMovableDownwards()) {
                        State successor = new State(currentState);
                        successor = successor.moveDown(currentState);
                        successor.setG(successor.getParentState().getG()+1);
                        successor.setF(successor.calculateF(successor.getG(), goalState.getTilePositions()));
                        if (!closedList.contains(successor)) {
                            openList.add(successor);
                        }
                    }
                }
            }
        }
        else{
            System.out.println("The Given initial configuration is not solvable for the current Goal State");
        }
    }

    /*
    The following method handles the inputs from the user with respect to the configuration of the tiles. It will also check for invalid
    inputs and repeated inputs
     */
    public static Integer[][] getInitialStates(){
        Integer[][] puzzle = new Integer[3][3];
        Scanner input = new Scanner(System.in);
        List<Integer> checkerList = new ArrayList<>();
        for (int m=0;m<3;m++){
            for(int n=0;n<3;n++){
                System.out.println("Input for ["+m+"]["+n+"]");
                if(input.hasNextInt()) {
                    puzzle[m][n] = input.nextInt();
                    if(checkerList.contains(puzzle[m][n])){
                        n--;
                        System.out.println("Position already defined for the puzzle");
                    }

                    if(!(puzzle[m][n]>=0 && puzzle[m][n]<=8)){
                        System.out.println("Integers only from 0-8 are allowed!.");
                        n--;
                    }
                    else{
                        checkerList.add(puzzle[m][n]);
                    }
                }
                else{
                    System.out.println("Only Integers accepted!.Please try again");
                    System.exit(100);
                }
            }
        }
        return puzzle;
    }

    /*
    The following method will check if the given instance of the 8-puzzle is solvable by checking the
    number of inversions in the input. Inversions are the pairs which are inverted in the initial state with respect to the Goal state
    if inversions are even in initial and goal state then the instance is solvable otherwise it isn't.
     */

    public static boolean isSolvable(Integer[][] initial, Integer[][] goal){
    //return true if solvable else false
        boolean solvable = false;
        int initialInversions = getInversions(initial);
        int goalInversions = getInversions(goal);
        if(initialInversions % 2 == goalInversions % 2){
            solvable = true;
        }
        return solvable;
    }

    /*
    The following method will count the inversions in the given instance of the 8 puzzle configuration.
    Inversion of i = no. of tiles lesser than i which are placed ahead of i.
     */
    public static int getInversions(Integer[][] puzzle){
        int inversions = 0;
        int count=0;
        int[] temp = new int[9];

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(puzzle[i][j]==0){
                    continue;
                }
                else{
                    temp[count++] = puzzle[i][j];
                }
            }
        }
        for(int i=0;i<9;i++){
            for(int j=i+1;j<9;j++){
                if(temp[i]>temp[j]){
                    inversions++;
                }
            }
        }

        return inversions;
    }
}
