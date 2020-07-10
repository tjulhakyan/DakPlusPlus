package dpp.aplication.model;

import java.util.Date;

public class WorkDone {
	private int id;
	private int employeeId; 
	private Employees employees=new Employees();
	private int projectId; 
	private Projects projects=new Projects();
	private Date date; 
	private double hoursWorked;
	private String remarks;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public Employees getEmployees() {
		return employees;
	}
	public Employees setEmployees(Employees employees) {
		this.employees = employees;
		return this.employees;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public Projects getProjects() {
		return projects;
	}
	public Projects setProjects(Projects projects) {
		this.projects = projects;
		return this.projects;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getHoursWorked() {
		return hoursWorked;
	}
	public void setHoursWorked(double hoursWorked) {
		this.hoursWorked = hoursWorked;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public String toString() {
		return "WorkDone [id=" + id +", employeeId=" + employeeId + ", employees=" + employees.getName() +" "+ employees.getSurname() 
				+ ", projectId=" + projectId
				+ ", projects(StartDate)=" + projects.getDateOfStart()
				+ ", projects(EndDate)="+ projects.getEndDate()
				+ ", projects(price)="+ projects.getPrice()
				+ ", date=" + date	+ ", hoursWorked=" + hoursWorked + ", remarks=" + remarks
				+ "]";
	}
	
}
