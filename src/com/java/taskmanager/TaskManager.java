package com.java.taskmanager;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class TaskManager {

    public static void main(String[] args) throws SQLException {

        // db connection
        String connectionString = "jdbc:mysql://localhost:3306/task_manager";
        String user = "root";
        String password = "ayaan2020";
        Connection connection = DriverManager.getConnection(connectionString, user, password);

        // create main frame
        JFrame frame = new JFrame("Task Manager");
        JButton button = new JButton("Click Me");
        frame.add(button);

        // Create table model with column names
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Name", "Description"}, 0);

        // action listener is used, when ok is clicked new frame opens named add details
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame popupFrame = new JFrame("Add Details");
                JPanel panel = new JPanel(new GridLayout(3, 2));

                JLabel nameLabel = new JLabel("Name: ");
                JTextField nameField = new JTextField(20);

                JLabel descriptionLabel = new JLabel("Description: ");
                JTextField descriptionField = new JTextField(20);
                JButton okButton = new JButton("OK");

                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int tasksAdded = 0;
                        String name = nameField.getText();
                        String description = descriptionField.getText();

                        try {
                            String insertQuery = "INSERT INTO details (name, description) VALUES (?, ?)";
                            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                            preparedStatement.setString(1, name);
                            preparedStatement.setString(2, description);
                            preparedStatement.executeUpdate();

                            // Add the new task to the table
                            tableModel.addRow(new Object[]{name, description});

                            tasksAdded++;

                            if (tasksAdded < 5) {
                                nameField.setText("");
                                descriptionField.setText("");
                            } else {
                                connection.close();
                                System.exit(0);
                            }
                        } catch (SQLException e1) {
                            e1.getMessage();
                        }
                    }
                });

                panel.add(nameLabel);
                panel.add(nameField);
                panel.add(descriptionLabel);
                panel.add(descriptionField);
                panel.add(okButton);

                popupFrame.add(panel);
                popupFrame.setSize(300, 150);
                popupFrame.setVisible(true);
            }
        });

        // Create table to display added items
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the table to the main frame
        frame.setLayout(new FlowLayout());
        frame.add(scrollPane);

        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}
