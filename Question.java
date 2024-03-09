import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Question extends JFrame {

    private static final String DRIVER   = "com.mysql.cj.jdbc.Driver";
    private static final String URL      = "jdbc:mysql:/"+"/localhost:3306/";
    private static final String SCHEMA   = "javacon";
    private static final String USERNAME = "aryan";
    private static final String PASSWORD = "";
    private Connection connection;

    public Question() {
        setTitle("HALT");
        setSize(750, 600);
        setLayout(new FlowLayout());
        setLocation(960, 310);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        try {
            startConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void startConnection() throws SQLException, ClassNotFoundException {
        Class.forName(DRIVER);
        connection = DriverManager.getConnection(URL + SCHEMA, USERNAME, PASSWORD);
    }

    public String getQuestion(int questionNumber) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT question FROM questions WHERE qid=?")) {
            ps.setInt(1, questionNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("question");
                }
            }
        }
        return null;
    }

	public String getOption(int questionNumber, int optionNumber) throws SQLException {
	    try (PreparedStatement ps = connection.prepareStatement("SELECT opt FROM options WHERE qid=? AND oid=?")) {
	        ps.setInt(1, questionNumber);
	        ps.setInt(2, optionNumber);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getString("opt");
	            }
	        }
	    }
	    return null;
	}

	public int getAnswer(int questionNumber) throws SQLException {
	    try (PreparedStatement ps = connection.prepareStatement("SELECT oid FROM answers WHERE qid=?")) {
	        ps.setInt(1, questionNumber);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("oid");
	            }
	        }
	    }
	    return -1;
	}

    public void endConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void hideFrame() {
		setVisible(false);
    }
}
