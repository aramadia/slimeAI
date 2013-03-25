/**
 * Created with IntelliJ IDEA.
 * User: jlauer
 * Date: 3/19/13
 * Time: 7:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class DumbSlimeAI extends SlimeAI {

    private int serveType = -1;

    DumbSlimeAI(SlimeV2 slimeGame, Player player) {
        super(slimeGame, player);
    }

    private int square(int i)
    {
        return i * i;
    }

    private int howManyFrames(int i)
    {
        int j = 0;
        int k = slimeGame.balls[0].ballX;
        int l = slimeGame.balls[0].ballY;
        while((k += --l) > i)
            j++;
        return j;
    }

    private int whereWillBallCross(int i)
    {
        int j = howManyFrames(i);
        int k = slimeGame.balls[0].ballX;
        int l = slimeGame.balls[0].ballVX;
        for(int i1 = 0; i1 < j; i1++)
        {
            k += l;
            if(k < 0)
            {
                k = 0;
                l = -l;
            } else
            if(k > 1000)
            {
                k = 1000;
                l = -l;
            }
        }

        return k;
    }

    @Override
    public void moveSlime() {

        Ball ball = slimeGame.balls[0];
        Player player1 = slimeGame.players[0];
        Player player2 = slimeGame.players[1];
        boolean player1ScoringRun = slimeGame.sides[0].scoringRun == Constants.SCORING_RUN_FOR_SUPER;
        boolean player2ScoringRun = slimeGame.sides[1].scoringRun == Constants.SCORING_RUN_FOR_SUPER;


        if(ball.ballX < 500 && serveType != -1) {
            serveType = -1;
        }
        int i = whereWillBallCross(125);
        howManyFrames(125);
        int j;
        if(player2.playerY != 0 && slimeGame.players[1].playerX < 575)
            j = 0;
        else
            j = 23 + (int)(15D * Math.random());
        if(ball.ballVX == 0 && ball.ballX == 800 || serveType != -1)
        {
            if(serveType == -1)
            {
                if(player1.playerX > 250)
                    serveType = 0;
                else
                    serveType = 1;
                if(Math.random() < 0.34999999999999998D)
                    serveType = (int)(2D * Math.random());
            }
            switch(serveType)
            {
                default:
                    break;

                case 0: // '\0'
                    if(ball.ballY < 300 && ball.ballVY < -3)
                    {
                        startMoveLeft();
                        startJump();
                        return;
                    }
                    break;

                case 1: // '\001'
                    if(ball.ballY < 300 && ball.ballVY < 0)
                    {
                        startMoveRight();
                        startJump();
                        return;
                    }
                    break;
            }
            return;
        }
        if(i < 500)
        {
            if(Math.abs(player2.playerX - 800) < 20)
            {
                stopMoveLeft(); stopMoveRight();;
                return;
            }
            if(player2.playerX > 800)
            {
                startMoveRight();
                return;
            }
            if(player2.playerX < 800)
                startMoveLeft();
            return;
        }
        if(Math.abs(player2.playerX - i) < j)
        {
            if(player2.playerY != 0 || player2ScoringRun && Math.random() < 0.29999999999999999D)
                return;
            if((player2.playerX >= 900 && ball.ballX > 830 || player2.playerX <= 580 && ball.ballX < 530) && Math.abs(ball.ballX - player2.playerX) < 100)
                doJump();
            else
            if(square(ball.ballX - player2.playerX) * 2 + square(ball.ballY - player2.playerY) < square(170) && ball.ballX != player2.playerX)
                doJump();
            else
            if(ball.ballVX * ball.ballVX + ball.ballVY * ball.ballVY < 20 && ball.ballX - player2.playerX < 30 && ball.ballX != player2.playerX)
                doJump();
            else
            if(Math.abs(ball.ballX - player2.playerX) < 150 && ball.ballY > 50 && ball.ballY < 400 && Math.random() < 0.5D)
                doJump();
        }
        if(player2.playerY == 0 && serveType == -1)
        {
            if(Math.abs(player2.playerX - i) < j)
            {
                stopMoveLeft(); stopMoveRight();
                return;
            }
            if(i + j < player2.playerX)
            {
                startMoveRight();
                return;
            }
            if(i + j > player2.playerX)
            {
                startMoveLeft();
                return;
            }
        } else
        if(serveType == -1)
        {
            if(player2.playerX < 575)
                return;
            if(player2.playerX > 900)
            {
                startMoveLeft();
                return;
            }
            if(Math.abs(player2.playerX - ball.ballX) < j)
            {
                stopMoveLeft(); stopMoveRight();;
                return;
            }
            if(ball.ballX < player2.playerX)
            {
                startMoveRight();
                return;
            }
            if(ball.ballX > player2.playerX)
                startMoveLeft();
        }
    }

    private void doJump()
    {
        if(Math.random() < 0.40000000000000002D)
            startJump();
    }
}
