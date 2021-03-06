package dtu.planning.acceptance_tests;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.GregorianCalendar;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import dtu.planning.app.Activity;
import dtu.planning.app.PlanningApp;
import dtu.planning.app.OperationNotAllowedException;

public class RegularActivitySteps {
    private PlanningAppHolder planningAppHolder;
    private ErrorMessageHolder errorMessageHolder;
    private EmployeeHolder employeeHolder;
    private ActivityHolder activityHolder;

    public RegularActivitySteps(PlanningAppHolder planningAppHolder, ErrorMessageHolder errorMessageHolder,
            EmployeeHolder employeeHolder, ActivityHolder activityHolder) {
        this.planningAppHolder = planningAppHolder;
        this.errorMessageHolder = errorMessageHolder;
        this.employeeHolder = employeeHolder;
        this.activityHolder = activityHolder;
    }

    @Given("I have the regular activity with name {string} , start: week {int} of year {int} and end: week {int} of year {int}")
    public void iHaveTheRegularActivityWithNameStartWeekOfYearAndEndWeekOfYear(String name, Integer startWeek,
            Integer startYear, Integer endWeek, Integer endYear) {
        // GregorianCalendar has sunday as the first day of the week and saturday as the last day
        GregorianCalendar start = new GregorianCalendar();
        start.setWeekDate(startYear, startWeek, GregorianCalendar.SUNDAY);

        GregorianCalendar end = new GregorianCalendar();
        end.setWeekDate(endYear, endWeek, GregorianCalendar.SATURDAY);

        activityHolder.setActivity(new Activity(name, start, end));
    }

    @Given("the regular activity is in the system")
    public void theRegularActivityIsInTheSystem() throws OperationNotAllowedException {
        planningAppHolder.getPlanningApp().addRegularActivity(activityHolder.getActivity(),
                employeeHolder.getEmployee().getInitials());
    }

    @Given("the regular activity is not in the system")
    public void theRegularActivityIsNotInTheSystem() {
        PlanningApp planningApp = planningAppHolder.getPlanningApp();
        assertThat(planningApp.getRegularActivities(), not(hasItem(activityHolder.getActivity())));
    }

    @When("I create the regular activity and assign the employee to it")
    public void iCreateTheRegularActivityAndAssignTheEmployeeToIt() throws OperationNotAllowedException {
        planningAppHolder.getPlanningApp().addRegularActivity(activityHolder.getActivity(),
                employeeHolder.getEmployee().getInitials());
    }

    @When("I change the start week of the regular activity to week {int} of year {int}")
    public void iChangeTheStartWeekOfTheRegularActivityToWeekOfYear(Integer startWeek, Integer startYear) {
        GregorianCalendar start = new GregorianCalendar();
        start.setWeekDate(startYear, startWeek, GregorianCalendar.SUNDAY);

        try {
            planningAppHolder.getPlanningApp().editStartWeekOfRegular(start, activityHolder.getActivity().getName());
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @When("I change the end week of the regular activity to week {int} of year {int}")
    public void iChangeTheEndWeekOfTheRegularActivityToWeekOfYear(Integer endWeek, Integer endYear) {
        GregorianCalendar end = new GregorianCalendar();
        end.setWeekDate(endYear, endWeek, GregorianCalendar.SATURDAY);

        try {
            planningAppHolder.getPlanningApp().editEndWeekOfRegular(end, activityHolder.getActivity().getName());
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the regular activity is created and the employee is assigned to it")
    public void theRegularActivityIsCreatedAndTheEmployeeIsAssignedToIt() {
        PlanningApp planningApp;
        planningApp = planningAppHolder.getPlanningApp();
        assertThat(planningApp.getRegularActivities(), hasItem(activityHolder.getActivity()));
        assertThat(activityHolder.getActivity().getAssignedEmployees(), hasItem(employeeHolder.getEmployee()));
    }

    @Then("the start week of the regular activity is week {int} of year {int}")
    public void thenTheStartWeekOfTheRegularActivityIsWeekOfYear(Integer startWeek, Integer startYear)
            throws OperationNotAllowedException {
        PlanningApp planningApp = planningAppHolder.getPlanningApp();
        GregorianCalendar start = new GregorianCalendar();
        start.setWeekDate(startYear, startWeek, GregorianCalendar.SUNDAY);
        assertEquals(planningApp.searchForRegActivity(activityHolder.getActivity().getName()).getStartWeek()
                .get(GregorianCalendar.DATE), start.get(GregorianCalendar.DATE));
        assertEquals(planningApp.searchForRegActivity(activityHolder.getActivity().getName()).getStartWeek()
                .get(GregorianCalendar.MONTH), start.get(GregorianCalendar.MONTH));
        assertEquals(planningApp.searchForRegActivity(activityHolder.getActivity().getName()).getStartWeek()
                .get(GregorianCalendar.YEAR), start.get(GregorianCalendar.YEAR));
    }

    @Then("the end week of the regular activity is week {int} of year {int}")
    public void thenTheEndWeekOfTheRegularActivityIsWeekOfYear(Integer endWeek, Integer endYear)
            throws OperationNotAllowedException {
        PlanningApp planningApp = planningAppHolder.getPlanningApp();
        GregorianCalendar end = new GregorianCalendar();
        end.setWeekDate(endYear, endWeek, GregorianCalendar.SATURDAY);
        assertEquals(planningApp.searchForRegActivity(activityHolder.getActivity().getName()).getEndWeek()
                .get(GregorianCalendar.DATE), end.get(GregorianCalendar.DATE));
        assertEquals(planningApp.searchForRegActivity(activityHolder.getActivity().getName()).getEndWeek()
                .get(GregorianCalendar.MONTH), end.get(GregorianCalendar.MONTH));
        assertEquals(planningApp.searchForRegActivity(activityHolder.getActivity().getName()).getEndWeek()
                .get(GregorianCalendar.YEAR), end.get(GregorianCalendar.YEAR));
    }
}
