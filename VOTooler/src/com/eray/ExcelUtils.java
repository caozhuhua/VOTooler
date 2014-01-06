package com.eray;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


public class ExcelUtils {
	public static String[] getSheetName(String path) throws IOException {
		// 找到文件
		InputStream inp = new FileInputStream(path);
		// 打开excel
		HSSFWorkbook workBook = new HSSFWorkbook(new POIFSFileSystem(inp));
		int count = workBook.getNumberOfSheets();
		String[] sheetName = new String[count];
		for (int i = 0; i < count; i++) {
			sheetName[i] = workBook.getSheetName(i);
		}
		return sheetName;
	}
	public static String[][] getSheetData(String xlsPath, String sheetName) throws Exception{
		InputStream inp = new FileInputStream(xlsPath);
		HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(inp));
		HSSFSheet sheet = workbook.getSheet(sheetName);
		int rowNumber = sheet.getPhysicalNumberOfRows();
		HSSFRow row = sheet.getRow(0);
		if (rowNumber == 0 || row == null) {
			return new String[0][0];
		}
		int columnNumber = row.getLastCellNum();
		
		if (row.getCell(0) == null || row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_BLANK) {// 增加新规则，如果(0,0)为空，则整页sheet跳过
			return new String[0][0];
		}
		// 判断列值
		Object tmp;
		for (int i = 0; i < columnNumber; i++) {
			tmp = row.getCell(i);
			if (!"".equals(tmp) && tmp != null) {
				continue;
			} else {
				columnNumber = i;
				break;
			}
		}
		String[][] data = new String[rowNumber][columnNumber];
		boolean end = false;
		int rowNumber_legal = rowNumber;
		for (int rowIndex = 0; rowIndex < rowNumber; rowIndex++) {
			if (end) {// 如果到达地端
				break;
			}

			row = sheet.getRow(rowIndex);
			data[rowIndex] = new String[columnNumber];

			if (row == null) {// 如果整行为空时，直接退出
				rowNumber_legal = rowIndex;
				end = true;
				break;
			}

			tmp = row.getCell(0);
			if ("".equals(tmp) || tmp == null) {// 如果某行的第一列为空时，退出
				rowNumber_legal = rowIndex;
				break;
			}

			for (int columnIndex = 0; columnIndex < columnNumber; columnIndex++) {
				data[rowIndex][columnIndex] = PoiUtils.getStringValue(row
						.getCell(columnIndex));
			}

		}
		if (rowNumber_legal != rowNumber) {
			// 最终数据整理
			String[][] back = new String[rowNumber_legal][columnNumber];
			System.arraycopy(data, 0, back, 0, rowNumber_legal);

			return back;
		} else {
			return data;
		}
	}
}
