package com.maybank.assessment.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.maybank.assessment.custom.exception.BusinessException;
import com.maybank.assessment.entity.Employee;
import com.maybank.assessment.entity.ExternalEmp;
import com.maybank.assessment.repository.EmployeeCrudRepo;

@Service
public class EmployeeService implements EmployeeServiceInterface{
	
	@Autowired
	private EmployeeCrudRepo crudRepo;

	@Override
	@Transactional
	public Employee addEmployee(Employee employee) {		
		
		try {
			Employee savedEmployee = crudRepo.save(employee);
			return savedEmployee;
		}
		catch (IllegalArgumentException e) {
			throw new BusinessException("602","given employee is null" + e.getMessage());
		}
		catch (Exception e) {
			throw new BusinessException("603","Something went wrong in service layer" + e.getMessage());
		}
		
	}

	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> empList = null;
		try{
			empList = crudRepo.findAll();
		}
		catch (Exception e) {
			throw new BusinessException("605","Something went wrong in service layer while fetching from db");
		}
		if(empList.isEmpty())
			throw new BusinessException("604","List is empty, we have nothing to return");
		
		return empList;
	}

	@Override
	public Employee getEmployeeById(Long eidL) {
		return crudRepo.findById(eidL).get();
	}

	@Override
	public void deleteEmployeeById(Long eidL) {
		crudRepo.deleteById(eidL);
		
	}
	
	@Override
	public List<Employee> employeePageList(int pageNo, int pageSize) {
		List<Employee> empList = null;
		try {
			Pageable paging = PageRequest.of(pageNo, pageSize );
	        Page<Employee> pagedResult = crudRepo.findAll(paging);
	        if(pagedResult.hasContent()) {
	            return pagedResult.getContent();
	        } else {
	        	empList= new ArrayList<Employee>();
	        }
		} 
//		catch (DataIntegrityViolationException dataException) {
//			throw new IllegalArgumentException("employees not found");
//		} 
		catch (IllegalArgumentException e) {
			throw new BusinessException("606","employees not found" + e.getMessage());
		}
		catch (Exception e) {
			throw new BusinessException("603","Something went wrong in service layer" + e.getMessage());
		}
		return empList;
	}
	
	
	@Override
	public ExternalEmp getEmpExternalInfo(String name) {
		ExternalEmp emp = null;
		try {
			String uri = "https://api.agify.io/?name=" + name;
			RestTemplate restTemplate = new RestTemplate();
			emp = restTemplate.getForObject(uri, ExternalEmp.class);
		} 
		catch (Exception e) {
			throw new BusinessException("607","External Info not found" + e.getMessage());
		}		
		return emp;
	}

}
