package com.eray;

import java.util.ArrayList;

public class ErayBean {
	public String fieldName;
	public String type;
	public String count;
	public String comment;
	public String fieldNa;
	public String methodStr;
	public String getMethodStr() {
		return methodStr;
	}
	public void setMethodStr(String methodStr) {
		this.methodStr = methodStr;
	}
	public String getFieldNa() {
		return fieldNa;
	}
	public void setFieldNa(String fieldNa) {
		this.fieldNa = comment;
	}
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
	public ArrayList<LogicVO> voLogic;
	public ArrayList<LogicVO> getVoLogic() {
		return voLogic;
	}
	public void setVoLogic(ArrayList<LogicVO> voLogic) {
		this.voLogic = voLogic;
	}
	//1,¶¯Ì¬£¬2£¬¹Ì¶¨
	public String countType="";
	public String getCountType() {
		return countType;
	}
	public void setCountType(String countType) {
		this.countType = countType;
	}


}
