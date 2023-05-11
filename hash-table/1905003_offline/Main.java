/******************************************************************************

Welcome to GDB Online.
GDB online is an online compiler and debugger tool for C, C++, Python, Java, PHP, Ruby, Perl,
C#, OCaml, VB, Swift, Pascal, Fortran, Haskell, Objective-C, Assembly, HTML, CSS, JS, SQLite, Prolog.
Code, Compile, Run and Debug online from anywhere in world.

*******************************************************************************/
import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.System;
import java.io.*;

public class Main
{
	public static void main(String[] args) throws IOException{
	    int N, M, p;   //N -> hashtable-size, M -> total number of elements in the hashtable, p -> number of selected elements
	    double [] LF = {0.4, 0.5, 0.6, 0.7, 0.8, 0.9};      //lf -> load factor
	    Scanner sc = new Scanner(System.in);
    	N = sc.nextInt();
    	
    	BufferedWriter bw = new BufferedWriter(new FileWriter("out.txt"));
	    
	    for(double lf : LF){
    	    M = (int) (N * lf);
    	    String[] words = generateRandomWords(M, 7);
    	    
    	    //separate chaining
    	    HashTable ht = new HashTable(N);
    	    for(int i = 0; i < M; i++){
    	        ht.insert(words[i]);
    	    }
    	    
    	    Random random = new Random();
    	    p = (int) (M * 0.1);
    	    double tot_exec_time = 0.0;
    	    double avg_exec_time_before;
    	    double avg_exec_time_after;
    	    for(int i = 0; i < p; i++){
    	        int j = random.nextInt(M);
    	        long startTime = System.nanoTime();
    	        ht.search(words[j]);
    	        tot_exec_time += (System.nanoTime() - startTime) / Math.pow(10, 9);
    	    }
    	    
    	    avg_exec_time_before = tot_exec_time / p;
    	    
    	    int[] selected = new int[p/2];
    	    int k = 0;
    	    for(int i = 0; i < p; i++){
    	        int j = random.nextInt(M);
    	        if(i % 2 == 0)  selected[k++] = j;
    	        ht.delete(words[j]);
    	    }
    	    tot_exec_time = 0.0;
    	    for(int i = 0; i < p/2; i++){
    	        long startTime = System.nanoTime();
    	        ht.search(words[selected[i]]);
    	        tot_exec_time += (System.nanoTime() - startTime) / Math.pow(10, 9);
    	    }
    	    for(int i = 0; i < (p - p/2); i++){
    	        long startTime = System.nanoTime();
    	        ht.search(words[i]);
    	        tot_exec_time += (System.nanoTime() - startTime) / Math.pow(10, 9);
    	    }
    	    
    	    avg_exec_time_after = tot_exec_time / p;
    	    
    	    //write in the file
    	    bw.write(lf + "\t\t" + String.valueOf(avg_exec_time_before) + "\t\tN/A\t\t" + String.valueOf(avg_exec_time_after) + "\t\tN/A" + "\n");
    	    
    	   // //open addressing
    	   // char [] coll_resolves = {'l', 'q', 'd'};
    	   // for(char c : coll_resolves){
        // 	    ht = new HashTable(N);
        // 	    ht.coll_resolve = c;
        // 	    for(int i = 0; i < M; i++){
        // 	        ht.insert(words[i]);
        // 	    }
        	    
        // 	    tot_exec_time = 0.0;
        // 	    double avg_num_probes_before = 0.0;
        // 	    double avg_num_probes_after = 0.0;
        // 	    int numProbes = 0;
        // 	    for(int i = 0; i < p; i++){
        // 	        int j = random.nextInt(M);
        // 	        long startTime = System.nanoTime();
        // 	        ht.search(words[j]);
        // 	        tot_exec_time += (System.nanoTime() - startTime) / Math.pow(10, 9);
        // 	        numProbes += ht.getNumOfProbes();
        // 	    }
        	    
        // 	    avg_exec_time_before = tot_exec_time / p;
        // 	    System.out.println(avg_exec_time_before);
        	    
        // 	    avg_num_probes_before = (numProbes / p);
        	    
        // 	    k = 0;
        // 	    for(int i = 0; i < p; i++){
        // 	        int j = random.nextInt(M);
        // 	        if(i % 2 == 0)  selected[k++] = j;
        // 	        ht.delete(words[j]);
        // 	    }
        // 	    tot_exec_time = 0.0;
        // 	   numProbes = 0;
        // 	    for(int i = 0; i < p/2; i++){
        // 	        long startTime = System.nanoTime();
        // 	        ht.search(words[selected[i]]);
        // 	        tot_exec_time += (System.nanoTime() - startTime) / Math.pow(10, 9);
        // 	        numProbes += ht.getNumOfProbes();
        // 	    }
        // 	    for(int i = 0; i < (p - p/2); i++){
        // 	        long startTime = System.nanoTime();
        // 	        ht.search(words[i]);
        // 	        tot_exec_time += (System.nanoTime() - startTime) / Math.pow(10, 9);
        // 	        numProbes += ht.getNumOfProbes();
        // 	    }
        	    
        // 	    avg_exec_time_after = tot_exec_time / p;
        // 	    avg_num_probes_after = (numProbes / p);
        // 	    bw.write(lf + "\t\t" + avg_exec_time_before + "\t\t" + String.valueOf(avg_num_probes_before) + "\t\t" + avg_exec_time_after + "\t" + String.valueOf(avg_num_probes_after) + "\n");
    	   // }
	    
	       // break;
	    }
	    
	    bw.close();
	   
	}
	
