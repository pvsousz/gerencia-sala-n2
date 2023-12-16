package br.com.robytech.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.robytech.model.ClassRoomModel;
import br.com.robytech.model.DisciplineModel;
import br.com.robytech.model.enums.HoraryEnum;
import br.com.robytech.model.enums.DaysWeekEnum;
import br.com.robytech.model.enums.StatusEnum;
import br.com.robytech.model.enums.TurnEnum;
import br.com.robytech.model.enums.TypeClassEnum;
import br.com.robytech.util.DatabaseUtil;

public class ClassRoomDAO {
    public List<ClassRoomModel> getAllClassRooms() {
        List<ClassRoomModel> classRooms = new ArrayList<>();
        String sql = "SELECT * FROM ClassRoom";
        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                ClassRoomModel classRoom = new ClassRoomModel(
                        resultSet.getString("idString"),
                        resultSet.getInt("block"),
                        resultSet.getInt("numberClass"),
                        TypeClassEnum.valueOf(resultSet.getString("typeClass")),
                        StatusEnum.valueOf(resultSet.getString("status")));

                classRoom.setIdString(resultSet.getString("idString"));

                List<DisciplineModel> disciplines = getDisciplinesForClassRoom(classRoom.getIdString());
                classRoom.setDisciplines(disciplines);

                classRooms.add(classRoom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classRooms;
    }

    private List<DisciplineModel> getDisciplinesForClassRoom(String idString) {
        List<DisciplineModel> disciplines = new ArrayList<>();
        String sql = "SELECT d.* FROM Discipline d " +
                "JOIN ClassRoom_has_Discipline cd ON d.idDiscipline = cd.Discipline_idDiscipline " +
                "WHERE cd.ClassRoom_idClassroom = ?";

        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idString);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                DisciplineModel discipline = new DisciplineModel(
                        resultSet.getString("nameDiscipline"),
                        resultSet.getString("course"),
                        resultSet.getInt("weeklyWorkload"),
                        resultSet.getString("teacher"),
                        TurnEnum.valueOf(resultSet.getString("turn")),
                        DaysWeekEnum.valueOf(resultSet.getString("day")),
                        HoraryEnum.valueOf(resultSet.getString("horary")));

                discipline.setCodDisc(resultSet.getString("codDiscipline"));
                disciplines.add(discipline);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return disciplines;
    }

    public void insertClassRoom(ClassRoomModel classRoom) {
        String insertClassRoomSql = "INSERT INTO ClassRoom (idClassroom, idString, block, numberClass, typeClass, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement insertClassRoomStatement = connection.prepareStatement(insertClassRoomSql,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertClassRoomStatement.setString(1, classRoom.getIdStringTemplate());
            insertClassRoomStatement.setString(2, classRoom.getIdString());
            insertClassRoomStatement.setInt(3, classRoom.getBlock());
            insertClassRoomStatement.setInt(4, classRoom.getNumberClass());
            insertClassRoomStatement.setString(5, classRoom.getTypeClass().toString());
            insertClassRoomStatement.setString(6, classRoom.getStatuss().toString());

            insertClassRoomStatement.executeUpdate();

            try (ResultSet generatedKeys = insertClassRoomStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    classRoom.setIdString(generatedKeys.getString(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateClassRoom(ClassRoomModel classRoom) {
        String updateClassRoomSql = "UPDATE ClassRoom SET block = ?, numberClass = ?, typeClass = ?, status = ? WHERE idString = ?";

        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement updateClassRoomStatement = connection.prepareStatement(updateClassRoomSql)) {

            updateClassRoomStatement.setInt(1, classRoom.getBlock());
            updateClassRoomStatement.setInt(2, classRoom.getNumberClass());
            updateClassRoomStatement.setString(3, classRoom.getTypeClass().toString());
            updateClassRoomStatement.setString(4, classRoom.getStatuss().toString());
            updateClassRoomStatement.setString(5, classRoom.getIdString());

            updateClassRoomStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClassRoom(String idString) {
        String deleteClassRoomSql = "DELETE FROM ClassRoom WHERE idString = ?";

        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement deleteClassRoomStatement = connection.prepareStatement(deleteClassRoomSql)) {

            deleteClassRoomStatement.setString(1, idString);
            deleteClassRoomStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
