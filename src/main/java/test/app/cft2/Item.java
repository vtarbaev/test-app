package test.app.cft2;

public class Item implements Comparable<Item> {

    private Integer id;
    private Integer groupId;

    public Item(Integer id, Integer groupId) {
        this.id = id;
        this.groupId = groupId;
    }

    @Override
    public int compareTo(Item o) {
        return this.id.compareTo(o.getId());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
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
