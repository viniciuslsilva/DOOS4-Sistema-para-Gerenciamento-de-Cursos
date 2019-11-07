package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import models.Course;
import models.Discipline;
import resources.CourseSingleton;
import utils.ListViewPropertyCellFactory;
import views.loaders.WindowSubjectsImport;

import java.util.Iterator;
import java.util.Map;

public class SubjectsViewController {
    @FXML private ListView<Discipline> subjectsList;

    private Course course;
    private ObservableList<Discipline> disciplines;

    @FXML
    private void initialize() {
        course = CourseSingleton.getInstance().getCourse();
        Iterator<Map.Entry<String, Discipline>> disciplinesIt = course.getDisciplines();
        disciplines = FXCollections.observableArrayList();
        while (disciplinesIt.hasNext())
            disciplines.add(disciplinesIt.next().getValue());
        subjectsList.setItems(disciplines);
        subjectsList.setCellFactory(new ListViewPropertyCellFactory<>(Discipline::getName));
    }


    @FXML
    private void openRegisterModal() {
        WindowSubjectsImport windowSubjectsImport = new WindowSubjectsImport(disciplines.iterator());
        windowSubjectsImport.show();
    }

    @FXML
    private void close() {
        Stage stage = (Stage) subjectsList.getScene().getWindow();
        stage.close();
    }
}
