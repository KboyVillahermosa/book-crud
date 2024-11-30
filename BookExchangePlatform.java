import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

class Book {
    private int id;
    private String title;
    private String author;
    private String genre;
    private String condition;
    private String imagePath;

    public Book(int id, String title, String author, String genre, String condition, String imagePath) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.condition = condition;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Book ID: " + id + "\n" +
               "Title: " + title + "\n" +
               "Author: " + author + "\n" +
               "Genre: " + genre + "\n" +
               "Condition: " + condition + "\n";
    }
}

public class BookExchangePlatform {
    private static final ArrayList<Book> bookList = new ArrayList<>();
    private static int bookIdCounter = 1;

    public static void main(String[] args) {
        showLandingPage();
    }

    private static void showLandingPage() {
        // Create a JPanel with a BoxLayout for the landing page
        JPanel landingPanel = new JPanel();
        landingPanel.setLayout(new BoxLayout(landingPanel, BoxLayout.Y_AXIS));
        
        // Title of the system
        JLabel titleLabel = new JLabel("Welcome to the Book Exchange Platform");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.DARK_GRAY);

    

        // Add an optional background image for the landing page
        JLabel imageLabel = new JLabel(new ImageIcon("pic.jpg"));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // "Get Started" button
        JButton getStartedButton = new JButton("Get Started");
        getStartedButton.setFont(new Font("Arial", Font.PLAIN, 18));
        getStartedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        getStartedButton.setPreferredSize(new Dimension(200, 40));
        getStartedButton.addActionListener(e -> startCrudOperations());

        // Add components to the landing panel
        landingPanel.add(Box.createRigidArea(new Dimension(0, 50))); // Padding from top
        landingPanel.add(titleLabel);
        landingPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Space between title and description
        landingPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Space between description and image
        landingPanel.add(imageLabel);
        landingPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Space between image and button
        landingPanel.add(getStartedButton);

        // Set a background color for the panel
        landingPanel.setBackground(new Color(240, 240, 240));

