import java.io.*;
import java.util.Comparator;
import java.util.PriorityQueue;

public class offline_binomial_fib_heap {
    private static int[] parent;
    private static int[] D;

    public static void main(String[] args) throws IOException {
        //take edges of graph as input
        int N, M;   // 3 <= N <= 1000, N <= M <= 10000
        BufferedReader br = new BufferedReader(new FileReader("in_graph.txt"));
        String firstLine = br.readLine();
        N = Integer.parseInt(firstLine.split(" ")[0]);
        M = Integer.parseInt(firstLine.split(" ")[1]);
        GraphList G = new GraphList(N);

        for(int i = 0; i < M; i++) {
            String s = br.readLine();
            if (s == null) break;

            int u = Integer.parseInt(s.split(" ")[0]);
            int v = Integer.parseInt(s.split(" ")[1]);
            int wt = Integer.parseInt(s.split(" ")[2]);
            G.setEdge(u, v, wt);
            G.setEdge(v, u, wt);
        }
        br.close();

        //input source and destination vertices
        br = new BufferedReader(new FileReader("in_source_dest.txt"));
        int k = Integer.parseInt(br.readLine());
        BufferedWriter bw = new BufferedWriter(new FileWriter("out.txt"));
        for(int i = 0; i < k; i++){
            String line = br.readLine();
            if (line == null) break;

            int s = Integer.parseInt(line.split(" ")[0]);
            int d = Integer.parseInt(line.split(" ")[1]);

            long startTime = System.nanoTime();
            Dijkstra_binomial(G, s);
            double execTime_binomial = (System.nanoTime() - startTime) / Math.pow(10, 9);

            startTime = System.nanoTime();
            Dijkstra_fib(G, s);
            double execTime_fib = (System.nanoTime() - startTime) / Math.pow(10, 9);

            bw.write(shortestPath(G.n(), s, d).length()-1 + '\t' + path_cost(G, s, d) + '\t' + String.valueOf(execTime_binomial) + '\t' + String.valueOf(execTime_fib));
        }

        br.close();
        bw.close();
    }

    public static void Dijkstra_binomial(GraphList G, int s){
        initialize(G, s);

        bin_heap pq = new bin_heap();
        node [] nodes = new node[G.n()];
        for(int i = 0; i < G.n(); i++){
            nodes[i] = new node(i);
            pq.insert(nodes[i]);
        }
        while(pq.getHead() != null){
            int u = pq.extract_min().key;
            for(int v = G.first(u); v != -1; v = G.next(u, v)){
                if(D[u] + G.weight(u, v) < D[v]){
                    pq.decrease_key(nodes[v], D[u] + G.weight(u, v));
                    parent[v] = u;
                }
            }
        }
    }

    public static void Dijkstra_fib(GraphList G, int s){
        initialize(G, s);

        fib_heap pq = new fib_heap();
        fib_node[] nodes = new fib_node[G.n()];
        for(int i = 0; i < G.n(); i++){
            nodes[i] = new fib_node(i);
            pq.insert(nodes[i]);
        }
        while(pq.minimum() != null){
            int u = pq.extract_min().key;
            for(int v = G.first(u); v != -1; v = G.next(u, v)){
                if(D[u] + G.weight(u, v) < D[v]){
                    pq.decrease_key(nodes[v], D[u] + G.weight(u, v));
                    parent[v] = u;
                }
            }
        }
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

    public static String shortestPath(int N, int s, int d){
        int w = d, len = 0;
        StringBuilder result = new StringBuilder();
        result.append(w);
        while(true){
            result.append(parent[w]);
            if(parent[w] == s)    break;
            w = parent[w];
            len++;
            if(len >= N-1) break;
        }
        return result.reverse().toString();
    }

    public static int path_cost(GraphList G, int s, int d){
        String path = shortestPath(G.n(), s, d);
        int c = 0;
        for(int i = s; i < path.length()-1; i++){
            int u = Integer.parseInt(String.valueOf(path.charAt(i)));
            int v = Integer.parseInt(String.valueOf(path.charAt(i+1)));
            c += G.weight(u,v);
        }
        return c;
    }
}
