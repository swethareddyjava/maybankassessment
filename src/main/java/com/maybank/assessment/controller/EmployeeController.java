package com.maybank.assessment.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maybank.assessment.custom.exception.BusinessException;
import com.maybank.assessment.custom.exception.ControllerException;
import com.maybank.assessment.entity.Employee;
import com.maybank.assessment.entity.ExternalEmp;
import com.maybank.assessment.service.EmployeeServiceInterface;

@RestController
@RequestMapping("/maybank")
public class EmployeeController {
	
	private final static Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired
	private EmployeeServiceInterface employeeServiceInterface;
		
	@PostMapping("/save")
	public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee){
		Employee employeeSaved = employeeServiceInterface.addEmployee(employee);
		return new ResponseEntity<Employee>(employeeSaved, HttpStatus.CREATED);
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllEmployees() {	
		try {
			List<Employee> listOfEmployees = employeeServiceInterface.getAllEmployees();
			return new ResponseEntity<List<Employee>>(listOfEmployees, HttpStatus.OK);
		}catch (BusinessException e) {
			ControllerException ce = new ControllerException(e.getErrorCode(),e.getErrorMessage());
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		}catch (Exception e) {
			ControllerException ce = new ControllerException("610","Something went wrong in Controller layer");
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/employee/{eid}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable("eid") Long eidL) {		
		Employee empRetrieved = employeeServiceInterface.getEmployeeById(eidL);
		return new ResponseEntity<Employee>(empRetrieved, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{eid}")
	public ResponseEntity<Void> deleteEmployeeById(@PathVariable("eid") Long eidL) {		
		employeeServiceInterface.deleteEmployeeById(eidL);
		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee){
		Employee empSaved = employeeServiceInterface.addEmployee(employee);
		return new ResponseEntity<Employee>(empSaved, HttpStatus.CREATED);
	}
	
	@GetMapping("/listbypage")
	@ResponseBody
	public ResponseEntity<List<Employee>> getEmployeeByPage(@RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
		List<Employee> employeeList = employeeServiceInterface.employeePageList( pageNo, pageSize);
		return new ResponseEntity<>(employeeList, HttpStatus.OK);
	}
	
	@GetMapping("/getAgeByName/{name}")
	@ResponseBody
	public ResponseEntity<ExternalEmp> getEmpExternalInfo(@PathVariable("name") String name) {
		ExternalEmp empInfo = employeeServiceInterface.getEmpExternalInfo(name);
		return new ResponseEntity<>(empInfo, HttpStatus.OK);
	}

}
