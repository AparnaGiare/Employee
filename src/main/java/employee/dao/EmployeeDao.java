package employee.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.LinkedList;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalTime;

import provided.util.DaoBase;
import employee.entity.Department;
import employee.entity.Employee;
import employee.entity.Project;
import employee.exception.DbException;

public class EmployeeDao extends DaoBase{
	
	private static final String EMPLOYEE_TABLE = "employee";
	private static final String DEPARTMENT_TABLE = "department";

	

	public Optional<Employee> fetchEmplById(Integer emplId) {
		Employee empl = null;
		String sSql = "SELECT * FROM " + EMPLOYEE_TABLE + " WHERE EMPLOYEE_ID = ?";
		try(Connection conn = DbConnection.getConnection()){
			startTransaction(conn);
		
			try {
				
				try(PreparedStatement stmt = conn.prepareStatement(sSql)){
				
					setParameter(stmt,1,emplId,Integer.class);
				
					try(ResultSet rs = stmt.executeQuery()){
						if(rs.next()) {
							empl = extract(rs,Employee.class);
						}
					}
				}
			
				if(Objects.nonNull(empl)) {
					Department emplDept = fetchEmplDept(conn,emplId);
					empl.setDeptId(emplDept.getDeptId());
					empl.getProjects().addAll(fetchEmplProjects(conn,empl.getDeptId()));
				
				}
				
				commitTransaction(conn);
				return Optional.ofNullable(empl);
			}
			catch(Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
			
		
			
		} catch (SQLException e) {
			throw new DbException(e);
		}
	}
	
	

	private List<Project> fetchEmplProjects(Connection conn, Integer deptId) {
		List<Project> projects = new LinkedList<>();
		String sSql = "SELECT P.PROJECT_NAME, P.PROJECT_ID "+
						" FROM DEPT_PROJECT DP " +
						"LEFT JOIN PROJECT P USING(PROJECT_ID" +
						"WHERE DP.DEPT_ID = ? ";
		
		try(PreparedStatement stmt = conn.prepareStatement(sSql)){
			
			setParameter(stmt,1,deptId,Integer.class);
			
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					projects.add(extract(rs,Project.class));
				}
			}
		}
			
		catch(SQLException e) {
			e.printStackTrace();
			
		}
		return projects;
	}
	
	public List<Project> fetchEmplProjects( Integer deptId) {
		try(Connection conn = DbConnection.getConnection()){
			
			startTransaction(conn);
			
			try {
				List<Project> projects = fetchEmplProjects(conn,deptId);
				commitTransaction(conn);
				return projects;
			}
			catch(SQLException e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
			
		}
			
		catch(SQLException e) {
			e.printStackTrace();
			
		}
		return null;
	}





	private Department fetchEmplDept(Connection conn, Integer emplId) {
		Department emplDept = new Department();
		String sSql = "SELECT * FROM  " + DEPARTMENT_TABLE + " WHERE EMPLOYEE_ID = ?";
		try(PreparedStatement stmt = conn.prepareStatement(sSql)){
				
				setParameter(stmt,1,emplId,Integer.class);
				
				try(ResultSet rs = stmt.executeQuery()){
					if(rs.next()) {
						emplDept = extract(rs,Department.class);
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return null;
	}





	public boolean deleteEmployee(Integer emplId) {
		boolean deleted = false;
		String sSql = "DELETE FROM "+ EMPLOYEE_TABLE + " WHERE EMPLOYEE_ID = ? ";
		
		try(Connection conn = DbConnection.getConnection()){
			startTransaction(conn);
			
			try(PreparedStatement stmt = conn.prepareStatement(sSql)){
				setParameter(stmt, 1,emplId, Integer.class);
				
				deleted = stmt.executeUpdate() == 1;
				
				commitTransaction(conn);
				return deleted;
			}
			catch(Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
			
		
			
		} catch (SQLException e) {
			throw new DbException(e);
		}
		
	}
	
	
	public List<Employee> getAllEmployees() {
		String sSql = "SELECT * FROM " + EMPLOYEE_TABLE + " ORDER BY EMPLOYEE_NAME";
		try(Connection conn = DbConnection.getConnection()){
			try(PreparedStatement stmt = conn.prepareStatement(sSql)){
				try(ResultSet rs = stmt.executeQuery()){
					List<Employee> emplist = new LinkedList<>();
					while(rs.next()) {
						emplist.add(extract(rs,Employee.class));
					}
					return emplist;
				}
				
			}
			catch(Exception e) {
				rollbackTransaction(conn);
				throw new DbException();
			}
		}
		catch(SQLException e){
			throw new DbException(e);
		}		
	}


	public Employee insertEmployee(Employee employee) {
		//@formatter:off
		String sql = ""
				+ "INSERT INTO " + EMPLOYEE_TABLE + " "
				+ "(employee_name, employee_email, JOINING_DATE) "
				+ "VALUES"
				+ "(?, ?, ?)";
		
		try (Connection conn = DbConnection.getConnection()){
			startTransaction(conn);
			
			try(PreparedStatement stmt = conn.prepareStatement(sql)){
				setParameter(stmt,1, employee.getEmployeeName(),String.class);
				setParameter(stmt,2,employee.getEmployeeEmail(),String.class);
				setParameter(stmt,3,employee.getJoiningDate(),LocalTime.class);
				
				stmt.executeUpdate();
				Integer employeeId = getLastInsertId(conn,EMPLOYEE_TABLE);
				commitTransaction(conn);
				
				employee.setEmployeeId(employeeId);
				return employee;
			}
			catch(Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
		}
		catch(Exception e) {
			throw new DbException(e);
		}
	}
	public void executeBatch(List<String> sqlBatch) throws SQLException {
		try (Connection conn = DbConnection.getConnection()){
			startTransaction(conn);
			
			try(Statement stmt = conn.createStatement()){
				
				for(String sql:sqlBatch) {
					stmt.addBatch(sql);
				}
				
				stmt.executeBatch();
				commitTransaction(conn);
				
			}catch(Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}

		}catch(Exception e) {
				throw new DbException(e);
		
		}
		
	}





}
