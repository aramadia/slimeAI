package SlimeGame;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: jlauer
 * Date: 4/1/13
 * Time: 11:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class SwapSlimeAI extends SlimeAI{

    private List<SlimeAI> ais;
    private int currentAI;
    private int lastSideCount;
    private static Random r = new Random();

    public SwapSlimeAI(List<SlimeAI> ais) {
        this.ais = ais;
    }

    @Override
    public void initialize(SlimeV2 slimeGame, Player player) {
        super.initialize(slimeGame, player);
        for (SlimeAI ai : ais) {
            ai.initialize(slimeGame, player);
        }
    }

    @Override
    public void moveSlime() {
        if (slimeGame.frames == 1) {
            currentAI = r.nextInt(ais.size());
        }

        int currentSideCount;
        if (slimeGame.side == SlimeV2.ServeSide.LEFT) {
            currentSideCount = slimeGame.balls[0].leftToRightCrosses;
        } else {
            currentSideCount = slimeGame.balls[0].rightToLeftCrosses;
        }

        if (lastSideCount != currentSideCount) {
            lastSideCount = currentSideCount;
            currentAI = r.nextInt(ais.size());
        }

        SlimeAI slimeAI = ais.get(currentAI);
        slimeAI.moveSlime();
    }

}
