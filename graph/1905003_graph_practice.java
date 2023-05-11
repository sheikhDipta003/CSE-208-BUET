import java.util.ArrayList;
import java.util.Scanner;

//Matrix implementation
class MatGraph{
    private int[][] matrix;
    private int numEdge;
    private int[] Mark;

    MatGraph(){}
    MatGraph(int n){
        Init(n);
    }

    public void Init(int n) {
        numEdge = 0;
        Mark = new int[n];
        matrix = new int[n][n];
    }

    public int n() {
        return matrix.length;
    }


    public int e() {
        return numEdge;
    }


    public int first(int v) {
        for(int i = 0; i < n(); i++){
            if(matrix[v-1][i] != 0){
                return (i+1);
            }
        }
        return n()+1;
    }


    public int next(int v, int w) {
        for(int j = w-1+1; j < n(); j++){
            if(matrix[v-1][j] != 0){
                return (j+1);
            }
        }
        return n()+1;
    }


    public void setEdge(int i, int j, int w) {
        if(w > 0) {
            if(matrix[i-1][j-1] == 0)    numEdge++;
            matrix[i-1][j-1] = w;
        }
    }


    public void delEdge(int i, int j) {
        if(matrix[i-1][j-1] != 0)    numEdge--;
        matrix[i-1][j-1] = 0;
    }


    public boolean isEdge(int i, int j) {
        return matrix[i-1][j-1] != 0;
    }


    public int weight(int i, int j) {
        return matrix[i-1][j-1];
    }

    public void setMark(int v, int val) { Mark[v-1] = val; }
    public int getMark(int v) { return Mark[v-1]; }

    public ArrayList<Integer> findDependencies(int v){
        ArrayList<Integer> d = new ArrayList<>();
        for(int i = 1; i <= n(); i++){
            if(matrix[i-1][v-1] != 0){
                d.add(i);
            }
        }
        return d;
    }

    public boolean isOrderingPossible(){
        for(int i = 1; i <= n(); i++){
            if(findDependencies(i).size() == 0){
                return true;
            }
        }
        return false;
    }
}
public class Car_Assembly_Ordering {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int d = scanner.nextInt();
        int [][] D = new int[d][];
        for(int i = 0; i < d; i++){
            D[i] = new int[2];
            D[i][0] = scanner.nextInt();
            D[i][1] = scanner.nextInt();
        }

        MatGraph stepsOrdering = new MatGraph(n);
        for(int i = 0; i < d; i++){
            stepsOrdering.setEdge(D[i][1], D[i][0], 1);
        }

        if(!stepsOrdering.isOrderingPossible()){
            System.out.println("Not Possible");
        }
    }
}
