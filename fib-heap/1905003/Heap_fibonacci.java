import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

class fib_node{
    public fib_node parent;
    public fib_node left;
    public fib_node right;
    public int key;
    public int degree;
    public fib_node child;
    public boolean mark;

    fib_node(int k){
        this.key = k;
        this.mark = false;
    }
}

class CircularDLL{
    private fib_node head;
    private int nodeCount = 0;

    // Function to insert node in the list
    public void add(fib_node new_node) {
        // List is empty so create a single fib_node first
        if (head == null) {
            new_node.left = new_node.right = new_node;
            head = new_node;
            nodeCount++;
            return;
        }

        // find last fib_node in the list if list is not empty
        fib_node last = this.head.left;    //previous of head is the last fib_node

        // next of new_fib_node will point to head since list is circular
        new_node.right = this.head;

        // similarly previous of head will be new_fib_node
        this.head.left = new_node;

        // change new_fib_node=>prev to last
        new_node.left = last;

        // Make new fib_node next of old last
        last.right = new_node;
        nodeCount++;
    }

    public void remove(fib_node r_node){
        if(head == null)    return;
        fib_node temp = head;
        while(temp.right != head){
            if(temp.key == r_node.key){
                if(r_node.key == head.key){
                    head = head.right;
                }
                r_node.left.right = r_node.right;
                r_node.right.left = r_node.left;
                nodeCount--;
                break;
            }
            temp = temp.right;
        }
    }

    public void printFib_nodes()   {
        fib_node temp = this.head;
        //traverse in forward direction starting from head to print the list
        while (temp.right != head)
        {
            System.out.print("\n[ " + temp.key + ", ");
            temp = temp.right;
        }
        System.out.print(temp.key + " ]");
    }

    public fib_node getHead() {
        return head;
    }

    public void setHead(fib_node head) {
        this.head = head;
    }

    public int getNodeCount() {
        return nodeCount;
    }
}

public class Heap_fibonacci {
    public static void main(String[] args) {

    }
}

class fib_heap{

    private fib_node min;
    private CircularDLL root_list;
    private int N = 0;
    private int D = 0;
    private static final int INF = 1000000;

    fib_heap(){
        N = 0;
        root_list = new CircularDLL();
    }

    public fib_node make_heap() {                       //O(1)
        N = 0;
        root_list = new CircularDLL();
        return this.min = null;
    }

    public void insert(fib_node x) {                    //O(1)
        x.degree = 0;
        x.parent = null;
        x.child = null;
        x.mark = false;

        root_list.add(x);

        if(this.min == null){
            this.min = x;
        }
        else {
            if(x.key < this.min.key)    this.min = x;
        }

        N++;
    }

    public fib_node minimum() {               //O(1)
        return this.min;
    }

    public fib_node extract_min() {             //O(logn)
        fib_node z = this.min;
        if (z != null){
            fib_node x = z.child;
            do{
                this.root_list.add(x);
                x = x.right;
            }while(z != x);

            this.root_list.remove(z);

            if(z == z.right){
                this.min = null;
            }
            else {
                this.min = z.right;
                consolidate();
            }

            this.N--;
        }

        return z;
    }

    public void consolidate() {
        fib_node[] A = new fib_node[D];
        Arrays.fill(A, null);

        fib_node w = this.root_list.getHead();
        while(w != this.root_list.getHead()){
            fib_node x = w;
            int d = x.degree;
            while(A[d] != null){
                fib_node y = A[d];
                if(x.key > y.key){
                    //exchange x and y
                    x.left.right = y;
                    y.left = x.left.right;
                    x.right = y.right;
                    y.right.left = x;
                    y.right = x;
                    x.left = y;
                }
                fib_heap_link(y, x);
                A[d] = null;
                d++;
            }
            A[d] = x;
            w = w.right;
        }

        this.min = null;
        for(int i = 0; i < this.D; i++){
            if(A[i] != null){
                if(this.min == null){
                    this.root_list = new CircularDLL();
                    this.root_list.add(A[i]);
                    this.min = A[i];
                }
                else{
                    this.root_list.add(A[i]);
                    if(A[i].key < this.min.key) this.min = A[i];
                }
            }
        }
    }

    public fib_heap union(fib_heap H2) {       //O(logn)
        //create a new heap, which is essentially a copy of this heap
        fib_heap H = new fib_heap();
        H.min = this.min;
        H.root_list = this.root_list;

        //find the last nodes of the root lists of the new heap H and H2
        fib_node H_last = H.root_list.getHead().left;
        fib_node H2_last = H2.root_list.getHead().left;

        //modify left and right pointers of the heads and the last nodes of H and H2
        H_last.right = H2.root_list.getHead();
        H2.root_list.getHead().left = H_last;
        H2_last.right = H.root_list.getHead();
        H.root_list.getHead().left = H2_last;

        //modify the minimum element of the combined heap is necessary
        if(this.min == null || (H2.min != null && H2.min.key < this.min.key))   H.min = H2.min;

        //total number of nodes is the sum of the number of nodes of this heap and H2
        H.N = this.N + H2.N;

        return H;
    }

    public void decrease_key(fib_node x, int newKey) {              //O(logn)
        if(newKey > x.key) System.out.println("Error! New key cannot be greater than current key.");

        x.key = newKey;
        fib_node y = x.parent;

        if(y != null && x.key < y.key){
            cut(x, y);
            cascading_cut(y);
        }

        if(x.key < this.min.key)    this.min = x;
    }

    public void cascading_cut(fib_node y) {
        fib_node z = y.parent;
        if(z != null){
            if(!y.mark){
                y.mark = true;
            }
            else{
                cut(y, z);
                cascading_cut(z);
            }
        }
    }

    public void cut(fib_node x, fib_node y) {
        //remove x from child list of y
        fib_node child_y = y.child;
        do{
            if(child_y.key == x.key){
                x.left.right = x.right;
                x.right.left = x.left;
                if(x.key == y.child.key){
                    y.child = x.left;
                }
                y.degree--;
                break;
            }
            child_y = child_y.right;
        }while(child_y != y.child);

        //add x to the root list of this Heap
        this.root_list.add(x);
        x.parent = null;
        x.mark = false;
    }

    public void delete(fib_node x) {            //O(logn)
        decrease_key(x, -INF);
        extract_min();
    }

    public void fib_heap_link(fib_node y, fib_node x){
        this.root_list.remove(y);

        //make y a child of x
        fib_node last = x.child.left;
        y.right = x.child;
        x.child.left = y;
        last.right = y;
        y.left = last;
        x.child = y;
        x.degree++;

        y.mark = false;
    }

    static class comp implements Comparator<fib_node> {
        @Override
        public int compare(fib_node x, fib_node y) {
            return x.degree-y.degree;
        }
    }
}