        // Show the landing page in a dialog box
        JOptionPane.showMessageDialog(null, landingPanel, "Welcome to Book Exchange Platform", JOptionPane.PLAIN_MESSAGE);
    }

    private static void startCrudOperations() {
        while (true) {
            String[] options = {"Add Book", "View Books", "Update Book", "Delete Book", "Exit"};
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Choose an operation:",
                    "Book Exchange Platform",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == 0) {
                addBook();
            } else if (choice == 1) {
                viewBooks();
            } else if (choice == 2) {
                updateBook();
            } else if (choice == 3) {
                deleteBook();
            } else if (choice == 4 || choice == JOptionPane.CLOSED_OPTION) {
                JOptionPane.showMessageDialog(null, "Goodbye!");
                break;
            }
        }
    }

    private static void addBook() {
        String title = JOptionPane.showInputDialog("Enter Book Title:");
        if (title == null) return;

        String author = JOptionPane.showInputDialog("Enter Author:");
        if (author == null) return;

        String genre = JOptionPane.showInputDialog("Enter Genre:");
        if (genre == null) return;

        String condition = JOptionPane.showInputDialog("Enter Condition (e.g., New, Good, Fair):");
        if (condition == null) return;

        // Let the user select an image
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Book Image");
        int result = fileChooser.showOpenDialog(null);
        String imagePath = null;
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            imagePath = selectedFile.getAbsolutePath();
        }

        bookList.add(new Book(bookIdCounter++, title, author, genre, condition, imagePath));
        JOptionPane.showMessageDialog(null, "Book added successfully!");
    }

    private static void viewBooks() {
        if (bookList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No books available.");
            return;
        }

        // Creating a JList to display book titles with their IDs
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Book book : bookList) {
            listModel.addElement("ID: " + book.getId() + " | Title: " + book.getTitle());
        }

        JList<String> bookListView = new JList<>(listModel);
        bookListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // Only one book can be selected
        JScrollPane scrollPane = new JScrollPane(bookListView);  // Make the list scrollable

        // Show the list of books in a dialog
        int option = JOptionPane.showConfirmDialog(null, scrollPane, "Choose a Book to View", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            // Get selected book
            String selectedValue = bookListView.getSelectedValue();
            if (selectedValue != null) {
                // Extract the book ID from the selected value
                int bookId = Integer.parseInt(selectedValue.split(" ")[1]);

                // Find the selected book
                Optional<Book> selectedBookOpt = bookList.stream().filter(b -> b.getId() == bookId).findFirst();
                if (selectedBookOpt.isPresent()) {
                    Book selectedBook = selectedBookOpt.get();
                    // Show detailed information of the selected book
                    JPanel panel = new JPanel(new GridBagLayout());
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.insets = new Insets(10, 10, 10, 10);

                    // Panel for the image
                    JPanel imagePanel = new JPanel();
                    JLabel imageLabel = new JLabel();
                    if (selectedBook.getImagePath() != null) {
                        ImageIcon bookImage = new ImageIcon(new ImageIcon(selectedBook.getImagePath()).getImage()
                                .getScaledInstance(150, 200, Image.SCALE_SMOOTH));  // Resize image
                        imageLabel.setIcon(bookImage);
                    } else {
                        imageLabel.setText("No Image Available");
                        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    }

                    imagePanel.add(imageLabel);

                    // Book details panel
                    JPanel detailsPanel = new JPanel();
                    detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

                    detailsPanel.add(new JLabel("Title: " + selectedBook.getTitle()));
                    detailsPanel.add(new JLabel("Author: " + selectedBook.getAuthor()));
                    detailsPanel.add(new JLabel("Genre: " + selectedBook.getGenre()));
                    detailsPanel.add(new JLabel("Condition: " + selectedBook.getCondition()));

                    // Add to the GridBagLayout
                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    panel.add(imagePanel, gbc);
                    gbc.gridx = 1;
                    panel.add(detailsPanel, gbc);

                    // Show details in dialog
                    JOptionPane.showMessageDialog(null, panel, "Book Details", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    private static void updateBook() {
        String bookIdStr = JOptionPane.showInputDialog("Enter Book ID to update:");
        if (bookIdStr == null) return;

        int bookId = Integer.parseInt(bookIdStr);
        Optional<Book> bookToUpdateOpt = bookList.stream().filter(b -> b.getId() == bookId).findFirst();

        if (bookToUpdateOpt.isPresent()) {
            Book bookToUpdate = bookToUpdateOpt.get();

            String title = JOptionPane.showInputDialog("Enter New Book Title:", bookToUpdate.getTitle());
            if (title != null) bookToUpdate.setTitle(title);

            String author = JOptionPane.showInputDialog("Enter New Author:", bookToUpdate.getAuthor());
            if (author != null) bookToUpdate.setAuthor(author);

            String genre = JOptionPane.showInputDialog("Enter New Genre:", bookToUpdate.getGenre());
            if (genre != null) bookToUpdate.setGenre(genre);

            String condition = JOptionPane.showInputDialog("Enter New Condition:", bookToUpdate.getCondition());
            if (condition != null) bookToUpdate.setCondition(condition);

            // Option to update the image
            int option = JOptionPane.showConfirmDialog(null, "Would you like to update the book image?", "Update Image", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select New Book Image");
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    bookToUpdate.setImagePath(selectedFile.getAbsolutePath());
                }
            }

            JOptionPane.showMessageDialog(null, "Book updated successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "Book with ID " + bookId + " not found.");
        }
    }

    private static void deleteBook() {
        String bookIdStr = JOptionPane.showInputDialog("Enter Book ID to delete:");
        if (bookIdStr == null) return;

        int bookId = Integer.parseInt(bookIdStr);
        Optional<Book> bookToDeleteOpt = bookList.stream().filter(b -> b.getId() == bookId).findFirst();

        if (bookToDeleteOpt.isPresent()) {
            bookList.remove(bookToDeleteOpt.get());
            JOptionPane.showMessageDialog(null, "Book deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "Book with ID " + bookId + " not found.");
        }
    }
}
