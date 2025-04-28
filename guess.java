import java.io.*;
import java.util.*;

public class guess {

    // Console colors
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";

    public static final String SCORE_FILE = "highscores_java";

    public static void main(String[] args) throws IOException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        String playAgain;

        do {
            playGame(stdin);
            System.out.print(YELLOW + "\nDo you want to play again? (y/n): " + RESET);
            playAgain = stdin.readLine();
        } while (playAgain.equalsIgnoreCase("y"));

        System.out.println(GREEN + "Thanks for playing! 👋" + RESET);
    }

    public static void playGame(BufferedReader stdin) throws IOException {
        int numGuesses = 0;
        int guess = -1;
        int answer;
        String name;

        // Welcome banner
        System.out.println(BLUE + "==============================");
        System.out.println("  🎲 Welcome to Number Guesser 🎲");
        System.out.println("==============================" + RESET);
        System.out.println("Guess a number between 1 and 100\n");

        // Generate random number between 1 and 100
        java.util.Random generator = new java.util.Random(System.currentTimeMillis());
        answer = generator.nextInt(100) + 1;

        // Game loop
        while (guess != answer) {
            numGuesses++;
            System.out.print(YELLOW + "Enter guess " + numGuesses + ": " + RESET);
            String result = stdin.readLine();

            try {
                guess = Integer.parseInt(result);
            } catch (NumberFormatException e) {
                System.out.println(RED + "❌ Please enter a valid number!" + RESET);
                numGuesses--;
                continue;
            }

            if (guess < 1 || guess > 100) {
                System.out.println(RED + "❌ Guess must be between 1 and 100!" + RESET);
                numGuesses--;
                continue;
            }

            if (guess < answer) {
                System.out.println(CYAN + "🔼 Higher..." + RESET);
            } else if (guess > answer) {
                System.out.println(CYAN + "🔽 Lower..." + RESET);
            }
        }

        System.out.println(GREEN + "🎉 Correct! That took " + numGuesses + " guesses.\n" + RESET);

        // Save high score
        System.out.print("Please enter your name: ");
        name = stdin.readLine();
        try {
            File file = new File(SCORE_FILE);
            file.createNewFile(); // create if not exists
            BufferedWriter outfile = new BufferedWriter(new FileWriter(file, true));
            outfile.write(name + " " + numGuesses + "\n");
            outfile.close();
        } catch (IOException exception) {
            System.out.println(RED + "ERROR: Can't write to " + SCORE_FILE + RESET);
        }

        // Display top 5 high scores
        showHighScores();
    }

    public static void showHighScores() {
        List<String> scores = new ArrayList<>();

        try {
            BufferedReader infile = new BufferedReader(new FileReader(SCORE_FILE));
            String line;
            while ((line = infile.readLine()) != null) {
                scores.add(line);
            }
            infile.close();

            // Sort by score (ascending)
            scores.sort(Comparator.comparingInt(s -> Integer.parseInt(s.split(" ")[1])));

            System.out.println(BLUE + "\n🏆 Top 5 High Scores:" + RESET);
            for (int i = 0; i < Math.min(5, scores.size()); i++) {
                System.out.println(GREEN + (i + 1) + ". " + scores.get(i) + RESET);
            }

        } catch (IOException exception) {
            System.out.println(RED + "ERROR: Can't read from " + SCORE_FILE + RESET);
        }
    }
}
