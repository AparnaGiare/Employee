package employee;

import java.time.LocalDateTime;

import java.util.List;

import employee.dao.DbConnection;
import employee.service.*;
import employee.entity.*;
import java.util.*; 

public class EmployeeApplication {

	public static EmployeeService employeeService = new EmployeeService();
	private static Scanner scanner = new Scanner(System.in);
	private static Employee curEmpl;
	
	public static void main(String[] args) {
		//DbConnection.getConnection();
		try {
			//create table
			//employeeService.createAndPopulateTables();
			//System.out.println("Created Tables!");
			//add employee
			//addEmployee();
			//list all employee
			//listEmployee();
			//UPDATE EMPLOYEE;
			modifyEmployeeProject();
			//GET THE EMPLOYEE ID
			//DELETE EMPLOYEE
			//deleteEmployee();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//add an employee

	}

	private static void modifyEmployeeProject() {
		setCurEmpl();
		List<Project> projects = employeeService.fetchProjects(curEmpl.getDeptId());
		System.out.println("\nCurrent Employee Projects are:");
		projects.forEach(project-> System.out.println("  " + project));
		Integer projectId = getIntInput("\nEnter the Project Id to update the project");
		String projectName = getStringInput("\nEnter new Project Name");
		
		Project proj = new Project();
		proj.setProjectId(projectId);
		proj.setProjectName(projectName);
	}

	private static String getStringInput(String prompt) {
		System.out.println(prompt + ": ");
		String line = scanner.nextLine();
		return line.isBlank()?null:line.trim();
	}

	private static void setCurEmpl() {
		List<Employee> employees = listEmployee();
		Integer emplId = getIntInput("\nEnter the Employee Id to update their project");
		curEmpl = null;
		
		for(Employee empl:employees) {
			if(empl.getEmployeeId().equals(emplId)) {
				curEmpl = employeeService.fetchEmplById(emplId);
			}
		}
		if(Objects.isNull(curEmpl)) {
			System.out.println("Invalid Employee Selected!");
		}
		
		
		
	}

	private static void deleteEmployee() {
		listEmployee();
		Integer emplId = getIntInput("Enter the Employee Id to delete");
		if(Objects.nonNull(emplId)){
			employeeService.deleteEmployee(emplId);
			System.out.println("You have deleted employee id = " + emplId);
		}
	}

	private static Integer getIntInput(String prompt) {
		System.out.println(prompt + ": ");
		String line = scanner.nextLine();
		if(!Objects.isNull(line)) {
			return Integer.parseInt(line);
		}
		return null;
	}

	private static List<Employee> listEmployee() {
		List<Employee> emplist = employeeService.getAllEmployees();
		System.out.println("Employee List -- \n");
		emplist.forEach(employee->System.out.println("Employee Id = " + employee.getEmployeeId() + "   Employee Name = " + employee.getEmployeeName() + "    Employee Email = " + employee.getEmployeeEmail()));
		return emplist;
	}

	private static void addEmployee() {
		Employee employee = new Employee();
		LocalDateTime date = LocalDateTime.now();
		
		employee.setEmployeeName("Ann Miller");
		employee.setEmployeeEmail("Amiller@gmail.com");
		employee.setJoiningDate(date);
		
		
		Employee addedEmp = employeeService.addEmployee(employee);
		System.out.println("I have added the following employee: " + addedEmp);
		
	}

}
