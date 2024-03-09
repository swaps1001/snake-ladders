import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Dice extends JPanel {

	public Dice() {
		setLayout(new BorderLayout());
	}

	public int roll() {
		return new Random().nextInt(6) + 1;
	}
}
