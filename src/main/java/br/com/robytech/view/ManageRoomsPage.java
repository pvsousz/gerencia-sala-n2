package br.com.robytech.view;

import br.com.robytech.dao.ClassRoomDAO;
import br.com.robytech.model.ClassRoomModel;
import br.com.robytech.model.enums.StatusEnum;
import br.com.robytech.model.enums.TypeClassEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ManageRoomsPage {

    private ObservableList<ClassRoomModel> classRooms;
    private ListView<ClassRoomModel> roomListView;
    private ClassRoomDAO classRoomDAO;

    {
        roomListView = new ListView<>();
        classRoomDAO = new ClassRoomDAO();
    }

    public void show(Stage primaryStage) {
        primaryStage.setTitle("Gerenciar Salas");

        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: #1cc6e8;");
        root.setAlignment(Pos.CENTER);

        classRooms = FXCollections.observableArrayList();

        TextField searchField = new TextField();
        searchField.setPromptText("Digite número da sala");

        roomListView = new ListView<>(FXCollections.observableArrayList());
        roomListView.setCellFactory(param -> new ListCell<ClassRoomModel>() {
            private HBox buttonsBox;

            {
                Button updateButton = new Button("Atualizar");
                Button deleteButton = new Button("Deletar");

                updateButton.setOnAction(event -> {
                    ClassRoomModel item = getItem();
                    System.out.println("Atualizando sala: " + item);
                    showEditRoomDialog(item);
                });

                deleteButton.setOnAction(event -> {
                    ClassRoomModel item = getItem();
                    System.out.println("Deletando sala: " + item);
                    classRoomDAO.deleteClassRoom(item.getIdString());
                    classRooms.remove(item);
                    updateListView(roomListView);
                });

                buttonsBox = new HBox(10, updateButton, deleteButton);
                buttonsBox.setAlignment(Pos.CENTER_LEFT);
            }

            @Override
            protected void updateItem(ClassRoomModel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getIdString() + " - Tipo de sala: " + item.getTypeClassString() + " - Status: "
                            + item.getStatusString());
                    setGraphic(new VBox(5, buttonsBox));
                }
            }
        });

        Button searchButton = new Button("Pesquisar");
        searchButton.setOnAction(event -> {
            String searchTerm = searchField.getText().toLowerCase();
            ObservableList<ClassRoomModel> filteredList = FXCollections.observableArrayList();

            for (ClassRoomModel room : classRooms) {
                if (String.valueOf(room.getNumberClass()).contains(searchTerm)) {
                    filteredList.add(room);
                }
            }

            roomListView.setItems(filteredList);
        });

        Button addButton = new Button("Adicionar Sala");
        addButton.setOnAction(event -> showAddRoomDialog());

        root.getChildren().addAll(searchField, searchButton, addButton, roomListView);

        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAddRoomDialog() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setTitle("Adicionar Sala");

        VBox dialogRoot = new VBox(20);
        dialogRoot.setAlignment(Pos.CENTER);

        TextField blockField = new TextField();
        blockField.setPromptText("Número do Bloco");

        TextField numberField = new TextField();
        numberField.setPromptText("Número da Sala");

        ComboBox<TypeClassEnum> typeComboBox = new ComboBox<>(
                FXCollections.observableArrayList(TypeClassEnum.values()));
        typeComboBox.setPromptText("Tipo de Sala");

        ComboBox<StatusEnum> statusComboBox = new ComboBox<>(FXCollections.observableArrayList(StatusEnum.values()));
        statusComboBox.setPromptText("Status");

        Button addButton = new Button("Adicionar");
        addButton.setOnAction(event -> {
            if (validateInput(blockField.getText(), numberField.getText(), typeComboBox.getValue(),
                    statusComboBox.getValue())) {
                int block = Integer.parseInt(blockField.getText());
                int number = Integer.parseInt(numberField.getText());
                TypeClassEnum type = typeComboBox.getValue();
                StatusEnum status = statusComboBox.getValue();

                ClassRoomModel newRoom = new ClassRoomModel(block, number, type, status);
                classRoomDAO.insertClassRoom(newRoom);
                classRooms.add(newRoom);
                updateListView();

                dialogStage.close();
            } else {
                showAlert("Erro", "Todos os campos devem ser preenchidos.");
            }
        });

        Button cancelButton = new Button("Cancelar");
        cancelButton.setOnAction(event -> dialogStage.close());

        dialogRoot.getChildren().addAll(blockField, numberField, typeComboBox, statusComboBox,
                addButton, cancelButton);

        Scene dialogScene = new Scene(dialogRoot, 400, 300);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }

    private void showEditRoomDialog(ClassRoomModel roomToEdit) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setTitle("Editar Sala");

        VBox dialogRoot = new VBox(20);
        dialogRoot.setAlignment(Pos.CENTER);

        TextField blockField = new TextField();
        blockField.setPromptText("Número do Bloco");
        blockField.setText(String.valueOf(roomToEdit.getBlock()));

        TextField numberField = new TextField();
        numberField.setPromptText("Número da Sala");
        numberField.setText(String.valueOf(roomToEdit.getNumberClass()));

        ComboBox<TypeClassEnum> typeComboBox = new ComboBox<>(
                FXCollections.observableArrayList(TypeClassEnum.values()));
        typeComboBox.setPromptText("Tipo de Sala");
        typeComboBox.setValue(roomToEdit.getTypeClass());

        ComboBox<StatusEnum> statusComboBox = new ComboBox<>(FXCollections.observableArrayList(StatusEnum.values()));
        statusComboBox.setPromptText("Status");
        statusComboBox.setValue(roomToEdit.getStatuss());

        Button editButton = new Button("Editar");
        editButton.setOnAction(event -> {
            if (validateInput(blockField.getText(), numberField.getText(), typeComboBox.getValue(),
                    statusComboBox.getValue())) {
                int block = Integer.parseInt(blockField.getText());
                int number = Integer.parseInt(numberField.getText());
                TypeClassEnum type = typeComboBox.getValue();
                StatusEnum status = statusComboBox.getValue();

                ClassRoomModel updatedRoom = new ClassRoomModel(block, number, type, status);
                classRoomDAO.updateClassRoom(updatedRoom);

                int index = classRooms.indexOf(roomToEdit);
                classRooms.set(index, updatedRoom);
                updateListView();

                dialogStage.close();
            } else {
                showAlert("Erro", "Todos os campos devem ser preenchidos.");
            }
        });

        Button cancelButton = new Button("Cancelar");
        cancelButton.setOnAction(event -> dialogStage.close());

        dialogRoot.getChildren().addAll(blockField, numberField, typeComboBox, statusComboBox,
                editButton, cancelButton);

        Scene dialogScene = new Scene(dialogRoot, 400, 300);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }

    public void updateListView() {
        classRooms.addAll(classRoomDAO.getAllClassRooms());
        roomListView.setItems(classRooms);
    }

    private boolean validateInput(String block, String number, TypeClassEnum type, StatusEnum status) {
        return !block.isEmpty() && !number.isEmpty() && type != null && status != null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
