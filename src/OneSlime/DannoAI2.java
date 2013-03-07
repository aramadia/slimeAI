package OneSlime;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DannoAI2.java


public class DannoAI2 extends SlimeAI
{

    public DannoAI2()
    {
        super.team = 2;
        serveType = -1;
    }

    public void moveSlime()
    {
        if(serveType != -1 || super.ballX == 800 && super.ballVX == 0)
        {
            serve();
            return;
        }
        int i = xAtY(super.p2Y + super.p2VY + 30);
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
            if(Math.abs(super.p2X - 666) < 10)
                move(3);
            else
            if(super.p2X > 666)
                move(0);
            else
            if(super.p2X < 666)
                move(1);
        } else
        if(Math.abs(super.p2X - i - byte0) < 10)
            move(3);
        else
        if(i + byte0 < super.p2X)
            move(0);
        else
        if(i + byte0 > super.p2X)
            move(1);
        if((super.p2X <= 900 || Math.random() >= 0.40000000000000002D) && i >= 620 && (super.ballY >= 130 || super.ballVY >= 0) && (!super.p2Fire || Math.random() >= 0.59999999999999998D))
        {
            if((super.p2X >= 900 && super.ballX > 830 || super.p2X <= 580 && super.ballX < 530) && Math.abs(super.ballX - super.p2X) < 100)
            {
                move(2);
                return;
            }
            if(square(super.ballX - super.p2X) * 2 + square(super.ballY - super.p2Y) < square(185) && super.ballX != super.p2X)
            {
                move(2);
                return;
            }
            if(super.ballVX * super.ballVX + super.ballVY * super.ballVY < 20 && super.ballX - super.p2X < 30 && super.ballX != super.p2X)
            {
                move(2);
                return;
            }
            if(Math.abs(super.ballX - super.p2X) < (super.p2Fire ? 135 : '\226') && (super.ballY > 50 && super.ballY < 250))
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
        int k = super.ballY;
        int l = super.ballVY;
        while((k += --l) > 0) 
            j++;
        return j;
    }

    private int xAtY(int i)
    {
        int j = super.ballX;
        int k = super.ballY;
        int l = super.ballVY;
        while((k += --l) > i) 
        {
            j += super.ballVX;
            if(j <= 0)
            {
                j = 0;
                super.ballVX = -super.ballVX;
            } else
            if(j >= 1000)
            {
                j = 1000;
                super.ballVX = -super.ballVX;
            }
        }
        return j;
    }

    private int ballDist()
    {
        int i = super.p2X - super.ballX;
        int j = super.p2Y - super.ballY;
        return (int)Math.sqrt(i * i + j * j);
    }

    private void serve()
    {
        if(serveType == -1)
        {
            if(Math.random() < 0.29999999999999999D)
            {
                if(super.p1X < 300 && !super.p2Fire)
                    serveType = 0;
                else
                if(super.p1X > 200)
                    serveType = 1;
                else
                    serveType = 2;
            } else
            {
                serveType = 2;
            }
            if(serveType == -1 || Math.random() < 0.29999999999999999D)
                serveType = (int)(Math.random() * 3D);
            if(super.p2Fire && serveType == 0)
                serveType = 1 + (int)(Math.random() * 2D);
        }
        switch(serveType)
        {
        default:
            break;

        case 0: // '\0'
        case 1: // '\001'
            char c3 = serveType != 0 ? '\u0348' : '\u035C';
            if(super.ballVY > 12 && super.p2X < c3)
                move(1);
            if(super.p2X >= c3)
                move(3);
            if(super.ballVY == -3 && super.p2X != 800)
                move(2);
            if(super.ballVY < -12 && super.p2Y != 0 && super.p2X >= c3 - 15 && serveType == 0)
                move(0);
            if(super.ballX < 700)
            {
                serveType = -1;
                return;
            }
            break;

        case 2: // '\002'
            char c = '\u0302';
            if(super.ballVY > 12 && super.p2X > c)
                move(0);
            if(super.p2X <= c)
                move(3);
            if(super.ballVY == -2 && super.p2X != 800)
                move(2);
            if(super.p2Y != 0 && super.ballX > 800)
            {
                serveType = 3 + fakeJump();
                return;
            }
            break;

        case 3: // '\003'
            char c1 = super.p2Fire ? '\u022B' : '\u0249';
            if(super.p2X > c1)
                move(0);
            if(super.p2X <= c1)
                move(3);
            if(super.ballX <= (super.p2Fire ? '\u02BC' : 730))
                move(2);
            if(super.ballX < 540)
            {
                serveType = -1;
                return;
            }
            break;

        case 4: // '\004'
            char c2 = super.p2Fire ? '\u022B' : '\u0249';
            if(super.p2X > c2)
                move(0);
            if(super.p2X <= c2)
                move(3);
            if(super.ballX <= (super.p2Fire ? '\u02BC' : 730))
                move(2);
            if(super.ballX < 600)
                move(1);
            if(super.ballX < 580)
                move(3);
            if(super.ballX < 540)
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
        if(super.p1X < 200)
            i = 1;
        else
        if(super.p1X > 300)
            i = 0;
        if(Math.random() < 0.34999999999999998D)
            i = 1 - i;
        return i;
    }

    private int serveType;
}
