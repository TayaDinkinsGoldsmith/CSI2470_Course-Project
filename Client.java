import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Client extends JFrame {
     // Text field for receiving radius
    private JTextField jtf = new JTextField(); 

    // Text area to display contents
    private JTextArea jta = new JTextArea(); 

    // IO streams
    private DataOutputStream toServer;
    private DataInputStream fromServer;
    private Socket socket;

    public static void main(String[] args) {
      new Client();
    }

    public Client() {
      // Panel p to hold the label and text field
      JPanel p = new JPanel();
      p.setLayout(new BorderLayout());

      // Red bar at the top
      JPanel redBar = new JPanel();
      redBar.setBackground(Color.RED);
      redBar.setPreferredSize(new Dimension(500, 50));

      JTextField titleText = new JTextField("Date Countdown!");
      titleText.setHorizontalAlignment(JTextField.CENTER);
      titleText.setEditable(false);
      titleText.setBackground(Color.RED);
      titleText.setForeground(Color.WHITE);
      titleText.setBorder(null);
      titleText.setFont(new Font("Arial", Font.BOLD, 30));
      redBar.setLayout(new BorderLayout());
      redBar.add(titleText, BorderLayout.CENTER);

      setLayout(new BorderLayout());

      // Input panel directly under the red bar
      JPanel inputPanel = new JPanel(new BorderLayout(8, 0));
      inputPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
      inputPanel.add(new JLabel("Enter a Date"), BorderLayout.WEST);
      jtf.setColumns(20);
      
      // Add placeholder text functionality
      final String placeholder = "yyyy-mm-dd";
      jtf.setText(placeholder);
      jtf.setForeground(Color.GRAY);
      
      jtf.addFocusListener(new FocusAdapter() {
        public void focusGained(FocusEvent e) {
          if (jtf.getText().equals(placeholder)) {
            jtf.setText("");
            jtf.setForeground(Color.BLACK);
          }
        }
        public void focusLost(FocusEvent e) {
          if (jtf.getText().isEmpty()) {
            jtf.setText(placeholder);
            jtf.setForeground(Color.GRAY);
          }
        }
      });
      
      inputPanel.add(jtf, BorderLayout.CENTER);

      // Group red bar and input panel into a single top section
      JPanel topPanel = new JPanel(new BorderLayout());
      topPanel.add(redBar, BorderLayout.NORTH);
      topPanel.add(inputPanel, BorderLayout.CENTER);

      add(topPanel, BorderLayout.NORTH);


    // Configure text area and add to center
    jta.setEditable(false);
    jta.setLineWrap(true);
    jta.setWrapStyleWord(true);
    add(new JScrollPane(jta), BorderLayout.CENTER);

     // Display welcome message immediately on start
     jta.append("Welcome to the Date Countdown! Enter a date that you are looking forward to, and the server will return how far away that date is!\n\n");

     jtf.addActionListener(new ButtonListener()); // Register listener
     setTitle("Client");
     setSize(500, 300);
     
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     
     // Add window listener to close socket on exit
     addWindowListener(new WindowAdapter() {
       public void windowClosing(WindowEvent e) {
         try {
           if (socket != null && !socket.isClosed()) {
             socket.close();
           }
         } catch (IOException ex) {
           System.err.println("Error closing socket: " + ex.getMessage());
         }
       }
     });
     
     setVisible(true); // It is necessary to show the frame here!

    try {
       // Create a socket to connect to the server
       socket = new Socket("localhost", 8000);
    

       // Create an input stream to receive data from the server
       fromServer = new DataInputStream(
         socket.getInputStream());

       // Create an output stream to send data to the server
       toServer =
         new DataOutputStream(socket.getOutputStream());
     }
      catch (IOException ex) {
       jta.append(ex.toString() + '\n');
     }
   }

   private class ButtonListener implements ActionListener {
     public void actionPerformed(ActionEvent e) {
       try {
         // Read date string from input
         String dateStr = jtf.getText().trim();
         if (dateStr.isEmpty() || dateStr.equals("yyyy-mm-dd")) {
           jta.append("Error: Please enter a date in yyyy-mm-dd format (e.g., 2025-12-25).\n\n");
           return;
         }

         // Validate date format and parseability
         if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
           jta.append("Error: Invalid date format. Please use yyyy-mm-dd (e.g., 2025-12-25).\n\n");
           return;
         }

         try {
           java.time.LocalDate.parse(dateStr);
         } catch (Exception ex) {
           jta.append("Error: Invalid date. Please enter a valid date in yyyy-mm-dd format (e.g., 2025-12-25).\n\n");
           return;
         }

         // Send date to the server
         toServer.writeUTF(dateStr);
         toServer.flush();

         // Read server response
         String response = fromServer.readUTF();

         // Generate Google Calendar link
         String[] dateParts = dateStr.split("-");
         String calendarLink = "https://calendar.google.com/calendar/r/day/" + 
                               dateParts[0] + "/" + dateParts[1] + "/" + dateParts[2];

         // Display server response
         jta.append("Entered date: " + dateStr + "\n");
         jta.append(response + "\n");
         jta.append("View in Google Calendar: " + calendarLink + "\n\n");
         
       }
       catch (IOException ex) {
         System.err.println(ex);
         jta.append("Communication error: " + ex.getMessage() + "\n\n");
       }
     }
   }
  }




