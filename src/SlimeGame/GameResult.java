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
    private int rtlNetCrosses;
    private int ltrNetCrosses;

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

    public int getRtlNetCrosses() {
        return rtlNetCrosses;
    }

    public void setRtlNetCrosses(int rtlNetCrosses) {
        this.rtlNetCrosses = rtlNetCrosses;
    }

    public int getLtrNetCrosses() {
        return ltrNetCrosses;
    }

    public void setLtrNetCrosses(int ltrNetCrosses) {
        this.ltrNetCrosses = ltrNetCrosses;
    }
}
