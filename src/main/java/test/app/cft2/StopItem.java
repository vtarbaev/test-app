package test.app.cft2;

public class StopItem extends Item {

    public StopItem(Integer id, Integer groupId) {
        super(id, groupId);
    }

    @Override
    public boolean isEnd() {
        return true;
    }
}
