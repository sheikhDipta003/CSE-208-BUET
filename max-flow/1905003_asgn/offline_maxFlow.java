import java.util.*;

public class offline_maxFlow {
    private static final int INF = 1000000;

    public static void main(String[] args) {
        int n;
        Scanner scanner = new Scanner(System.in);
        n = Integer.parseInt(scanner.nextLine());
        String [] teamNames = new String[n];
        int[] win = new int[n];
        int[] loss = new int[n];
        int[] rem = new int[n];
        int[][] g = new int[n][n];
        for(int i = 0; i < n; i++){
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");
            teamNames[i] = tokens[0];
            win[i] = Integer.parseInt(tokens[1]);
            loss[i] = Integer.parseInt(tokens[2]);
            rem[i] = Integer.parseInt(tokens[3]);
            for(int j = 0; j < n; j++) {
                g[i][j] = Integer.parseInt(tokens[j+4]);
            }
        }

        for(int i = 0; i < n; i++){
            if(!isNotEliminated(win, rem, g, i)){
                System.out.println(teamNames[i] + " is eliminated.");
            }
        }
    }

    public static boolean isNotEliminated(int[] w, int[] r, int[][] g, int x){
        //count total number of vertices
        int V = (g.length - 1) + 2;
        for(int i = 0; i < g.length; i++){
            for(int j = 0; j < g.length; j++){
                if(j > i && i != x && j != x && g[i][j] > 0){
                    V++;
                }
            }
        }

        //flow network
        GraphList G = new GraphList(V);
        //capacities of the edges
        int[][] cap = new int[V][V];
        int numGameNodes = V - ((g.length - 1) + 2);
        int temp = numGameNodes;
        Dictionary<Integer, Integer> dict = new Hashtable<>();
        for(int i = 1; i <= g.length; i++){
            if(x == 0){
                if(i >= g.length)  break;
                dict.put(i, i+temp);
            }
            else {
                if(i-1 == x)  {
                    temp--;
                    continue;
                }
                dict.put(i-1, i+temp);
            }
        }

        int c = 1;
        for(int i = 0; i < g.length; i++){
            for(int j = 0; j < g.length; j++){
                if(j > i && i != x && j != x && g[i][j] > 0){
                    G.setEdge(c, dict.get(i), 1);
                    G.setEdge(c, dict.get(j), 1);
                    cap[c][dict.get(i)] = INF;
                    cap[c][dict.get(j)] = INF;
                    c++;
                }
            }
        }

        for(int i = 1; i <= numGameNodes; i++)  {
            G.setEdge(0, i, 1);
            int u = getKey(dict, G.first(i));
            int v = getKey(dict, G.next(i, G.first(i)));
            cap[0][i] = g[u][v];
        }
        for(int i = numGameNodes+1; i < V-1; i++)    {
            G.setEdge(i, V-1, 1);
            cap[i][V-1] = w[x] + r[x] - w[getKey(dict, i)];
        }

        int[][] f = new int[V][V];
        for (int i = 0; i < V; i++) Arrays.fill(f[i], 0);
        int[][] minCut = new int[2][V];
        for (int[] ints : minCut) Arrays.fill(ints, -1);

        G.ford_fulkerson(cap, f, minCut, 0, V-1);

        for(int i = 0; minCut[0][i] != -1; i++){
            int v = minCut[0][i];
            if(f[0][v] != cap[0][v]){
                return false;
            }
        }

        return true;
    }

    public static int getKey(Dictionary<Integer, Integer> dict, int val){
        for(Enumeration<Integer> e = dict.keys(); e.hasMoreElements();){
            Integer k = e.nextElement();
            if(dict.get(k) == val)    return k;
        }
        return -1;
    }
}
