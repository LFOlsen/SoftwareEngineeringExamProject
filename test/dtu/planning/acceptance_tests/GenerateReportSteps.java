package dtu.planning.acceptance_tests;

import cucumber.api.java.en.When;
import dtu.planning.app.NotProjectLeaderException;
import dtu.planning.app.PlanningApp;
import dtu.planning.app.TimeRegistration;

public class GenerateReportSteps {
	// "Global" variable holders so steps can be used across features
	private PlanningAppHolder planningAppHolder;
	private ProjectHolder projectHolder;
	private EmployeeHolder employeeHolder;
	private ErrorMessageHolder errorMessageHolder;
	private ActorHolder actorHolder;
	
	// Private variables, will give problems when otheres need to use them. Create holder then?
	private TimeRegistration timeRegistration;

	public GenerateReportSteps(PlanningAppHolder planningAppHolder, ErrorMessageHolder errorMessageHolder, ProjectHolder projectHolder, EmployeeHolder employeeHolder, ActorHolder actorHolder) {
		this.planningAppHolder = planningAppHolder;
		this.errorMessageHolder = errorMessageHolder;
		this.projectHolder = projectHolder;
		this.employeeHolder = employeeHolder;
		this.actorHolder = actorHolder;
	}
	
	@When("The actor generates a report for the project")
	public void theActorGeneratesAReportForTheProject() {
		PlanningApp planningApp = planningAppHolder.getPlanningApp();
		try {
			planningApp.generateReport(projectHolder.getProject(), actorHolder.getActor());
		} catch (NotProjectLeaderException e) {
			errorMessageHolder.setErrorMessage(e.getMessage());
		}
	}
}