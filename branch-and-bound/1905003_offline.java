/******************************************************************************

Welcome to GDB Online.
GDB online is an online compiler and debugger tool for C, C++, Python, Java, PHP, Ruby, Perl,
C#, OCaml, VB, Swift, Pascal, Fortran, Haskell, Objective-C, Assembly, HTML, CSS, JS, SQLite, Prolog.
Code, Compile, Run and Debug online from anywhere in world.

*******************************************************************************/
import java.lang.Math;
import java.io.*;

public class Main
{
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(new File("in.txt")));
		int n = Integer.parseInt(br.readLine().trim());
		node x = new node(n);
		int i = 1;
		while(true){
		    String line = br.readLine();
		    if(line == null) break;
		    String [] cols = line.trim().split(" ");
		    for(int j = 1; j <= n; j++)  x.M[i][j] = cols[j-1].charAt(0);
		    i++;
		}
		br.close();
		
		findBound(x);
		x.f_row = x.f_col = x.level = x.order = 0;
		
		Priority_Queue pq = new Priority_Queue(n*n + n + 1);
		pq.insert(x);
		int rf = 0, cf = 0, lev = 0, order;
		while(pq.minimum().f_row < n-1 && pq.minimum().f_col < n-1){
		    node y = pq.extract_min();
		    lev++;
		    order = 0;
		    for(i = 1; i <= n; i++){
		        node z;
		        if(lev % 2 == 1)  z = getNewNode(y, 'c', i, order, lev);
		        else    z = getNewNode(y, 'r', i, order, lev);
		        order++;
		        findBound(z);
		        pq.insert(z);
		    }
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("out.txt"));
		char [][] result = pq.minimum().M;
		bw.write(pq.minimum().bound + "\n");
		for(i = 1; i <= n; i++){
		    for(int j = 1; j <= n; j++){
		        bw.write(result[i][j] + "\t");
		    }
		    bw.write("\n");
		}
        bw.close();
		
	}
	
	public static node getNewNode(node x, char rc, int k, int order, int lev){
	    int n = x.M.length - 1;
	    int p;
        node y = new node(n, order, lev);
        
        //place k-th row or k-th column in right after the fixed region
        if(rc == 'r'){
            for(int i = 1; i <= x.f_row; i++){
                for(int j = 1; j <= n; j++){
                    y.M[i][j] = x.M[i][j];
                }
            }
            
            p = x.f_row + 1;
        
            for(int j = 1; j <= n; j++){
                y.M[p][j] = x.M[k][j];
            }
            
            y.f_row++;
            
            for(int i = x.f_row + 2; i <= n; i++){
                if(p == k){
                    p++;
                    i--;
                    continue;
                }
                for(int j = 1; j <= n; j++){
                    y.M[i][j] = x.M[p][j];
                }
                p++;
            }
        }
        else if(rc == 'c'){
            for(int j = 1; j <= x.f_col; j++){
                for(int i = 1; i <= n; i++){
                    y.M[i][j] = x.M[i][j];
                }
            }
            
            p = x.f_col + 1;
        
            for(int i = 1; i <= n; i++){
                y.M[i][p] = x.M[i][k];
            }
            
            y.f_col++;
            
            for(int j = x.f_col + 2; j <= n; j++){
                if(p == k){
                    p++;
                    j--;
                    continue;
                }
                for(int i = 1; i <= n; i++){
                    y.M[i][j] = x.M[i][p];
                }
                p++;
            }
        }
        
        //return the new node
        return y;
    }
    
    public static void findBound(node x){
        int n = x.M.length - 1;
        x.bound = 0;
        
        //find bound for the unfixed region
        for(int i = x.f_row + 1; i <= n; i++){
            for(int j = x.f_col + 1; j <= n ; j++){
                int nonzero_count = 0;
                if(x.M[i][j] == 'x'){
                    nonzero_count++;
                }
                x.bound = Math.max(x.bound, nonzero_count);
            }
        }
        for(int j = x.f_row + 1; j <= n; j++){
            for(int i = x.f_col + 1; i <= n ; i++){
                int nonzero_count = 0;
                if(x.M[i][j] == 'x'){
                    nonzero_count++;
                }
                x.bound = Math.max(x.bound, nonzero_count);
            }
        }
        x.bound = (int) Math.floor((x.bound + 1) / 2);
        //
        
        //find bound for the fixed region
        for(int i = 1; i <= x.f_row; i++){
            x.bound = Math.max(x.bound, findBoundHelp(x, 'r', i));
        }
        for(int j = 1; j <= x.f_col; j++){
            x.bound = Math.max(x.bound, findBoundHelp(x, 'c', j));
        }
        
    }
    
    public static int findBoundHelp(node x, char rc, int pos){
        int b = -1;
        int n = x.M.length - 1;
        
        if(rc == 'r'){
            int nonzero_count = 0;

            //calculate number of nonzeroes in that row to the right of the cell on the main diagonal
            for(int j = pos + 1; j <= n; j++){
                if(x.M[pos][j] == 'x')    nonzero_count++;
            }
            
            if(nonzero_count > 0) {
                if(x.M[pos][pos] == 'x'){
                    b = Math.max(b, x.f_col + nonzero_count - pos + 2);
                }
                else{
                    b = Math.max(b, x.f_col + nonzero_count - pos + 1);
                }
            }
            else{
                if(x.M[pos][pos] == 'x'){
                    b = Math.max(b, 1);
                }
                else{
                    b = Math.max(b, 0);
                }
            }
            
            //calculate number of nonzeroes in that row to the left of the cell on the main diagonal
            nonzero_count = 0;
            for(int j = 1; j < pos; j++){
                if(x.M[pos][j] == 'x')    nonzero_count++;
            }
            
            if(nonzero_count > 0) {
                if(x.M[pos][pos] == 'x'){
                    b = Math.max(b, pos - x.f_col - nonzero_count + 2);
                }
                else{
                    b = Math.max(b, pos - x.f_col - nonzero_count + 1);
                }
            }
            else{
                if(x.M[pos][pos] == 'x'){
                    b = Math.max(b, 1);
                }
                else{
                    b = Math.max(b, 0);
                }
            }
        }
        else if(rc == 'c'){
            int nonzero_count = 0;
            
            //calculate number of nonzeroes in that row below the cell on the main diagonal
            for(int i = pos + 1; i <= n; i++){
                if(x.M[i][pos] == 'x')    nonzero_count++;
            }
            
            if(nonzero_count > 0) {
                if(x.M[pos][pos] == 'x'){
                    b = Math.max(b, x.f_row + nonzero_count - pos + 2);
                }
                else{
                    b = Math.max(b, x.f_row + nonzero_count - pos + 1);
                }
            }
            else{
                if(x.M[pos][pos] == 'x'){
                    b = Math.max(b, 1);
                }
                else{
                    b = Math.max(b, 0);
                }
            }
            
            //calculate number of nonzeroes in that row above the cell on the main diagonal
            nonzero_count = 0;
            for(int i = 1; i < pos; i++){
                if(x.M[i][pos] == 'x')    nonzero_count++;
            }
            
            if(nonzero_count > 0) {
                if(x.M[pos][pos] == 'x'){
                    b = Math.max(b, pos - x.f_row - nonzero_count + 2);
                }
                else{
                    b = Math.max(b, pos - x.f_row - nonzero_count + 1);
                }
            }
            else{
                if(x.M[pos][pos] == 'x'){
                    b = Math.max(b, 1);
                }
                else{
                    b = Math.max(b, 0);
                }
            }
        }
        
        return b;
    }
}

