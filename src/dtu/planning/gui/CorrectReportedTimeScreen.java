package dtu.planning.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.BorderUIResource;

import dtu.planning.app.PlanningApp;
import dtu.planning.app.Activity;
import dtu.planning.app.ActivityNotFoundException;
import dtu.planning.app.Employee;
import dtu.planning.app.TimeRegistration;
import dtu.planning.app.OperationNotAllowedException;

public class CorrectReportedTimeScreen {
	private MainScreen parentWindow;
	private PlanningApp planningApp;
	private JPanel panelCorrectTime;
	private JPanel panelSelectEmployee;
	private JPanel panelSelectDate;
	private JPanel panelSelectTimeReg;
	private JPanel panelEditTime;
	private JTextField searchField;
	private JLabel lblPhase;
//	private JLabel employeeReminderField;
	private JList<Employee> listSearchResult;
	private JList<Activity> listTimeRegistrations;
	private JScrollPane listScrollPane;
	private DefaultListModel<Employee> searchResults;
	private DefaultListModel<Activity> timeRegistrations;
	private List<Integer> relevantProjectNumbers;
	private Employee employee;
	private Activity activity;
	private GregorianCalendar date;
	private int projectNumber;
	private JButton btnBack;
	private JButton btnNext;
	private JButton btnPrevious;
	private JComboBox<Integer> hoursComboBox;
	private JComboBox<Integer> minutesComboBox;
	private JLabel lblReportingDetails;
	private JComboBox<String> dayComboBox;
	private JComboBox<String> monthComboBox;
	private JComboBox<String> yearComboBox;
	private float amountOfTime;
  	
	public CorrectReportedTimeScreen(PlanningApp planningApp, MainScreen parentWindow) {
		this.planningApp = planningApp;
		this.parentWindow = parentWindow;
		initialize();
	}

