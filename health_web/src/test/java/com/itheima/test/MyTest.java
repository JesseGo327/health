package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author: tangx
 * @Date: 2019/11/12 9:49
 * @Description: com.itheima.test
 */
public class MyTest {
    @Test
    public void fun01() throws Exception {

        //创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook("E:/hello.xlsx");
        //获取工作表对象
        XSSFSheet sheet = workbook.getSheetAt(0);
        //遍历工作表 获取列对象
        for (Row row : sheet) {
            //遍历行对象
            for (Cell cell : row) {
                //获取列里面的内容
                System.out.println(cell.getStringCellValue());
            }
            System.out.println("--------------");
        }

        //关闭
        workbook.close();
    }

    @Test
    public void fun02() throws Exception {
        //1.创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook("E:/hello.xlsx");
        //2.获得工作表对象
        XSSFSheet sheet = workbook.getSheetAt(0);
        //3.获得最后一行的行号
        int lastRowNum = sheet.getLastRowNum();
        //4.遍历行
        for (int i = 0; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            //5.获得最后一列的列号
            short lastCellNum = row.getLastCellNum();
            //6.遍历列
            for (int j = 0; j < lastCellNum; j++) {
                //7.取出数据
                XSSFCell cell = row.getCell(j);
                System.out.println(cell.getStringCellValue());
            }
        }
        //8.关闭
        workbook.close();
    }

    /**
     * 向Excel文件写入数据
     */
    @Test
    public void fun() throws IOException {
        //使用POI可以再内存中创建一个Excel文件并将数据写入到这个文件,最后通过输出流将内存中的Excel文件下载到磁盘
        //创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表对象
        XSSFSheet sheet = workbook.createSheet("学生名单");
        //创建行
        XSSFRow row = sheet.createRow(0);
        //创建列,设置内容
        row.createCell(0).setCellValue("姓名");
        row.createCell(1).setCellValue("性别");
        row.createCell(2).setCellValue("地址");

        XSSFRow row02 = sheet.createRow(1);
        row02.createCell(0).setCellValue("张三");
        row02.createCell(1).setCellValue("男");
        row02.createCell(2).setCellValue("深圳");

        XSSFRow row03 = sheet.createRow(2);
        row03.createCell(0).setCellValue("李四");
        row03.createCell(1).setCellValue("男");
        row03.createCell(2).setCellValue("北京");

        //通过输出流对象写到磁盘
        FileOutputStream fileOutputStream = new FileOutputStream("E:/student.xlsx");
        workbook.write(fileOutputStream);
        workbook.close();
        workbook.close();

    }
}
