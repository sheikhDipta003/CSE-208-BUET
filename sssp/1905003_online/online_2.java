import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class online_2 {
    private static int[] D;
    private static int[] parent;

    public static void main(String[] args) {
        int N, E, s;   //N -> number of nodes, E -> number of edges
        Scanner scanner = new Scanner(System.in);
        N = scanner.nextInt();
        E = scanner.nextInt();
        GraphList G = new GraphList(N);
        int[] best = new int[N];

        for(int i = 0; i < E; i++){
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            G.setEdge(u, v, w);
        }
        s = scanner.nextInt();

        minEdgeMST(G, best, s);

        for(int i = 0; i < G.n(); i++){
            System.out.println(best[i]);
        }
    }

    public static void minEdgeMST(GraphList G, int[] best, int s){
        D = new int[G.n()];
        parent = new int[G.n()];
        int [] best2 = new int[G.n()];
        Arrays.fill(best, 0);
        Arrays.fill(best2, 0);
        for(int i = 0; i < G.n(); i++){
            D[i] = 1000000;
            parent[i] = -1;
        }
        D[s] = 0;

        PriorityQueue<Integer> pq = new PriorityQueue<>(new comp());
        for(int i = 0; i < G.n(); i++){
            pq.add(i);
        }
        while(!pq.isEmpty()){
            int u = pq.remove();
            for(int v = G.first(u); v != -1; v = G.next(u, v)){
                if(D[u] + G.weight(u, v) < D[v]){
                    pq.remove(v);
                    D[v] = D[u] + G.weight(u, v);
                    pq.add(v);
                    parent[v] = u;
                    int c = shortestPath(G.n(), s, v).length()-1;
                    best[v] = c;
                    best2[v] = c;
                }
                if(D[u] + G.weight(u, v) == D[v]){
                    pq.remove(v);
                    D[v] = D[u] + G.weight(u, v);
                    pq.add(v);
                    parent[v] = u;
                    int c = shortestPath(G.n(), s, v).length()-1;
                    best2[v] = c;
                }
            }
        }

        for(int i = 0; i < G.n(); i++){
            best[i] = Math.min(best[i], best2[i]);
        }
    }

    public static String shortestPath(int N, int s, int d){
        int w = d, k = 0;
        StringBuilder result = new StringBuilder();
        result.append(w);
        while(true){
            result.append(">-");
            result.append(parent[w]);
            if(parent[w] == s)    break;
            w = parent[w];
            k++;
            if(k >= N-1) break;
        }
        return result.reverse().toString();
    }

    static class comp implements Comparator<Integer> {
        @Override
        public int compare(Integer u, Integer v) {
            return D[u] - D[v];
        }
    }
}