	public static String[] generateRandomWords(int numberOfWords, int length)
    {
        String[] randomStrings = new String[numberOfWords];
        Random random = new Random();
        for(int i = 0; i < numberOfWords; i++)
        {
            while(true){
                char[] word = new char[length];
                for(int j = 0; j < length; j++)
                {
                    word[j] = (char)('a' + random.nextInt(26));
                }
                randomStrings[i] = new String(word);
                
                if(!isPresent(randomStrings, randomStrings[i]))    break;
            }
        }
        return randomStrings;
    }
    
    public static boolean isPresent(String[] arr, String key){
        for(int i = 0; i < arr.length; i++){
            if(arr.equals(key))  return true;
        }
        
        return false;
    }
}

class data{
    public String key;
    public int value;
    public data next;
    public static int nodeOrder;
    public boolean isDeleted = false;

    data(String k, data next){
        key = k;
        this.next = next;
        value =  ++nodeOrder;
    }
    data(data next){
        this.next = next;
    }
}

class LList{
    private data head;
    private data tail;
    private data curr;
    private int cnt;
    
    LList(){
        curr = tail = head = new data(null);
        cnt = 0;
    }

    public void clear() {
        head.next = null;
        curr = tail = head = new data(null);
        cnt = 0;
    }

    public void insert(String key) {
        curr.next = new data(key, curr.next);
        if(curr == tail)    tail = curr.next;
        cnt++;
    }

    public void append(String key) {
        tail = tail.next = new data(key, null);
        cnt++;
    }

    public int remove() {
        if(curr.next == null)    return cnt;  //no element
        int val = curr.next.value;
        if(tail == curr.next)  tail = curr;
        curr.next = curr.next.next;
        cnt--;
        return val;
    }

    public void moveToStart() {
        curr = head;
    }

    public void moveToEnd() {
        data temp = head;
        while(temp.next != tail)   temp = temp.next;
        curr = temp;
        //curr = tail;
    }

    public void prev() {
        if(curr == head)    return;
        data temp = head;
        while(temp.next != curr)   temp = temp.next;
        curr = temp;
    }

    public void next() {
        if(curr.next != tail)    curr = curr.next;
    }

    public int length() {
        return cnt;
    }

    public int currPos() {
        int pos;
        data temp = head;
        for(pos = 0 ; curr != temp; pos++)
            temp = temp.next;
        return pos;
    }

    public void moveToPos(int pos) {
        if(pos < 0 || pos >= cnt)   return;
        curr = head;
        for(int i = 0 ; i < pos ; i++){
            curr = curr.next;
        }
    }

