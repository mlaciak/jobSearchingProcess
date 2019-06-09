package com.laciak;

import java.io.IOException;
import java.time.LocalDate;

import com.laciak.Model.Job;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import static com.laciak.PrimaryController.showAlert;
import static java.time.temporal.ChronoUnit.DAYS;

public class SecondaryController {
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
    public Label labelID;
    public TextField textFieldCompany;
    public TextField textFieldPosition;
    public TextArea textAreaDescription;
    public DatePicker datePickerApplication;
    public DatePicker datePickerResponse;
    public ComboBox<String> comboBoxTypeOfApplication;
    public ComboBox<String> comboBoxTypeOfResponses;
    public CheckBox checkBoxTest;
    public CheckBox checkBoxResponseDate;
    public Button editButton;
    public Button cancelButton;

    private static Job job;

    protected void setId(Job job) {
        SecondaryController.job = job;
        setDataIntoEditBoxes();
        editButton.setOnAction(actionEvent -> {
            Job jobEdited = new Job();
            boolean flagCompany = true;
            boolean flagPosition = true;
            boolean flagApplicationDate = true;
            boolean flagResponseDate = true;
            boolean flagEdited = false;
            jobEdited.setJobId(job.getJobId());
            jobEdited.setCompany(textFieldCompany.getText());
            jobEdited.setPosition(textFieldPosition.getText());
            jobEdited.setDescription(textAreaDescription.getText());
            jobEdited.setDate(datePickerApplication.getValue().plusDays(1));
            if(checkBoxResponseDate.isSelected()){
                jobEdited.setResponseDate(datePickerResponse.getValue().plusDays(1));
            }else {
                jobEdited.setResponseDate(null);
            }
            jobEdited.setTest(checkBoxTest.isSelected());
            jobEdited.setTypeOfApplication(comboBoxTypeOfApplication.getSelectionModel().getSelectedItem());
            jobEdited.setTypeOfResponse(comboBoxTypeOfResponses.getSelectionModel().getSelectedItem());
            if (jobEdited.getCompany().equals("")) flagCompany = false;
            if (jobEdited.getPosition().equals("")) flagPosition = false;
            if (jobEdited.getDate() == null) flagApplicationDate = false;
            if (jobEdited.getResponseDate() != null && checkBoxResponseDate.isSelected() && flagCompany && flagPosition && flagApplicationDate) {
                if (DAYS.between(jobEdited.getDate(), jobEdited.getResponseDate()) >= 0) {
                    JobsManager.update(jobEdited);
                    flagEdited=true;
                    showAlert(flagCompany, flagPosition, flagApplicationDate, flagResponseDate, flagEdited);
                    cancelButton.fire();
                } else {
                    flagResponseDate = false;
                    showAlert(flagCompany, flagPosition, flagApplicationDate, flagResponseDate, flagEdited);
                }
            } else if (flagCompany && flagPosition && flagApplicationDate) {
                JobsManager.update(jobEdited);
                flagEdited=true;
                showAlert(flagCompany, flagPosition, flagApplicationDate, flagResponseDate, flagEdited);
                cancelButton.fire();
            } else {
                showAlert(flagCompany, flagPosition, flagApplicationDate, flagResponseDate, flagEdited);
            }
        });
        cancelButton.setOnAction(actionEvent -> {
            try {
                App.setRoot("primary");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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

    private void setDataIntoEditBoxes() {
        labelID.setText(job.getJobId().toString());
        textFieldCompany.setText(job.getCompany());
        textFieldPosition.setText(job.getPosition());
        textAreaDescription.setText(job.getDescription());
        datePickerApplication.setValue(job.getDate());
        datePickerResponse.setValue(job.getResponseDate());
        if (job.getResponseDate() != null) {
            datePickerResponse.setDisable(false);
            checkBoxResponseDate.setSelected(true);
            datePickerResponse.setValue(job.getResponseDate());
        }else {
            datePickerResponse.setDisable(true);
            checkBoxResponseDate.setSelected(false);
            datePickerResponse.setValue(null);
        }
        checkBoxTest.setSelected(job.isTest());
        comboBoxTypeOfApplication.setItems(comboBoxTypeOfApplicationOptions);
        comboBoxTypeOfApplication.getSelectionModel().select(job.getTypeOfApplication());
        comboBoxTypeOfResponses.setItems(comboBoxTypeOfResponsesOptions);
        comboBoxTypeOfResponses.getSelectionModel().select(job.getTypeOfResponse());
        initCheckBoxResponseDateActionListener();
    }
}