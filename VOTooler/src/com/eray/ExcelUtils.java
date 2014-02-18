package com.eray;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.eray.vo.SheetVO;


public class ExcelUtils {

	public static ArrayList<LogicVO> praseVOLogic(String voLogic){
		ArrayList<LogicVO> list = new ArrayList<LogicVO> ();
		voLogic = voLogic.trim();
		if("".equals(voLogic)){
			return list;
		}
		String [] strList = voLogic.split(";");
		for(int i = 0;i<strList.length;++i){
			String itemStr = strList[i];
			String [] typeList = itemStr.split(":");
			if(typeList.length==2){
				LogicVO logicVO = new LogicVO();
				String type = typeList[0];
				logicVO.setVoType(type);
				String [] numList = typeList[1].split(",");
				if(numList.length==2){
					logicVO.setNum(numList[0]);
					logicVO.setObjType(numList[1]);
					list.add(logicVO);
				}else{
					System.out.println( typeList[1]+"异常了");
				}
			}else{
				System.out.println(itemStr+"异常了");
			}
		}
		return list;
	}
	
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
	public static String lowerCaseFirstLetter(String originalLeter){
		return originalLeter.substring(0, 1).toLowerCase()+ originalLeter.substring(1);
	}
	public static ArrayList<SheetVO> getSheetList(List<String> listFiles) throws IOException{
		ArrayList<SheetVO> list = new ArrayList<SheetVO>();
		for (String s : listFiles) {
			if (FilenameUtils.isExtension(s, "xls")) {
				String xlsName = FilenameUtils.getBaseName(s);
				String[] sheets = ExcelUtils.getSheetName(s);
				for(int i = 0;i<sheets.length;++i){
					SheetVO vo = new SheetVO();
					vo.setExcelName(xlsName);
					vo.setSheetName(sheets[i]);
					vo.setExcelNativePath(s);
					list.add(vo);
				}
			
			}
		}
		return list;
	}
	
	
}
