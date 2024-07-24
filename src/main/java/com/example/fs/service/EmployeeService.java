package com.example.fs.service;

import com.example.fs.exception.EmployeeAlreadyExistsException;
import com.example.fs.exception.EmployeeNotFoundException;
import com.example.fs.model.Employee;
import com.example.fs.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee addEmployee(Employee employee) {

        if(employeeAlreadyExist(employee.getEmail())){
            throw new EmployeeAlreadyExistsException(employee.getEmail() + " already exists!");
        }

        return employeeRepository.save(employee);
    }

    private boolean employeeAlreadyExist(String email) {
        return employeeRepository.findByEmail(email).isPresent();
    }

    @Override
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployee(Employee employee, Long id) {
        return employeeRepository.findById(id).map(em -> {
            em.setFirstName(employee.getFirstName());
            em.setLastName(employee.getLastName());
            em.setEmail(employee.getEmail());
            em.setPosition(employee.getPosition());
            return employeeRepository.save(em);
        }).orElseThrow(() -> new EmployeeNotFoundException("Sorry, this employee could not be found"));
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Sorry, this employee could not be found"));
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException("Sorry, this employee could not be found");
        }
        employeeRepository.deleteById(id);
    }
}
