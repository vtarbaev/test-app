package test.app.cft;

public class StopItem extends Item {

    public StopItem(int id, int groupId) {
        super(id, groupId);
    }

    @Override
    public boolean isEnd() {
        return true;
    }
}