	private void initialize() {
		panelCorrectTime = new JPanel();
		panelCorrectTime.setVisible(false);
		parentWindow.addPanel(panelCorrectTime);
		panelCorrectTime.setLayout(null);
		panelCorrectTime.setBorder(BorderFactory.createTitledBorder(
                "Correct Reported Time"));
		
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				clear();
				parentWindow.setVisible(true);
			}
		});
		btnBack.setBounds(21, 28, 59, 29);
		panelCorrectTime.add(btnBack);
		
		// ––––––––––– Step 1: Select Employee ––––––––––-
		panelSelectEmployee = new JPanel();
		panelSelectEmployee.setBounds(0, 28, 350, 500);
		panelCorrectTime.add(panelSelectEmployee);
		panelSelectEmployee.setLayout(null);
		panelSelectEmployee.setVisible(true);
		
		lblPhase = new JLabel();
		lblPhase.setHorizontalAlignment(SwingConstants.LEFT);
		lblPhase.setVerticalAlignment(SwingConstants.TOP);
		lblPhase.setBounds(160, 0, 150, 90);
		StringBuffer b = new StringBuffer();
		b.append("<html><h2>&nbsp;Step 1</h2>Select Employee</html>");
		lblPhase.setText(b.toString());
		panelSelectEmployee.add(lblPhase);
		
		JLabel lblEmployee = new JLabel("Employee:");
		lblEmployee.setBounds(80, 100, 150, 30);
		panelSelectEmployee.add(lblEmployee);
		
		searchField = new JTextField();
		searchField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchEmployees();
			}
		});
		searchField.setBounds(190, 100, 140, 30);
		panelSelectEmployee.add(searchField);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchEmployees();
			}
		});
		btnSearch.setBounds(160, 135, 100, 30);
		panelSelectEmployee.add(btnSearch);
		btnSearch.getRootPane().setDefaultButton(btnSearch);
		
		searchResults = new DefaultListModel<>();
		listSearchResult = new JList<Employee>(searchResults);
		listSearchResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listSearchResult.setSelectedIndex(0);
		listSearchResult.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (listSearchResult.getSelectedIndex() == -1) {
					searchField.setText("");
				} else {
					searchField.setText(listSearchResult.getSelectedValue().getName());
				}
				
			}
		});
		listSearchResult.setVisibleRowCount(5);
        listScrollPane = new JScrollPane(listSearchResult);

        listScrollPane.setBounds(80, 170, 250, 100);
		panelSelectEmployee.add(listScrollPane);
		
		btnNext = new JButton();
		b = new StringBuffer(); b.append("<html><h2>Next</h2></html>");
		btnNext.setText(b.toString());
		btnNext.setBounds(245, 290, 90, 40);
		panelSelectEmployee.add(btnNext);
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (listSearchResult.getSelectedIndex() == -1) {
					System.out.println("You need to select an employee");
				} else {
					employee = listSearchResult.getSelectedValue();
//					findRelevantActivities();
					
					panelSelectEmployee.setVisible(false);
					panelSelectDate.setVisible(true);
				}
			}
		});
		// –––––––––––––––––––––––––––––––––––––––––––––––
		
		// ––––––––––– Step 2: Select Date ––––––––––––––-
		panelSelectDate = new JPanel();
		panelSelectDate.setBounds(0, 28, 350, 500);
		panelCorrectTime.add(panelSelectDate);
		panelSelectDate.setLayout(null);
		panelSelectDate.setVisible(false);
		
		lblPhase = new JLabel();
		lblPhase.setHorizontalAlignment(SwingConstants.LEFT);
		lblPhase.setVerticalAlignment(SwingConstants.TOP);
		lblPhase.setBounds(160, 0, 150, 90);
		b = new StringBuffer();
		b.append("<html><h2>&nbsp;Step 2</h2>Select Date</html>");
		lblPhase.setText(b.toString());
		panelSelectDate.add(lblPhase);
		
		String[] comboBoxDates = new String[32];
		comboBoxDates[0] = "Day";
		for (int i = 1; i < 32; i++) {
			comboBoxDates[i] = "" + i;
		}
		dayComboBox = new JComboBox<>(comboBoxDates);
		dayComboBox.setBounds(80, 170, 75, 30);
		panelSelectDate.add(dayComboBox);
		
		String[] comboBoxMonths = new String[13];
		comboBoxMonths[0] = "Month";
		for (int i = 1; i < 13; i++) {
			comboBoxMonths[i] = "" + i;
		}
		monthComboBox = new JComboBox<>(comboBoxMonths);
		monthComboBox.setBounds(155, 170, 90, 30);
		panelSelectDate.add(monthComboBox);
		
		
		// TODO: use first and last year
		String[] comboBoxYears = new String[32];
		comboBoxYears[0] = "Year";
		for (int i = 1; i < 32; i++) {
			comboBoxYears[i] = i + 2000 + "";
		}
		yearComboBox = new JComboBox<>(comboBoxYears);
		yearComboBox.setBounds(245, 170, 85, 30);
		panelSelectDate.add(yearComboBox);
		
		btnNext = new JButton();
		b = new StringBuffer(); b.append("<html><h2>Next</h2></html>");
		btnNext.setText(b.toString());
		btnNext.setBounds(245, 290, 90, 40);
		panelSelectDate.add(btnNext);
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dayComboBox.getSelectedIndex() == 0 ||
						monthComboBox.getSelectedIndex() == 0 ||
						yearComboBox.getSelectedIndex() == 0) {
					System.out.println("You need to select a date");
				} else {
					date = new GregorianCalendar(Integer.parseInt(yearComboBox.getSelectedItem().toString()), Integer.parseInt(monthComboBox.getSelectedItem().toString()), Integer.parseInt(dayComboBox.getSelectedItem().toString()));
					panelSelectEmployee.setVisible(false);
					panelSelectDate.setVisible(false);
					panelSelectTimeReg.setVisible(true);
				}
			}
		});
		
		btnPrevious = new JButton();
		b = new StringBuffer(); b.append("<html><h2>Previous</h2></html>");
		btnPrevious.setText(b.toString());
		btnPrevious.setBounds(80, 290, 120, 40);
		panelSelectDate.add(btnPrevious);
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelSelectEmployee.setVisible(true);
				panelSelectDate.setVisible(false);
				panelSelectTimeReg.setVisible(false);
				timeRegistrations.clear();
			}
		});
		// –––––––––––––––––––––––––––––––––––––––––––––––
		
		// –––––––– Step 3: Select TimeRegistration –––––-
		panelSelectTimeReg = new JPanel();
		panelSelectTimeReg.setBounds(0, 28, 350, 500);
		panelCorrectTime.add(panelSelectTimeReg);
		panelSelectTimeReg.setLayout(null);
		panelSelectTimeReg.setVisible(false);
		
		lblPhase = new JLabel();
		lblPhase.setHorizontalAlignment(SwingConstants.LEFT);
		lblPhase.setVerticalAlignment(SwingConstants.TOP);
		lblPhase.setBounds(160, 0, 150, 90);
		b = new StringBuffer();
		b.append("<html><h2>&nbsp;Step 3</h2>Select Time Registration</html>");
		lblPhase.setText(b.toString());
		panelSelectTimeReg.add(lblPhase);
		
		JButton btnEdit = new JButton();
		b = new StringBuffer(); b.append("<html><h2>Edit</h2></html>");
		btnEdit.setText(b.toString());
		btnEdit.setBounds(245, 290, 90, 40);
		panelSelectTimeReg.add(btnEdit);
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dayComboBox.getSelectedIndex() == 0 ||
						monthComboBox.getSelectedIndex() == 0 ||
						yearComboBox.getSelectedIndex() == 0) {
					System.out.println("You need to select a date");
				} else {
					date = new GregorianCalendar(Integer.parseInt(yearComboBox.getSelectedItem().toString()), Integer.parseInt(monthComboBox.getSelectedItem().toString()), Integer.parseInt(dayComboBox.getSelectedItem().toString()));
					panelSelectEmployee.setVisible(false);
					panelSelectDate.setVisible(false);
					panelSelectTimeReg.setVisible(false);
					panelEditTime.setVisible(true);
				}
			}
		});
		
		btnPrevious = new JButton();
		b = new StringBuffer(); b.append("<html><h2>Previous</h2></html>");
		btnPrevious.setText(b.toString());
		btnPrevious.setBounds(80, 290, 120, 40);
		panelSelectTimeReg.add(btnPrevious);
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelSelectEmployee.setVisible(false);
				panelSelectDate.setVisible(true);
				panelSelectTimeReg.setVisible(false);
				timeRegistrations.clear();
			}
		});
		// –––––––––––––––––––––––––––––––––––––––––––––––
		
		// –––––––– Step 4: Edit  TimeRegistration ––––––-
		panelEditTime = new JPanel();
		panelEditTime.setBounds(0, 28, 365, 500);
		panelCorrectTime.add(panelEditTime);
		panelEditTime.setLayout(null);
		panelEditTime.setVisible(false);
		
		lblPhase = new JLabel();
		lblPhase.setHorizontalAlignment(SwingConstants.LEFT);
		lblPhase.setVerticalAlignment(SwingConstants.TOP);
		lblPhase.setBounds(160, 0, 150, 90);
		b = new StringBuffer();
		b.append("<html><h2>&nbsp;Step 3</h2>Select Time Registration</html>");
		lblPhase.setText(b.toString());
		panelEditTime.add(lblPhase);
		
		JButton btnSave = new JButton();
		b = new StringBuffer(); b.append("<html><h2>Save Change</h2></html>");
		btnSave.setText(b.toString());
		btnSave.setBounds(205, 290, 165, 40);
		panelEditTime.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dayComboBox.getSelectedIndex() == 0 ||
						monthComboBox.getSelectedIndex() == 0 ||
						yearComboBox.getSelectedIndex() == 0) {
					System.out.println("You need to select a date");
				} else {
					date = new GregorianCalendar(Integer.parseInt(yearComboBox.getSelectedItem().toString()), Integer.parseInt(monthComboBox.getSelectedItem().toString()), Integer.parseInt(dayComboBox.getSelectedItem().toString()));
					panelSelectEmployee.setVisible(false);
					panelSelectDate.setVisible(false);
					panelSelectTimeReg.setVisible(true);
				}
			}
		});
		
		btnPrevious = new JButton();
		b = new StringBuffer(); b.append("<html><h2>Previous</h2></html>");
		btnPrevious.setText(b.toString());
		btnPrevious.setBounds(80, 290, 120, 40);
		panelEditTime.add(btnPrevious);
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelSelectEmployee.setVisible(false);
				panelSelectDate.setVisible(false);
				panelSelectTimeReg.setVisible(true);
				panelEditTime.setVisible(false);
				timeRegistrations.clear();
			}
		});
		// ––––––––––––––––––––––––––––––––––––––––––––––––
	}
	
	public void setVisible(boolean aFlag) {
		panelCorrectTime.setVisible(aFlag);
	}
	
	public void clear() {
		panelSelectEmployee.setVisible(true);
//		panelSelectDate.setVisible(false);
//		panelSelectTimeReg.setVisible(false);
//		panelEditTime.setVisible(false);
//		employeeReminderField.setText("");
//		searchField.setText("");
		searchResults.clear();
//		hoursComboBox.setSelectedItem(0);
//		minutesComboBox.setSelectedItem(0);
//		dayComboBox.setSelectedIndex(0);
//		monthComboBox.setSelectedIndex(0);
//		yearComboBox.setSelectedIndex(0);
	}
	
	protected void searchEmployees() {
		searchResults.clear();
		try {
			planningApp.searchForEmployeesByName(searchField.getText())
			.forEach((m) -> {searchResults.addElement(m);});
		} catch (OperationNotAllowedException e) {
			System.out.println(e.getMessage());
		}		
	}
}