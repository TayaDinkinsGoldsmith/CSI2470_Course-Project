package csi2470_Project; // ignore the name, i named the package the wrong class code
import java.time.*; // imports functions to gather current date and time
import java.time.format.DateTimeFormatter; // imports function to format date and time in a certain way
import java.util.Arrays;
import java.util.Scanner;

public class server {

    public static void main(String[] args) {
        // This code gets the current date, I think it'd be wise to have the
        // client input the user's desired date, and it is sent here where 
        // the distance from the current date is calculated, and then sent back.
        
        LocalDate currentDate = LocalDate.now(); // Gets current date as LocalDate object
        // Format "Month dd, yyyy" for displaying the date more proper.
        DateTimeFormatter properFormat = DateTimeFormatter.ofPattern("LLLL d, yyyy");
        String properDate = currentDate.format(properFormat);
        
        System.out.println("Hello! This program will calculate how far in time is your date compared to the current.");
        System.out.println("For instance, you will input a date in this format: 2024-09-24 (i.e. yyyy-mm-dd)");
        System.out.println("and we will push out the distance from today in years, months, and days.");
        System.out.println("Today's date is "+properDate+" ("+currentDate+").");
        
        while(true) {
        	try {
        		Scanner scanner = new Scanner(System.in);
            	System.out.print("Enter your date: ");
                String userDate = scanner.nextLine();
                LocalDate parsedUser = LocalDate.parse(userDate); // Converts the String user input into a LocalDate object
                // This is where the "client-side" code should end.
                // The parsedUser should be sent to the server, in which the dateDifference is also calculated at the server.
                // the printOutput can instead return the calculation from the server to the client, but until the client-server
                // code is done, this is what I have.
                int[] dateDiff = dateDifference(currentDate,parsedUser);
                printOutput(parsedUser,dateDiff[0],dateDiff[1],dateDiff[2]);
        	} catch (Exception e) {
        		System.out.println("Not a valid date. Recall that you should enter your date as yyyy-mm-dd.");
        	}
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
    
    // Prints the output in a more rudimentary fashion. Can be stored in the client instead.
    public static void printOutput(LocalDate inputDate, int years, int months, int days) {
    	String formattedInput = inputDate.format(DateTimeFormatter.ofPattern("LLLL d, yyyy"));
    	if(!inputDate.equals(LocalDate.now())) {
        	System.out.println(formattedInput+" is approximately:");
    		if(years>0) {
        		if(years>1) {
        			System.out.println(years+" years");
        		} else {
        			System.out.println(years+" year");
        		}
        	}
        	if(months>0) {
        		if(months>1) {
        			System.out.println(months+" months");
        		} else {
        			System.out.println(months+" month");
        		}
        	}
        	if(days>0) {
        		if(days>1) {
        			System.out.println(days+" days");
        		} else {
        			System.out.println(days+" day");
        		}
        	}
        	if(inputDate.compareTo(LocalDate.now())>0) {
        		System.out.println("away.");
        	} else if(inputDate.compareTo(LocalDate.now())<0) {
        		System.out.println("ago.");
        	}
    	} else {
    		System.out.println(formattedInput+" is today!");
    	}
    	
    }
}
