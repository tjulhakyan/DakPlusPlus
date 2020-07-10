package dpp.aplication.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface ExtarFunctional {
	
	public static int requestIntInput(int lower, int upper) {
		System.out.print("write a number: ");
		Scanner keyboardInput = new Scanner(System.in);
		int inputNumber = -1;
        do {
            try {
                inputNumber = keyboardInput.nextInt();
            } catch (InputMismatchException e) {
            	inputNumber = -1;
            }
            keyboardInput.nextLine();
            if (inputNumber < lower || inputNumber > upper) System.out.println("You need to write from "+ lower +" to "+upper);
        } while (inputNumber < lower || inputNumber > upper);
		return inputNumber;
	}
	
	
	
	// we ask the user Yes or No, 
	// and if it is Yes, then return true
	// No: is false
	public static boolean toBeOrNotToBe() {
		Scanner scanner = new Scanner(System.in);
		char input=' ';
		do {
			try {
				input = scanner.next().charAt(0);
				if (input == 'y') return true;
				if (input == 'n') return false;
			} catch (Exception e) {
				System.out.println("please enter 'y' or 'n'");
			}
			
			if(input != 'y' || input != 'n') System.out.println("please enter 'y' or 'n'");
		} while (input != 'y' && input != 'n');
		

		return false;
	}
	
	public static boolean checkingTextOnlyLetters(String inputText) {
		Pattern pattern = Pattern.compile("^[a-zA-Z]*$");
		Matcher matcher = pattern.matcher(inputText);
		return matcher.find();
	}
	
	public static Date getScannerDatum(Date dateCheching) {
		Scanner scanner = new Scanner(System.in);
		String input = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		Date date = null;
		do {
			try {
				input = scanner.next();
				date = sdf.parse(input);
				
				if (date.before(dateCheching)) {
					
					System.out.println("You can't write a date earlier than "+(dateCheching==null? "NULL":sdf.format(dateCheching))+", try again:");
					date = null;
				}
			} catch (Exception ignore) {
				date = null;
				System.out.println("Please enter a valid date.");
			}
			
		} while (date == null);
		return date;
	}
}
