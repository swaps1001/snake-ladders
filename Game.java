import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Game extends JFrame {

	private Board board;
	private Player player1, player2, currentPlayer;

	private Dice dice;
	private JLabel diceImageLabel;
	private JButton diceButton;
	private JLabel diceResultLabel;

	private Question question;
	private JLabel questionLabel;
	private ButtonGroup optionButtonGroup;
	private JRadioButton option1, option2, option3, option4;
	private JButton submit;
	private JLabel answerLabel;
	private int questionNumber;

	private Game() {
		setTitle("Enhanced Snakes and Ladders");
		setSize(1150, 650);
		setLayout(new GridLayout(1, 2, 10, 10));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		player1 = new Player(1);
		player2 = new Player(1);
		board = new Board(player1, player2);

		dice = new Dice();
		diceImageLabel = new JLabel();
		diceButton = new JButton("Roll");
		diceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					rollDice();
				} catch (SQLException s) {}
			}
		});
		diceResultLabel = new JLabel("Result: ");
		answerLabel = new JLabel();

		dice.add(diceResultLabel, BorderLayout.NORTH);
		dice.add(diceImageLabel, BorderLayout.CENTER);
		dice.add(diceButton, BorderLayout.SOUTH);

		currentPlayer = player1;

		questionNumber = 1;

		add(board);
		add(dice);
		setVisible(true);
	}

	private void rollDice() throws SQLException {
		int diceResult = dice.roll();
		diceResultLabel.setText("Result: " + diceResult);

		int playerPosition = currentPlayer.getPosition() + diceResult;
		playerPosition = Math.min(playerPosition, 100);

		if (playerPosition == 100) {
		    currentPlayer.setPosition(100);
		    question.endConnection();
		    dice.remove(diceImageLabel);
		    dice.add(answerLabel, BorderLayout.CENTER);
		    answerLabel.setText("Player " + getPlayerNumber() + " wins!");
		    diceButton.setEnabled(false);
		}

		currentPlayer.setPosition(playerPosition);
		board.repaint();

		String imagePath = "../img/dice-" + diceResult + ".png";
		ImageIcon diceIcon = new ImageIcon(imagePath);
		diceImageLabel.setIcon(diceIcon);

		if (playerPosition == 16 || playerPosition == 47 || playerPosition == 76) {
		    handleSpecialSquare(false);
		} else if (playerPosition == 51 || playerPosition == 99) {
		    handleSpecialSquare(true);
		} else {
		    switchPlayer();
		}
	}

	private void handleSpecialSquare(boolean isSnake) throws SQLException {
		question = new Question();
		questionLabel = new JLabel(question.getQuestion(questionNumber));
		option1 = new JRadioButton(question.getOption(questionNumber, 1));
		option2 = new JRadioButton(question.getOption(questionNumber, 2));
		option3 = new JRadioButton(question.getOption(questionNumber, 3));
		option4 = new JRadioButton(question.getOption(questionNumber, 4));
		optionButtonGroup = new ButtonGroup();
		optionButtonGroup.add(option1);
		optionButtonGroup.add(option2);
		optionButtonGroup.add(option3);
		optionButtonGroup.add(option4);
		submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        try {
		            checkMarkedValue(isSnake);
		            switchPlayer();
		            questionNumber++;
		            if (questionNumber == 6) {
		            	questionNumber = 1;
		            }
		            question.hideFrame();
		            dice.remove(answerLabel);
		            dice.add(diceImageLabel, BorderLayout.CENTER);
		        } catch (SQLException s) {
		            s.printStackTrace();
		        }
		    }
		});
		question.setLayout(new GridLayout(7, 1));
		question.add(questionLabel);
		question.add(option1);
		question.add(option2);
		question.add(option3);
		question.add(option4);
		question.add(submit);
		question.setVisible(true);
	}

	private void checkMarkedValue(boolean isSnake) throws SQLException {
	    int selectedOption = getSelectedOption();
	    int correctAnswer = question.getAnswer(questionNumber);
		dice.remove(diceImageLabel);
		dice.add(answerLabel, BorderLayout.CENTER);

		if (isSnake) {
		    if (!(selectedOption == correctAnswer)) {
		        answerLabel.setText("Correct!");
		        currentPlayer.setPosition(getNewPos(currentPlayer.getPosition()));
		    } else {
		        answerLabel.setText("Wrong!");
		    }

		} else {
		    if (selectedOption == correctAnswer) {
		        answerLabel.setText("Correct!");
		        currentPlayer.setPosition(getNewPos(currentPlayer.getPosition()));
		    } else {
		        answerLabel.setText("Wrong!");
		    }
		}
	}

	private int getSelectedOption() {
	    if (option1.isSelected()) {
	        return 1;
	    } else if (option2.isSelected()) {
	        return 2;
	    } else if (option3.isSelected()) {
	        return 3;
	    } else if (option4.isSelected()) {
	        return 4;
	    }
		return -1;
	}

	private int getNewPos(int position) {
		switch (position) {
			case 16:
				return 63;
			case 47:
				return 89;
			case 76:
				return 97;
			case 51:
				return 6;
			case 99:
				return 55;
			default:
				return position;
		}
	}

	private void switchPlayer() {
		if (currentPlayer == player1) {
			currentPlayer = player2;
		} else {
			currentPlayer = player1;
		}
	}

	private int getPlayerNumber() {
		if (currentPlayer == player1) {
			return 1;
		} else {
			return 2;
		}
	}

	public static void main(String[] args) {
		new Game();
	}
}
