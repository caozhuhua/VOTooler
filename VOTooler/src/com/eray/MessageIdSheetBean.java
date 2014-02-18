package com.eray;

public class MessageIdSheetBean {
	public String messageId;
	public String messageVO;
	public String actionHandler;
	public String messageIDStr;
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getMessageVO() {
		return messageVO;
	}
	public void setMessageVO(String messageVO) {
		this.messageVO = messageVO;
	}
	public String getActionHandler() {
		return actionHandler;
	}
	public void setActionHandler(String actionHandler) {
		this.actionHandler = actionHandler.trim();
	}
	public String getMessageIDStr() {
		return messageIDStr;
	}
	public void setMessageIDStr(String messageIDStr) {
		this.messageIDStr = messageIDStr;
	}
	public String messageType;
	public String comment;
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType.trim();
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment.trim();
	}
	
}
