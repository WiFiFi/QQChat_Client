package com.example.network.bean;

import java.util.ArrayList;

public class FriendGroup {
    public String title;
    public ArrayList<Friend> friend_list;

    public FriendGroup() {
        this.title = "";
        this.friend_list = new ArrayList<Friend>();
    }

    public FriendGroup(String title, ArrayList<Friend> friend_list) {
        this.title = title;
        this.friend_list = friend_list;
    }

    public static FriendGroup getDefaultFriendGroup(int index) {
        if (index == 1) {
            ArrayList<Friend> friendArrayList = new ArrayList<>();
            friendArrayList.add(new Friend("", "牛顿", "122537"));
            friendArrayList.add(new Friend("", "爱因斯坦", "132537"));
            friendArrayList.add(new Friend("", "莫言", "222637"));
            friendArrayList.add(new Friend("", "张召忠", "195810"));
            return new FriendGroup("偶像", friendArrayList);
        } else if (index == 2) {
            ArrayList<Friend> friendArrayList = new ArrayList<>();
            friendArrayList.add(new Friend("", "余里", "230151"));
            friendArrayList.add(new Friend("", "亚鹏", "132508"));
            friendArrayList.add(new Friend("", "钟琴", "102647"));
            friendArrayList.add(new Friend("", "吴帆", "130210"));
            friendArrayList.add(new Friend("", "流畅", "075819"));
            friendArrayList.add(new Friend("", "张志宇", "155310"));
            friendArrayList.add(new Friend("", "李展鹏", "095817"));
            return new FriendGroup("同学", friendArrayList);
        }
        return null;
    }

}
