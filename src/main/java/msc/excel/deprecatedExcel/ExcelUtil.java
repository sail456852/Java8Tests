package msc.excel.deprecatedExcel;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class ExcelUtil {
	static final Log log = LogFactory.getLog(ExcelUtil.class);
	
	public static void main(String[] args) throws Exception {
		File file = new File("D:\\3.xlsx");
		List<String[]> result = getExceltData(file, 2, null, 0, 2);
		for (int i = 0; i < result.size(); i++) {
			for (int j = 0; j < result.get(i).length; j++) {
				System.out.print(result.get(i)[j] + "\t\t");
			}

			System.out.println();
		}
	}
	
	/**
	 * 读取excel
	 * 
	 * @param file
	 * @param startRow 开始行，从0开始
	 * @param endRow 结束行，从0开始
	 * @param startCol 开始列，从0开始
	 * @param endCol 结束列，从0开始
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<String[]> getExceltData(File file, Integer startRow, Integer endRow, Integer startCol, Integer endCol) throws FileNotFoundException, IOException {
		List<String[]> result = new ArrayList<String[]>();
		Workbook wb = null;
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			wb = WorkbookFactory.create(in);

			if (wb instanceof XSSFWorkbook) {
				result = getXSSFResult(wb, startRow, endRow, startCol, endCol);
			} else if (wb instanceof HSSFWorkbook) {
				result = getHSSFResult(wb, startRow, endRow, startCol, endCol);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}

//		String[][] returnArray = new String[0][0];
//		if (result != null && result.size() > 0) {
//			returnArray = new String[result.size()][result.get(0).length];
//			for (int i = 0; i < returnArray.length; i++) {
//				returnArray[i] = (String[]) result.get(i);
//			}
//		}      

		return result;
	}
	
	/**
	 * 获取表格内容
	 * 
	 * @param wb
	 * @param startRow 开始行，从0开始
	 * @param endRow 结束行，从0开始
	 * @param startCol 开始列，从0开始
	 * @param endCol 结束列，从0开始
	 * @return
	 */
	private static List<String[]> getXSSFResult(Workbook wb, Integer startRow, Integer endRow, Integer startCol, Integer endCol) {
		List<String[]> result = new ArrayList<String[]>();

		for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
			XSSFSheet sheet = (XSSFSheet) wb.getSheetAt(sheetIndex);
			int firstRowNum = sheet.getFirstRowNum();
			int lastRowNum = sheet.getLastRowNum();
			
			if (startRow == null || startRow < firstRowNum) {
				startRow = firstRowNum;
			}

			if (endRow == null || endRow > lastRowNum) {
				endRow = lastRowNum;
			}
			
			for (int rowIndex = startRow; rowIndex <= endRow; rowIndex++) {
				XSSFRow row = sheet.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				
				int firstCellNum = row.getFirstCellNum();
				int lastCellNum = row.getLastCellNum();
				
				if (startCol == null || startCol < firstCellNum) {
					startCol = firstCellNum;
				}
				
				if (endCol == null || endCol > lastCellNum) {
					endCol = lastCellNum;
				}
				
				String[] values = new String[endCol - startCol + 1];
				Arrays.fill(values, "");

				int blankCellCount = 0;
				for (int colIndex = startCol; colIndex <= endCol; colIndex++) {
					String value = "";
					XSSFCell cell = row.getCell(colIndex);
					
					if (cell != null) {
						// 注意：一定要设成这个，否则可能会出现乱码
						// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						switch (cell.getCellType()) {
						case HSSFCell.CELL_TYPE_NUMERIC:
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								Date date = cell.getDateCellValue();
								if (date != null) {
									value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
								} else {
									value = "";
								}
							} else {
								value = new DecimalFormat("0").format(cell.getNumericCellValue());
							}
							break;
						case HSSFCell.CELL_TYPE_STRING:
							value = cell.getStringCellValue();
							break;
						case HSSFCell.CELL_TYPE_FORMULA:
							// 导入时如果为公式生成的数据则无值
							if (!cell.getStringCellValue().equals("")) {
								value = cell.getStringCellValue();
							} else {
								value = cell.getNumericCellValue() + "";
							}
							break;
						case HSSFCell.CELL_TYPE_BLANK:
							blankCellCount ++;
							break;
						case HSSFCell.CELL_TYPE_BOOLEAN:
							value = cell.getBooleanCellValue() + "";
							break;
						case HSSFCell.CELL_TYPE_ERROR:
							value = cell.getErrorCellValue() + "";
							break;
						default:
							value = "";
						}
					}

					values[colIndex] = rightTrim(value.trim());
				}

				if (blankCellCount < endCol - startCol + 1) {
					result.add(values);
				}
			}
		}

		return result;
	}

	/**
	 * 获取表格内容
	 * 
	 * @param wb
	 * @param startRow 开始行，从0开始
	 * @param endRow 结束行，从0开始
	 * @param startCol 开始列，从0开始
	 * @param endCol 结束列，从0开始
	 * @return
	 */
	private static List<String[]> getHSSFResult(Workbook wb, Integer startRow, Integer endRow, Integer startCol, Integer endCol) {
		List<String[]> result = new ArrayList<String[]>();

		for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
			HSSFSheet sheet = (HSSFSheet) wb.getSheetAt(sheetIndex);
			int firstRowNum = sheet.getFirstRowNum();
			int lastRowNum = sheet.getLastRowNum();
			
			if (startRow == null || startRow < firstRowNum) {
				startRow = firstRowNum;
			}

			if (endRow == null || endRow > lastRowNum) {
				endRow = lastRowNum;
			}
			
			for (int rowIndex = startRow; rowIndex <= endRow; rowIndex++) {
				HSSFRow row = sheet.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				
				int firstCellNum = row.getFirstCellNum();
				int lastCellNum = row.getLastCellNum();
				
				if (startCol == null || startCol < firstCellNum) {
					startCol = firstCellNum;
				}
				
				if (endCol == null || endCol > lastCellNum) {
					endCol = lastCellNum;
				}

				String[] values = new String[endCol - startCol + 1];
				Arrays.fill(values, "");

				int blankCellCount = 0;
				for (int colIndex = startCol; colIndex <= endCol; colIndex++) {
					String value = "";
					HSSFCell cell = row.getCell(colIndex);
					if (cell != null) {
						// 注意：一定要设成这个，否则可能会出现乱码
						// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						switch (cell.getCellType()) {
						case HSSFCell.CELL_TYPE_NUMERIC:
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								Date date = cell.getDateCellValue();
								if (date != null) {
									value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
								} else {
									value = "";
								}
							} else {
								value = new DecimalFormat("0").format(cell.getNumericCellValue());
							}
							break;
						case HSSFCell.CELL_TYPE_STRING:
							value = cell.getStringCellValue();
							break;
						case HSSFCell.CELL_TYPE_FORMULA:
							// 导入时如果为公式生成的数据则无值
							if (!cell.getStringCellValue().equals("")) {
								value = cell.getStringCellValue();
							} else {
								value = cell.getNumericCellValue() + "";
							}
							break;
						case HSSFCell.CELL_TYPE_BLANK:
							blankCellCount++;
							break;
						case HSSFCell.CELL_TYPE_BOOLEAN:
							value = cell.getBooleanCellValue() + "";
							break;
						case HSSFCell.CELL_TYPE_ERROR:
							value = cell.getErrorCellValue() + "";
							break;
						default:
							value = "";
						}
					}

					values[colIndex] = rightTrim(value.trim());
				}

				if (blankCellCount < endCol - startCol + 1) {
					result.add(values);
				}
			}
		}

		return result;
	}

	/**
	 * 去掉字符串右边的空格
	 * 
	 * @param str 要处理的字符串
	 * @return 处理后的字符串
	 * @author mickey
	 */

	public static String rightTrim(String str) {
		if (str == null) {
			return "";
		}

		int length = str.length();
		for (int i = length - 1; i >= 0; i--) {
			if (str.charAt(i) != 0x20) {
				break;
			}

			length--;
		}

		return str.substring(0, length);
	}

	/**
	 * 把数据作为excel导出（需保证表头与列的别名一致）
	 * 
	 * @param head
     * @param content
     * @param outputFile
     * @author cony
	 * @throws IOException 
	 */
	public static void export(String [] head,List<Map<String, Object>> content,File outputFile) throws IOException{
		 XSSFWorkbook workbook=new XSSFWorkbook();
         XSSFSheet sheet=workbook.createSheet("sheet");
         //创建表头
         XSSFRow row = sheet.createRow((short)0);
         for(int i = 0;i<head.length;i++){
        	 XSSFCell cell=row.createCell((short)i);
        	 cell.setCellValue(head[i]);
         }
         //行
         for(int i = 0;i < content.size();i++){
        	 //列
        	 XSSFRow contentRow = sheet.createRow(i+1);
        	 for(int j = 0;j < head.length;j++){
        		 XSSFCell cell=contentRow.createCell(j);
                 cell.setCellValue(String.valueOf(content.get(i).get(head[j])));
        	 }
         }
         FileOutputStream FOut = new FileOutputStream(outputFile);
         try {
			workbook.write(FOut);
			FOut.flush();
		} catch (IOException e) {
             log.info("export() \"e\": " + e);
			e.printStackTrace();
		}finally{
			FOut.close();
		}
	}


	/**
	 * add by Geno
	 * @param filePath 文件路径
	 * @param sheetName 表单名
	 * @param format 表头格式
	 * @param data 内容
     */
	public static void export(String filePath, String sheetName, JSONObject format, List<JSONObject> data){
		//相关excel参数
		//开始行 0 为第一行
		int beginColumn = 0;
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(sheetName);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow(beginColumn);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.forInt(2)); // 创建一个居中格式 = 2 be CENTER @author: eugene @date: 2019/1/8 
		// 创建表头
		if(format != null) {
			int columnNum = 0;
			for (String s : format.keySet()) {
				HSSFCell cell = row.createCell(columnNum);
				cell.setCellValue(s);
				cell.setCellStyle(style);
				columnNum++;
			}
		}
		//写数据
		int rowNum = beginColumn+1;
		for (JSONObject jsonObject : data) {
			row = sheet.createRow(rowNum);
			// 第四步，创建单元格，并设置值
			int valueNum = 0;
			for (String s : jsonObject.keySet()) {
				HSSFCell cell = row.createCell(valueNum);
				cell.setCellValue(jsonObject.getString(s));
				cell.setCellStyle(style);
				valueNum++;
			}
			rowNum++;
		}
		//保存到文件
		try {
			FileOutputStream fout = new FileOutputStream(filePath);
			wb.write(fout);
			fout.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
