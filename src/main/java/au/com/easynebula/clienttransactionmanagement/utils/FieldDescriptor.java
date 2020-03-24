package au.com.easynebula.clienttransactionmanagement.utils;

import java.lang.reflect.Field;

public class FieldDescriptor {
	private int startAt;
	private int width;
	private Field field;
	private boolean isDate;

	int getStartAt() {
		return startAt;
	}

	public void setStartAt(final int startAt) {
		this.startAt = startAt;
	}

	int getWidth() {
		return width;
	}

	public void setWidth(final int width) {
		this.width = width;
	}

	Field getField() {
		return field;
	}

	public void setField(final Field field) {
		this.field = field;
	}

	public boolean isDate() {
		return isDate;
	}

	public void setDate(final boolean date) {
		isDate = date;
	}
}
