package com.jaya.cmt;


public class ChangeEmailRequest {
    private Integer userId;
    private String currentEmail;
    private String newEmail;
    private String confirmEmail;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getCurrentEmail() {
		return currentEmail;
	}
	public void setCurrentEmail(String currentEmail) {
		this.currentEmail = currentEmail;
	}
	public String getNewEmail() {
		return newEmail;
	}
	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}
	public String getConfirmEmail() {
		return confirmEmail;
	}
	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}

}
