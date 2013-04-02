package SlimeGame;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlauer
 * Date: 4/1/13
 * Time: 4:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThreeSwapSlimeAI extends SlimeAI {

    private List<SlimeAI> ais = new LinkedList<SlimeAI>();

    public ThreeSwapSlimeAI() {
        ais.add(new CrapSlimeAI());
        ais.add(new DannoAI());
        ais.add(new DannoAI2());

        Collections.shuffle(ais);
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
        int currentAI = (slimeGame.balls[0].leftToRightCrosses + slimeGame.balls[0].rightToLeftCrosses) % ais.size();
//        System.out.println("ThreeWay Slime AI using AI " + currentAI);
        ais.get(currentAI).moveSlime();
    }
}
