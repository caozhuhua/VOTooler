package com.eray;

public class ItemClassVO {
	public String classStr;
	public String classInitItem;
	public String classInitItemValue;
	public String getClassInitItemValue() {
		return classInitItemValue;
	}
	public void setClassInitItemValue(String classInitItemValue) {
		this.classInitItemValue = classInitItemValue;
	}
	public String getClassStr() {
		return classStr;
	}
	public void setClassStr(String classStr) {
		this.classStr = classStr.trim();
	}
	public String getClassInitItem() {
		return classInitItem;
	}
	public void setClassInitItem(String classInitItem) {
		this.classInitItem = classInitItem.trim();
	}
}
