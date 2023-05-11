import java.io.*;

public class offline_reb_black_tree {
    public static void main(String[] args) throws IOException {
        rb_tree T = new rb_tree();
        BufferedReader br = new BufferedReader(new FileReader("in.txt"));
        int N = Integer.parseInt(br.readLine());
        BufferedWriter bw = new BufferedWriter(new FileWriter("out.txt"));
        bw.write(N + "\n");

        for(int i = 0; i < N; i++) {
            String line = br.readLine();
            if (line == null) break;

            int c = Integer.parseInt(line.split(" ")[0]);
            int x = Integer.parseInt(line.split(" ")[1]);
            int r = -1;

            if(c == 0){     //terminate (delete) program with priority x
                r = (T.search(x) != null) ? 1 : 0;
                T.delete(x);
            }
            else if(c == 1){        //Initiate a program with priority x
                r = (T.search(x) == null) ? 1 : 0;
                if(r != 0)  T.insert(new rbNode(x));
            }
            else if(c == 2){        //Search the program with priority x
                r = (T.search(x) == null) ? 0 : 1;
            }
            else if(c == 3){
                r = T.countNodesLessThan(x);
            }

            bw.write(c + "\t" + x + "\t" + r + "\n");
        }
        br.close();
        bw.close();
    }
}

class rbNode{
    public int key;
    public rbNode left;
    public rbNode right;
    public rbNode parent;
    public char color;      //{'r', 'b'}

    rbNode(){this(-1);}
    rbNode(int key){
        left = null;
        right = null;
        parent = null;
        this.key = key;
        this.color = 'b';
    }
}

class rb_tree{
    private rbNode root;
    private rbNode tree_null;

    rb_tree(){tree_null = new rbNode();    this.root = tree_null;}
    rb_tree(rbNode rt){tree_null = new rbNode();    this.root = rt;}

    public void insert(rbNode z){        //inserts a node 'z' into this tree
        rbNode y = tree_null;                //to store the to-be parent of z
        rbNode x = this.root;

        //secure correct value for 'y'
        while(x != tree_null){
            y = x;
            if(z.key < x.key)   x = x.left;
            else x = x.right;
        }

        //set z's parent to be y
        z.parent = y;

        if(y == tree_null)   this.root = z;  //if the tree is empty, then 'z' is the new root
        else if(z.key < y.key)  y.left = z;     //assign 'z' as 'y'-s left or right child according to the key
        else y.right = z;

        z.left = tree_null;
        z.right = tree_null;
        z.color = 'r';      //make 'z'-s color to be red

        insert_fixup(z);
    }

    public rbNode delete(int key){
        rbNode temp = search(key);
        if(temp != null)    return delete_help(temp);
        return null;
    }

    public int countNodesLessThan(int key){
        return countNodesLessThan_help(this.root, key);
    }

    private int countNodesLessThan_help(rbNode rt, int key) {
        int count = 0;

        if (rt == tree_null) {
            return 0;
        }
        else if (rt.key < key) {
            count++;
            count += countNodesLessThan_help(rt.left, key);
            count += countNodesLessThan_help(rt.right, key);
        }
        else {
            count += countNodesLessThan_help(rt.left, key);
        }

        return count;
    }

    public rbNode delete_help(rbNode z){
        rbNode y = new rbNode();

        //if z is an external node, store it in y; else store z's successor or predecessor in y
        if(z.left == tree_null || z.right == tree_null)   y = z;
        else{
            rbNode temp = tree_successor(this.root, z);
            if(temp != tree_null)    y = temp;
            else y = tree_predecessor(this.root, z);
        }

        rbNode x = new rbNode();
        if(y.left != tree_null)  x = y.left;
        else x = y.right;

        x.parent = y.parent;

        if(y.parent == tree_null)    this.root = x;
        else if(y == y.parent.left)     y.parent.left = x;
        else y.parent.right = x;

        if(y != z)  z.key = y.key;

        if(y.color == 'b')  delete_fixup(x);

        return y;
    }

    public rbNode search(int key){
        rbNode rt = this.root;
        while(rt != tree_null){
            if(rt.key == key)   return rt;
            else if(rt.key > key)   rt = rt.left;
            else rt = rt.right;
        }
        return null;
    }

