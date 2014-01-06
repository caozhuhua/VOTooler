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
		// �ҵ��ļ�
		InputStream inp = new FileInputStream(path);
		// ��excel
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
		
		if (row.getCell(0) == null || row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_BLANK) {// �����¹������(0,0)Ϊ�գ�����ҳsheet����
			return new String[0][0];
		}
		// �ж���ֵ
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
			if (end) {// �������ض�
				break;
			}

			row = sheet.getRow(rowIndex);
			data[rowIndex] = new String[columnNumber];

			if (row == null) {// �������Ϊ��ʱ��ֱ���˳�
				rowNumber_legal = rowIndex;
				end = true;
				break;
			}

			tmp = row.getCell(0);
			if ("".equals(tmp) || tmp == null) {// ���ĳ�еĵ�һ��Ϊ��ʱ���˳�
				rowNumber_legal = rowIndex;
				break;
			}

			for (int columnIndex = 0; columnIndex < columnNumber; columnIndex++) {
				data[rowIndex][columnIndex] = PoiUtils.getStringValue(row
						.getCell(columnIndex));
			}

		}
		if (rowNumber_legal != rowNumber) {
			// ������������
			String[][] back = new String[rowNumber_legal][columnNumber];
			System.arraycopy(data, 0, back, 0, rowNumber_legal);

			return back;
		} else {
			return data;
		}
	}
}
