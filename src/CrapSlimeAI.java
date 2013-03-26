
public class CrapSlimeAI extends SlimeAI {
	
	Ball ball;
	
	int p2X, p2Y;
	int p1X, p1Y;
	boolean p2Fire;
	

	CrapSlimeAI() {
        serveType = -1;
	}

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
	
   
       
    

    private int square(int i)
    {
        return i * i;
    }

    private int howManyFrames(int i)
    {
        int j = 0;
        int k = ball.ballY;
        int l = ball.ballVY;
        while((k += --l) > i) 
            j++;
        return j;
    }

    private int whereWillBallCross(int i)
    {
        int j = howManyFrames(i);
        int k = ball.ballX;
        int l = ball.ballVX;
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

    private void doMoveSlime()
    {
        if(ball.ballX < 500 && serveType != -1)
            serveType = -1;
        int i = whereWillBallCross(125);
        howManyFrames(125);
        int j;
        if(this.p2Y != 0 && this.p2X < 575)
            j = 0;
        else
            j = 23 + (int)(15D * Math.random());
        if(ball.ballVX == 0 && ball.ballX == 800 || serveType != -1)
        {
            if(serveType == -1)
            {
                if(p1X > 250)
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
                    move(1);
                    move(2);
                    return;
                }
                break;

            case 1: // '\001'
                if(ball.ballY < 300 && ball.ballVY < 0)
                {
                    move(0);
                    move(2);
                    return;
                }
                break;
            }
            return;
        }
        if(i < 500)
        {
            if(Math.abs(this.p2X - 800) < 20)
            {
                move(3);
                return;
            }
            if(this.p2X > 800)
            {
                move(0);
                return;
            }
            if(this.p2X < 800)
                move(1);
            return;
        }
        if(Math.abs(this.p2X - i) < j)
        {
            if(this.p2Y != 0 || this.p2Fire && Math.random() < 0.29999999999999999D)
                return;
            if((this.p2X >= 900 && ball.ballX > 830 || this.p2X <= 580 && ball.ballX < 530) && Math.abs(ball.ballX - this.p2X) < 100)
                jump();
            else
            if(square(ball.ballX - this.p2X) * 2 + square(ball.ballY - this.p2Y) < square(170) && ball.ballX != this.p2X)
                jump();
            else
            if(ball.ballVX * ball.ballVX + ball.ballVY * ball.ballVY < 20 && ball.ballX - this.p2X < 30 && ball.ballX != this.p2X)
                jump();
            else
            if(Math.abs(ball.ballX - this.p2X) < 150 && ball.ballY > 50 && ball.ballY < 400 && Math.random() < 0.5D)
                jump();
        }
        if(this.p2Y == 0 && serveType == -1)
        {
            if(Math.abs(this.p2X - i) < j)
            {
                move(3);
                return;
            }
            if(i + j < this.p2X)
            {
                move(0);
                return;
            }
            if(i + j > this.p2X)
            {
                move(1);
                return;
            }
        } else
        if(serveType == -1)
        {
            if(this.p2X < 575)
                return;
            if(this.p2X > 900)
            {
                move(1);
                return;
            }
            if(Math.abs(this.p2X - ball.ballX) < j)
            {
                move(3);
                return;
            }
            if(ball.ballX < this.p2X)
            {
                move(0);
                return;
            }
            if(ball.ballX > this.p2X)
                move(1);
        }
    }

    private void jump()
    {
        if(Math.random() < 0.40000000000000002D)
            move(2);
    }
    
    protected final void move(int i)
    {
        
        switch(i)
        {
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

    private final double JUMPINESS = 0.40000000000000002D;
    private int serveType;

}
