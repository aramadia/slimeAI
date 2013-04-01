package SlimeGame;

/**
 * Created with IntelliJ IDEA.
 * User: jlauer
 * Date: 3/31/13
 * Time: 11:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameResult {

    private int winner;
    private int numTouches;
    private long numFrames;      // NOT YET IMPLEMENTED
    private int numNetCrosses;

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public int getNumTouches() {
        return numTouches;
    }

    public void incrementNumTouches() {
        numTouches++;
    }

    public long getNumFrames() {
        return numFrames;
    }

    public void setNumFrames(long numFrames) {
        this.numFrames = numFrames;
    }

    public int getNumNetCrosses() {
        return numNetCrosses;
    }

    public void setNumNetCrosses(int numNetCrosses) {
        this.numNetCrosses = numNetCrosses;
    }
}
