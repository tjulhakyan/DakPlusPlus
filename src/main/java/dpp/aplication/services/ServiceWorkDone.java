package dpp.aplication.services;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import dpp.aplication.data.WorkDoneDAO;
import dpp.aplication.exseptions.NonUniqueResultException;
import dpp.aplication.model.Employees;
import dpp.aplication.model.InCome;
import dpp.aplication.model.Projects;
import dpp.aplication.model.WorkDone;

public class ServiceWorkDone {
	
	private WorkDoneDAO workDoneDAO=new WorkDoneDAO();
	private ServiceEmployees serviceEmployees= new ServiceEmployees();
	private Employees employees=new Employees();
	private ServiceProjects serviceProjects=new ServiceProjects();
	private Projects projects=new Projects();


	public List<WorkDone> getAllEmployee() throws SQLException {
		return workDoneDAO.getAllLines();
	}
	
	public void addNewWorkDone() throws SQLException, NonUniqueResultException {
		WorkDone workDone = new WorkDone();
		workDone=fillAllInfoOverWorkDone(workDone);
		if(this.workDoneDAO.addOneRow(workDone)) System.out.println("The row was successfully added.");		
	}

	private WorkDone fillAllInfoOverWorkDone(WorkDone workDone) throws SQLException, NonUniqueResultException {
		// get valid 'Id' for EmployeeId
		int id=0;
		boolean validId=false;
		do {
			System.out.print("For Id Employee ");
			id=ExtraFunctional.requestIntInput(1, Integer.MAX_VALUE);
			
			validId=checkingIdForEmployee(id); // if in Employee there is 'id' then we get true
			if(!validId) System.out.println("Ther is no employee with id "+id+". Try again");
			
		} while (!validId);
		workDone.setEmployeeId(id);
		workDone.setEmployees(this.employees);
		
		//get valid 'Id' for projectId
		id=0;
		validId=false;
		do {
			System.out.print("For Id project ");
			id=ExtraFunctional.requestIntInput(1, Integer.MAX_VALUE);
			validId=checkingIdForProject(id); // if in Project there is 'id' then we get true
			if(!validId) System.out.println("Ther is no project with id "+id+". Try again");
			
		} while (!validId);
		workDone.setProjectId(id);
		workDone.setProjects(this.projects);
		
		System.out.println("Write the date (Example 31.12.2000):");
		workDone.setDate(ExtraFunctional.getScannerDatum(this.projects.getDateOfStart()));
		
		System.out.println("For hour ");
		workDone.setHoursWorked(ExtraFunctional.requestIntInput(1, Integer.MAX_VALUE));
		
		System.out.println("Do you want to enter remarks ? (y / n)");
		if (ExtraFunctional.toBeOrNotToBe()) workDone.setRemarks(getScannerText());
		else workDone.setRemarks(null);
		
		return workDone;
	}
	

	private boolean checkingIdForProject(int id) throws SQLException, NonUniqueResultException {
		Optional<Projects> optionalProject=serviceProjects.getProjectById(id);
		if(optionalProject.isPresent()) {
			projects=optionalProject.get();
			return true;
		}
		return false;
	}

	private boolean checkingIdForEmployee(int employeeId) throws SQLException, NonUniqueResultException {
		Optional<Employees> optionalEmployees=serviceEmployees.getEmployeeById(employeeId);
		if(optionalEmployees.isPresent()) {
			employees=optionalEmployees.get();
			return true;
		}
		return false;
	}

	private String getScannerText() {
		System.out.println("Write the description:");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();		
		return input;
	}

	public Optional<WorkDone> getWorkDoneById(int id) throws NonUniqueResultException, SQLException {
		return workDoneDAO.getElementById(id);
	}

	public boolean deleteWorkDoneById(int id) throws SQLException {
		return workDoneDAO.deleteElementById(id);
	}

	public void editEmployeeById(int id) throws SQLException, NonUniqueResultException {
		WorkDone workDone = new WorkDone();
		workDone.setId(id);
		workDone=fillAllInfoOverWorkDone(workDone);
		if(this.workDoneDAO.editRowById(workDone)) System.out.println("The row has been edited successfully..");
	}

	public List<InCome> getRecentProjects() throws SQLException {
		System.out.println("Write the date from which date should be shown (Example 31.01.2000): ");
		Date fromDate = getScannerStartDate();
		return workDoneDAO.getRecentProjects(fromDate);
	}

	private Date getScannerStartDate() {
		Scanner scan=new Scanner(System.in);
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		String input="";
		Date strDate=null;
		do {
			try {
				input=scan.next();
				strDate = sdf.parse(input);
				
				if (strDate.after(new Date())) {
					System.out.println("You can't write a date later than "+(strDate==null? "NULL":sdf.format(new Date()))+", try again:");
					strDate = null;
				}
			} catch (Exception ignore) {
				strDate = null;
				System.out.println("Please enter a valid date.");
			}
		} while (strDate==null);
		
		return strDate;
	}



}
