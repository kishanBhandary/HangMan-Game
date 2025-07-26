import javax.swing.*;
import java.awt.*;

public class HangmanPanel extends JPanel {
    private int wrongGuesses = 0;

    public HangmanPanel() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    public void setWrongGuesses(int wrongGuesses) {
        this.wrongGuesses = wrongGuesses;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(Color.BLACK);

        int width = getWidth();
        int height = getHeight();

        // Base coordinates
        int baseX = width / 2 - 50;
        int baseY = height - 50;
        int poleHeight = height - 100;
        int armLength = 80;

        // Always draw the gallows
        drawGallows(g2d, baseX, baseY, poleHeight, armLength);

        // Draw hangman parts based on wrong guesses
        if (wrongGuesses >= 1) drawHead(g2d, baseX + armLength, 80);
        if (wrongGuesses >= 2) drawBody(g2d, baseX + armLength, 80);
        if (wrongGuesses >= 3) drawLeftArm(g2d, baseX + armLength, 80);
        if (wrongGuesses >= 4) drawRightArm(g2d, baseX + armLength, 80);
        if (wrongGuesses >= 5) drawLeftLeg(g2d, baseX + armLength, 80);
        if (wrongGuesses >= 6) drawRightLeg(g2d, baseX + armLength, 80);
    }

    private void drawGallows(Graphics2D g2d, int baseX, int baseY, int poleHeight, int armLength) {
        // Base
        g2d.drawLine(baseX - 30, baseY, baseX + 30, baseY);

        // Vertical pole
        g2d.drawLine(baseX, baseY, baseX, baseY - poleHeight);

        // Horizontal arm
        g2d.drawLine(baseX, baseY - poleHeight, baseX + armLength, baseY - poleHeight);

        // Noose
        g2d.drawLine(baseX + armLength, baseY - poleHeight, baseX + armLength, baseY - poleHeight + 30);
    }

    private void drawHead(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x - 15, y + 30, 30, 30);

        // Face
        g2d.setStroke(new BasicStroke(2));
        // Eyes (X marks if dead)
        if (wrongGuesses >= 6) {
            g2d.drawLine(x - 8, y + 38, x - 4, y + 42);
            g2d.drawLine(x - 4, y + 38, x - 8, y + 42);
            g2d.drawLine(x + 4, y + 38, x + 8, y + 42);
            g2d.drawLine(x + 8, y + 38, x + 4, y + 42);
        } else {
            // Normal eyes
            g2d.fillOval(x - 8, y + 38, 3, 3);
            g2d.fillOval(x + 5, y + 38, 3, 3);
        }

        // Mouth
        if (wrongGuesses >= 6) {
            // Sad mouth
            g2d.drawArc(x - 6, y + 48, 12, 8, 0, -180);
        } else {
            // Neutral mouth
            g2d.drawLine(x - 4, y + 50, x + 4, y + 50);
        }

        g2d.setStroke(new BasicStroke(3));
    }

    private void drawBody(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.BLACK);
        g2d.drawLine(x, y + 60, x, y + 120);
    }

    private void drawLeftArm(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.BLACK);
        g2d.drawLine(x, y + 80, x - 25, y + 100);
    }

    private void drawRightArm(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.BLACK);
        g2d.drawLine(x, y + 80, x + 25, y + 100);
    }

    private void drawLeftLeg(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.BLACK);
        g2d.drawLine(x, y + 120, x - 20, y + 150);
    }

    private void drawRightLeg(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.BLACK);
        g2d.drawLine(x, y + 120, x + 20, y + 150);
    }
}
