import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Dijkstra_BellmanFord_Algo {
    private static int[] D;
    private static int[] parent;


    public static void main(String[] args) {
        int n, m, s, d;   //n -> number of nodes, m -> number of edges
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        m = scanner.nextInt();
        GraphList G = new GraphList(n);

        for(int i = 0; i < m; i++){
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            G.setEdge(u, v, w);
        }
        s = scanner.nextInt();
        d = scanner.nextInt();

        if(Bellman_Ford(G, s)) {
            System.out.println("The graph does not contain a negative cycle");
            System.out.println("Shortest path cost: " + minCost(G, s, d));
            System.out.println(shortestPath(G.n(), s, d));
        }
        else {
            System.out.println("The graph contains a negative cycle");
        }

//        Dijkstra(G, s);
//        System.out.println("Shortest path cost: " + minCost(G, s, d));
//        System.out.println(shortestPath(G.n(), s, d));
    }

    public static void Dijkstra(GraphList G, int s){
        initialize(G, s);

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
                }
            }
        }
    }

    public static boolean Bellman_Ford(GraphList G, int s){
        initialize(G, s);

        for(int i = 0; i < G.n()-1; i++){
            for(int u = 0; u < G.n(); u++){
                for(int v = G.first(u); v != -1; v = G.next(u, v)){
                    relax(u, v, G.weight(u, v));
                }
            }
        }

        for(int u = 0; u < G.n(); u++){
            for(int v = G.first(u); v != -1; v = G.next(u, v)){
                if(D[v] > D[u] + G.weight(u, v))    return false;
            }
        }

        return true;
    }

    public static void initialize(GraphList G, int s){
        D = new int[G.n()];
        parent = new int[G.n()];
        for(int i = 0; i < G.n(); i++){
            D[i] = 1000000;
            parent[i] = -1;
        }
        D[s] = 0;
    }

    public static void relax(int u, int v, int wt){
        if(D[u] + wt < D[v]){
            D[v] = D[u] + wt;
            parent[v] = u;
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

    public static int minCost(GraphList G, int s, int d){
        String path = shortestPath(G.n(), s, d);
        String [] v_path = path.split("->");
        int c = 0;
        for(int i = s; i < v_path.length-1; i++){
            c += G.weight(Integer.parseInt(v_path[i]), Integer.parseInt(v_path[i+1]));
        }
        return c;
    }

    static class comp implements Comparator<Integer> {
        @Override
        public int compare(Integer u, Integer v) {
            return D[u] - D[v];
        }
    }
}