    private void delete_fixup(rbNode x) {
        while (x.color == 'b' && x != this.root){
            if(x == x.parent.left){
                rbNode w = x.right;

                if(w.color == 'r'){
                    w.color = 'b';
                    x.parent.color = 'r';
                    left_rotate(x.parent);
                    w = x.parent.right;
                }

                if(w.left.color == 'b' && w.right.color == 'b'){
                    w.color = 'r';
                    x = x.parent;
                }
                else if (w.right.color == 'b') {
                        w.left.color = 'b';
                        w.color = 'r';
                        right_rotate(w);
                        w = x.right;
                }
                else{
                    w.color = x.parent.color;
                    x.parent.color = 'b';
                    w.right.color = 'b';
                    left_rotate(x.parent);
                    x = this.root;
                }
            }
            else{
                rbNode w = x.left;

                if(w.color == 'r'){
                    w.color = 'b';
                    x.parent.color = 'r';
                    right_rotate(x.parent);
                    w = x.parent.left;
                }

                if(w.right.color == 'b' && w.left.color == 'b'){
                    w.color = 'r';
                    x = x.parent;
                }
                else if (w.left.color == 'b') {
                    w.right.color = 'b';
                    w.color = 'r';
                    left_rotate(w);
                    w = x.left;
                }
                else{
                    w.color = x.parent.color;
                    x.parent.color = 'b';
                    w.left.color = 'b';
                    right_rotate(x.parent);
                    x = this.root;
                }
            }
        }

        x.color = 'b';
    }

    private rbNode tree_predecessor(rbNode rt, rbNode z) {         //returns the node with the highest key < z.key
        if (rt == tree_null)
            return null;

        rbNode pre = new rbNode();

        while (rt != tree_null)
        {
            if (rt.key == z.key)
            {
                if (rt.left != tree_null)
                {
                    pre = root.left;
                    while (pre.right != tree_null)
                        pre = pre.right;
                }
                break;
            }

            else if (rt.key < z.key)
            {
                pre = rt;
                rt = rt.right;
            }
        }

        return pre;
    }

    private rbNode tree_successor(rbNode rt, rbNode z) {           //returns the node with the lowest key > z.key
        if (rt == tree_null)
            return null;

        rbNode suc = new rbNode();

        while (rt != tree_null)
        {
            if (rt.key == z.key)
            {
                if (rt.right != tree_null)
                {
                    suc = rt.right;
                    while (suc.left != tree_null)
                        suc = suc.left;
                }
                break;
            }

            else if(rt.key > z.key)
            {
                suc = rt;
                rt = rt.left;
            }
        }

        return suc;
    }

    //helper function to maintain red-black tree properties after an insertion
    public void insert_fixup(rbNode z){             //z is red
        if(z == root || z == root.left || z == root.right)  return;

        while(z.parent.color == 'r'){               //while 'z'-s parent is red
            if(z.parent == z.parent.parent.left){
                rbNode y = z.parent.parent.right;
                if(y.color == 'r'){                 //if z's uncle is also red
                    z.parent.color = 'b';           //make parent and uncle black , and grandparent red
                    y.color = 'b';
                    y.parent.color = 'r';
                    z = z.parent.parent;            //advance upward
                }
                else{
                    if(z == z.parent.right){
                        z = z.parent;
                        left_rotate(z);         //because otherwise black-height property is violated
                    }
                    else{
                        z.parent.color = 'b';
                        z.parent.parent.color = 'r';        //y is already black
                        right_rotate(z.parent.parent);      //because root of a rbt must be black
                    }
                }
            }
            else{
                rbNode y = z.parent.parent.left;
                if(y.color == 'r'){                 //if z's uncle is also red
                    z.parent.color = 'b';           //make parent and uncle black , and grandparent red
                    y.color = 'b';
                    y.parent.color = 'r';
                    z = z.parent.parent;            //advance upward
                }
                else{
                    if(z == z.parent.left){
                        z = z.parent;
                        right_rotate(z);         //because otherwise black-height property is violated
                    }
                    else{
                        z.parent.color = 'b';
                        z.parent.parent.color = 'r';        //y is already black
                        left_rotate(z.parent.parent);      //because root of a rbt must be black
                    }
                }
            }
        }
        this.root.color = 'b';
    }

    public void right_rotate(rbNode x) {
        rbNode y = x.left;     //left child of x is stored in y
        x.left = y.right;

        if(y.right != tree_null)  y.right.parent = x;

        y.parent = x.parent;        //parent of x will now become parent of y

        if(x.parent == tree_null)    this.root = y;      //if x is the root, due to right rotation y will be new root
        else if(x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;

        y.right = x;
        x.parent = y;
    }

    public void left_rotate(rbNode x) {     //this function assumes that right child of x is not null
        rbNode y = x.right;     //right child of x is stored in y
        x.right = y.left;

        if(y.left != tree_null)  y.left.parent = x;

        y.parent = x.parent;        //parent of x will now become parent of y

        if(x.parent == tree_null)    this.root = y;      //if x is the root, due to left rotation y will be new root
        else if(x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;

        y.left = x;
        x.parent = y;
    }

}
