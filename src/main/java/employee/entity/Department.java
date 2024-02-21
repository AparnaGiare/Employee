package employee.entity;

public class Department {
	private Integer employeeId;
	private Integer deptId;
	private String deptName;
	
	public Integer getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}
	public Integer getDeptId() {
		return deptId;
	}
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	@Override
	public String toString() {
		return "Department [employeeId=" + employeeId + ", deptId=" + deptId + ", deptName=" + deptName + "]";
	}
	

	
	
}
