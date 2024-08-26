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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class TaskManager {

    private static Connection getConnection() throws SQLException {
        String connectionString = "jdbc:mysql://localhost:3306/task_manager";
        String user = "root";
        String password = "ayaan2020";
        return DriverManager.getConnection(connectionString, user, password);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Task Manager");
            JButton button = new JButton("Add Task");
            DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Name", "Description"}, 0);

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
                            try (Connection connection = getConnection()) {
                                String name = nameField.getText();
                                String description = descriptionField.getText();
                                String insertQuery = "INSERT INTO details (name, description) VALUES (?, ?)";

                                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                                    preparedStatement.setString(1, name);
                                    preparedStatement.setString(2, description);
                                    preparedStatement.executeUpdate();
                                    tableModel.addRow(new Object[]{name, description});
                                }

                                nameField.setText("");
                                descriptionField.setText("");
                                popupFrame.dispose(); // Close the popup frame after adding the task
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(popupFrame, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);

            frame.setLayout(new FlowLayout());
            frame.add(button);
            frame.add(scrollPane);
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
