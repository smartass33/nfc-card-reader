package pointeuse.pojo;

import java.util.List;

public class SimpleEmployee {

	public String firstName;
	public String lastName;
	public Boolean status;
	public List<SimpleInAndOut> inOrOuts;
	
	
	public List<SimpleInAndOut> getInOrOuts() {
		return inOrOuts;
	}
	public void setInAndOuts(List<SimpleInAndOut> inOrOuts) {
		this.inOrOuts = inOrOuts;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}

	
}
