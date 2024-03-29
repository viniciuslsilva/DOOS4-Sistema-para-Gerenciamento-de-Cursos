package controllers;

import dao.DisciplineDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import models.CheckBox;
import models.Course;
import models.Discipline;
import resources.CourseSingleton;
import views.loaders.MockSingleton;

import java.util.Iterator;

public class DisciplineRegisterController {
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtCode;
    @FXML
    private TextField txtWorkload;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField txtModule;

    @FXML
    private ListView<CheckBox> checkBoxListView;

    private Discipline discipline;
    private ObservableList<CheckBox> disciplines = FXCollections.observableArrayList();
    private DisciplineDAO disciplineDAO = new DisciplineDAO();

    @FXML
    void initialize() {
        checkBoxListView.setCellFactory(CheckBoxListCell.forListView(s -> s.onProperty()));
        checkBoxListView.setItems(disciplines);
    }

    @FXML
    private void close() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void register() {
        if (!isFormValid()) {
            // TODO show msg in invalid fields
            return;
        }
        Course course = CourseSingleton.getInstance().getCourse();
        String code = txtCode.getText();
        if (!course.hasDiscipline(code)) {
            if (discipline == null) {
                discipline = new Discipline(code, txtName.getText(),
                        Double.parseDouble(txtWorkload.getText()), Integer.parseInt(txtModule.getText()));
                setDependencies(discipline);
            } else {
                discipline.setCode(code);
                discipline.setName(txtName.getText());
                discipline.setWorkload(Double.parseDouble(txtWorkload.getText()));
                discipline.setModule(Integer.parseInt(txtModule.getText()));

                setDependencies(discipline);
                discipline.setCourse(course);
                disciplineDAO.save(discipline);
            }
            course.addDiscipline(discipline);
            // TODO Msg de Success
            close();
        } else {
            // TODO show msg discipline already registered
        }
    }

    private void setDependencies(Discipline discipline) {
        Iterator<CheckBox> itCheckBox = disciplines.iterator();
        while (itCheckBox.hasNext()) {
            CheckBox checkBox = itCheckBox.next();
            if (checkBox.isOn())
                discipline.addDependency(checkBox.getDiscipline());
        }
    }

    private boolean isFormValid() {
        return true;
    }

    public void setDisciplines(Iterator<Discipline> disciplinesIt) {
        disciplines = FXCollections.observableArrayList();
        while (disciplinesIt.hasNext())
            disciplines.add(new CheckBox(disciplinesIt.next(), false));
        checkBoxListView.setItems(disciplines);
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }
}
