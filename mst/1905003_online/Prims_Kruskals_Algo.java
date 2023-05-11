import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

class DisjointSet{
    private int [] parent;
    private int [] rank;

    public DisjointSet(int N){
        parent = new int[N];
        rank = new int[N];
    }

    public void makeSet(int v){
        parent[v] = v;
        rank[v] = 0;
    }

    public void link(int u, int v){
        if(rank[u] > rank[v]){
            parent[v] = u;
        }
        else if(rank[u] < rank[v]){
            parent[u] = v;
        }
        else {
            parent[u] = v;
            rank[v] += 1;
        }
    }

    public void union(int u, int v){
        link(findSet(u), findSet(v));
    }

    public int findSet(int v){
        if(v != parent[v]){
            parent[v] = findSet(parent[v]);
        }
        return parent[v];
    }
}

class kruskalEdge implements Comparable<kruskalEdge>{
    private int u;
    private int v;
    private int w;

    public kruskalEdge(int v1, int v2, int wt){u = v1; v = v2; w = wt;}

    public int V1() {
        return u;
    }

    public int V2() {
        return v;
    }

    public int weight() {
        return w;
    }

    @Override
    public int compareTo(kruskalEdge e) {
        return (this.weight() - e.weight());
    }
}

public class Prims_Kruskals_Algo {

    private static int[] key;
    private static int[] parent;
    private static int minCost;

    public static void main(String[] args) throws IOException {

        int N, M;   // 3 <= N <= 1000, N <= M <= 10000

        BufferedReader br = new BufferedReader(new FileReader("mst.in"));
        String firstLine = br.readLine();
        N = Integer.parseInt(firstLine.split(" ")[0]);
        M = Integer.parseInt(firstLine.split(" ")[1]);
        GraphList G = new GraphList(N);

        while (true) {
            String s = br.readLine();
            if (s == null) break;

            int u = Integer.parseInt(s.split(" ")[0]);
            int v = Integer.parseInt(s.split(" ")[1]);
            int wt = (int)Double.parseDouble(s.split(" ")[2]);
            G.setEdge(u, v, wt);
            G.setEdge(v, u, wt);
        }
        br.close();

        minCost = 0;
        String K_edges = kruskal(G);
        String P_edges = Prim(G, 0);
        System.out.println("Cost of the minimum spanning tree :" + minCost);
        System.out.println("List of edges selected by Prim’s: " + P_edges);
        System.out.println("List of edges selected by Kruskal’s: " + K_edges);
    }

    public static String kruskal(GraphList G){
        kruskalEdge [] edges = new kruskalEdge[G.e()];
        int k = 0;
        StringBuilder mst = new StringBuilder();    mst.append("{");
        DisjointSet S = new DisjointSet(G.n());

        for(int i = 0; i < G.n(); i++){
            S.makeSet(i);
        }

        for(int i = 0; i < G.n(); i++){
            for(int j = G.first(i); j != -1; j = G.next(i, j)){
                edges[k++] = new kruskalEdge(i, j, G.weight(i, j));
            }
        }

        Arrays.sort(edges);

        for(kruskalEdge e : edges){
            if(S.findSet(e.V1()) != S.findSet(e.V2())){
                if(mst.length() > 1)   mst.append(",");
                mst.append("(").append(e.V1()).append(",").append(e.V2()).append(")");
                S.union(e.V1(), e.V2());
                minCost += G.weight(e.V1(), e.V2());
            }
        }
        mst.append("}");

        return mst.toString();
    }

    public static String Prim(GraphList G, int r){
        key = new int[G.n()];
        parent = new int[G.n()];

        for(int i = 0; i < G.n(); i++){
            key[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }

        key[r] = 0;
        PriorityQueue<Integer> pq = new PriorityQueue<>(new comp());
        for(int i = 0; i < G.n(); i++){
            pq.add(i);
        }
        while(!pq.isEmpty()){
            int u = pq.remove();
            for(int v = G.first(u); v != -1; v = G.next(u, v)){
                if(pq.contains(v) && G.weight(u, v) < key[v]){
                    parent[v] = u;
                    pq.remove(v);
                    key[v] = G.weight(u, v);
                    pq.add(v);
                }
            }
        }

        StringBuilder mst = new StringBuilder();
        mst.append("{");
        for(int i = 1; i < G.n(); i++){
            if(mst.length() > 1)   mst.append(",");
            mst.append("(").append(parent[i]).append(",").append(i).append(")");
        }
        mst.append("}");

        return mst.toString();
    }

    static class comp implements Comparator<Integer> {
        @Override
        public int compare(Integer u, Integer v) {
            return key[u]-key[v];
        }
    }
}
