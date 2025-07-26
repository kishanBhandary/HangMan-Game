import java.util.*;

public class HangmanGame {
    private static final String[] WORDS = {
            "PROGRAMMING", "COMPUTER", "JAVA", "HANGMAN", "CHALLENGE",
            "DEVELOPER", "SOFTWARE", "ALGORITHM", "DATABASE", "NETWORK",
            "SECURITY", "FRAMEWORK", "INTERFACE", "VARIABLE", "FUNCTION"
    };

    private static final String[] HANGMAN_STAGES = {
            """
          +---+
          |   |
              |
              |
              |
              |
        =========
        """,
            """
          +---+
          |   |
          O   |
              |
              |
              |
        =========
        """,
            """
          +---+
          |   |
          O   |
          |   |
              |
              |
        =========
        """,
            """
          +---+
          |   |
          O   |
         /|   |
              |
              |
        =========
        """,
            """
          +---+
          |   |
          O   |
         /|\\  |
              |
              |
        =========
        """,
            """
          +---+
          |   |
          O   |
         /|\\  |
         /    |
              |
        =========
        """,
            """
          +---+
          |   |
          O   |
         /|\\  |
         / \\  |
              |
        =========
        """
    };

    private String wordToGuess;
    private char[] guessedWord;
    private Set<Character> guessedLetters;
    private int wrongGuesses;
    private static final int MAX_WRONG_GUESSES = 6;
    private Scanner scanner;

    public HangmanGame() {
        this.scanner = new Scanner(System.in);
        this.guessedLetters = new HashSet<>();
        this.wrongGuesses = 0;
    }

    public void startGame() {
        System.out.println("=================================");
        System.out.println("    WELCOME TO HANGMAN GAME!    ");
        System.out.println("=================================");
        System.out.println();

        boolean playAgain = true;

        while (playAgain) {
            initializeGame();
            playRound();
            playAgain = askPlayAgain();
        }

        System.out.println("Thanks for playing Hangman! Goodbye!");
        scanner.close();
    }

    private void initializeGame() {
        // Reset game state
        this.wrongGuesses = 0;
        this.guessedLetters.clear();

        // Select random word
        Random random = new Random();
        this.wordToGuess = WORDS[random.nextInt(WORDS.length)];

        // Initialize guessed word with underscores
        this.guessedWord = new char[wordToGuess.length()];
        for (int i = 0; i < wordToGuess.length(); i++) {
            guessedWord[i] = '_';
        }

        System.out.println("New game started!");
        System.out.println("Word length: " + wordToGuess.length() + " letters");
        System.out.println();
    }

    private void playRound() {
        while (!isGameOver()) {
            displayGameState();
            char guess = getPlayerGuess();
            processGuess(guess);
        }

        displayGameState();
        displayGameResult();
    }

    private void displayGameState() {
        // Display hangman
        System.out.println(HANGMAN_STAGES[wrongGuesses]);

        // Display word progress
        System.out.print("Word: ");
        for (int i = 0; i < guessedWord.length; i++) {
            System.out.print(guessedWord[i] + " ");
        }
        System.out.println();

        // Display guessed letters
        if (!guessedLetters.isEmpty()) {
            System.out.print("Guessed letters: ");
            List<Character> sortedGuesses = new ArrayList<>(guessedLetters);
            Collections.sort(sortedGuesses);
            for (char letter : sortedGuesses) {
                System.out.print(letter + " ");
            }
            System.out.println();
        }

        // Display remaining guesses
        System.out.println("Wrong guesses: " + wrongGuesses + "/" + MAX_WRONG_GUESSES);
        System.out.println();
    }

    private char getPlayerGuess() {
        while (true) {
            System.out.print("Enter a letter: ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.length() != 1) {
                System.out.println("Please enter exactly one letter!");
                continue;
            }

            char guess = input.charAt(0);

            if (!Character.isLetter(guess)) {
                System.out.println("Please enter a valid letter!");
                continue;
            }

            if (guessedLetters.contains(guess)) {
                System.out.println("You already guessed that letter! Try again.");
                continue;
            }

            return guess;
        }
    }

    private void processGuess(char guess) {
        guessedLetters.add(guess);
        boolean correctGuess = false;

        // Check if the guess is in the word
        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == guess) {
                guessedWord[i] = guess;
                correctGuess = true;
            }
        }

        if (correctGuess) {
            System.out.println("Good guess! '" + guess + "' is in the word.");
        } else {
            wrongGuesses++;
            System.out.println("Sorry! '" + guess + "' is not in the word.");
        }

        System.out.println();
    }

    private boolean isGameOver() {
        return isWordGuessed() || wrongGuesses >= MAX_WRONG_GUESSES;
    }

    private boolean isWordGuessed() {
        for (char c : guessedWord) {
            if (c == '_') {
                return false;
            }
        }
        return true;
    }

    private void displayGameResult() {
        System.out.println("=================================");
        if (isWordGuessed()) {
            System.out.println("🎉 CONGRATULATIONS! YOU WON! 🎉");
            System.out.println("You guessed the word: " + wordToGuess);
            System.out.println("Wrong guesses: " + wrongGuesses + "/" + MAX_WRONG_GUESSES);
        } else {
            System.out.println("💀 GAME OVER! YOU LOST! 💀");
            System.out.println("The word was: " + wordToGuess);
        }
        System.out.println("=================================");
        System.out.println();
    }

    private boolean askPlayAgain() {
        while (true) {
            System.out.print("Do you want to play again? (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                System.out.println();
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Please enter 'y' for yes or 'n' for no.");
            }
        }
    }

    public static void main(String[] args) {
        HangmanGame game = new HangmanGame();
        game.startGame();
    }
}
