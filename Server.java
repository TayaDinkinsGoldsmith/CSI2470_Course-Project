//package csi2470_Project; // ignore the name, i named the package the wrong class code
import java.time.*; // imports functions to gather current date and time
import java.time.format.DateTimeFormatter; // imports function to format date and time in a certain way
import java.io.*;
 import java.net.*;
 import java.util.*;
 import java.awt.*;
 import javax.swing.*;

public class Server extends JFrame {
    // Text area for displaying contents
  private JTextArea jta = new JTextArea();
	private JLabel statusLabel = new JLabel("Waiting for client...");

  public static void main(String[] args) {
  new Server();
  }

public Server() {

	// Top red bar with title
	JPanel redBar = new JPanel(new BorderLayout());
	redBar.setBackground(Color.RED);
	redBar.setPreferredSize(new Dimension(500, 50));
	JTextField titleText = new JTextField("Date Countdown Server");
	titleText.setHorizontalAlignment(JTextField.CENTER);
	titleText.setEditable(false);
	titleText.setBackground(Color.RED);
	titleText.setForeground(Color.WHITE);
	titleText.setBorder(null);
	titleText.setFont(new Font("Arial", Font.BOLD, 28));
	redBar.add(titleText, BorderLayout.CENTER);

	// Input/status panel under red bar
	JPanel infoPanel = new JPanel(new BorderLayout(8, 0));
	infoPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
	infoPanel.add(new JLabel("Server Status:"), BorderLayout.WEST);
	infoPanel.add(statusLabel, BorderLayout.CENTER);

	JPanel topPanel = new JPanel(new BorderLayout());
	topPanel.add(redBar, BorderLayout.NORTH);
	topPanel.add(infoPanel, BorderLayout.CENTER);

	// Center text area
	jta.setEditable(false);
	jta.setLineWrap(true);
	jta.setWrapStyleWord(true);

	setLayout(new BorderLayout());
	add(topPanel, BorderLayout.NORTH);
	add(new JScrollPane(jta), BorderLayout.CENTER);

	setTitle("Server");
	setSize(500, 300);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

	setVisible(true); // Show frame

	  try{

		// Create a server socket
        final ServerSocket serverSocket = new ServerSocket(8000);
        
        // Add window listener to close server socket on exit
        addWindowListener(new java.awt.event.WindowAdapter() {
          public void windowClosing(java.awt.event.WindowEvent e) {
            try {
              if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                jta.append("Server socket closed.\n");
              }
            } catch (IOException ex) {
              System.err.println("Error closing server socket: " + ex.getMessage());
            }
          }
        });
        jta.append("Server started at " + new Date() + '\n');

				// Outer loop - keep server running to accept multiple clients
				while (true) {
					// Listen for a connection request
					Socket socket = serverSocket.accept();
					statusLabel.setText("Client connected");

					// Create data input and output streams
					DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
					DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

					jta.append("Client connected: " + socket.getRemoteSocketAddress() + "\n");

					// Inner loop - process dates sent from this specific client
					while (true) {
						try {
							String userDate = inputFromClient.readUTF();
							LocalDate parsedUser = LocalDate.parse(userDate);

							LocalDate currentDate = LocalDate.now();
							int[] dateDiff = dateDifference(currentDate, parsedUser);

							// Compose a response message
							String response = formatDifference(parsedUser, dateDiff[0], dateDiff[1], dateDiff[2]);

							// Log to server UI
							jta.append("Received date: " + userDate + " -> " + response.replace("\n", " ") + "\n");

							// Send response back to client
							outputToClient.writeUTF(response);
							outputToClient.flush();
						} catch (EOFException e) {
							// Client closed connection
							jta.append("Client disconnected.\n");
							statusLabel.setText("Waiting for client...");
							break;
						} catch (Exception e) {
							// If parsing failed or other error
							jta.append("Error processing input: " + e.getMessage() + "\n");
							statusLabel.setText("Waiting for client...");
							break;
						}
					}
					
					// Close socket and loop back to accept next client
					socket.close();
				}
    }
	catch(Exception ex) {
		System.err.println(ex);
	  }
	}
    
    // Calculates the difference between today and the inputted date, accounting for years, months, and days separately in
    // the returned int[] array. Can be stored in the server, and the returned array can be the data sent back from the server.
    public static int[] dateDifference(LocalDate date1, LocalDate date2) {
    	int dayDiff = Math.abs(date1.getDayOfYear() - date2.getDayOfYear()); //difference in days
    	
    	int[] dateDiff = new int[3];
    	dateDiff[0] = Math.abs(date1.getYear() - date2.getYear()); //difference in years
    	dateDiff[1] = (int)(dayDiff/30); //difference in months (on average)
    	dateDiff[2] = dayDiff%30; //difference in days after months
    	
    	return dateDiff;
    }
    
	// Formats the output string for the difference
	public static String formatDifference(LocalDate inputDate, int years, int months, int days) {
    	String formattedInput = inputDate.format(DateTimeFormatter.ofPattern("LLLL d, yyyy"));
		StringBuilder sb = new StringBuilder();
		if(!inputDate.equals(LocalDate.now())) {
			sb.append(formattedInput).append(" is approximately:\n");
			if(years>0) sb.append(years).append(years>1?" years\n":" year\n");
			if(months>0) sb.append(months).append(months>1?" months\n":" month\n");
			if(days>0) sb.append(days).append(days>1?" days\n":" day\n");
			if(inputDate.compareTo(LocalDate.now())>0) sb.append("away.");
			else if(inputDate.compareTo(LocalDate.now())<0) sb.append("ago.");
		} else {
			sb.append(formattedInput).append(" is today!");
		}
		return sb.toString();
    }
}