    public int getValue() {
        if(curr.next == null)    return cnt;    //no element
        return curr.next.value;
    }

    public int search(String search_key) {
        int keyPos = -1;
        curr = head;
        for(int i = 0 ; curr != tail ; i++){
            curr = curr.next;
            if(curr.key.equals(search_key)){
                keyPos = i;
                break;
            }
        }
        return keyPos;
    }
}


class HashTable{
    private LList [] lists;
    private data[] nodes;
    private int numOfProbes;
    private int N;
    public char coll_resolve;    //{'l', 'q', 'd'
    
    HashTable(int N){
        this.N = N;
	    lists = new LList[N];                 //separate chaining
        for(int i = 0; i < N; i++){
            lists[i] = new LList();
        }
        
        nodes = new data[N];
    }
    
    public int insert(String key){
        //separate chaining
        int index = (int) this.h1(key);
        // System.out.println("[" + index + "]");
        if(lists[index].length() == 0){
            lists[index].insert(key);
        }
        else{
            lists[index].append(key);
        }
        
        return index;
        
        
        // //open addressing
        // int i = 0;
        // int j = 0;
        // do{
        //     if(coll_resolve == 'l') j = (int) ((this.h1(key) + i) % N);     //linear probing
        //     else if(coll_resolve == 'q')  j = (int) ((this.h1(key) + 0.5*i*i + 0.5*i) % N);       //quadratic probing; N must be power of 2
        //     else if(coll_resolve == 'd')   j = (int) ((this.h1(key) + i * this.h2(key)) % N);      //double hashing; N must be prime
            
        //     if(nodes[j] == null || nodes[j].isDeleted == true){
        //         nodes[j] = new data(key, null);
        //         return j;
        //     }
        //     else{
        //         i++;
        //     }
        // } while(i < N);
        
        // return -1;
    }
    
    public void delete(String key){
        //separate chaining
        int j = (int) this.h1(key);
        lists[j].moveToPos(j);
        lists[j].remove();
        
        // //open addressing
        // int j = this.search(key);
        // nodes[j].isDeleted = true;
    }
    
    public int search(String key){
        //separate chaining
        int index = (int) this.h1(key);
        // System.out.println("search index: " + index);
        int result;
        if(lists[index].length() == 0)    result = -1;
        else{
            result = lists[index].search(key);
        }
        return result;
        
        
        // //open addressing
        // int i = 0;
        // int j = 0;
        // numOfProbes = 0;
        // do{
        //     if(coll_resolve == 'l') j = (int) ((this.h1(key) + i) % N);     //linear probing
        //     else if(coll_resolve == 'q')  j = (int) ((this.h1(key) + 0.5*i*i + 0.5*i) % N);       //quadratic probing; N must be power of 2
        //     else if(coll_resolve == 'd')   j = (int) ((this.h1(key) + i * this.h2(key)) % N);      //double hashing; N must be prime
            
        //     if(nodes[j].key.equals(key)){
        //         return j;
        //     }
        //     else{
        //         i++;
        //         numOfProbes++;
        //     }
        // } while(nodes[j] != null && i < N);
        
        // return -1;
    }
    
    public long h1(String key) {
        int p = 31;
        int m = (int)Math.pow(10, 9) + 9;
        long hash_value = 0;
        long temp = 1;
        for(int i = 0; i < key.length(); i++) {
            hash_value = (hash_value + (key.charAt(i) - 'a' + 1) * temp) % m;
            temp = (temp * p) % m;
        }
        return hash_value % N;
    }
    
    public long h2(String key)
    {
        long hash = 0;
    
        do{
            for(int i = 0; i < key.length(); i++)
                hash =  (int)key.charAt(i) + (hash << 6) + (hash << 16) - hash;
            if(hash > 0){
                hash = hash % N;
                break;
            }
        } while(true);
    
        return hash;
    }
    
    public LList getList(int i){
        return lists[i];
    }
    
    public int getNumOfProbes(){return this.numOfProbes;}
}









