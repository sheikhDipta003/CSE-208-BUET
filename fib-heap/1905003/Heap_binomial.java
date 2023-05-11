import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class node{
    public node parent;
    public int key;
    public int degree;
    public node child;
    public node sibling;

    node(int k){
        this.key = k;
    }
}

public class Heap_binomial {
    public static void main(String[] args) {
        bin_heap H = new bin_heap();
        H.insert(new node(37));
        H.insert(new node(41));
        H.insert(new node(10));
        H.insert(new node(28));
        H.insert(new node(13));
        node N = new node(77);
        H.insert(N);

        System.out.println(H.extract_min().key);
    }
}

class bin_heap{

    private node head;
    private static final int INF = 1000000;

    public node make_heap() {                       //O(1); OK
        return this.head = null;
    }

    public void insert(node x) {                    //O(logn);OK
        bin_heap H1 = new bin_heap();
        H1.make_heap();
        x.parent = null;
        x.sibling = null;
        x.degree = 0;
        x.child = null;
        H1.setHead(x);
        setHead(this.union(H1));
    }

    public node minimum() {               //O(logn);OK
        node y = null;
        node x = getHead();
        int min = INF;

        while(x != null){
            if(x.key < min){
                min = x.key;
                y = x;
            }
            x = x.sibling;
        }

        return y;
    }

    public node extract_min() {             //O(logn);buggy
        node x = minimum();
        node prev_x = getHead();

        while(prev_x.sibling.key != x.key){
            prev_x = prev_x.sibling;
        }
        prev_x.sibling = null;

        bin_heap H1 = new bin_heap();
        H1.make_heap();

        ArrayList<node> list_child_x = new ArrayList<>();
        node y = x.child;
        while(y != null){
            list_child_x.add(y);
            y = y.sibling;
        }

        Collections.reverse(list_child_x);
        for(int i = 1; i < list_child_x.size(); i++){
            list_child_x.get(i-1).sibling = list_child_x.get(i);
        }

        H1.setHead(list_child_x.get(0));
        setHead(this.union(H1));
        return x;
    }

    public node union(bin_heap H2) {       //O(logn);OK
        bin_heap H = new bin_heap();
        H.make_heap();
        H.setHead(binomial_heap_merge(H2));
        if(H.getHead() == null) return H.getHead();

        node prev_x = null;
        node x = H.getHead();
        node next_x = x.sibling;

        while(next_x != null){
            if( (x.degree != next_x.degree) || (next_x.sibling != null && next_x.sibling.degree == x.degree) ){
                prev_x = x;
                x = next_x;
            }
            else if(x.key <= next_x.key){
                x.sibling = next_x.sibling;
                binomial_link(next_x, x);
            }
            else{
                if(prev_x == null){
                    setHead(next_x);
                }
                else{
                    prev_x.sibling = next_x;
                }
                binomial_link(x, next_x);
                x = next_x;
            }
            next_x = x.sibling;
        }

        return H.getHead();
    }

    public void decrease_key(node x, int newKey) {              //O(logn);OK
        if(newKey > x.key) System.out.println("New key cannot be greater than current key");
        x.key = newKey;
        node y = x;
        node z = y.parent;
        while(z != null && y.key < z.key){
            int temp = y.key;
            y.key = z.key;
            z.key = temp;
            y = z;
            z = y.parent;
        }
    }

    public void delete(node x) {            //O(logn);buggy
        decrease_key(x, -INF);
        extract_min();
    }

    public node getHead() {
        return head;
    }

    public void setHead(node head) {
        this.head = head;
    }

    public void binomial_link(node left, node right){           //O(1);OK
        left.parent = right;
        left.sibling = right.child;
        right.child = left;
        right.degree++;
    }

    public node binomial_heap_merge(bin_heap H2){          //O(nlogn);needs improvement
        ArrayList<node> nodes = new ArrayList<>();
        node x = this.getHead();
        while(x != null){
            nodes.add(x);
            x = x.sibling;
        }
        x = H2.getHead();
        while(x != null){
            nodes.add(x);
            x = x.sibling;
        }

        nodes.sort(new comp());

        for(int i = 1; i < nodes.size(); i++){
            nodes.get(i-1).sibling = nodes.get(i);
        }

        return nodes.get(0);
    }

    static class comp implements Comparator<node> {
        @Override
        public int compare(node x, node y) {
            return x.degree-y.degree;
        }
    }
}
