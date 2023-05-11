import java.util.ArrayList;

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
    private ArrayList<ArrayList<Edge>> lst;
    private int numEdge;
    private int[] Mark;
    private int[] Color;

    GraphList(){}
    GraphList(int n){
        lst = new ArrayList<>();
        for(int i = 0; i < n; i++){
            lst.add(new ArrayList<>());
            lst.get(i).add(new Edge());
        }
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
        return temp.get(0).vertex();
    }


    public int next(int v, int w) {
        ArrayList<Edge> temp = lst.get(v);
        if(temp.size() <= 1)    return -1;

        for(int k = 1; k < temp.size(); k++){
            if(temp.get(k).vertex() == w)   return temp.get(k).vertex();
        }

        return -1;
    }


    public void setEdge(int i, int j, int w) {
        if(w > 0) {
            if(!isEdge(i, j))    numEdge++;
            lst.get(i).add(new Edge(j, w));
        }
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

    public int getColor(int v) {
        return Color[v];
    }
    public void setColor(int v, int c) {
        Color[v] = c;
    }

}

