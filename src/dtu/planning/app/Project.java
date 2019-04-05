package dtu.planning.app;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dtu.planning.app.NotProjectLeaderException;

public class Project {
	private String name;
	private boolean isProjectInternal;
	private Employee projectLeader;
	private List<Activity> activities = new ArrayList<>();
	private int number;
	private GregorianCalendar startDate = new GregorianCalendar(0000, 1, 1);
	private GregorianCalendar endDate = new GregorianCalendar(3000, 1, 1);

	public Project(String name, boolean isProjectInternal, int projectCount) {
		this.name = name;
		this.isProjectInternal = isProjectInternal;
		this.number = projectCount;
//		this.number = generateNumber();
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isProjectInternal() {
		return this.isProjectInternal;
	}
	
	public void setProjectLeader(Employee employee) {
		this.projectLeader = employee;
	}
	
	public Employee getProjectLeader() {
		return projectLeader;
	}
	
	public int getProjectNumber() {
		return number;
	}

	public void addActivity(String name, int expectedStart, int expectedEnd, int expectedAmountOfHours, int associatedProjectNumber) {
		// Does not check that the projectID of the activity does match the project of which it is being assigned.
		activities.add(new Activity(name, expectedStart, expectedEnd, expectedAmountOfHours, associatedProjectNumber));
	}
	
	public void assignEmployee(String activityName, Employee projectLeader, Employee employee ) throws NotProjectLeaderException {
		// Check that projectleader is projectleader for this project. If not stop!
		if (this.projectLeader != projectLeader) {
			throw new NotProjectLeaderException("You are not the project leader for this project");
		}
		
		// Find and get the activity by name.
		Activity activity = getActivityByName(activityName);
		
		// assign employee to that activity
		activity.assignEmployee(projectLeader,employee);
	}
	
	public List<Employee> getEmployeesAssignedToActivity(String activityName) {
		// Find and get the activity by name.
		Activity activity = getActivityByName(activityName);
		
		// Return list of assigned employees
		return activity.getAssignedEmployees();
	}
	
	private Activity getActivityByName(String activityName) {
		// Find activity by name
		Optional r = activities
			      .stream()
			      .filter(b -> b.getName().equals(activityName))
			      .findFirst();
	    return (Activity) r.get();
	}
	
//	public boolean hasProjectLeader() {
//		return false;
//	}
	
	public void setStartDate(GregorianCalendar newStartDate) throws OperationNotAllowedException {
		if (newStartDate.after(endDate)) {
			throw new OperationNotAllowedException("The start date must be before the end date");
		}
		startDate = newStartDate;
	}
	
	public void setEndDate(GregorianCalendar newEndDate) throws OperationNotAllowedException {
		if (newEndDate.before(startDate)) {
			throw new OperationNotAllowedException("The end date must be after the start date");
		}
		endDate = newEndDate;
	}
	
	public GregorianCalendar getStartDate() {
		return startDate;
	}
	
	public GregorianCalendar getEndDate() {
		return endDate;
	}
	
//	private int generateNumber() {
//		// todo: include dato
//	}

}
