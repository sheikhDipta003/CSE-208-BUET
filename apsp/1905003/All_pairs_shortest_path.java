import java.util.Arrays;
import java.util.Scanner;

public class All_pairs_shortest_path {
    private static final int INF = 1000000;

    public static void main(String[] args) {
        int n, m;
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        m = scanner.nextInt();
        GraphList G = new GraphList(n);

        for(int i = 0; i < m; i++){
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            G.setEdge(u-1, v-1, w);
        }

        //create weight matrix
        int[][] W = new int[G.n()][];
        for(int i= 0; i < n; i++){
            W[i] = new int[n];
            for(int j = 0; j < n; j++){
                if(i == j)  W[i][j] = 0;
                else {
                    if(G.isEdge(i, j))  W[i][j] = G.weight(i, j);
                    else    W[i][j] = INF;
                }
            }
        }

        int[][] result = APSP_matrix(W);
//        int[][] result = floyd_warshall(W);
        for(int[] row: result){
            for(int col: row) {
                if(col < INF)  System.out.print(col + " ");
                else System.out.print("INF" + " ");
            }
            System.out.println();
        }
    }

    public static int[][] APSP_matrix(int[][] W){
        int n = W.length;
        int[][] L1 = W;
        int m = 1;

        while(m < n-1){
            L1 = APSP_help(L1, L1);
            m = 2*m;
        }

        return L1;
    }

    public static int[][] APSP_help(int[][] L, int[][] W){
        int n = L.length;
        int[][] L1 = new int[n][];
        for(int i = 0; i < n; i++){
            L1[i] = new int[n];
            Arrays.fill(L1[i], INF);
        }

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                for(int k = 0; k < n; k++){
                    L1[i][j] = Integer.min(L1[i][j], L[i][k]+W[k][j]);
                }
            }
        }

        return L1;
    }

    public static int[][] floyd_warshall(int[][] W){
        int n = W.length;

        for(int k = 0; k < n; k++){
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    W[i][j] = Integer.min(W[i][j], W[i][k]+ W[k][j]);
                }
            }
        }

        return W;
    }
}
