package br.com.robytech.view;

import br.com.robytech.dao.ClassRoomDAO;
import br.com.robytech.dao.DisciplineDAO;
import br.com.robytech.model.ClassRoomModel;
import br.com.robytech.model.DisciplineModel;
import br.com.robytech.model.enums.StatusEnum;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

public class RoomAllocationPage {

    private List<ClassRoomModel> classRooms;
    private List<DisciplineModel> disciplines;

    public void show(Stage primaryStage) {
        loadClassRoomsAndDisciplines();

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        for (ClassRoomModel classRoom : classRooms) {
            Button roomButton = new Button(classRoom.getIdString());
            StatusEnum roomStatus = calculateRoomStatus(classRoom);
            roomButton.setStyle("-fx-background-color: " + getColorForStatus(roomStatus));
            roomButton.setOnAction(event -> handleRoomButtonClick(classRoom, roomStatus));
            grid.add(roomButton, classRoom.getBlock(), classRoom.getNumberClass());
        }

        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Room Allocation");
        primaryStage.show();
    }

    private void loadClassRoomsAndDisciplines() {
        ClassRoomDAO classRoomDAO = new ClassRoomDAO();
        DisciplineDAO disciplineDAO = new DisciplineDAO();

        classRooms = classRoomDAO.getAllClassRooms();
        disciplines = disciplineDAO.getAllDisciplines();
    }

    private StatusEnum calculateRoomStatus(ClassRoomModel classRoom) {
        for (DisciplineModel discipline : disciplines) {
            if (isDisciplineScheduled(discipline, classRoom)) {
                return StatusEnum.RESERVADO;
            }
        }
        if (classRoom.getStatuss() == StatusEnum.OCUPADO) {
            return StatusEnum.OCUPADO;
        }

        return StatusEnum.DISPONIVEL;
    }

    private boolean isDisciplineScheduled(DisciplineModel discipline, ClassRoomModel classRoom) {
        return discipline.isScheduledFor();
    }

    private String getColorForStatus(StatusEnum status) {
        switch (status) {
            case RESERVADO:
                return "yellow";
            case OCUPADO:
                return "red";
            case DISPONIVEL:
            default:
                return "green";
        }
    }

    private void handleRoomButtonClick(ClassRoomModel classRoom, StatusEnum currentStatus) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Room Clicked");
        alert.setHeaderText("Room: " + classRoom.getIdString());
        alert.setContentText("Current Status: " + currentStatus);
        alert.showAndWait();
    }
}