class node{
    public char[][] M;
    public int f_row;
    public int f_col;
    public int bound;
    public int level;
    public int order;       //from 0 to n-1, inclusive
    
    public node(int N){
        int n = N;
        M = new char[n + 1][n + 1];
        for(int i = 0; i < n+1; i++){
            M[i] = new char[n+1];
        }
    }
    public node(int N, int bound, int level, int order){
        M = new char[N + 1][N + 1];
        for(int i = 0; i < N + 1; i++){
            M[i] = new char[N + 1];
        }

        this.bound = bound;
        this.level = level;
        this.order = order;
    }
    public node(int N, int order, int level){
        M = new char[N + 1][N + 1];
        for(int i = 0; i < N + 1; i++){
            M[i] = new char[N + 1];
        }
        this.level = level;
        this.order = order;
    }
}

class Priority_Queue{
    private node [] heap;
    private int maxSize;
    private int n;
    
    Priority_Queue(int size){
        maxSize = size;
        heap = new node[maxSize];
        n = 0;
    }
    
    public node minimum(){
        return heap[0];
    }
    
    public node extract_min(){
        if (n < 1){
            System.out.println("heap underflow");
            return null;
        }
        node min = heap[0];
        heap[0] = heap[n-1];
        n--;
        heapify(0);
        return min;
    }
    
    public void decrease_key(int i, int n_bound, int n_level, int n_order){
        if(n_bound > heap[i].bound)   return;
        else if(n_bound == heap[i].bound && n_level < heap[i].level)    return;
        else if(n_bound == heap[i].bound && n_level == heap[i].level && n_order < heap[i].order)  return;
        
        heap[i].bound = n_bound;
        heap[i].level = n_level;
        heap[i].order = n_order;
        while (i > 0 && (getSmallerNode(i, parent(i)) == i)){
            swapNodes(i, parent(i));
            i = parent(i);
        }
    }
    
    public void insert(node newNode){
        if(n >= maxSize){
            return;
        }
        n++;
        int n_bound = newNode.bound;
        newNode.bound = Integer.MAX_VALUE;
        int n_order = newNode.order;
        newNode.order = Integer.MIN_VALUE;
        int n_level = newNode.level;
        newNode.level = Integer.MIN_VALUE;
        heap[n-1] = newNode;
        decrease_key(n-1, n_bound, n_level, n_order);
    }
    
    void heapify(int pos)
    {
        int lc = left(pos);
        int rc = right(pos);
        int smallest;
        if (lc < n && (getSmallerNode(lc, pos) == lc))
        {
            smallest = lc;
        }
        else
            smallest = pos;
        if (rc < n && (getSmallerNode(rc, pos) == rc))
        {
            smallest = rc;
        }
        if (smallest != pos)
        {
            swapNodes(pos, smallest);
            heapify(smallest);
        }
    }
    int left(int i)
    {
        return (i << 1) + 1;
    }
    int right(int i)
    {
        return (i << 1) + 2;
    }
    int parent(int i)
    {
        return (i - 1) >> 2;
    }
    void swapNodes(int i, int j)
    {
        node temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    int getSmallerNode(int i, int j){
        if(heap[i].bound < heap[j].bound)   return i;
        else if(heap[i].bound == heap[j].bound){
            if(heap[i].level > heap[j].level)   return i;
            else if(heap[i].level == heap[j].level){
                if(heap[i].order > heap[j].order)   return i;
                else if(heap[i].order == heap[j].order){
                    return i;
                }
                return j;
            }
            return j;
        }
        return j;
    }
}
