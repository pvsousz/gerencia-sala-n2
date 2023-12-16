package br.com.robytech.model;

import java.io.Serializable;
import java.util.List;

import br.com.robytech.model.enums.DaysWeekEnum;
import br.com.robytech.model.enums.HoraryEnum;
import br.com.robytech.model.enums.TurnEnum;

public class DisciplineModel implements Serializable {
    private String nameDiscipline;
    private String codDiscipline;
    private String course;
    private int weeklyWorkload;
    private String teacher;
    private TurnEnum turn = TurnEnum.M;
    private DaysWeekEnum day = DaysWeekEnum.SEGUNDA;
    private HoraryEnum horary = HoraryEnum.AB;
    private List<ClassRoomModel> clasrooms;

    public DisciplineModel(String nameDiscipline, String course, int weeklyWorkload,
            String teacher, TurnEnum turn, DaysWeekEnum day, HoraryEnum horary) {
        this.nameDiscipline = nameDiscipline;
        this.course = course;
        this.weeklyWorkload = weeklyWorkload;
        this.teacher = teacher;
        this.turn = turn;
        this.day = day;
        this.horary = horary;
    }

    public DisciplineModel(String nameDiscipline, String course, int weeklyWorkload,
            String teacher, TurnEnum turn, DaysWeekEnum day, HoraryEnum horary, List<ClassRoomModel> classrooms) {
        this.nameDiscipline = nameDiscipline;
        this.course = course;
        this.weeklyWorkload = weeklyWorkload;
        this.teacher = teacher;
        this.turn = turn;
        this.day = day;
        this.horary = horary;
        this.clasrooms = classrooms;
    }

    public String getNameDisc() {
        return nameDiscipline;
    }

    public void setNameDisc(String nameDiscipline) {
        this.nameDiscipline = nameDiscipline;
    }

    public String getCodDisc() {
        return codDiscipline;
    }

    public void setCodDisc(String codDiscipline) {
        this.codDiscipline = codDiscipline;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getWeeklyWorkload() {
        return weeklyWorkload;
    }

    public void setWeeklyWorkload(int weeklyWorkload) {
        this.weeklyWorkload = weeklyWorkload;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public TurnEnum getTurn() {
        return turn;
    }

    public void setTurn(TurnEnum turn) {
        this.turn = turn;
    }

    public DaysWeekEnum getDay() {
        return day;
    }

    public void setDay(DaysWeekEnum day) {
        this.day = day;
    }

    public HoraryEnum getHorary() {
        return horary;
    }

    public void setHorary(HoraryEnum horary) {
        this.horary = horary;
    }

    public List<ClassRoomModel> getClasrooms() {
        return clasrooms;
    }

    public void setClasrooms(List<ClassRoomModel> clasrooms) {
        this.clasrooms = clasrooms;
    }
    public boolean isScheduledFor() {
        return this.turn == getTurn() && this.horary == getHorary();
    }

}
