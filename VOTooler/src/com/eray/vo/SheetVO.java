package com.eray.vo;

public class SheetVO {
	private String sheetName;
	private String excelName;
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public String getExcelName() {
		return excelName;
	}
	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}
	private String excelNativePath;
	public String getExcelNativePath() {
		return excelNativePath;
	}
	public void setExcelNativePath(String excelNativePath) {
		this.excelNativePath = excelNativePath;
	}
}
