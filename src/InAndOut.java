
import java.util.Date;


class InAndOut {
	public Date getLoggingTime() {
		return loggingTime;
	}
	public void setLoggingTime(Date loggingTime) {
		this.loggingTime = loggingTime;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isPointed() {
		return pointed;
	}
	public void setPointed(boolean pointed) {
		this.pointed = pointed;
	}
	public boolean isSystemGenerated() {
		return systemGenerated;
	}
	public void setSystemGenerated(boolean systemGenerated) {
		this.systemGenerated = systemGenerated;
	}
	public int getRegularizationType() {
		return regularizationType;
	}
	public void setRegularizationType(int regularizationType) {
		this.regularizationType = regularizationType;
	}
	public boolean isRegularization() {
		return regularization;
	}
	public void setRegularization(boolean regularization) {
		this.regularization = regularization;
	}
	public Date getModifyingTime() {
		return modifyingTime;
	}
	public void setModifyingTime(Date modifyingTime) {
		this.modifyingTime = modifyingTime;
	}
	public Boolean getIsOutsideSite() {
		return isOutsideSite;
	}
	public void setIsOutsideSite(Boolean isOutsideSite) {
		this.isOutsideSite = isOutsideSite;
	}
	Date loggingTime;
	Date time;
	int day;
	int month;
	int year;
	int week;
	String type;
	boolean pointed;
	boolean systemGenerated;
	int regularizationType;
	boolean regularization;
	Date modifyingTime;
	Boolean isOutsideSite;
}
