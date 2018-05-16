package test.app.cft;

public class Item implements Comparable<Item>  {

    private Integer id;
    private Integer groupId;

    public Item(int id, int groupId) {
        this.id = id;
        this.groupId = groupId;
    }

    @Override
    public int compareTo(Item o) {
        return id.compareTo(o.getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }


    public boolean isEnd() {
        return false;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", groupId=" + groupId +
                '}';
    }
}
