package SlimeGame;

/**
 * Created with IntelliJ IDEA.
 * User: jlauer
 * Date: 3/19/13
 * Time: 7:09 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class SlimeAI
{

    protected SlimeV2 slimeGame;
    protected Player player;

    protected int ballX, ballY;
    protected int ballVX, ballVY;
    protected int p1X, p1Y;
    protected int p1XV, p1YV;
    protected int p2X, p2Y;
    protected int p2XV, p2YV;
    protected boolean p1Fire, p2Fire;

    public void initialize(SlimeV2 slimeGame, Player player) {
        this.slimeGame = slimeGame;
        this.player = player;
    }
    
    protected int howManyFrames(int yPos)
    {
        int time = 0;
        int curY = ballY;
        int curVY = ballVY;
        
        // gravity
        while((curY += --curVY) > yPos) 
            time++;
        return time;
    }

    protected int whereWillBallCross(int yPos)
    {
        int time = howManyFrames(yPos);
        int curX = ballX;
        int curVX = ballVX;
        for(int t = 0; t < time; t++)
        {
            curX += curVX;
            if(curX < 0)
            {
                curX = 0;
                curVX = -curVX;
            } else
            if(curX > 1000)
            {
                curX = 1000;
                curVX = -curVX;
            }
        }

        return curX;
    }

    public void setVars() {
        Ball ball = slimeGame.balls[0];
        Player player1 = slimeGame.players[0];
        Player player2 = slimeGame.players[1];


        if (player == player1) {
            // you are the left player swap the vars
            ballX = 1000 - ball.ballX;
            ballY = ball.ballY;
            ballVX = -ball.ballVX;
            ballVY = ball.ballVY;

            p1X = 1000 - player2.playerX;
            p1Y = player2.playerY;
            p1XV = -player2.playerXV;
            p1YV = player2.playerYV;

            p2X = 1000 - player1.playerX;
            p2Y = player1.playerY;
            p2XV = -player1.playerXV;
            p2YV = player1.playerYV;

            p1Fire = player2.onScoringRun(); //disabled
            p2Fire = player1.onScoringRun(); //disabled

        } else {
            ballX = ball.ballX;
            ballY = ball.ballY;
            ballVX = ball.ballVX;
            ballVY = ball.ballVY;

            p1X = player1.playerX;
            p1Y = player1.playerY;
            p1XV = player1.playerXV;
            p1YV = player1.playerYV;

            p2X = player2.playerX;
            p2Y = player2.playerY;
            p2XV = player2.playerXV;
            p2YV = player2.playerYV;

            p1Fire = player1.onScoringRun(); //disabled
            p2Fire = player2.onScoringRun(); //disabled
        }
        

    }

    public abstract void moveSlime();

    protected final void startMoveTowardsNet() {
        if (player == slimeGame.players[0]) {
            player.startMoveRight();
        } else {
            player.startMoveLeft();
        }
    }

    protected final void startMoveAwayFromNet() {
        if (player == slimeGame.players[0]) {
            player.startMoveLeft();
        } else {
            player.startMoveRight();
        }
    }

    protected final void startJump() {
        player.startJump();
    }

    protected final void stopMoving() {
        player.stopMoveLeft();
        player.stopMoveRight();
    }

    protected final void move(int i)
    {
//    	System.out.print(
//            	String.format("%d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d\n",
//            		ballX, ballY, ballVX, ballVY, p1X, p1Y, p1XV, p1YV, p2X, p2Y, p2XV, p2YV, i)
//        );
        switch(i)
        {
            case 0: // '\0'
                //app.moveP2Left();
                startMoveTowardsNet();
                return;

            case 1: // '\001'
                //app.moveP2Right();
                startMoveAwayFromNet();
                return;

            case 2: // '\002'
//            app.moveP2Jump();
                startJump();
                return;

            case 3: // '\003'
//            app.moveP2Stop();
                stopMoving();
                return;
        }
    }

}