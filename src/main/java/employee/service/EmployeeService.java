package employee.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import employee.dao.*;
import employee.entity.Employee;
import employee.entity.Project;
import employee.exception.DbException;

public class EmployeeService {
	
	private static final String SCHEMA_FILE = "employee_schema.sql";
	
	private EmployeeDao employeeDao = new EmployeeDao();
	
	

	public Employee fetchEmplById(Integer emplId) {
		return employeeDao.fetchEmplById(emplId)
			.orElseThrow(()-> new NoSuchElementException("Employee with Id = " + emplId + " does not exist!"));
	}

	
	
	
	public void deleteEmployee(Integer emplId) {
		if(!employeeDao.deleteEmployee(emplId)) {
			throw new DbException("Employee with ID = " + emplId + " does not exist!");
		}
		
	}
	
	
	public void createAndPopulateTables() {
		try {
			loadFromFile(SCHEMA_FILE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Employee addEmployee(Employee employee) {
		return employeeDao.insertEmployee(employee);
		
	}
	public List<Employee> getAllEmployees() {
		return employeeDao.getAllEmployees();
	}


	
	
	
	
	
	
	
	private void loadFromFile(String fileName) throws SQLException {
		String content = readFileContent(fileName);
		List<String> sqlStatements = convertContentToSqlStatements(content);
		sqlStatements.forEach(line->System.out.println());
		employeeDao.executeBatch(sqlStatements);
	}

	private List<String> convertContentToSqlStatements(String content) {
		content = removeComments(content);
		content = replaceWhitespaceSequencesWithSingleSpace(content);

		return extractLinesFromContent(content);
	}

	private List<String> extractLinesFromContent(String content) {
		List<String> lines = new LinkedList<>();
		while (!content.isEmpty()) {
			int semicolon = content.indexOf(";");

			if (semicolon == -1) {
				if (!content.isEmpty()) {
					lines.add(content);
				}

				content = "";
			} else {
				lines.add(content.substring(0, semicolon).trim());
				content = content.substring(semicolon + 1);
			}
		}

		return lines;
	}
	private String replaceWhitespaceSequencesWithSingleSpace(String content) {
		return content.replaceAll("\\s+", " ");
	}

	private String removeComments(String content) {
		StringBuilder builder = new StringBuilder(content);
		int commentPos = 0;

		while ((commentPos = builder.indexOf("-- ", commentPos)) != -1) {
			int eolPos = builder.indexOf("\n", commentPos + 1);

			if (eolPos == -1) {
				builder.replace(commentPos, builder.length(), "");
			} else {
				builder.replace(commentPos, eolPos + 1, "");
			}
		}
		return builder.toString();
	}
	private String readFileContent(String fileName) {
		try {

			Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
			
			return Files.readString(path);

		} catch (Exception e) {
			throw new DbException(e);
		}
	}




	public List<Project> fetchProjects(Integer deptId) {
		return employeeDao.fetchEmplProjects(deptId);
	}








}
