package SlimeGame;

public class CrapSlimeAI extends SlimeAI {

    private final double JUMPINESS = 0.40000000000000002D;
    private int serveType = -1;
	
	@Override
	public void moveSlime() {
        setVars();
		doMoveSlime();
	}

    private int square(int i)
    {
        return i * i;
    }

    private int howManyFrames(int i)
    {
        int j = 0;
        int k = ballY;
        int l = ballVY;
        while((k += --l) > i) 
            j++;
        return j;
    }

    private int whereWillBallCross(int i)
    {
        int j = howManyFrames(i);
        int k = ballX;
        int l = ballVX;
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
        if(ballX < 500 && serveType != -1)
            serveType = -1;
        int i = whereWillBallCross(125);
        howManyFrames(125);
        int j;
        if(this.p2Y != 0 && this.p2X < 575)
            j = 0;
        else
            j = 23 + (int)(15D * Math.random());
        if(ballVX == 0 && ballX == 800 || serveType != -1)
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
                if(ballY < 300 && ballVY < -3)
                {
                    move(1);
                    move(2);
                    return;
                }
                break;

            case 1: // '\001'
                if(ballY < 300 && ballVY < 0)
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
            if((this.p2X >= 900 && ballX > 830 || this.p2X <= 580 && ballX < 530) && Math.abs(ballX - this.p2X) < 100)
                jump();
            else
            if(square(ballX - this.p2X) * 2 + square(ballY - this.p2Y) < square(170) && ballX != this.p2X)
                jump();
            else
            if(ballVX * ballVX + ballVY * ballVY < 20 && ballX - this.p2X < 30 && ballX != this.p2X)
                jump();
            else
            if(Math.abs(ballX - this.p2X) < 150 && ballY > 50 && ballY < 400 && Math.random() < 0.5D)
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
            if(Math.abs(this.p2X - ballX) < j)
            {
                move(3);
                return;
            }
            if(ballX < this.p2X)
            {
                move(0);
                return;
            }
            if(ballX > this.p2X)
                move(1);
        }
    }

    private void jump()
    {
        if(Math.random() < JUMPINESS)
            move(2);
    }
    
}
