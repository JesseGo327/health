package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.PackageService;
import com.itheima.service.ReportService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: tangx
 * @Date: 2019/11/16 19:02
 * @Description: com.itheima.controller
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;
    @Reference
    private PackageService packageService;
    @Reference
    private ReportService reportService;

    @RequestMapping("/getMemberReport")
    @PreAuthorize("hasAnyAuthority('REPORT_VIEW')")
    public Result getMemberReport() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);//获得当前日期之前12个月的日期
        ArrayList<String> list = new ArrayList();
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            list.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("months", list);

        List<Integer> memberCount = memberService.findMemberCountByMonth(list);
        map.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }

    @RequestMapping("/getPackageReport")
    @PreAuthorize("hasAnyAuthority('REPORT_VIEW')")
    public Result getPackageReport() {
        try {
            Map map = packageService.findPackageCount();
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    /**
     * 获取运营统计数据
     *
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    @PreAuthorize("hasAnyAuthority('REPORT_VIEW')")
    public Result getBusinessReportData() {
        try {
            Map map = reportService.getBusinessReport();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出报表
     * @return
     */
    @RequestMapping("/exportBusinessReport")
    @PreAuthorize("hasAnyAuthority('REPORT_VIEW')")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            //远程调用报表服务获取报表数据
            Map reportMap = reportService.getBusinessReport();

            //取出数据返回结果数据,准备将报表数据写入到Excel文件中
            String reportDate = (String) reportMap.get("reportDate");
            Integer todayNewMember = (Integer) reportMap.get("todayNewMember");
            Integer totalMember = (Integer) reportMap.get("totalMember");
            Integer thisWeekNewMember = (Integer) reportMap.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) reportMap.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) reportMap.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) reportMap.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) reportMap.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) reportMap.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) reportMap.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) reportMap.get("thisMonthVisitsNumber");
            List<Map> hotPackage = (List<Map>) reportMap.get("hotPackage");

            //获取Excel模板的绝对路径
            String temlateRealPath = request.getSession().getServletContext().getRealPath("template")
                    + File.separator+"report_template.xlsx";

            //读取模板文件创建Excel表格对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//本日新增会员数
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for(Map map : hotPackage){//热门套餐
                String name = (String) map.get("name");
                Long package_count = (Long) map.get("count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(package_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //通过输出流进行文件下载
            ServletOutputStream os = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(os);

            os.flush();
            os.close();
            workbook.close();
            return null;
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

}
