import java.util.Arrays;
import java.util.Scanner;

public class online {
    public static void main(String[] args) {
        int T, m, n;
        Scanner scanner = new Scanner(System.in);
        T = scanner.nextInt();
        int[] maxMarriage = new int[T];
        for(int i = 0; i < T; i++) {
            m = scanner.nextInt();
            n = scanner.nextInt();
            int[] M_age = new int[m];
            int[] M_height = new int[m];
            int[] M_div = new int[m];
            int[] W_age = new int[n];
            int[] W_height = new int[n];
            int[] W_div = new int[n];

            for(int j = 0; j < m; j++){
                String[] tokens = (scanner.nextLine()).split(" ");
                M_age[j] = Integer.parseInt(tokens[0]);
                M_height[j] = Integer.parseInt(tokens[1]);
                M_div[j] = Integer.parseInt(tokens[2]);
            }

            for(int j = 0; j < n; j++){
                String[] tokens = (scanner.nextLine()).split(" ");
                W_age[j] = Integer.parseInt(tokens[0]);
                W_height[j] = Integer.parseInt(tokens[1]);
                W_div[j] = Integer.parseInt(tokens[2]);
            }

            int V = m+n+2;
            GraphList G = new GraphList(V);
            int[][] cap = new int[V][];
            int[][] f = new int[V][V];
            for (int j = 0; j < V; j++) Arrays.fill(f[j], 0);
            int[][] minCut = new int[2][V];
            for (int[] ints : minCut) Arrays.fill(ints, -1);
            for(int j = 0; j < m; j++){
                for(int k = 0; k < n; k++){
                    if(Math.abs(M_height[j] - W_height[k]) <= 12 && Math.abs(M_age[j] - W_age[k]) <= 5 && (M_div[j] == W_div[k]))    {
                        G.setEdge(j+1, m+k, 1);
                        G.setEdge(m+k, j+1, 1);
                        cap[j+1][m+k] = 1;
                        cap[m+k][j+1] = 1;
                    }
                }
            }
            for(int j = 0; j < n; j++){
                G.setEdge(j+m+1, m+n+1, 1);
            }

            maxMarriage[i] = G.ford_fulkerson(cap, f, minCut, 0, m+n+1);
        }

        for(int i = 0; i < maxMarriage.length; i++){
            System.out.println("Case " + (i+1) + ": " + maxMarriage[i]);
        }
    }
}
