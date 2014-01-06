package com.eray;

public class ErayBean {
	public String fieldName;
	public String type;
	public String count;
	public String comment;
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count.trim();
	}
	


}
