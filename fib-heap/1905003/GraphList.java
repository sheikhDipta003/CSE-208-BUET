import java.util.*;

class Edge {
    private int v, wt;

    public Edge(){
        v = -1;
        wt = Integer.MAX_VALUE;
    }

    public Edge(int v, int w) {
        this.v = v; wt = w;
    }

    public int vertex() {
        return this.v;
    }

    public int weight() {
        return wt;
    }
}

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

public class GraphList {
    private static final int INF = 1000000;
    private static int minCost;
    private ArrayList<ArrayList<Edge>> lst;
    private int numEdge;
    private int[] Mark;     //0 -> unvisited(WHITE), 1 -> partially visited(GRAY), 2 -> completely visited(BLACK)
    private int[] parent;
    private static int[] key;

    GraphList(){}
    GraphList(int n){
        lst = new ArrayList<>();
        for(int i = 0; i < n; i++){
            lst.add(new ArrayList<>());
            lst.get(i).add(new Edge());
        }
        Mark = new int[n];
        parent = new int[n];
        Arrays.fill(parent, -1);
        Arrays.fill(Mark, 0);
    }


    public int n() {
        return lst.size();
    }


    public int e() {
        return numEdge;
    }


    public int first(int v) {                   //O(1)
        ArrayList<Edge> temp = lst.get(v);
        if(temp.size() <= 1)    return -1;
        return temp.get(1).vertex();
    }


    public int next(int v, int w) {                 //O(V)
        ArrayList<Edge> temp = lst.get(v);
        if(temp.size() <= 1)    return -1;

        for(int k = 1; k < temp.size(); k++){
            if(temp.get(k).vertex() == w)   {
                if(k+1 >= temp.size())  break;
                return temp.get(k+1).vertex();
            }
        }

        return -1;
    }


    public void setEdge(int i, int j, int w) {
        if(!isEdge(i, j))    numEdge++;
        lst.get(i).add(new Edge(j, w));
    }


    public void delEdge(int i, int j) {
        if(isEdge(i, j))    numEdge--;
        ArrayList<Edge> temp = lst.get(i);
        for(int k = 1; k < temp.size(); k++){
            if(temp.get(k).vertex() == j)   temp.remove(k);
        }
    }


    public boolean isEdge(int i, int j) {
        ArrayList<Edge> temp = lst.get(i);
        if(temp.size() <= 1)    return false;
        for(int k = 1; k < temp.size(); k++){
            if(temp.get(k).vertex() == j)   return true;
        }
        return false;
    }


    public int weight(int i, int j) {
        ArrayList<Edge> temp = lst.get(i);
        for(int k = 1; k < temp.size(); k++){
            if(temp.get(k).vertex() == j)   return temp.get(k).weight();
        }
        return -1;
    }

    public void setMark(int v, int val) { Mark[v] = val; }
    public int getMark(int v) { return Mark[v]; }

    public void setParent(int v, int p){ parent[v] = p;}
    public int getParent(int v) {
        return parent[v];
    }

    public static void main(String[] args) {
        GraphList G = new GraphList(5);
        G.setEdge(0, 1, 1);
        G.setEdge(0, 2, 1);
        G.setEdge(0, 3, 1);
        G.setEdge(1, 3, 1);
        G.setEdge(2, 1, 1);
        G.setEdge(2, 4, 1);
        G.setEdge(3, 4, 1);
    }

    public int[] bfs(int s, int d){
        Arrays.fill(parent, -1);
        Arrays.fill(Mark, 0);
       this.setMark(s, 0);
       Queue<Integer> Q = new LinkedList<>();
       Q.add(s);
       this.setMark(s, 1);

       while(!Q.isEmpty()){
           int u = Q.remove();
           for(int v = this.first(u); v != -1 && v != this.next(u, v); v = this.next(u, v)){
               if(this.getMark(v) == 0){
                   this.setMark(v, 1);
                   parent[v] = u;
                   Q.add(v);
               }
           }
           this.setMark(u, 2);
       }

       ArrayList<Integer> path = new ArrayList<>();
       int w = d;
       while(w != -1){
           path.add(w);
           w = parent[w];
       }

       int N = path.size();
       int[] result = new int[N];
       for(int i = path.size()-1; i >= 0; i--){
           result[N-i-1] = path.get(i);
       }

       return result;
    }

    public GraphList copy(){
        GraphList Gc = new GraphList(this.n());
        for(int u = 0; u < this.n(); u++){
            for(int v = this.first(u); v != -1; v = this.next(u, v)){
                Gc.setEdge(u, v, this.weight(u, v));
            }
        }

        return Gc;
    }

