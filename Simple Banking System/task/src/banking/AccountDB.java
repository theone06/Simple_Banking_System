package banking;

import java.sql.*;

public class AccountDB {
    String url = "jdbc:sqlite:";

    //constructor
    public AccountDB(String input) {
        url = url + input;
        createNewDataBase();
    }
    //method to create the initial database
    public void createNewDataBase() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                createNewTable();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    //method to create the card table
    public void createNewTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	 id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "	number TEXT,\n"
                + "	pin TEXT,\n"
                + "	balance INTEGER DEFAULT 0\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertData(String num, String pin) {
        String insertSql =  "INSERT INTO card (number, pin) VALUES(?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
             pstmt.setString(1, num);
             pstmt.setString(2, pin);
             pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void addToBalance(String num, int amount) {
        String updateSql = "UPDATE card SET balance = balance + " + amount
                +  " WHERE number = " + num + ";";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
             pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void subtractFromBalance(String num, int amount) {
        String updateSql = "UPDATE card SET balance = balance - " + amount
                +  " WHERE number = " + num + ";";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement prep = conn.prepareStatement(updateSql)) {
             prep.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public boolean containsRecord(String num, String pin) {
        boolean found = false;
        String sql = "SELECT * FROM card WHERE number = ? AND pin = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
             pstmt.setString(1, num);
             pstmt.setString(2, pin);
             ResultSet rs  = pstmt.executeQuery();
             if (rs.next()) {
                 found = true;
             }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return found;
    }

    public boolean accountNumberExists(String num) {
        boolean found = false;
        String sql = "SELECT * FROM card WHERE number = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setString(1, num);
            ResultSet rs  = pstmt.executeQuery();
            if (rs.next()) {
                found = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return found;
    }
     //To Do: replace all instances with returnBalance()
    public void printBalance(String num, String pin) {
        String balanceSQL = "SELECT balance FROM card WHERE number = ? AND pin = ?";
        try (Connection conn = this.connect();
             PreparedStatement prep = conn.prepareStatement(balanceSQL)){
             prep.setString(1, num);
             prep.setString(2, pin);
             ResultSet rs = prep.executeQuery();
             while (rs.next()) {
                 System.out.println("Balance: " + rs.getInt("balance"));
             }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int returnBalance(String num) {
        String balanceSQL = "SELECT balance FROM card WHERE number = ?";
        int balance = 0;
        try (Connection conn = this.connect();
             PreparedStatement prep = conn.prepareStatement(balanceSQL)){
             prep.setString(1, num);
             ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                balance = rs.getInt("balance");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return balance;
    }

    public void transferAction(String fromAccount, String toAccount, int amount) {
        if (returnBalance(fromAccount) < amount) {
            System.out.println("Not enough money!");
        } else {
            subtractFromBalance(fromAccount, amount);
            addToBalance(toAccount, amount);
            System.out.println("Success!");
        }
    }

    public void closeAccount(String account) {
        String deleteSQL = "DELETE FROM card WHERE number = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement prep = conn.prepareStatement(deleteSQL)) {
             prep.setString(1, account);
             prep.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayTable() {
        String selectAll = "SELECT * FROM card;";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(selectAll)){
             ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("number") + "\t" +
                        rs.getString("pin") + "\t" +
                        rs.getInt("balance"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

}

