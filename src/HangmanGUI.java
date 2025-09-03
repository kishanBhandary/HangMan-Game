import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class HangmanGUI extends JFrame {
    private static final String[] WORDS = {
           };

    private String wordToGuess;
    private char[] guessedWord;
    private Set<Character> guessedLetters;
    private int wrongGuesses;
    private static final int MAX_WRONG_GUESSES = 6;

    // GUI Components
    private HangmanPanel hangmanPanel;
    private JLabel wordLabel;
    private JLabel statusLabel;
    private JLabel guessedLettersLabel;
    private JTextField guessField;
    private JButton guessButton;
    private JButton newGameButton;
    private JPanel letterButtonsPanel;
    private Map<Character, JButton> letterButtons;

    public HangmanGUI() {
        initializeGUI();
        initializeGame();
    }

    private void initializeGUI() {
        setTitle("Hangman Gamee");
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        createComponents();
        layoutComponents();

        pack();
        setLocationRelativeTo(null);
    }

    private void createComponents() {
        hangmanPanel = new HangmanPanel();
        hangmanPanel.setPreferredSize(new Dimension(300, 350));
        hangmanPanel.setBorder(BorderFactory.createTitledBorder("Hangman"));
        wordLabel = new JLabel("", SwingConstants.CENTER);
        wordLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        wordLabel.setBorder(BorderFactory.createTitledBorder("Word"));
        wordLabel.setPreferredSize(new Dimension(400, 80));
        statusLabel = new JLabel("Welcome to Hangman! Guess the word.", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setForeground(Color.BLUE);

        // Guessed letters display
        guessedLettersLabel = new JLabel("Guessed: ", SwingConstants.CENTER);
        guessedLettersLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        // Input components
        guessField = new JTextField(2);
        guessField.setFont(new Font("Arial", Font.BOLD, 16));
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessButton = new JButton("Guess");
        guessButton.setFont(new Font("Arial", Font.BOLD, 14));
        guessButton.setBackground(new Color(70, 130, 180));
        guessButton.setForeground(Color.WHITE);
        newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.BOLD, 14));
        newGameButton.setBackground(new Color(34, 139, 34));
        newGameButton.setForeground(Color.WHITE);
        createAlphabetButtons();
        guessButton.addActionListener(new GuessButtonListener());
        newGameButton.addActionListener(e -> startNewGame());
        guessField.addActionListener(new GuessButtonListener());
    }
    private void createAlphabetButtons() {
        letterButtonsPanel = new JPanel(new GridLayout(3, 9, 2, 2));
        letterButtonsPanel.setBorder(BorderFactory.createTitledBorder("Click a Letter"));
        letterButtons = new HashMap<>();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZAbcdefghcidjdefgh";
        for (char c : alphabet.toCharArray()) {
            JButton button = new JButton(String.valueOf(c));
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setPreferredSize(new Dimension(35, 35));
            button.setBackground(new Color(240, 240, 240));
            button.addActionListener(new LetterButtonListener(c));
            letterButtons.put(c, button);
            letterButtonsPanel.add(button);
        }
    }
    private void layoutComponents() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(hangmanPanel, BorderLayout.CENTER);
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setPreferredSize(new Dimension(450, 350));
        JPanel wordPanel = new JPanel(new BorderLayout(5, 5));
        wordPanel.add(wordLabel, BorderLayout.CENTER);
        wordPanel.add(statusLabel, BorderLayout.SOUTH);
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Enter letter:"));
        inputPanel.add(guessField);
        inputPanel.add(guessButton);
        inputPanel.add(Box.createHorizontalStrut(20));
        inputPanel.add(newGameButton);
        JPanel guessedPanel = new JPanel(new BorderLayout());
        guessedPanel.add(guessedLettersLabel, BorderLayout.CENTER);
        rightPanel.add(wordPanel, BorderLayout.NORTH);
        rightPanel.add(letterButtonsPanel, BorderLayout.CENTER);
        rightPanel.add(guessedPanel, BorderLayout.SOUTH);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void initializeGame() {
        this.guessedLetters = new HashSet<>();
        this.wrongGuesses = 0;
        startNewGame();
    }

    private void startNewGame() {
        wrongGuesses = 0;
        guessedLetters.clear();

        Random random = new Random();
        wordToGuess = WORDS[random.nextInt(WORDS.length)];

        // Initialize guessed word
        guessedWord = new char[wordToGuess.length()];
        for (int i = 0; i < wordToGuess.length(); i++) {
            guessedWord[i] = '_';
        }
        updateDisplay();
        enableAllLetterButtons();
        guessField.setEnabled(true);
        guessButton.setEnabled(true);
        guessField.requestFocus();

        statusLabel.setText("New game started! Word length: " + wordToGuess.length() + " letters");
        statusLabel.setForeground(Color.BLUE);
    }

    private void processGuess(char guess) {
        if (guessedLetters.contains(guess)) {
            statusLabel.setText("You already guessed '" + guess + "'! Try another letter.");
            statusLabel.setForeground(Color.ORANGE);
            return;
        }

        guessedLetters.add(guess);
        boolean correctGuess = false;
        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == guess) {
                guessedWord[i] = guess;
                correctGuess = true;
            }
        }

        if (correctGuess) {
            statusLabel.setText("Goods guess! '" + guess + "' is in the word.");
            statusLabel.setForeground(new Color(34, 139, 34));
        } else {
            wrongGuesses++;
            statusLabel.setText("Sorry! '" + guess + "' is not in the word. (" + wrongGuesses + "/" + MAX_WRONG_GUESSES + ")");
            statusLabel.setForeground(Color.RED);
        }

        // Disable the guessed letter button
        letterButtons.get(guess).setEnabled(false);
        letterButtons.get(guess).setBackground(correctGuess ? Color.GREEN : Color.RED);

        updateDisplay();
        checkGameEnd();
        guessField.setText("");
    }

    private void updateDisplay() {
        // Update word display
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < guessedWord.length; i++) {
            display.append(guessedWord[i]).append(" ");
        }
        wordLabel.setText(display.toString().trim());

        // Update guessed letters
        if (!guessedLetters.isEmpty()) {
            List<Character> sortedGuesses = new ArrayList<>(guessedLetters);
            Collections.sort(sortedGuesses);
            StringBuilder guessed = new StringBuilder("Guessed: ");
            for (char c : sortedGuesses) {
                guessed.append(c).append(" ");
            }
            guessedLettersLabel.setText(guessed.toString());
        } else {
            guessedLettersLabel.setText("Guessszzzzz: ");
        }

        // Update hangman drawing
        hangmanPanel.setWrongGuesses(wrongGuesses);
        hangmanPanel.repaint();
    }

    private void checkGameEnd() {
        if (isWordGuessed()) {
            statusLabel.setText("🎉 CONGRATULATIONS! YOU WON! 🎉");
            statusLabel.setForeground(new Color(34, 139, 34));
            disableInput();
            showGameEndDialog("Congratulations!", "You won! The word was: " + wordToGuess);
        } else if (wrongGuesses >= MAX_WRONG_GUESSES) {
            statusLabel.setText("💀 GAME OVER! The word was: " + wordToGuess);
            statusLabel.setForeground(Color.RED);
            disableInput();
            showGameEndDialog("Game Over!", "You lost! The word was: " + wordToGuess);
        }
    }

    private boolean isWordGuessed() {
        for (char c : guessedWord) {
            if (c == '_') {
                return false;
            }
        }
        return true;
    }

    private void disableInput() {
        guessField.setEnabled(false);
        guessButton.setEnabled(false);
        for (JButton button : letterButtons.values()) {
            button.setEnabled(false);
        }
    }

    private void enableAllLetterButtons() {
        for (Map.Entry<Character, JButton> entry : letterButtons.entrySet()) {
            JButton button = entry.getValue();
            button.setEnabled(true);
            button.setBackground(new Color(240, 240, 240));
        }
    }

    private void showGameEndDialog(String title, String message) {
        int option = JOptionPane.showConfirmDialog(
                this,
                message + "\n\nWould you like to play againuuuuuuuu?",
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            startNewGame();
        }
    }

    private class GuessButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = guessField.getText().trim().toUpperCase();

            if (input.length() != 1) {
                statusLabel.setText("Please enter exactly one letter!");
                statusLabel.setForeground(Color.RED);
                return;
            }

            char guess = input.charAt(0);

            if (!Character.isLetter(guess)) {
                statusLabel.setText("Please enter a valid letter!");
                statusLabel.setForeground(Color.RED);
                return;
            }

            processGuess(guess);
        }
    }

    private class LetterButtonListener implements ActionListener {
        private char letter;

        public LetterButtonListener(char letter) {
            this.letter = letter;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            processGuess(letter);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HangmanGUI().setVisible(true);
        });
    }
}
