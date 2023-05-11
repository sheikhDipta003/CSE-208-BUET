import java.util.ArrayList;
import java.util.Scanner;

public class online_1 {

    public static void main(String[] args) {
        int T;
        GraphList G = new GraphList(9);
        Scanner scanner = new Scanner(System.in);
        T = Integer.parseInt(scanner.nextLine());
        for(int i = 0; i < T; i++){
            char [][] board = new char[3][3];

            for(int j = 0; j < 3; j++) {
                String input = scanner.nextLine();
                board[j][0] = input.charAt(0);
                board[j][1] = input.charAt(1);
                board[j][2] = input.charAt(2);
            }
            
        }

    }

}
