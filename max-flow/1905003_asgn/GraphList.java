import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

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

public class GraphList {
    private static final int INF = 1000000;
    private ArrayList<ArrayList<Edge>> lst;
    private int numEdge;
    private int[] Mark;     //0 -> unvisited(WHITE), 1 -> partially visited(GRAY), 2 -> completely visited(BLACK)
    private int[] parent;

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


    public int first(int v) {
        ArrayList<Edge> temp = lst.get(v);
        if(temp.size() <= 1)    return -1;
        return temp.get(1).vertex();
    }


    public int next(int v, int w) {
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

    public int ford_fulkerson(int[][] cap, int[][] flow, int[][] cut, int s, int t) {
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

}

