package com.techacademy.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;


public interface ReportRepository extends JpaRepository <Report,Integer>{
    
    List<Report> findByEmployeeAndReportDate(Employee employee, LocalDate reportDate);
    List<Report> findByEmployeeAndReportDateAndId(Employee employee, LocalDate reportDate, Integer id);
    List<Report> findByEmployee(Employee employee);
}


