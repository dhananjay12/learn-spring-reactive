package com.mynotes.spring.reactive.reactivewebapp;

public class Hobby {

	private int personId;

	private String hobby;

	public Hobby() {

	}

	public Hobby(int personId, String hobby) {
		super();
		this.personId = personId;
		this.hobby = hobby;
	}

	public int getPersonId() {
		return personId;
	}

	public String getHobby() {
		return hobby;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

}
