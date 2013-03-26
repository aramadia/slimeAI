/**
 * Created with IntelliJ IDEA.
 * User: jlauer
 * Date: 3/26/13
 * Time: 7:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoseAI extends SlimeAI {
    LoseAI(SlimeV2 slimeGame, Player player) {
        super(slimeGame, player);
    }

    @Override
    public void moveSlime() {
        startMoveLeft();
    }
}
