package dpp.aplication.services;

import java.util.Scanner;

import dpp.aplication.data.WorkDoneDAO;
import dpp.aplication.model.WorkDone;

public class ServiceWorkDone {
	
	private WorkDoneDAO workDoneDAO=new WorkDoneDAO();

	public void addNewWorkDone() {
		WorkDone workDone = new WorkDone();
		workDone=fillAllInfoOverWorkDone(workDone);
		if(this.workDoneDAO.addOneRow(workDone)) System.out.println("The row was successfully added.");		
	}

	private WorkDone fillAllInfoOverWorkDone(WorkDone workDone) {
		workDone.setEmployeeId(ExtarFunctional.requestIntInput(1, Integer.MAX_VALUE));
		workDone.setProjectId(ExtarFunctional.requestIntInput(1, Integer.MAX_VALUE));
//		workDone.setDate(getScannerDatum());
		workDone.setHoursWorked(ExtarFunctional.requestIntInput(1, Integer.MAX_VALUE));
		
		System.out.println("Do you want to enter remarks ? (y / n)");
		if (ExtarFunctional.toBeOrNotToBe()) workDone.setRemarks(getScannerText());
		else workDone.setRemarks(null);
		
		return workDone;
	}
	
	private String getScannerText() {
		System.out.println("Write the description:");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();		
		return input;
	}


}
