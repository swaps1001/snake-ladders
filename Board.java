import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
	private Player player1, player2;

	public Board(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int x, y;
		int squareNumber = 100;

		for (int row = 0; row < 10; row++) {
		boolean isEvenRow = row % 2 == 0;

			for (int col = isEvenRow ? 0 : 9; isEvenRow ? col < 10 : col >= 0; col += isEvenRow ? 1 : -1) {

				x = (col * 50) + 50;
				y = (row * 50) + 50;

				paintSquare(g, x, y, squareNumber);

				paintSnake(g, x, y, squareNumber);
				paintLadder(g, x, y, squareNumber);

				if (squareNumber == player1.getPosition() && squareNumber == player2.getPosition()) {
					paintPlayer(player1, g, x, y, true);
				} else if (squareNumber == player1.getPosition()) {
					paintPlayer(player1, g, x, y, false);
				} else if (squareNumber == player2.getPosition()) {
					paintPlayer(player2, g, x, y, false);
				}

				squareNumber--;
			}
		}
	}

	private void paintSquare(Graphics g, int x, int y, int squareNumber) {
        if ((squareNumber % 2) == 0) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.GREEN);
        }

        g.fillRect(x, y, 50, 50);

        g.setColor(Color.BLACK);

        Font font = new Font("Open Sans", Font.PLAIN, 14);
        g.setFont(font);

        String squareNumberString = Integer.toString(squareNumber);

        int stringHeight = g.getFontMetrics().getHeight();
        g.drawString(squareNumberString, x + 4, y + (stringHeight - 4));
    }

	private void paintSnake(Graphics g, int x, int y, int squareNumber) {
		if (squareNumber == 55) {
            drawSnakeOrLadder(g, x, y, "../img/snake.png", 300, 94, 208);
        } else if (squareNumber == 6) {
            drawSnakeOrLadder(g, x, y, "../img/snake.png", 320, 100, 295);
        }
	}

	private void paintLadder(Graphics g, int x, int y, int squareNumber) {
		if (squareNumber == 16) {
            drawSnakeOrLadder(g, x, y, "../img/ladder.png", 35, 290, 158);
        } else if (squareNumber == 47) {
            drawSnakeOrLadder(g, x, y, "../img/ladder.png", 35, 240, 205);
        } else if (squareNumber == 76) {
            drawSnakeOrLadder(g, x, y, "../img/ladder.png", 30, 130, 148);
//            drawSnakeOrLadder(g, x, y, "../img/ladder1.png", 130, 70, 230);
        }
	}

	private void drawSnakeOrLadder(Graphics g, int x, int y, String imagePath, int scaleX, int scaleY, int rotationAngle) {
		Graphics2D g2d = (Graphics2D) g;

		ImageIcon originalIcon = new ImageIcon(imagePath);

		Image originalImage = originalIcon.getImage();
		Image resizedImage = originalImage.getScaledInstance(scaleX, scaleY, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(resizedImage);

		g2d.rotate(Math.toRadians(rotationAngle), x + 25, y + 25);

		resizedIcon.paintIcon(this, g2d, x + 5, y + 5);

		g2d.rotate(-Math.toRadians(rotationAngle), x + 25, y + 25);
	}

	private void paintPlayer(Player player, Graphics g, int x, int y, boolean sameSquare) {
		if (player == player1) {
			g.setColor(Color.BLUE);
		} else if (player == player2) {
			g.setColor(Color.YELLOW);
		}

		g.fillOval(x + 10, y + 10, 30, 30);

		if (sameSquare) {
			g.setColor(Color.YELLOW);
			g.fillOval(x + 15, y + 15, 20, 20);
		}
	}
}
