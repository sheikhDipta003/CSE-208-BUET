import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class online {
    public static void main(String[] args) throws IOException {
        int N, M, p;   //N -> hashtable-size, M -> total number of elements in the hashtable, p -> number of selected elements
        double lf = 0.2;
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        M = (int) (N * lf);
        String[] words = Main.generateRandomWords(M, 7);
        Random random = new Random();

        HashTable ht = new HashTable(N);
        ht.coll_resolve = 'd';

        //insert
        int probeCount = 0;
        for(int i = 0; i < M; i++){
            ht.insert(words[i]);
            probeCount += ht.getNumOfProbes();
        }

        double tot_exec_time = 0.0;
        double avg_num_probes_before = 0.0;
        double avg_num_probes_after = 0.0;
        int numProbes = 0;
        for(int i = 0; i < M; i++){
            int j = random.nextInt(M);
            long startTime = System.nanoTime();
            ht.search(words[j]);
            tot_exec_time += (System.nanoTime() - startTime) / Math.pow(10, 9);
            numProbes += ht.getNumOfProbes();
        }

        double avg_exec_time_before = tot_exec_time / M;
        avg_num_probes_before = (double)(numProbes / M);

        if(Math.abs(probeCount/M - 2) > 0.000001){
            HashTable temp = new HashTable(2*N+1);
            for(int i = 0; i < M; i++){
                temp.insert(words[i]);
            }

            ht = new HashTable(2*N+1);
            ht = temp;
        }

        tot_exec_time = 0.0;
        numProbes = 0;
        for(int i = 0; i < M; i++){
            int j = random.nextInt(M);
            long startTime = System.nanoTime();
            ht.search(words[j]);
            tot_exec_time += (System.nanoTime() - startTime) / Math.pow(10, 9);
            numProbes += ht.getNumOfProbes();
        }

        double avg_exec_time_after = tot_exec_time / M;
        avg_num_probes_after = (double)(numProbes / M);

        System.out.println(avg_exec_time_before + ", " + avg_exec_time_after);
        System.out.println(avg_num_probes_before + ", " + avg_num_probes_after);


        //deletion
        for(int i = 0; i < M/2; i++){
            ht.delete(words[i]);
        }

        tot_exec_time = 0.0;
        numProbes = 0;
        for(int i = 0; i < M; i++){
            int j = random.nextInt(M);
            long startTime = System.nanoTime();
            ht.search(words[j]);
            tot_exec_time += (System.nanoTime() - startTime) / Math.pow(10, 9);
            numProbes += ht.getNumOfProbes();
        }

        avg_exec_time_before = tot_exec_time / M;
        avg_num_probes_before = (double)(numProbes / M);

        if(Math.abs((double)ht.rem / N - 0.4) < 0.0){
            HashTable temp = new HashTable(N/2+1);
            for(int i = M/2+1; i < M; i++){
                temp.insert(words[i]);
            }
            ht = new HashTable(N/2+1);
            ht = temp;
        }
        tot_exec_time = 0.0;
        numProbes = 0;
        for(int i = 0; i < M; i++){
            int j = random.nextInt(M);
            long startTime = System.nanoTime();
            ht.search(words[j]);
            tot_exec_time += (System.nanoTime() - startTime) / Math.pow(10, 9);
            numProbes += ht.getNumOfProbes();
        }

        avg_exec_time_after = tot_exec_time / M;
        avg_num_probes_after = (double)(numProbes / M);

        System.out.println(avg_exec_time_before + ", " + avg_exec_time_after);
        System.out.println(avg_num_probes_before + ", " + avg_num_probes_after);
    }
    
}
