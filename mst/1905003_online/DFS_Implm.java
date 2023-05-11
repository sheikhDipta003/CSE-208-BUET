public class DFS_Implm {

    private static StringBuilder dfsPath = new StringBuilder();

    public static void main(String[] args) {
        GraphList G = new GraphList(5);
        G.setEdge(0, 1, 10);
        G.setEdge(0, 2, 3);
        G.setEdge(0, 3, 20);
        G.setEdge(1, 3, 5);
        G.setEdge(2, 1, 2);
        G.setEdge(2, 4, 15);
        G.setEdge(3, 4, 11);

        System.out.println(DFS(G, 0));
    }

    public static String DFS(GraphList G, int v){
        findDFSHelp(G, v);
        return dfsPath.toString();
    }

    public static void findDFSHelp(GraphList G, int v){
        G.setMark(v, 1);
        dfsPath.append(v);
        for(int w = G.first(v); w != -1; w = G.next(v, w)){
            if(G.getMark(w) == 0){
                findDFSHelp(G, w);
            }
        }
    }
}
