package msc.excel.deprecatedExcel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ExcelUtils {
    static final Log log = LogFactory.getLog(ExcelUtils.class);

    private Workbook wb;
    private Sheet sheet;
    private Row row;

    public ExcelUtils(String filepath) {
        if (filepath == null) {
            return;
        }

        String ext = filepath.substring(filepath.lastIndexOf("."));

        try {
            InputStream is = new FileInputStream(filepath);

            if (".xls".equals(ext)) {
                wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(ext)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = null;
            }
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException", e);
        } catch (IOException e) {
            log.error("IOException", e);
        }
    }

    /**
     * 读取Excel表格表头的内容
     *
     * @return String 表头内容的数组
     * @throws Exception
     * @author zengwendong
     */
    public String[] readExcelTitle() throws Exception {
        if (wb == null) {
            throw new Exception("Workbook对象为空！");
        }
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(0);
        // 标题总列数  
        int colNum = row.getPhysicalNumberOfCells();
        System.out.println("colNum:" + colNum);
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            // title[i] = getStringCellValue(row.getCell((short) i));  
            title[i] = row.getCell(i).getCellFormula();
        }
        return title;
    }

    /**
     * 写入Excel数据内容
     *
     * @param map
     * @throws Exception
     * @author zengwendong
     */
    public static void writeExcelContent(Map<Integer, Map<Integer, Object>> map) throws Exception {
        //第一步，创建一个workbook对应一个excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        //第二部，在workbook中创建一个sheet对应excel中的sheet
        HSSFSheet sheet = workbook.createSheet("12月份");
        //第三部，在sheet表中添加表头第0行，老版本的poi对sheet的行列有限制
        HSSFRow row = sheet.createRow(0);
        //第四步，创建单元格，设置表头
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("商户编号");
        cell = row.createCell(1);
        cell.setCellValue("金额");
        cell = row.createCell(2);
        cell.setCellValue("姓名");
        cell = row.createCell(3);
        cell.setCellValue("电话");
        cell = row.createCell(4);
        cell.setCellValue("地址");

        //第五步，写入实体数据，实际应用中这些数据从数据库得到,对象封装数据，集合包对象。对象的属性值对应表的每行的值
        Set<Entry<Integer, Map<Integer, Object>>> set = map.entrySet();
        Map<Integer, Object> value = null;
        int amout = 1;

        for (Entry<Integer, Map<Integer, Object>> entry : set) {
            HSSFRow row1 = sheet.createRow(amout++);

            value = entry.getValue();

            if (null != value) {
                //创建单元格设值
                row1.createCell(0).setCellValue(value.get(0).toString().trim());
                row1.createCell(1).setCellValue(value.get(1).toString().trim());
                row1.createCell(2).setCellValue(value.get(2).toString().trim());
                row1.createCell(3).setCellValue(value.get(3).toString().trim());
                row1.createCell(4).setCellValue(value.get(4).toString().trim());
            }
        }

        //将文件保存到指定的位置
        try {
            FileOutputStream fos = new FileOutputStream("/home/webzx/tmp/12New.xls");
//            FileOutputStream fos = new FileOutputStream("E://12New.xls");
            workbook.write(fos);
            log.info("写入成功");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取Excel数据内容
     *
     * @return Map 包含单元格数据内容的Map对象
     * @throws Exception
     * @author zengwendong
     */
    public Map<Integer, Map<Integer, Object>> readExcelContent() throws Exception {
        if (wb == null) {
            throw new Exception("Workbook对象为空！");
        }

        Map<Integer, Map<Integer, Object>> content = new HashMap<Integer, Map<Integer, Object>>();

        sheet = wb.getSheetAt(0);

        // 得到总行数  
        int rowNum = sheet.getLastRowNum();

        row = sheet.getRow(0);

        int colNum = row.getPhysicalNumberOfCells();

        // 正文内容应该从第二行开始,第一行为表头的标题  
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            Map<Integer, Object> cellValue = new HashMap<Integer, Object>();

            while (j < colNum) {
                Object obj = getCellFormatValue(row.getCell(j));
                cellValue.put(j, obj);
                j++;
            }
            content.put(i, cellValue);
        }

        return content;
    }

    /**
     * 根据Cell类型设置数据
     *
     * @param cell
     * @return
     * @author zengwendong
     */
    private Object getCellFormatValue(Cell cell) {
        Object cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type  
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:// 如果当前Cell的Type为NUMERIC
                case Cell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式
                        // data格式是带时分秒的：2013-7-10 0:00:00
                        // cellvalue = cell.getDateCellValue().toLocaleString();
                        // data格式是不带带时分秒的：2013-7-10
                        Date date = cell.getDateCellValue();
                        cellvalue = date;
                    } else {// 如果是纯数字

                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:// 如果当前Cell的Type为STRING
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                default:// 默认的Cell值
                    cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }


    public static void main(String[] args) {
        try {
            String filepath = "E://12月份.xlsx";
            ExcelUtils excelReader = new ExcelUtils(filepath);
            // 对读取Excel表格标题测试  
//          String[] title = excelReader.readExcelTitle();  
//          System.out.println("获得Excel表格的标题:");  
//          for (String s : title) {  
//              System.out.print(s + " ");  
//          }  

            // 对读取Excel表格内容测试  
            Map<Integer, Map<Integer, Object>> map = excelReader.readExcelContent();

            log.info("获得Excel表格的内容:");
            for (int i = 1; i <= map.size(); i++) {
                System.out.println(map.get(i));
            }
        } catch (FileNotFoundException e) {
            log.error("未找到指定路径的文件!");
            log.error(e);
            e.printStackTrace();
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }
}
