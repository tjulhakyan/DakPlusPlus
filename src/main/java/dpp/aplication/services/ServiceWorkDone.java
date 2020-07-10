package dpp.aplication.services;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

import dpp.aplication.data.WorkDoneDAO;
import dpp.aplication.exseptions.NonUniqueResultException;
import dpp.aplication.model.Employees;
import dpp.aplication.model.Projects;
import dpp.aplication.model.WorkDone;

public class ServiceWorkDone {
	
	private WorkDoneDAO workDoneDAO=new WorkDoneDAO();
	private ServiceEmployees serviceEmployees= new ServiceEmployees();
	private Employees employees=new Employees();
	private ServiceProjects serviceProjects=new ServiceProjects();
	private Projects projects=new Projects();

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
			id=ExtarFunctional.requestIntInput(1, Integer.MAX_VALUE);
			
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
			id=ExtarFunctional.requestIntInput(1, Integer.MAX_VALUE);
			validId=checkingIdForProject(id); // if in Project there is 'id' then we get true
			if(!validId) System.out.println("Ther is no project with id "+id+". Try again");
			
		} while (!validId);
		workDone.setProjectId(id);
		workDone.setProjects(this.projects);
		
		System.out.println("Write the date (Example 31.12.2000):");
		workDone.setDate(ExtarFunctional.getScannerDatum(this.projects.getDateOfStart()));
		
		System.out.println("For hour ");
		workDone.setHoursWorked(ExtarFunctional.requestIntInput(1, Integer.MAX_VALUE));
		
		System.out.println("Do you want to enter remarks ? (y / n)");
		if (ExtarFunctional.toBeOrNotToBe()) workDone.setRemarks(getScannerText());
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


}
