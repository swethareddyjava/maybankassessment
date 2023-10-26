package com.maybank.assessment.service;

import java.util.List;

import com.maybank.assessment.entity.Employee;
import com.maybank.assessment.entity.ExternalEmp;

public interface EmployeeServiceInterface {

	public Employee addEmployee(Employee employee);

	public List<Employee> getAllEmployees();

	public Employee getEmployeeById(Long eidL);

	public void deleteEmployeeById(Long eidL);
	
	public List<Employee> employeePageList(int pageNo, int pageSize);
	
	public ExternalEmp getEmpExternalInfo(String name);
}
