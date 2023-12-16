package br.com.robytech.model;

import java.io.Serializable;

import java.util.List;

import br.com.robytech.model.enums.StatusEnum;
import br.com.robytech.model.enums.TypeClassEnum;

public class ClassRoomModel implements Serializable {
  
    private int block;
    private int numberClass;
    private TypeClassEnum typeClass = TypeClassEnum.SALADEAULA;
    private StatusEnum status = StatusEnum.DISPONIVEL;
    private List<DisciplineModel> disciplines;

    public ClassRoomModel(int block, int numberClass, TypeClassEnum typeClass, StatusEnum status) {
        this.block = block;
        this.numberClass = numberClass;
        this.typeClass = typeClass;
        this.status = status;
    }

    public ClassRoomModel(String idString, int block, int numberClass, TypeClassEnum typeClass, StatusEnum status) {
        this.block = block;
        this.numberClass = numberClass;
        this.typeClass = typeClass;
        this.status = status;
    }

    public String getIdString() {
        return getIdStringTemplate();
    }

    public void setIdString(String idString) {
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public int getNumberClass() {
        return numberClass;
    }

    public void setNumberClass(int numberClass) {
        this.numberClass = numberClass;
    }

    public StatusEnum getStatuss() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public TypeClassEnum getTypeClass() {
        return typeClass;
    }

    public void setTypeClass(TypeClassEnum typeClass) {
        this.typeClass = typeClass;
    }

    public String getTypeClassString() {
        if (getTypeClass().equals(TypeClassEnum.SALADEAULA)) {
            return "Sala de Aula";
        } else if (getTypeClass().equals(TypeClassEnum.LABORATORIO)) {
            return "Laboratorio";
        } else {
            return null;
        }
    }

    public String getStatusString() {
        if (getStatuss().equals(StatusEnum.DISPONIVEL)) {
            return "Disponível";
        } else if (getStatuss().equals(StatusEnum.OCUPADO)) {
            return "Ocupado";
        } else if (getStatuss().equals(StatusEnum.RESERVADO)) {
            return "Reservado";
        } else {
            return null;
        }
    }

    public String getIdStringTemplate() {
        if (getNumberClass() < 10) {
            return "B" + getBlock() + "S0" + getNumberClass();
        } else {
            return "B" + getBlock() + "S" + getNumberClass();
        }
    }

    public List<DisciplineModel> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(List<DisciplineModel> disciplines) {
        this.disciplines = disciplines;
    }

    @Override
    public String toString() {
        return getIdStringTemplate();
    }


}
