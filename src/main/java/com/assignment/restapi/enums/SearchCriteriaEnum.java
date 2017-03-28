package com.assignment.restapi.enums;

public enum SearchCriteriaEnum {
	FILENAME("FileName") ,USERNAME ("FileUserName");
	private String criteriaField;
	 
	private SearchCriteriaEnum(String criteriaField) {
		this.criteriaField = criteriaField;
	}
 
	public String getCriteriaField() {
		return criteriaField;
	}

}
