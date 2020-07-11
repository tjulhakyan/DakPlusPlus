package dpp.aplication;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import dpp.aplication.exseptions.NonUniqueResultException;
import dpp.aplication.model.Employees;
import dpp.aplication.model.InCome;
import dpp.aplication.model.Projects;
import dpp.aplication.model.WorkDone;
import dpp.aplication.services.ExtraFunctional;
import dpp.aplication.services.ServiceEmployees;
import dpp.aplication.services.ServiceProjects;
import dpp.aplication.services.ServiceWorkDone;

public class DppApp {

	public static void main(String[] args) {
		int chooseManu = 0;
		int action = 0;
		do {
			printManu();

			// choose the table
			chooseManu = ExtraFunctional.requestIntInput(0, 3);

			if (chooseManu != 0) {
				if (chooseManu == 1) {
					// choose the action for Employees
					actionForEmployees();
					action = ExtraFunctional.requestIntInput(0, 8);

				}
				if (chooseManu == 2) {
					// choose the action for Projects
					actionForProjects();
					action = ExtraFunctional.requestIntInput(0, 5);
				}

				if (chooseManu == 3) {
					// choose the action for Projects
					actionForWorkDone();
					action = ExtraFunctional.requestIntInput(0, 7);
				}
				if (action != 0 && action != 1)
					doAction(chooseManu, action);
			}
		} while (chooseManu != 0 && action != 0);

		System.out.println("Thanks for using IntecBrussel's program.");
	}

