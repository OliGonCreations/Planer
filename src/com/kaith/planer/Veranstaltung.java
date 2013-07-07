package com.kaith.planer;

public class Veranstaltung {

	String name, description;
	int id, beginning, length, day;

	public Veranstaltung() {

	}

	public Veranstaltung(int id, String name, String description, int beginning, int length, int day) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.beginning = beginning;
		this.length = length;
		this.day = day;
	}

	public Veranstaltung(String name, String description, int beginning, int length, int day) {
		this.name = name;
		this.description = description;
		this.beginning = beginning;
		this.length = length;
		this.day = day;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBeginning() {
		return beginning;
	}

	public void setBeginning(int beginning) {
		this.beginning = beginning;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

}
