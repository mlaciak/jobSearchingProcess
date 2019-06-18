package com.laciak;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import static java.time.temporal.ChronoUnit.DAYS;
import com.laciak.Model.Job;
import com.laciak.Model.LocalJob;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class PrimaryController implements Initializable {

    private Collection<Job> jobs;
    private ObservableList<LocalJob> observableListNew = FXCollections.observableArrayList();
    private ContextMenu contextMenu = new ContextMenu();
    private MenuItem menuItemEdit = new MenuItem("Edit");
    private MenuItem menuItemDelete = new MenuItem("Delete");
    private ObservableList<String> comboBoxTypeOfApplicationOptions = FXCollections.observableArrayList(
            "Email",
            "LinkedIn",
            "WebSite"
    );
    private ObservableList<String> comboBoxTypeOfResponsesOptions = FXCollections.observableArrayList(
            "None",
            "Rejcted",
            "Test",
            "First Interview",
            "Second Interview",
            "Accepted",
            "Hired"
    );

    @FXML
    private MenuItem munuItemCloseApplication;
    public MenuItem menuItemAbout;
    public TableView<LocalJob> tableView;
    public TableColumn<LocalJob, String> tableColumnCompany;
    public TableColumn<LocalJob, String> tableColumnPosition;
    public TableColumn<LocalJob, Integer> tableColumnDays;
    public TableColumn<LocalJob, Boolean> tableColumnTestExam;
    public TableColumn<LocalJob, String> tableColumnTypeOfApplication;
    public TableColumn<LocalJob, String> tableColumnTypeOfResponse;
    public Button buttonAddApplication;
    public TextField textFieldCompany;
    public TextField textFieldPosition;
    public TextArea textAreaDescription;
    public DatePicker datePickerApplication;
    public DatePicker datePickerResponse;
    public ComboBox<String> comboBoxTypeOfApplication;
    public ComboBox<String> comboBoxTypeOfResponses;
    public CheckBox checkBoxTest;
    public CheckBox checkBoxResponseDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //        Calendar c3 = new GregorianCalendar(2019, 4, 26); //2019 - 5(-1) - 25(+1)
        initTableView();
        initTableViewMenu();
        initMenuBar();
        initComboBoxTypeOfApplication();
        initComboBoxTypeOfResponses();
        initCheckBoxResponseDateActionListener();
        initAddApplication();


        reloadData();
    }

    private void initCheckBoxResponseDateActionListener() {
        checkBoxResponseDate.setOnAction(actionEvent -> {
            if (checkBoxResponseDate.isSelected()) {
                datePickerResponse.setDisable(false);
            } else {
                datePickerResponse.setDisable(true);
            }
        });
    }
    protected static void showAlert(boolean flagCompany, boolean flagPosition, boolean flagApplicationDate, boolean flagResponseDate, boolean flagEdited){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        if(flagEdited){
            alert.setContentText("Job application updated!");
        }else {
            if(!flagCompany || !flagApplicationDate || !flagPosition || !flagResponseDate){
                if(!flagResponseDate)alert.setContentText("Response date can't be earlier than application date\n");
                if(!flagCompany)alert.setContentText(alert.getContentText()+"Field company has to be filled\n");
                if(!flagPosition)alert.setContentText(alert.getContentText()+"Field position has to be filled\n");
                if(!flagApplicationDate)alert.setContentText(alert.getContentText()+"Field application date has to be filled");
            }else {
                alert.setContentText("Application added to database!");
            }
        }
        alert.showAndWait();
    }
    private void cleanFieldsInAddApplicationTable(){
        textFieldCompany.setText("");
        textFieldPosition.setText("");
        textAreaDescription.setText("");
        datePickerApplication.setValue(null);
        checkBoxResponseDate.setSelected(false);
        datePickerResponse.setValue(null);
        datePickerResponse.setDisable(true);
        checkBoxTest.setSelected(false);
        comboBoxTypeOfApplication.getSelectionModel().selectFirst();
        comboBoxTypeOfResponses.getSelectionModel().selectFirst();
    }

    private void initAddApplication() {
        buttonAddApplication.setOnAction(actionEvent -> {
            boolean flagCompany=true;
            boolean flagPosition=true;
            boolean flagApplicationDate=true;
            boolean flagResponseDate=true;
            boolean flagEdited=false;
            String company = textFieldCompany.getText();
            String position = textFieldPosition.getText();
            String description = textAreaDescription.getText();
            LocalDate applicationDate = datePickerApplication.getValue().plusDays(1);
            LocalDate responseDate = datePickerResponse.getValue();
            Boolean test = checkBoxTest.isSelected();
            String typeOfApplication = comboBoxTypeOfApplication.getSelectionModel().getSelectedItem();
            String typeOfResponse = comboBoxTypeOfResponses.getSelectionModel().getSelectedItem();
            if(company.equals(""))flagCompany=false;
            if(position.equals(""))flagPosition=false;
            if(applicationDate==null) flagApplicationDate=false;
            if (responseDate != null && checkBoxResponseDate.isSelected() && flagCompany && flagPosition && flagApplicationDate) {
                if(DAYS.between(applicationDate,responseDate)>=0){
                    JobsManager.create(company, position, applicationDate, responseDate, test, description, typeOfApplication, typeOfResponse);
                    showAlert(flagCompany,flagPosition,flagApplicationDate,flagResponseDate,flagEdited);
                    cleanFieldsInAddApplicationTable();
                }else {
                    flagResponseDate=false;
                    showAlert(flagCompany,flagPosition,flagApplicationDate,flagResponseDate,flagEdited);
                }
            } else if(flagCompany && flagPosition && flagApplicationDate) {
                JobsManager.create(company, position, applicationDate, test, description, typeOfApplication, typeOfResponse);
                showAlert(flagCompany,flagPosition,flagApplicationDate,flagResponseDate,flagEdited);
                cleanFieldsInAddApplicationTable();
            }else {
                showAlert(flagCompany,flagPosition,flagApplicationDate,flagResponseDate,flagEdited);
            }
            reloadData();
        });
    }

    private void initComboBoxTypeOfApplication() {
        comboBoxTypeOfApplication.setItems(comboBoxTypeOfApplicationOptions);
        comboBoxTypeOfApplication.getSelectionModel().selectFirst();
    }

    private void initComboBoxTypeOfResponses() {
        comboBoxTypeOfResponses.setItems(comboBoxTypeOfResponsesOptions);
        comboBoxTypeOfResponses.getSelectionModel().selectFirst();
    }

    private void initMenuBar() {
        menuItemAbout.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About - Information");
            alert.setHeaderText(null);
            alert.setContentText("This application's purpose is to help with the process of finding a job.\n" +
                    "It is based on MySQL, JPA, Hibernate and Maven.\n" +
                    "After adding a new job application, you can manage it by right-clicking on it and selecting a propoer option.\n" +
                    "If you just want to see description of a job application, simply drag your mouse cursor over desired position and tooltip will appear!");
            alert.showAndWait();
        });
        munuItemCloseApplication.setOnAction(actionEvent -> {
            System.exit(0);
        });
    }

    private void initTableView() {
        tableView.setRowFactory(tv -> new TableRow<LocalJob>() {
            private Tooltip tooltip = new Tooltip();
            @Override
            public void updateItem(LocalJob job, boolean empty) {
                super.updateItem(job, empty);
                if (job == null) {
                    setTooltip(null);
                } else {
                    tooltip.setText(job.getDescription());
                    setTooltip(tooltip);
                }
            }
        });
        tableColumnCompany.setCellValueFactory(new PropertyValueFactory<LocalJob, String>("company"));
        tableColumnPosition.setCellValueFactory(new PropertyValueFactory<LocalJob, String>("position"));
        tableColumnDays.setCellValueFactory(new PropertyValueFactory<LocalJob, Integer>("days"));
        tableColumnTestExam.setCellValueFactory(new PropertyValueFactory<LocalJob, Boolean>("test"));
        tableColumnTypeOfApplication.setCellValueFactory(new PropertyValueFactory<LocalJob, String>("typeOfApplication"));
        tableColumnTypeOfResponse.setCellValueFactory(new PropertyValueFactory<LocalJob, String>("typeOfResponse"));
    }

    private void initTableViewMenu() {
        contextMenu.getItems().add(menuItemEdit);
        contextMenu.getItems().add(menuItemDelete);
        tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.SECONDARY && tableView.getSelectionModel().getSelectedItem() != null) {
                    contextMenu.show(tableView, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                }
            }
        });
        menuItemEdit.setOnAction(actionEvent -> {
            Job job = new Job();
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                LocalJob localJob = tableView.getSelectionModel().getSelectedItem();
                job.setJobId(localJob.getJobId());
                job.setCompany(localJob.getCompany());
                job.setPosition(localJob.getPosition());
                job.setTest(localJob.isTest());
                job.setDescription(localJob.getDescription());
                job.setDate(localJob.getDate());
                job.setResponseDate(localJob.getResponseDate());
                job.setTypeOfApplication(localJob.getTypeOfApplication());
                job.setTypeOfResponse(localJob.getTypeOfResponse());
            } else {
                try {
                    App.setRoot("secondary");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                App.setRoot("secondary", job);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        menuItemDelete.setOnAction(actionEvent -> {
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                JobsManager.remove(tableView.getSelectionModel().getSelectedItem().getJobId());
                reloadData();
            }
        });
    }

    private void reloadData() {
        observableListNew.clear();
        jobs = JobsManager.findAll();
        JobsManager.end();
        for (Job j : jobs) {
            LocalJob lJ = new LocalJob();
            lJ.setJobId(j.getJobId());
            lJ.setCompany(j.getCompany());
            lJ.setPosition(j.getPosition());
            lJ.setTest(j.isTest());
            lJ.setDescription(j.getDescription());
            lJ.setDate(j.getDate());
            lJ.setResponseDate(j.getResponseDate());
            lJ.setDays(lJ.dateToDays());
            lJ.setTypeOfApplication(j.getTypeOfApplication());
            lJ.setTypeOfResponse(j.getTypeOfResponse());
            observableListNew.add(lJ);
        }
        tableView.setItems(observableListNew);
    }
}
