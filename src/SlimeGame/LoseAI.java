package SlimeGame;

/**
 * Created with IntelliJ IDEA.
 * User: jlauer
 * Date: 3/26/13
 * Time: 7:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoseAI extends SlimeAI {
    @Override
    public void moveSlime() {
        startMoveTowardsNet();
    }
}
