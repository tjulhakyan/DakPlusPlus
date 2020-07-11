package dpp.aplication.services;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import dpp.aplication.data.ProjectsDAO;
import dpp.aplication.exseptions.NonUniqueResultException;
import dpp.aplication.model.Projects;

public class ServiceProjects {
	
	private ProjectsDAO projectsDAO=new ProjectsDAO();
	
	public void addNewproject() throws SQLException{
		Projects projects = new Projects();
		projects=fillAllInfoOverProjects(projects);
		if(this.projectsDAO.addOneRow(projects)) System.out.println("The row was successfully added.");		
		System.out.println(projects);
	}

	private Projects fillAllInfoOverProjects(Projects projects) {
		
		System.out.println("Write the start date (example 31.12.2020):");
		Instant before = Instant.now().minus(Duration.ofDays(1));
		Date dateBefore = Date.from(before);
		projects.setDateOfStart(ExtraFunctional.getScannerDatum(dateBefore));
		
		System.out.println("Do you want to enter description ? (y / n)");
		if(ExtraFunctional.toBeOrNotToBe()) projects.setDescription(getScannerText());
		else projects.setDescription(null);
		
		System.out.print("Price. ");
		projects.setPrice(ExtraFunctional.requestIntInput(1, Integer.MAX_VALUE));

		System.out.println("Write the end date (example 31.12.2020):");
		projects.setEndDate(ExtraFunctional.getScannerDatum(projects.getDateOfStart()));
		
		return projects;
	}


	private String getScannerText() {
		System.out.println("Write the description:");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();		
		return input;
	}

	public Optional<Projects> getProjectById(int id) throws SQLException, NonUniqueResultException {
		return projectsDAO.getElementById(id);
	}

	public boolean deleteProjectById(int id) throws SQLException {
		return projectsDAO.deleteElementById(id);
	}

	public List<Projects> getProjectsDate(int action) throws SQLException {
		return projectsDAO.getLinesWithDate(action);
	}
	
}