import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class online_mst {
    private static int minCost;

    public static void main(String[] args) {
        int N, E;   //N -> number of nodes, E -> number of edges
        Scanner scanner = new Scanner(System.in);
        N = scanner.nextInt();
        E = scanner.nextInt();
        GraphList G = new GraphList(N);
        minCost= 0;

        for(int i = 0; i < E; i++){
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            G.setEdge(u, v, w);
            G.setEdge(v, u, w);
            minCost += w;
        }

        String mst = MST(G);
        System.out.println("Edges in MST");
        String [] edges = mst.split(",");
        for(String s : edges){
            System.out.println(s);
        }
        System.out.println("Total weight of MST is" + minCost);
    }

    public static String MST(GraphList G){
        kruskalEdge [] edges = new kruskalEdge[G.e()];
        GraphList H = copy(G);
        int k = 0;
        for(int i = 0; i < G.n(); i++){
            for(int c = G.first(i); c != -1; c = G.next(i, c)){
                edges[k++] = new kruskalEdge(i, c, G.weight(i, c));
            }
        }

        Arrays.sort(edges, Collections.reverseOrder());
        int j = 0;
        while(j < G.e()){
            H.delEdge(edges[j].V1(), edges[j].V2());
            if(isConnected(H, edges[j].V1()))   {
                G.delEdge(edges[j].V1(), edges[j].V2());
                minCost -= G.weight(edges[j].V1(), edges[j].V2());
            }
            else {
                H.setEdge(edges[j].V1(), edges[j].V2(), edges[j].weight());
            }
            j++;
        }

        StringBuilder mst = new StringBuilder();
        for(int u = 0; u < G.n(); u++){
            for(int v = G.first(u); v != -1; v = G.next(u, v)){
                if(G.isEdge(u, v)){
                    if(mst.length() > 1)    mst.append(",");
                    mst.append("(").append(u).append(",").append(v).append(")");
                }
            }
        }

        return mst.toString();
    }

    public static boolean isConnected(GraphList G, int s){
        DFS_Implm.DFS(G, s);

        //check if there is any unvisited vertex
        for(int i = 0; i < G.n(); i++){
            if(G.getMark(i) == 0)   return false;
        }

        return true;
    }

    public static GraphList copy(GraphList G){
        GraphList H = new GraphList(G.n());
        for(int v = 0; v < G.n(); v++){
            for(int w = G.first(v); w != -1; w = G.next(v, w)){
                int wt = G.weight(v, w);
                H.setEdge(v, w, wt);
            }
        }

        return H;
    }
}
