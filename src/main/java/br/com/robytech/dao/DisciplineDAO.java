package br.com.robytech.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.robytech.model.ClassRoomModel;
import br.com.robytech.model.DisciplineModel;
import br.com.robytech.model.enums.DaysWeekEnum;
import br.com.robytech.model.enums.HoraryEnum;
import br.com.robytech.model.enums.StatusEnum;
import br.com.robytech.model.enums.TurnEnum;
import br.com.robytech.model.enums.TypeClassEnum;
import br.com.robytech.util.DatabaseUtil;

public class DisciplineDAO {

    private int nextDisciplineId = 0;
    private int idDiscipline = 0;

    public List<DisciplineModel> getAllDisciplines() {
        List<DisciplineModel> disciplines = new ArrayList<>();
        String sql = "SELECT * FROM discipline";

        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {

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

                List<ClassRoomModel> classRooms = getClassRoomsForDiscipline(discipline.getCodDisc());
                discipline.setClasrooms(classRooms);

                disciplines.add(discipline);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return disciplines;
    }

    private List<ClassRoomModel> getClassRoomsForDiscipline(String codDiscipline) {
        List<ClassRoomModel> classRooms = new ArrayList<>();
        String sql = "SELECT cr.* FROM classroom cr " +
                "JOIN ClassRoom_has_Discipline cd ON cr.idString = cd.ClassRoom_idClassroom " +
                "WHERE cd.Discipline_idDiscipline = ?";

        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, codDiscipline);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ClassRoomModel classRoom = new ClassRoomModel(
                        resultSet.getString("idString"),
                        resultSet.getInt("block"),
                        resultSet.getInt("numberClass"),
                        TypeClassEnum.valueOf(resultSet.getString("typeClass")),
                        StatusEnum.valueOf(resultSet.getString("status")));

                classRoom.setIdString(resultSet.getString("idString"));
                classRooms.add(classRoom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classRooms;
    }

    public DisciplineModel getDisciplineByCode(String codDiscipline) {
        String sql = "SELECT * FROM discipline WHERE codDiscipline = ?";
        DisciplineModel discipline = null;

        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, codDiscipline);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    discipline = new DisciplineModel(
                            resultSet.getString("nameDiscipline"),
                            resultSet.getString("course"),
                            resultSet.getInt("weeklyWorkload"),
                            resultSet.getString("teacher"),
                            TurnEnum.valueOf(resultSet.getString("turn")),
                            DaysWeekEnum.valueOf(resultSet.getString("day")),
                            HoraryEnum.valueOf(resultSet.getString("horary")));

                    discipline.setCodDisc(resultSet.getString("codDiscipline"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return discipline;
    }

    public void insertDiscipline(DisciplineModel discipline) {
        String insertDisciplineSql = "INSERT INTO discipline (idDiscipline, nameDiscipline, codDiscipline, course, weeklyWorkload, teacher, turn, day, horary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertClassRoomDisciplineSql = "INSERT INTO ClassRoom_has_Discipline (ClassRoom_idClassroom, Discipline_idDiscipline) VALUES (?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement insertDisciplineStatement = connection.prepareStatement(insertDisciplineSql,
                        PreparedStatement.RETURN_GENERATED_KEYS);
                PreparedStatement insertClassRoomDisciplineStatement = connection
                        .prepareStatement(insertClassRoomDisciplineSql)) {

            discipline.setCodDisc(generateNextDisciplineId());
            insertDisciplineStatement.setString(1, discipline.getCodDisc());
            insertDisciplineStatement.setString(2, discipline.getNameDisc());
            insertDisciplineStatement.setString(3, discipline.getCodDisc());
            insertDisciplineStatement.setString(4, discipline.getCourse());
            insertDisciplineStatement.setInt(5, discipline.getWeeklyWorkload());
            insertDisciplineStatement.setString(6, discipline.getTeacher());
            insertDisciplineStatement.setString(7, discipline.getTurn().toString());
            insertDisciplineStatement.setString(8, discipline.getDay().toString());
            insertDisciplineStatement.setString(9, discipline.getHorary().toString());

            insertDisciplineStatement.executeUpdate();

            try (ResultSet generatedKeys = insertDisciplineStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    discipline.setCodDisc(generatedKeys.getString(1));
                }
            }

            for (ClassRoomModel classRoom : discipline.getClasrooms()) {
                insertClassRoomDisciplineStatement.setString(1, classRoom.getIdStringTemplate());
                insertClassRoomDisciplineStatement.setString(2, discipline.getCodDisc());
                insertClassRoomDisciplineStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDiscipline(DisciplineModel discipline) {
        String updateDisciplineSql = "UPDATE discipline SET nameDiscipline = ?, course = ?, weeklyWorkload = ?, teacher = ?, turn = ?, day = ?, horary = ? WHERE codDiscipline = ?";
        String deleteClassRoomDisciplineSql = "DELETE FROM ClassRoom_has_Discipline WHERE Discipline_codDiscipline = ?";
        String insertClassRoomDisciplineSql = "INSERT INTO ClassRoom_has_Discipline (ClassRoom_idDiscipline, Discipline_codDiscipline) VALUES (?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement updateDisciplineStatement = connection.prepareStatement(updateDisciplineSql);
                PreparedStatement deleteClassRoomDisciplineStatement = connection
                        .prepareStatement(deleteClassRoomDisciplineSql);
                PreparedStatement insertClassRoomDisciplineStatement = connection
                        .prepareStatement(insertClassRoomDisciplineSql)) {

            updateDisciplineStatement.setString(1, discipline.getNameDisc());
            updateDisciplineStatement.setString(2, discipline.getCourse());
            updateDisciplineStatement.setInt(3, discipline.getWeeklyWorkload());
            updateDisciplineStatement.setString(4, discipline.getTeacher());
            updateDisciplineStatement.setString(5, discipline.getTurn().toString());
            updateDisciplineStatement.setString(6, discipline.getDay().toString());
            updateDisciplineStatement.setString(7, discipline.getHorary().toString());
            updateDisciplineStatement.setString(8, discipline.getCodDisc());

            deleteClassRoomDisciplineStatement.setString(1, discipline.getCodDisc());
            deleteClassRoomDisciplineStatement.executeUpdate();

            for (ClassRoomModel classRoom : discipline.getClasrooms()) {
                insertClassRoomDisciplineStatement.setString(1, classRoom.getIdStringTemplate());
                insertClassRoomDisciplineStatement.setString(2, discipline.getCodDisc());
                insertClassRoomDisciplineStatement.executeUpdate();
            }

            updateDisciplineStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDiscipline(String codDiscipline) {
        String deleteDisciplineSql = "DELETE FROM discipline WHERE codDiscipline = ?";
        String deleteClassRoomDisciplineSql = "DELETE FROM ClassRoom_has_Discipline WHERE Discipline_codDiscipline = ?";

        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement deleteDisciplineStatement = connection.prepareStatement(deleteDisciplineSql);
                PreparedStatement deleteClassRoomDisciplineStatement = connection
                        .prepareStatement(deleteClassRoomDisciplineSql)) {

            deleteClassRoomDisciplineStatement.setString(1, codDiscipline);
            deleteClassRoomDisciplineStatement.executeUpdate();

            deleteDisciplineStatement.setString(1, codDiscipline);
            deleteDisciplineStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ClassRoomModel> getAllClassRooms() {
        List<ClassRoomModel> classRooms = new ArrayList<>();
        String sql = "SELECT * FROM classroom";

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
                classRooms.add(classRoom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classRooms;
    }

    public String generateNextDisciplineId() {
        String nextId = "D" + nextDisciplineId;
        nextDisciplineId += 1;
        return nextId;
    }

    public int gerenateIdDIscipline() {
        return idDiscipline++;
    }

}
