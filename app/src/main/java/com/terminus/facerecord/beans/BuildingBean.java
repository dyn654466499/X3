package com.terminus.facerecord.beans;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

public class BuildingBean implements IPickerViewData {

    /**
     * name : 1栋
     * building : [{"name":"1单元"},{"name":"2单元"},{"name":"3单元"},{"name":"4单元"},{"name":"5单元"},{"name":"6单元"}]
     */

    private String name;
    private List<Building> building;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Building> getBuilding() {
        return building;
    }

    public void setBuilding(List<Building> building) {
        this.building = building;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }

    public static class Building {
        /**
         * name : 1单元
         */

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
