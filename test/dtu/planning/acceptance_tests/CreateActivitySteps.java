package dtu.planning.acceptance_tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import dtu.planning.app.Activity;
import dtu.planning.app.ActivityNotFoundException;
import dtu.planning.app.Employee;
import dtu.planning.app.NotProjectLeaderException;
import dtu.planning.app.OperationNotAllowedException;
import dtu.planning.app.Project;

public class CreateActivitySteps {
		
	private ErrorMessageHolder errorMessageHolder;
	private ProjectHolder projectHolder;
	private EmployeeHolder employeeHolder; 
	private ActivityHolder activityHolder; 
	private Project project;
		
	public CreateActivitySteps(ErrorMessageHolder errorMessageHolder, ProjectHolder projectHolder, EmployeeHolder employeeHolder, ActivityHolder activityHolder) {
		this.errorMessageHolder = errorMessageHolder; 
		this.projectHolder = projectHolder; 
		this.employeeHolder = employeeHolder; 
		this.activityHolder = activityHolder; 
	}


	@Given("project leader has initials {string}")
	public void projectLeaderHasInitials(String initials){
		Employee employee = new Employee("Bob Small",initials);
		employeeHolder.setEmployee(employee);
		projectHolder.getProject().setProjectLeader(employee);	
	}

	@When("the project leader {string} creates an activity {string}")
	public void theProjectLeaderCreatesAnActivity(String initials, String name) throws NotProjectLeaderException, OperationNotAllowedException {
		assertTrue(projectHolder.getProject().getProjectLeader().getInitials().equals(initials));
		Activity activity = new Activity(name, null, null, 0.0);
		projectHolder.getProject().addActivity(activity,initials);
	}

	@Then("the activity {string} is created for the project")
	public void theActivityIsCreatedForTheProject(String name) throws ActivityNotFoundException {
		assertThat(projectHolder.getProject().getActivityByName(name).getName(),is(equalTo(name)));    
	}

	@When("an employee {string} creates an activity {string}")
	public void anEmployeeCreatesAnActivity(String initials, String name) {
	
		Employee employee = new Employee(null,initials);
		try {
			Activity activity = new Activity(name, null, null, 0);
			projectHolder.getProject().addActivity(activity,employee.getInitials());
			assertTrue(false);
		} catch (NotProjectLeaderException e) {
			errorMessageHolder.setErrorMessage(e.getMessage());
		} catch (OperationNotAllowedException e) {
			errorMessageHolder.setErrorMessage(e.getMessage());
		}
	}
	

	@When("the project leader edits the start week of the activity to {int}\\/{int}")
	public void theProjectLeaderEditsTheStartWeekOfTheActivityTo(Integer numWeekYear, Integer year) throws OperationNotAllowedException {
		GregorianCalendar startWeek = new GregorianCalendar();
        startWeek.setWeekDate(year, numWeekYear, GregorianCalendar.SUNDAY);
		
		activityHolder.getActivity().setStartWeek(startWeek); 
	}

	@Then("the start week of the project is {int}\\/{int}")
	public void theStartWeekOfTheProjectIs(Integer numWeekYear, Integer year) {
		GregorianCalendar compareWeek = new GregorianCalendar();
        compareWeek.setWeekDate(year, numWeekYear, GregorianCalendar.SUNDAY);
		
		assertEquals(compareWeek.get(Calendar.WEEK_OF_YEAR),activityHolder.getActivity().getStartWeek().get(Calendar.WEEK_OF_YEAR)); 
		assertEquals(compareWeek.get(Calendar.YEAR),activityHolder.getActivity().getStartWeek().get(Calendar.YEAR)); 
	}

	
	
		
}
