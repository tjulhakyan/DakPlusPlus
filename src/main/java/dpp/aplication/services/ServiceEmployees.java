package dpp.aplication.services;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dpp.aplication.data.EmployeesDAO;
import dpp.aplication.exseptions.NonUniqueResultException;
import dpp.aplication.model.Employees;
import dpp.aplication.model.Projects;

public class ServiceEmployees {
	private EmployeesDAO employeesDAO=new EmployeesDAO();
	
	
	public List<Employees> getAllEmployee() throws SQLException {
		return employeesDAO.getAllLines();
	}
	
	public List<Employees> getEmployeesByLetters(String letters) throws SQLException {
		return employeesDAO.getLinesByLetters(letters);
	}
	
	public List<Employees> getEmployeesWithTodaysBirthdays(int action) throws SQLException {
		return employeesDAO.getLinesWithTodaysBirthdays(action);
	}
	
	public void editEmployeeById(int id) throws SQLException {
		Employees employees = new Employees();
		employees.setId(id);
		employees=getAllInfoOverEmployee(employees);
		if(this.employeesDAO.editRowById(employees)) System.out.println("The row has been edited successfully..");
	}


	public void addNewEmployee() throws SQLException {
		Employees employees = new Employees();
		employees=getAllInfoOverEmployee(employees);
		if(this.employeesDAO.addOneRow(employees)) System.out.println("The row was successfully added.");
	}
	
	
	public Optional<Employees> getEmployeeById(int id) throws SQLException, NonUniqueResultException {
		return employeesDAO.getElementById(id);
	}
	
	public boolean deleteEmployeeById(Employees OpEmp) throws SQLException {
		return employeesDAO.deleteElementById(OpEmp);
	}


	private int getScannerSalary() {
		Scanner scanner = new Scanner(System.in);
        int input = 0;
        do {
            try {
                System.out.print("Write salary: ");
                input = scanner.nextInt();
            } catch (InputMismatchException e) {
                input = 0;
            }
            scanner.nextLine();
            if (input < 1) System.out.println("Salary must be positive. Try again:");
        } while (input < 1);

        return input;
	}


	private Date getScannerDatumWithBeforDate(String sms) {
		System.out.println("Write " + sms + ": For example 31.01.2000");
		Scanner scanner = new Scanner(System.in);
		String input = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		Date date1 = null;
		do {
			try {
				input = scanner.next();
				date1 = sdf.parse(input);

				// we get the last day 18 years ago
				String lastDayOfCurrentYear18YearAgo = "31-12-" + (LocalDateTime.now().getYear() - 18);
				Date date2 = sdf.parse(lastDayOfCurrentYear18YearAgo);

				// checking the difference between 2 dates
				if (date2.before(date1)) {
					System.out.println("A man under the age of 18, try again:");
					date1 = null;
				}
			} catch (ParseException ignore) {
				date1 = null;
				System.out.println("Please enter a valid date of birth.");
			}

		} while (date1 == null);

		return date1;
	}

	private String getScannerText(String sms, int countOfMinSymbols, String typeOfInput) {
		System.out.println("Write the " + sms + ":" + (typeOfInput == "number" ? " For example 012345678" : ""));
		Scanner scanner = new Scanner(System.in);
		String input = "";
		boolean result = false;
		boolean isCorrectInput = false;

		do {
			input = scanner.next();
			// if our input need to be string
			if (typeOfInput == "string")
				isCorrectInput = ExtraFunctional.checkingTextOnlyLetters(input);

			// if our input need to be number
			if (typeOfInput == "number")
				isCorrectInput = checkingTextOnlyNumbers(input);

			if (isCorrectInput && input.length() > countOfMinSymbols)
				result = true;// There is only Alphabets in your input string
			else {
				result = false;// the string contains some number or special char
				System.out.println("Please enter correctly and not less then " + (countOfMinSymbols+1) + " letters.");
			}
		} while (!result);

		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}

	private boolean checkingTextOnlyNumbers(String inputText) {
		boolean isValid = false;
		Pattern pattern = Pattern.compile("^[0-9]*$");
		Matcher matcher = pattern.matcher(inputText);
		if (matcher.find()) {
			if (inputText.charAt(0) == '0' && inputText.length() < 11)
				isValid = true;
		}
		return isValid;
	}
	

	private Employees getAllInfoOverEmployee(Employees employees) {
		employees.setName(getScannerText("Name", 2, "string"));
		employees.setSurname(getScannerText("Surname", 2, "string"));
		System.out.println("Do you want to enter phone number ? (y / n)");
		if (ExtraFunctional.toBeOrNotToBe()) employees.setPhone(getScannerText("phone number", 9, "number"));
		else employees.setPhone(null);
		
		System.out.println("Do you want to enter phone number ICE ? (y / n)");
		if (ExtraFunctional.toBeOrNotToBe()) employees.setPhoneICE(getScannerText("phone number ICE", 9, "number"));
		else employees.setPhone(null);
		
		System.out.println("Do you want to enter date of birthday ? (y / n)");
		if (ExtraFunctional.toBeOrNotToBe()) employees.setBirthday(getScannerDatumWithBeforDate("date of birthday"));
		else employees.setPhone(null);
		
		System.out.println("Do you want to enter salary ? (y / n)");
		if (ExtraFunctional.toBeOrNotToBe()) employees.setSalary(getScannerSalary());
		else employees.setPhone(null);
		return employees;
	}


}
