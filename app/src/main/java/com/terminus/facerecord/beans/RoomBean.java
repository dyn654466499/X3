package com.terminus.facerecord.beans;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

public class RoomBean implements IPickerViewData {


    /**
     * name : 1楼
     * room : [{"name":"101","members":[{"name":"邓*","head":"hp_icon_key"}]},{"name":"102","members":[{"name":"邓*","head":"hp_icon_key"}]},{"name":"103","members":[{"name":"邓*","head":"hp_icon_key"}]},{"name":"104"},{"name":"105"},{"name":"106"}]
     */

    private String name;
    private List<Room> room;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Room> getRoom() {
        return room;
    }

    public void setRoom(List<Room> room) {
        this.room = room;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }

    public static class Room {
        /**
         * name : 101
         * members : [{"name":"邓*","head":"hp_icon_key"}]
         */

        private String name;
        private List<MembersBean> members;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<MembersBean> getMembers() {
            return members;
        }

        public void setMembers(List<MembersBean> members) {
            this.members = members;
        }
    }

    public static class MembersBean {
        /**
         * name : 邓*
         * head : hp_icon_key
         */

        private String name;
        private String head;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }
    }
}
