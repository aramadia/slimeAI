package SlimeGame;

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

    private SwapSlimeAI swapSlimeAI;

    public ThreeSwapSlimeAI() {
        List<SlimeAI> ais = new LinkedList<SlimeAI>();
        ais.add(new CrapSlimeAI());
        ais.add(new DannoAI());
        ais.add(new DannoAI2());

        swapSlimeAI = new SwapSlimeAI(ais);
    }

    @Override
    public void initialize(SlimeV2 slimeGame, Player player) {
        super.initialize(slimeGame, player);
        swapSlimeAI.initialize(slimeGame, player);
    }

    @Override
    public void moveSlime() {
        swapSlimeAI.moveSlime();
    }
}
