package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CoffeeShopApp extends JFrame {

    private JTextField coffeeField;
    private JTextArea orderArea;
    private Connection connection;

    public CoffeeShopApp() {
        // Set up the frame
        setTitle("Coffee Shop");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel for coffee input and buttons
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel coffeeLabel = new JLabel("Enter coffee name:");
        coffeeField = new JTextField(15);
        inputPanel.add(coffeeLabel);
        inputPanel.add(coffeeField);

        // Panel for buttons with GridLayout
        JPanel buttonPanel = new JPanel(new GridLayout(1, 0, 10, 0)); // 1 row, variable columns, with 10px horizontal gap
        String[] coffeeTypes = {"Espresso", "Latte", "Cappuccino", "Americano", "Mocha"};
        for (String type : coffeeTypes) {
            JButton button = new JButton(type);
            button.addActionListener(e -> placeOrder(type));
            buttonPanel.add(button);
        }

        // Order area
        orderArea = new JTextArea(10, 20);
        orderArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderArea);

        // Add components to main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Establish database connection
        connectToDatabase();
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:postgresql://localhost:5432/{databse_name}";
            String user = "postgres";
            String password = "{your_password}";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
        }
    }

    private void placeOrder(String coffeeType) {
        String query = "INSERT INTO orders (coffee_type) VALUES (?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, coffeeType);
            pstmt.executeUpdate();
            orderArea.append("Ordered: " + coffeeType + "\n");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Order failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CoffeeShopApp frame = new CoffeeShopApp();
            frame.setVisible(true);
        });
    }
}