    public int edmond_carp(int[][] cap, int[][] flow, int[][] cut, int s, int t) {
        //create cf array for storing residual capacities
        int[][] cf = new int[this.n()][this.n()];
        for(int i = 0; i < this.n(); i++){
            if (this.n() >= 0) System.arraycopy(cap[i], 0, cf[i], 0, this.n());
        }

        //if capacity of any edge in this graph is zero, delete that edge
        for(int u = 0; u < this.n(); u++) {
            for (int v = this.first(u); v != -1; v = this.next(u, v)) {
                if (cap[u][v] == 0) this.delEdge(u, v);
                else if(cap[u][v] < 0)  {
                    this.delEdge(u, v);
                    this.setEdge(v, u, 1);
                    cap[v][u] = -cap[u][v];
                    cap[u][v] = 0;
                }
            }
        }

        //initialize residual network
        GraphList Gf = this.copy();
        while (true) {
            /*call bfs to find an augmenting path from s to t*/
            int[] aug_path = Gf.bfs(s, t);
            if(aug_path[0] != s)    break;      //if the path's first element is not source, then break

            //find minimum residual capacity of the augmenting path
            int min_res_cap = INF;
            for(int i = 0; i < aug_path.length-1; i++){
                int res_cap = cf[aug_path[i]][aug_path[i+1]];
                if(res_cap < min_res_cap)   min_res_cap = res_cap;
            }

            //update flow of the original graph
            for(int i = 0; i < aug_path.length - 1; i++){
                int u = aug_path[i];
                int v = aug_path[i+1];
                if(Gf.isEdge(u, v))   flow[u][v] += min_res_cap;
                else    flow[u][v] -= min_res_cap;
            }

            //create residual flow network for the next step
            for(int u = 0; u < this.n(); u++){
                for(int v = this.first(u); v != -1; v = this.next(u, v)){
                    if(flow[u][v] > 0) {
                        int add_flow = cap[u][v] - flow[u][v];
                        if (add_flow == 0)  Gf.delEdge(u, v);
                        Gf.setEdge(v, u, 1);
                        cf[u][v] = add_flow;
                        cf[v][u] = flow[u][v];
                    }
                    else if(flow[u][v] == 0){
                        cf[u][v] = cap[u][v];
                    }
                }
            }
        }

        //calculate maximum flow and minimum cut
        int maxflow = 0;
        int k1 = 0, k2 = 0;
        for(int v = 0; v < this.n(); v++){
            if(this.isEdge(s, v) || v == s)  {
                maxflow += flow[s][v];
                cut[0][k1++] = v;
            }
            else    cut[1][k2++] = v;
        }

        return maxflow;
    }

    public String kruskal(){
        kruskalEdge [] edges = new kruskalEdge[this.e()];
        int k = 0;
        StringBuilder mst = new StringBuilder();    mst.append("{");
        DisjointSet S = new DisjointSet(this.n());

        for(int i = 0; i < this.n(); i++){
            S.makeSet(i);
        }

        for(int i = 0; i < this.n(); i++){
            for(int j = this.first(i); j != -1; j = this.next(i, j)){
                edges[k++] = new kruskalEdge(i, j, this.weight(i, j));
            }
        }

        Arrays.sort(edges);

        for(kruskalEdge e : edges){
            if(S.findSet(e.V1()) != S.findSet(e.V2())){
                if(mst.length() > 1)   mst.append(",");
                mst.append("(").append(e.V1()).append(",").append(e.V2()).append(")");
                S.union(e.V1(), e.V2());
                minCost += this.weight(e.V1(), e.V2());
            }
        }
        mst.append("}");

        return mst.toString();
    }

    public String Prim(int r){
        key = new int[this.n()];
        parent = new int[this.n()];

        for(int i = 0; i < this.n(); i++){
            key[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }

        key[r] = 0;
        PriorityQueue<Integer> pq = new PriorityQueue<>(new comp());
        for(int i = 0; i < this.n(); i++){
            pq.add(i);
        }
        while(!pq.isEmpty()){
            int u = pq.remove();
            for(int v = this.first(u); v != -1; v = this.next(u, v)){
                if(pq.contains(v) && this.weight(u, v) < key[v]){
                    parent[v] = u;
                    pq.remove(v);
                    key[v] = this.weight(u, v);
                    pq.add(v);
                }
            }
        }

        StringBuilder mst = new StringBuilder();
        mst.append("{");
        for(int i = 1; i < this.n(); i++){
            if(mst.length() > 1)   mst.append(",");
            mst.append("(").append(parent[i]).append(",").append(i).append(")");
        }
        mst.append("}");

        return mst.toString();
    }

    public void Dijkstra(int s){
        initialize(s);

        PriorityQueue<Integer> pq = new PriorityQueue<>(new comp());
        for(int i = 0; i < this.n(); i++){
            pq.add(i);
        }
        while(!pq.isEmpty()){
            int u = pq.remove();
            for(int v = this.first(u); v != -1; v = this.next(u, v)){
                if(key[u] + this.weight(u, v) < key[v]){
                    pq.remove(v);
                    key[v] = key[u] + this.weight(u, v);
                    pq.add(v);
                    parent[v] = u;
                }
            }
        }
    }

    public boolean Bellman_Ford(int s){
        initialize(s);

        for(int i = 0; i < this.n()-1; i++){
            for(int u = 0; u < this.n(); u++){
                for(int v = this.first(u); v != -1; v = this.next(u, v)){
                    relax(u, v, this.weight(u, v));
                }
            }
        }

        for(int u = 0; u < this.n(); u++){
            for(int v = this.first(u); v != -1; v = this.next(u, v)){
                if(key[v] > key[u] + this.weight(u, v))    return false;
            }
        }

        return true;
    }

    public void initialize(int s){
        key = new int[this.n()];
        parent = new int[this.n()];
        for(int i = 0; i < this.n(); i++){
            key[i] = 1000000;
            parent[i] = -1;
        }
        key[s] = 0;
    }

    public void relax(int u, int v, int wt){
        if(key[u] + wt < key[v]){
            key[v] = key[u] + wt;
            parent[v] = u;
        }
    }

    public String shortestPath(int N, int s, int d){
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

    public int minCost(int s, int d){
        String path = shortestPath(this.n(), s, d);
        String [] v_path = path.split("->");
        int c = 0;
        for(int i = s; i < v_path.length-1; i++){
            c += this.weight(Integer.parseInt(v_path[i]), Integer.parseInt(v_path[i+1]));
        }
        return c;
    }

//    static class comp implements Comparator<Integer> {
//        @Override
//        public int compare(Integer u, Integer v) {
//            return D[u] - D[v];
//        }
//    }

    static class comp implements Comparator<Integer> {
        @Override
        public int compare(Integer u, Integer v) {
            return key[u]-key[v];
        }
    }

}