	private static void doAction(int chooseManu, int action) {
		if (chooseManu == 1) {
			ServiceEmployees serviceEmployees = new ServiceEmployees();

			// Show all Employees.
			if (action == 2) {
				List<Employees> employees = null;
				try {
					employees = serviceEmployees.getAllEmployee();
					employees.forEach(System.out::println);
				} catch (SQLException e) {
					System.out.println("Problems with database :( ...");
					e.printStackTrace();
				}
			}

			// Add Employee.
			if (action == 3) {
				try {
					serviceEmployees.addNewEmployee();
				} catch (SQLException e) {
					System.out.println("Problems with database :( ...");
					e.printStackTrace();
				}
			}

			// Edit Employee by id.
			if (action == 4) {
				try {
					System.out.print("For id ");
					int id = ExtraFunctional.requestIntInput(1, Integer.MAX_VALUE);
					serviceEmployees.editEmployeeById(id);
				} catch (Exception e) {
					System.out.println("Problems with database :( ...");
					e.printStackTrace();
				}
			}

			// Delete Employee by id.
			if (action == 5) {
				Optional<Employees> optionalEmployees = null;
				System.out.print("For id ");
				int id = ExtraFunctional.requestIntInput(1, Integer.MAX_VALUE);
				try {
					optionalEmployees = serviceEmployees.getEmployeeById(id);
				} catch (SQLException e) {
					System.out.println("Problems with database :( ...");
					e.printStackTrace();
				} catch (NonUniqueResultException e) {
					System.out.println(e.getMessage());
				}
				if (optionalEmployees.isPresent()) {
					System.out.println("Are you sure you want to delete this employee?");
					System.out.println(optionalEmployees.get().toString());
					System.out.println("y/n");
					if (ExtraFunctional.toBeOrNotToBe()) {
						try {
							if (serviceEmployees.deleteEmployeeById(id)) {
								System.out.println("Employee was deleted successfully");
							}
						} catch (SQLException e) {
							System.out.println("Problems with database :( ...");
							e.printStackTrace();
						}
					}
				} else {
					System.out.println("Employee with id: " + id + " was not found.");
				}

			}

			// Find Employee(s) by letter(s).
			if (action == 6) {
				List<Employees> employees = null;
				String letters = inputLetters();
				try {
					employees = serviceEmployees.getEmployeesByLetters(letters);
					if (employees.isEmpty())
						System.out.println("no names or surname found with '" + letters + "' letters");
					else
						employees.forEach(System.out::println);
				} catch (SQLException e) {
					System.out.println("Problems with database :( ...");
					e.printStackTrace();
				}
			}

			// (7) Find all employees whose birthday is today.
			// (8) See who has a birthday in the next 7 days.
			if (action == 7 || action == 8) {
				List<Employees> employees = null;
				try {
					employees = serviceEmployees.getEmployeesWithTodaysBirthdays(action);
					if (employees.isEmpty())
						System.out.println("No one");
					else
						employees.forEach(System.out::println);
				} catch (SQLException e) {
					System.out.println("Problems with database :( ...");
					e.printStackTrace();
				}
			}
		}

		// We will use Projects
		if (chooseManu == 2) {
			ServiceProjects serviceProjects = new ServiceProjects();

			// Add Projects.
			if (action == 2) {
				try {
					serviceProjects.addNewproject();
				} catch (SQLException e) {
					System.out.println("Problems with database :( ...");
					e.printStackTrace();
				}
			}

			// Delete Projects by id.
			if (action == 3) {
				Optional<Projects> optionalProjects = null;
				System.out.print("For id ");
				int id = ExtraFunctional.requestIntInput(1, Integer.MAX_VALUE);
				try {
					optionalProjects = serviceProjects.getProjectById(id);
				} catch (SQLException e) {
					System.out.println("Problems with database :( ...");
					e.printStackTrace();
				} catch (NonUniqueResultException e) {
					System.out.println(e.getMessage());
				}
				if (optionalProjects.isPresent()) {
					System.out.println("Are you sure you want to delete this project?");
					System.out.println(optionalProjects.get().toString());
					System.out.println("y/n");
					if (ExtraFunctional.toBeOrNotToBe()) {
						try {
							if (serviceProjects.deleteProjectById(id)) {
								System.out.println("Project was deleted successfully");
							}
						} catch (SQLException e) {
							System.out.println("Problems with database :( ...");
							e.printStackTrace();
						}
					}
				} else {
					System.out.println("Project with id: " + id + " was not found.");
				}
			}

			// (4)  Find all current projects.
			// (5)  Find all projects started today.
			if (action == 4 || action == 5) {
				List<Projects> projects = null;
				try {
					projects = serviceProjects.getProjectsDate(action);
					if (projects.isEmpty())
						System.out.println("No one");
					else
						projects.forEach(System.out::println);
				} catch (SQLException e) {
					System.out.println("Problems with database :( ...");
					e.printStackTrace();
				}
			}

		}

		// We will use WorkDone
		if (chooseManu == 3) {
			ServiceWorkDone serviceWorkDone = new ServiceWorkDone();
			// Add record (in WorkDone).
			if (action == 2) {
				try {
					serviceWorkDone.addNewWorkDone();
				} catch (SQLException e) {
					System.out.println("Problems with database :( ...");
					e.printStackTrace();
				} catch (NonUniqueResultException e) {
					System.out.println(e.getMessage());
				}
			}

			// delete record (in WorkDone).
			if (action == 3) {
				Optional<WorkDone> optionalWorkDone = null;
				System.out.print("For id ");
				int id = ExtraFunctional.requestIntInput(1, Integer.MAX_VALUE);
				try {
					optionalWorkDone = serviceWorkDone.getWorkDoneById(id);
				} catch (SQLException e) {
					System.out.println("Problems with database :( ...");
					e.printStackTrace();
				} catch (NonUniqueResultException e) {
					System.out.println(e.getMessage());
				}
				if (optionalWorkDone.isPresent()) {
					System.out.println("Are you sure you want to delete this workDone?");
					System.out.println(optionalWorkDone.get().toString());
					System.out.println("y/n");
					if (ExtraFunctional.toBeOrNotToBe()) {
						try {
							if (serviceWorkDone.deleteWorkDoneById(id)) {
								System.out.println("Project was deleted successfully");
							}
						} catch (SQLException e) {
							System.out.println("Problems with database :( ...");
							e.printStackTrace();
						}
					}
				} else {
					System.out.println("workDone with id: " + id + " was not found.");
				}
			}

			// edit record (in WorkDone).
			if (action == 4) {
				try {
					System.out.print("For id ");
					int id = ExtraFunctional.requestIntInput(1, Integer.MAX_VALUE);
					serviceWorkDone.editEmployeeById(id);
				} catch (Exception e) {
					System.out.println("Problems with database :( ...");
					e.printStackTrace();
				}
			}
			
			// get all from WorkDone
			if(action == 5) {
				List<WorkDone> workDone = null;
				try {
					workDone = serviceWorkDone.getAllEmployee();
					workDone.forEach(System.out::println);
				} catch (SQLException e) {
					System.out.println("Problems with database :( ...");
					e.printStackTrace();
				}
			}
			
			// See a list of all recent projects.
			if(action == 6) {
				List<InCome> inCome = null;
				try {
					inCome = serviceWorkDone.getRecentProjects();
					inCome.forEach(System.out::println);
					
					System.out.print("Total: "
					+inCome.stream().filter(a -> a != null && a.getIncomePerProject() != 0).mapToDouble(InCome::getIncomePerProject).sum());
				} catch (SQLException e) {
					System.out.println("Problems with database :( ...");
					e.printStackTrace();
				}
			}
			
			// A top 3 Employees for a particular project
			if(action == 7) {
				List<WorkDone> workDone = null;
				try {
					System.out.print("For Project id ");
					int id = ExtraFunctional.requestIntInput(1, Integer.MAX_VALUE);
					workDone = serviceWorkDone.getTopThriEmployeesByProjectId(id);
					workDone.forEach(System.out::println);
				} catch (SQLException e) {
					System.out.println("Problems with database :( ...");
					e.printStackTrace();
				}
			}

		}

		System.out.println("\n========================================================");
	}

