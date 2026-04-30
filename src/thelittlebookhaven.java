import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Main application class
public class thelittlebookhaven {
    private JFrame frame;
    private User currentUser;
    private List<Book> books = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private final String BOOKS_FILE = "books.txt";
    private final String USERS_FILE = "users.txt";
    private final String CATEGORIES_FILE = "categories.txt";

    public thelittlebookhaven() {
        frame = new JFrame("The Little Book Haven System");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        loadBooks();
        loadUsers();
        loadCategories();
        showLoginScreen();
    }

    private void loadBooks() {
    try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 6) {
                String name = parts[0];
                String author = parts[1];
                String category = parts[2];
                String genre = parts[3];
                double price = Double.parseDouble(parts[4]);
                int quantity = Integer.parseInt(parts[5]);
                books.add(new Book(name, author, category, genre, price, quantity));
            }
        }
    } catch (IOException e) {
        System.out.println("No previous books found or error reading file.");
    }
}


    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    String role = parts[2];
                    if (role.equals("manager")) {
                        users.add(new Manager(username, password));
                    } else {
                        users.add(new Cashier(username, password));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("No previous users found or error reading file.");
            loadDefaultUsers();
        }
    }

    private void loadDefaultUsers() {
        users.add(new Manager("Manager", "Manager@123"));
        users.add(new Cashier("Cashier", "Cashier@123"));
        saveUsers();
    }

    private void loadCategories() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CATEGORIES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                categories.add(line);
            }
        } catch (IOException e) {
            System.out.println("No previous categories found or error reading file.");
            loadDefaultCategories();
        }
    }

    private void loadDefaultCategories() {
        categories.add("Fiction");
        categories.add("Non-Fiction");
        categories.add("Mystery");
        categories.add("Science Fiction");
        categories.add("Historical");
        categories.add("Fantasy");
        saveCategories();
    }

    private void saveBooks() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
        for (Book book : books) {
            writer.write(book.getName() + "," + book.getAuthor() + "," +
                         book.getCategory() + "," + book.getGenre() + "," +
                         book.getPrice() + "," + book.getQuantity());
            writer.newLine();
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(frame, "Error saving books: " + e.getMessage());
    }
}


    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getRole());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving users: " + e.getMessage());
        }
    }

    private void saveCategories() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CATEGORIES_FILE))) {
            for (String category : categories) {
                writer.write(category);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving categories: " + e.getMessage());
        }
    }

    private void showLoginScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("The Little Book Haven", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridy =1;
        panel.add(Box.createVerticalStrut(20), gbc);


        JLabel userLabel = new JLabel("Username:");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(userLabel, gbc);
        
        JTextField userText = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(userText, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(passwordLabel, gbc);
        
        JPasswordField passwordText = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(passwordText, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(loginButton, gbc);
        
        loginButton.addActionListener(e -> login(userText.getText(), new String(passwordText.getPassword())));

        frame.revalidate();
        frame.repaint();
    }

    private void login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                JOptionPane.showMessageDialog(frame, "Welcome to The Little Book Haven");
                showMainMenu();
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Invalid username or password.");
    }

    private void showMainMenu() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel welcomeLabel = new JLabel("Welcome " + currentUser.getUsername(), JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(welcomeLabel, gbc);

        JButton viewBookdetailsButton = new JButton("View Book Details");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(viewBookdetailsButton, gbc);
        viewBookdetailsButton.addActionListener(e -> viewBookdetail());

        JButton addNewbookButton = new JButton("Add New Book");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(addNewbookButton, gbc);
        addNewbookButton.addActionListener(e -> addNewbook());

        JButton transactionButton = new JButton("Make Transaction");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(transactionButton, gbc);
        transactionButton.addActionListener(e -> makeTransaction());

        if (currentUser instanceof Manager) {
            JButton createAccountButton = new JButton("Create Account");
            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
            panel.add(createAccountButton, gbc);
            createAccountButton.addActionListener(e -> createAccount());
        }

        JButton logoutButton = new JButton("Logout");
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(logoutButton, gbc);
        logoutButton.addActionListener(e -> {
            currentUser = null;
            showLoginScreen();
        });

        frame.revalidate();
        frame.repaint();
    }

   private void addNewbook() {
         JDialog dialog = new JDialog(frame, "Add New Book", true);
         dialog.setSize(500, 400); 
         dialog.setLocationRelativeTo(frame);
         dialog.setLayout(new BorderLayout());

    JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JLabel nameLabel = new JLabel("Book Name:");
    panel.add(nameLabel);
    JTextField nameText = new JTextField();
    panel.add(nameText);

    JLabel authorLabel = new JLabel("Author:");
    panel.add(authorLabel);
    JTextField authorText = new JTextField();
    panel.add(authorText);

    JLabel categoryLabel = new JLabel("Category:");
    panel.add(categoryLabel);
    JComboBox<String> categoryCombo = new JComboBox<>(getCategoryArray());
    panel.add(categoryCombo);

    JLabel genreLabel = new JLabel("Genre:");
    panel.add(genreLabel);
    JTextField genreText = new JTextField();
    panel.add(genreText);

    JLabel priceLabel = new JLabel("Price:");
    panel.add(priceLabel);
    JTextField priceText = new JTextField();
    panel.add(priceText);

    JLabel quantityLabel = new JLabel("Quantity:");
    panel.add(quantityLabel);
    JTextField quantityText = new JTextField();
    panel.add(quantityText);

    JPanel buttonPanel = new JPanel();
    JButton addButton = new JButton("Add Book");
    JButton cancelButton = new JButton("Cancel");

    addButton.addActionListener(e -> {
        String name = nameText.getText().trim();
        String author = authorText.getText().trim();
        String category = categoryCombo.getSelectedItem().toString();
        String genre = genreText.getText().trim();
        String priceInput = priceText.getText().trim();
        String quantityInput = quantityText.getText().trim();

        if (name.isEmpty() || author.isEmpty() || genre.isEmpty() || priceInput.isEmpty() || quantityInput.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Please fill in all fields.");
            return;
        }

        double price;
        int quantity;
        try {
            price = Double.parseDouble(priceInput);
            quantity = Integer.parseInt(quantityInput);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "Please enter valid numeric values for price and quantity.");
            return;
        }

        Book newBook = new Book(name, author, category, genre, price, quantity);
        books.add(newBook);
        saveBooks();
        JOptionPane.showMessageDialog(dialog, "Book added successfully!");
        dialog.dispose();
    });

    cancelButton.addActionListener(e -> dialog.dispose());

    buttonPanel.add(addButton);
    buttonPanel.add(cancelButton);

    dialog.add(panel, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);
    dialog.setVisible(true);
}


    private void viewBookdetail() {
        JPanel panel = new JPanel(new BorderLayout());
        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);

    
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel categoryLabel = new JLabel("Filter by Category:");
        topPanel.add(categoryLabel);

        List<String> allCategories = new ArrayList<>(categories);
        allCategories.add(0, "All Categories");
        JComboBox<String> categoryCombo = new JComboBox<>(allCategories.toArray(new String[0]));
        topPanel.add(categoryCombo);

        panel.add(topPanel, BorderLayout.NORTH);

        // Center panel with book details
        JTextArea booksArea = new JTextArea(15, 50);
        booksArea.setEditable(false);
        booksArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(booksArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Update books display based on category selection
        Runnable updateBooksDisplay = () -> {
        String selectedCategory = (String) categoryCombo.getSelectedItem();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-25s %-20s %-15s %-15s %-10s %-8s\n", 
        "Book Title", "Author", "Category", "Genre", "Price", "Qty"));
        sb.append("-------------------------------------------------------------------------------------------------------------\n");
    
        for (Book book : books) {
        if (selectedCategory.equals("All Categories") || book.getCategory().equals(selectedCategory)) {
            sb.append(String.format("%-25s %-20s %-15s %-15s $%-9.2f %-8d\n", 
                book.getName(), book.getAuthor(), book.getCategory(), book.getGenre(),
                book.getPrice(), book.getQuantity()));
          }
        }
           booksArea.setText(sb.toString());
        };


        categoryCombo.addActionListener(e -> updateBooksDisplay.run());

        // Bottom panel with back button
        JPanel bottomPanel = new JPanel();
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> showMainMenu());
        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Initial display
        updateBooksDisplay.run();

        frame.revalidate();
        frame.repaint();
    }

    private void makeTransaction() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Make Transaction", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JLabel bankLabel = new JLabel("Bank Name:");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(bankLabel, gbc);
        
        JTextField bankText = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(bankText, gbc);

        JLabel branchLabel = new JLabel("Bank Branch:");
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(branchLabel, gbc);
        
        JTextField branchText = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(branchText, gbc);

        JLabel accountNumberLabel = new JLabel("Account Number:");
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(accountNumberLabel, gbc);
        
        JTextField accountNumberText = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(accountNumberText, gbc);

        JLabel accountNameLabel = new JLabel("Account Holder Name:");
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(accountNameLabel, gbc);
        
        JTextField accountNameText = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(accountNameText, gbc);

        JLabel amountLabel = new JLabel("Amount:");
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(amountLabel, gbc);
        
        JTextField amountText = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 5;
        panel.add(amountText, gbc);

        JPanel buttonPanel = new JPanel();
        JButton transferButton = new JButton("Transfer");
        JButton backButton = new JButton("Back");
        
        transferButton.addActionListener(e -> {
            String bank = bankText.getText().trim();
            String branch = branchText.getText().trim();
            String accountNumber = accountNumberText.getText().trim();
            String accountName = accountNameText.getText().trim();
            String amountInput = amountText.getText().trim();

            if (bank.isEmpty() || branch.isEmpty() || accountNumber.isEmpty() || accountName.isEmpty() || amountInput.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountInput);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(frame, "Please enter a positive amount.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid amount.");
                return;
            }

            // Confirmation popup
            int confirm = JOptionPane.showConfirmDialog(frame, 
                "Please confirm the transaction details:\n\n" +
                "Bank: " + bank + "\n" +
                "Branch: " + branch + "\n" +
                "Account Number: " + accountNumber + "\n" +
                "Account Holder: " + accountName + "\n" +
                "Amount: $" + String.format("%.2f", amount) + "\n\n" +
                "Proceed with transfer?",
                "Confirm Transaction",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(frame, 
                    "Transaction successful!\n\n" +
                    "Bank: " + bank + "\n" +
                    "Branch: " + branch + "\n" +
                    "Account Number: " + accountNumber + "\n" +
                    "Account Holder: " + accountName + "\n" +
                    "Amount: $" + String.format("%.2f", amount));
                
                showMainMenu();
            }
        });

        backButton.addActionListener(e -> showMainMenu());

        buttonPanel.add(transferButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        frame.revalidate();
        frame.repaint();
    }

    private void createAccount() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Create New Account", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(usernameLabel, gbc);
        
        JTextField usernameText = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(usernameText, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(passwordLabel, gbc);
        
        JPasswordField passwordText = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(passwordText, gbc);

        JLabel roleLabel = new JLabel("Role: Cashier");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(roleLabel, gbc);

        JPanel buttonPanel = new JPanel();
        JButton createButton = new JButton("Create Account");
        JButton backButton = new JButton("Back");

        createButton.addActionListener(e -> {
            String username = usernameText.getText().trim();
            String password = new String(passwordText.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                return;
            }

            // Check if username already exists
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    JOptionPane.showMessageDialog(frame, "Username already exists. Please choose a different one.");
                    return;
                }
            }

            User newUser = new Cashier(username, password);
            
            users.add(newUser);
            saveUsers();
            JOptionPane.showMessageDialog(frame, "Cashier account created successfully!");
            showMainMenu();
        });

        backButton.addActionListener(e -> showMainMenu());

        buttonPanel.add(createButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        frame.revalidate();
        frame.repaint();
    }

    private String[] getCategoryArray() {
        return categories.toArray(new String[0]);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            thelittlebookhaven app = new thelittlebookhaven();
            app.frame.setVisible(true);
        });
    }
}