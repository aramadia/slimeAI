/**
 * Created with IntelliJ IDEA.
 * User: jlauer
 * Date: 3/27/13
 * Time: 12:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class DannoAI extends SlimeAI {

    Ball ball;

    int p2X, p2Y;
    int p1X, p1Y;
    boolean p2Fire;
    
    private final double JUMPINESS = 0.84999999999999998D;
    private int serveType = -1;

    @Override
    public void moveSlime() {
        ball = slimeGame.balls[0];
        p2X = slimeGame.players[1].playerX;
        p2Y = slimeGame.players[1].playerY;
        p1X = slimeGame.players[0].playerX;
        p1Y = slimeGame.players[0].playerY;
        p2Fire = slimeGame.players[1].onScoringRun();

        doMoveSlime();
    }                          

    private int square(int i) {
        return i * i;
    }

    private int howManyFrames(int i) {
        int j = 0;
        int k = ball.ballY;
        int l = ball.ballVY;
        while ((k += --l) > i)
            j++;
        return j;
    }

    private int whereWillBallCross(int i) {
        int j = howManyFrames(i);
        int k = ball.ballX;
        int l = ball.ballVX;
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
        if (ball.ballX < 500 && serveType != -1)
            serveType = -1;
        int i = whereWillBallCross(125);
        howManyFrames(125);
        int j;
        if (p2Y != 0 && p2X < 575)
            j = 0;
        else
            j = 25 + (int) (10D * Math.random());
        if (ball.ballVX == 0 && ball.ballX == 800 || serveType != -1) {
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
                    if (ball.ballY < 300 && ball.ballVY < -3) {
                        move(1);
                        move(2);
                        return;
                    }
                    break;

                case 1: // '\001'
                    if (ball.ballY < 300 && ball.ballVY < 0) {
                        move(0);
                        move(2);
                        return;
                    }
                    break;

                case 2: // '\002'
                    char c = '\u035C';
                    if (ball.ballVY > 12 && p2X < c)
                        move(1);
                    if (p2X >= c)
                        move(3);
                    if (ball.ballVY == -3 && p2X != 800)
                        move(2);
                    if (ball.ballVY < -12 && p2Y != 0 && p2X >= c - 15) {
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
            if ((p2X >= 900 && ball.ballX > 830 || p2X <= 580 && ball.ballX < 530) && Math.abs(ball.ballX - p2X) < 100)
                jump();
            else if (square(ball.ballX - p2X) * 2 + square(ball.ballY - p2Y) < square(170) && ball.ballX != p2X)
                jump();
            else if (ball.ballVX * ball.ballVX + ball.ballVY * ball.ballVY < 20 && ball.ballX - p2X < 30 && ball.ballX != p2X)
                jump();
            else if (Math.abs(ball.ballX - p2X) < 150 && ball.ballY > 50 && ball.ballY < 400 && Math.random() < 0.66600000000000004D)
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
            if (Math.abs(p2X - ball.ballX) < j) {
                move(3);
                return;
            }
            if (ball.ballX < p2X) {
                move(0);
                return;
            }
            if (ball.ballX > p2X)
                move(1);
        }
    }

    private void jump() {
        if (Math.random() < 0.84999999999999998D)
            move(2);
    }

    protected final void move(int i) {

        switch (i) {
            case 0: // '\0'
                //app.moveP2Left();
                startMoveLeft();
                return;

            case 1: // '\001'
                //app.moveP2Right();
                startMoveRight();
                return;

            case 2: // '\002'
//            app.moveP2Jump();
                startJump();
                return;

            case 3: // '\003'
//            app.moveP2Stop();
                stopMoveLeft();
                stopMoveRight();
                return;
        }
    }
}
