package csi2999Project; // ignore the name, i named the package the wrong class code
import java.time.*; // imports functions to gather current date and time
import java.time.format.DateTimeFormatter; // imports function to format date and time in a certain way
import java.util.Arrays;

public class Server {

    public static void main(String[] args) {
        // This code gets the current date, I think it'd be wise to have the
        // client input the user's desired date, and it is sent here where 
        // the distance from the current date is calculated, and then sent back.
        
        LocalDate currentDate = LocalDate.now();
        // Format "Month dd, yyyy" for displaying the date more proper.
        DateTimeFormatter properFormat = DateTimeFormatter.ofPattern("LLLL d, yyyy");
        String properDate = currentDate.format(properFormat);
        int[] intdate = intDate(currentDate);
        int year = intdate[0];
        int month = intdate[1];
        int day = intdate[2];
        //debugging
        System.out.println("Raw date: "+currentDate);
        System.out.println("Date properly formatted: "+properDate);
        System.out.println("Date as array of integers: "+Arrays.toString(intdate));
        System.out.println("Year: "+year);
        System.out.println("Month: "+month);
        System.out.println("Day: "+day);

        // 
    }
    
    public static int[] intDate(LocalDate date) {
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy LL dd"));
        String[] dateArray = formattedDate.split(" ");
        int[] intDate = new int[3];
        for(int i=0;i<dateArray.length;i++) {
            intDate[i] = Integer.parseInt(dateArray[i]);
        }        
        return intDate;
    }

}
