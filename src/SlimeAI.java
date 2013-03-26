/**
 * Created with IntelliJ IDEA.
 * User: jlauer
 * Date: 3/19/13
 * Time: 7:09 PM
 * To change this template use File | Settings | File Templates.
 */
abstract class SlimeAI
{

    protected SlimeV2 slimeGame;
    protected Player player;

    public void initialize(SlimeV2 slimeGame, Player player) {
        this.slimeGame = slimeGame;
        this.player = player;
    }

    public abstract void moveSlime();

    protected final void startMoveLeft() {
        player.startMoveLeft();
    }

    protected final void startMoveRight() {
        player.startMoveRight();
    }

    protected final void startJump() {
        player.startJump();
    }

    protected final void stopMoveLeft() {
        player.stopMoveLeft();
    }

    protected final void stopMoveRight() {
        player.stopMoveRight();
    }

}