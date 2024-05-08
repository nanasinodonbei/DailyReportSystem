package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.techacademy.repository.ReportRepository;
import jakarta.transaction.Transactional;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportsRepository) {

        this.reportRepository = reportsRepository;

    }

    // 日報一覧表示
    public List<Report> findAll() {
        return reportRepository.findAll();

    }

    // 日報の保存処理
    @Transactional
    public ErrorKinds save(Report report, UserDetail userDetail) {

        // 日付チェック
        List<Report> reportList = reportRepository.findByEmployeeAndReportDate(userDetail.getEmployee(),
                report.getReportDate());

        if (reportList.size() != 0) {
            return ErrorKinds.DATECHECK_ERROR;
        }

        report.setEmployee(userDetail.getEmployee());
        report.setDeleteFlg(false);
        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;

    }

    // 従業員の日報詳細検索
    public Report findById(Integer id) {
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }

    // 日報更新処理
    @Transactional
    public ErrorKinds update(Report report, Integer id, UserDetail userDetail) {
        Report currentReport = reportRepository.findById(report.getId()).orElse(null);

        List<Report> reportList = reportRepository.findByEmployeeAndReportDateAndId(userDetail.getEmployee(),
                report.getReportDate(), report.getId());
        if (reportList.size() != 1) {

            return ErrorKinds.DATECHECK_ERROR;

        }
     
        report.setId(id); 
        report.setEmployee(userDetail.getEmployee());
        // 更新対象のフィールドをコピー
        ReflectionUtils.shallowCopyFieldState(report, currentReport);
        currentReport.setUpdatedAt(LocalDateTime.now());
        reportRepository.save(currentReport);

        return ErrorKinds.SUCCESS;

    }

    // 日報削除
    @Transactional
    public void delete(Integer id) {

        Report report = findById(id);
        reportRepository.deleteById(id);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);

    }
    // 日報詳細
    public List<Report> findByEmployee(Employee employee) {
        return reportRepository.findByEmployee(employee);
    }

}
