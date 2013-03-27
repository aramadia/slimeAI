/**
 * Created with IntelliJ IDEA.
 * User: jlauer
 * Date: 3/27/13
 * Time: 12:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class DannoAI extends SlimeAI {

    private final double JUMPINESS = 0.84999999999999998D;
    private int serveType = -1;

    @Override
    public void moveSlime() {
        setVars();
        doMoveSlime();
    }                          

    private int square(int i) {
        return i * i;
    }

    private int howManyFrames(int i) {
        int j = 0;
        int k = ballY;
        int l = ballVY;
        while ((k += --l) > i)
            j++;
        return j;
    }

    private int whereWillBallCross(int i) {
        int j = howManyFrames(i);
        int k = ballX;
        int l = ballVX;
        for (int i1 = 0; i1 < j; i1++) {
            k += l;
            if (k < 0) {
                k = 0;
                l = -l;
            } else if (k > 1000) {
                k = 1000;
                l = -l;
            }
        }

        return k;
    }

    public void doMoveSlime() {
        if (ballX < 500 && serveType != -1)
            serveType = -1;
        int i = whereWillBallCross(125);
        howManyFrames(125);
        int j;
        if (p2Y != 0 && p2X < 575)
            j = 0;
        else
            j = 25 + (int) (10D * Math.random());
        if (ballVX == 0 && ballX == 800 || serveType != -1) {
            if (serveType == -1) {
                if (p1X > 250)
                    serveType = 0;
                else if (p1X < 200)
                    serveType = 1;
                else if (p1X < 250)
                    serveType = 2;
                if (Math.random() < 0.34999999999999998D)
                    serveType = (int) (3D * Math.random());
            }
            switch (serveType) {
                default:
                    break;

                case 0: // '\0'
                    if (ballY < 300 && ballVY < -3) {
                        move(1);
                        move(2);
                        return;
                    }
                    break;

                case 1: // '\001'
                    if (ballY < 300 && ballVY < 0) {
                        move(0);
                        move(2);
                        return;
                    }
                    break;

                case 2: // '\002'
                    char c = '\u035C';
                    if (ballVY > 12 && p2X < c)
                        move(1);
                    if (p2X >= c)
                        move(3);
                    if (ballVY == -3 && p2X != 800)
                        move(2);
                    if (ballVY < -12 && p2Y != 0 && p2X >= c - 15) {
                        move(0);
                        return;
                    }
                    break;
            }
            return;
        }
        if (i < 500) {
            if (Math.abs(p2X - 666) < 20) {
                move(3);
                return;
            }
            if (p2X > 666) {
                move(0);
                return;
            }
            if (p2X < 666)
                move(1);
            return;
        }
        if (Math.abs(p2X - i) < j) {
            if (p2Y != 0 || p2Fire && Math.random() < 0.29999999999999999D)
                return;
            if ((p2X >= 900 && ballX > 830 || p2X <= 580 && ballX < 530) && Math.abs(ballX - p2X) < 100)
                jump();
            else if (square(ballX - p2X) * 2 + square(ballY - p2Y) < square(170) && ballX != p2X)
                jump();
            else if (ballVX * ballVX + ballVY * ballVY < 20 && ballX - p2X < 30 && ballX != p2X)
                jump();
            else if (Math.abs(ballX - p2X) < 150 && ballY > 50 && ballY < 400 && Math.random() < 0.66600000000000004D)
                jump();
        }
        if (p2Y == 0 && serveType == -1) {
            if (Math.abs(p2X - i) < j) {
                move(3);
                return;
            }
            if (i + j < p2X) {
                move(0);
                return;
            }
            if (i + j > p2X) {
                move(1);
                return;
            }
        } else if (serveType == -1) {
            if (p2X < 575)
                return;
            if (p2X > 900) {
                move(1);
                return;
            }
            if (Math.abs(p2X - ballX) < j) {
                move(3);
                return;
            }
            if (ballX < p2X) {
                move(0);
                return;
            }
            if (ballX > p2X)
                move(1);
        }
    }

    private void jump() {
        if (Math.random() < JUMPINESS)
            move(2);
    }

}