	private static String inputLetters() {
		System.out.println("Write letter:");
		Scanner scanner = new Scanner(System.in);
		String input = "";
		boolean result = false;
		do {

			input = scanner.next();
			if (!ExtraFunctional.checkingTextOnlyLetters(input))
				System.out.println("Please enter correctly, try again:");
			else
				result = true;
		} while (!result);
		return input;
	}

	public static void printManu() {
		System.out.println("\nWhich bibliotek do you want use?");
		System.out.println("Press 0  for exit");
		System.out.println("Press 1  'Employees'");
		System.out.println("Press 2  'Projects'");
		System.out.println("Press 3  'WorkDone'");
	}

	public static void actionForEmployees() {
		System.out.println("\nWhat do you want to do?");
		System.out.println("Press 0  For exit.");
		System.out.println("Press 1  Go back.");
		System.out.println("Press 2  Show all Employees.");
		System.out.println("Press 3  Add Employee.");
		System.out.println("Press 4  Edit Employee by id.");
		System.out.println("Press 5  Delete Employee by id.");
		System.out.println("Press 6  Find Employee(s) by letter(s).");
		System.out.println("Press 7  Find all employees whose birthday is today.");
		System.out.println("Press 8  See who has a birthday in the next 7 days.");
	}

	public static void actionForProjects() {
		System.out.println("\nWhat do you want to do?");
		System.out.println("Press 0  For exit.");
		System.out.println("Press 1  Go back.");
		System.out.println("Press 2  Add Projects.");
		System.out.println("Press 3  Delete Projects by id.");
		System.out.println("Press 4  Find all current projects.");
		System.out.println("Press 5  Find all projects starting today.");
	}

	public static void actionForWorkDone() {
		System.out.println("\nWhat do you want to do?");
		System.out.println("Press 0  For exit.");
		System.out.println("Press 1  Go back.");
		System.out.println("Press 2  Add record in WorkDone.");
		System.out.println("Press 3  Delete record by id in WorkDone.");
		System.out.println("Press 4  Edit record by id in WorkDone.");
		System.out.println("Press 5  See all record.");
		System.out.println("Press 6  See a list of all recent projects.");
		System.out.println("Press 7  A top 3 Employees for a particular project.");
	}

}
