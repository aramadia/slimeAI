package SlimeGame;

/**
 * Created with IntelliJ IDEA.
 * User: jlauer
 * Date: 3/27/13
 * Time: 1:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class DannoAI2 extends SlimeAI
{
    private int serveType = -1;

    @Override
    public void moveSlime() {
        setVars();
        doMoveSlime();
    }

    public void doMoveSlime()
    {
        if(serveType != -1 || ballX == 800 && ballVX == 0)
        {
            serve();
            return;
        }
        int i = xAtY(p2Y + p2YV + 30);
        byte byte0;
        if(i < 600)
            byte0 = 0;
        else
        if(i < 700)
            byte0 = 10;
        else
            byte0 = 20;
        if(i < 450)
        {
            if(Math.abs(p2X - 666) < 10)
                move(3);
            else
            if(p2X > 666)
                move(0);
            else
            if(p2X < 666)
                move(1);
        } else
        if(Math.abs(p2X - i - byte0) < 10)
            move(3);
        else
        if(i + byte0 < p2X)
            move(0);
        else
        if(i + byte0 > p2X)
            move(1);
        if((p2X <= 900 || Math.random() >= 0.40000000000000002D) && i >= 620 && (ballY >= 130 || ballVY >= 0) && (!p2Fire || Math.random() >= 0.59999999999999998D))
        {
            if((p2X >= 900 && ballX > 830 || p2X <= 580 && ballX < 530) && Math.abs(ballX - p2X) < 100)
            {
                move(2);
                return;
            }
            if(square(ballX - p2X) * 2 + square(ballY - p2Y) < square(185) && ballX != p2X)
            {
                move(2);
                return;
            }
            if(ballVX * ballVX + ballVY * ballVY < 20 && ballX - p2X < 30 && ballX != p2X)
            {
                move(2);
                return;
            }
            if(Math.abs(ballX - p2X) < (p2Fire ? 135 : '\226') && (ballY > 50 && ballY < 250))
                move(2);
        }
    }

    private int square(int i)
    {
        return i * i;
    }

    private int framesTillY(int i)
    {
        int j = 0;
        int k = ballY;
        int l = ballVY;
        while((k += --l) > 0)
            j++;
        return j;
    }

    private int xAtY(int i)
    {
        int j = ballX;
        int k = ballY;
        int l = ballVY;
        while((k += --l) > i)
        {
            j += ballVX;
            if(j <= 0)
            {
                j = 0;
                ballVX = -ballVX;
            } else
            if(j >= 1000)
            {
                j = 1000;
                ballVX = -ballVX;
            }
        }
        return j;
    }

    private int ballDist()
    {
        int i = p2X - ballX;
        int j = p2Y - ballY;
        return (int)Math.sqrt(i * i + j * j);
    }

    private void serve()
    {
        if(serveType == -1)
        {
            if(Math.random() < 0.29999999999999999D)
            {
                if(p1X < 300 && !p2Fire)
                    serveType = 0;
                else
                if(p1X > 200)
                    serveType = 1;
                else
                    serveType = 2;
            } else
            {
                serveType = 2;
            }
            if(serveType == -1 || Math.random() < 0.29999999999999999D)
                serveType = (int)(Math.random() * 3D);
            if(p2Fire && serveType == 0)
                serveType = 1 + (int)(Math.random() * 2D);
        }
        switch(serveType)
        {
            default:
                break;

            case 0: // '\0'
            case 1: // '\001'
                char c3 = serveType != 0 ? '\u0348' : '\u035C';
                if(ballVY > 12 && p2X < c3)
                    move(1);
                if(p2X >= c3)
                    move(3);
                if(ballVY == -3 && p2X != 800)
                    move(2);
                if(ballVY < -12 && p2Y != 0 && p2X >= c3 - 15 && serveType == 0)
                    move(0);
                if(ballX < 700)
                {
                    serveType = -1;
                    return;
                }
                break;

            case 2: // '\002'
                char c = '\u0302';
                if(ballVY > 12 && p2X > c)
                    move(0);
                if(p2X <= c)
                    move(3);
                if(ballVY == -2 && p2X != 800)
                    move(2);
                if(p2Y != 0 && ballX > 800)
                {
                    serveType = 3 + fakeJump();
                    return;
                }
                break;

            case 3: // '\003'
                char c1 = p2Fire ? '\u022B' : '\u0249';
                if(p2X > c1)
                    move(0);
                if(p2X <= c1)
                    move(3);
                if(ballX <= (p2Fire ? '\u02BC' : 730))
                    move(2);
                if(ballX < 540)
                {
                    serveType = -1;
                    return;
                }
                break;

            case 4: // '\004'
                char c2 = p2Fire ? '\u022B' : '\u0249';
                if(p2X > c2)
                    move(0);
                if(p2X <= c2)
                    move(3);
                if(ballX <= (p2Fire ? '\u02BC' : 730))
                    move(2);
                if(ballX < 600)
                    move(1);
                if(ballX < 580)
                    move(3);
                if(ballX < 540)
                {
                    serveType = -1;
                    return;
                }
                break;
        }
    }

    private int fakeJump()
    {
        int i = 0;
        if(p1X < 200)
            i = 1;
        else
        if(p1X > 300)
            i = 0;
        if(Math.random() < 0.34999999999999998D)
            i = 1 - i;
        return i;
    }

}
