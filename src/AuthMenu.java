import java.util.Scanner;

public class AuthMenu {

    public static int numberScanner() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {

                String inputStr = scanner.nextLine();


                int input = Integer.parseInt(inputStr);


                return input;

            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }
}