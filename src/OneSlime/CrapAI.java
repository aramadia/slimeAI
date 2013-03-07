package OneSlime;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CrapAI.java


public class CrapAI extends SlimeAI
{

    public CrapAI()
    {
        super.team = 4;
        serveType = -1;
    }

    private int square(int i)
    {
        return i * i;
    }

    private int howManyFrames(int i)
    {
        int j = 0;
        int k = super.ballY;
        int l = super.ballVY;
        while((k += --l) > i) 
            j++;
        return j;
    }

    private int whereWillBallCross(int i)
    {
        int j = howManyFrames(i);
        int k = super.ballX;
        int l = super.ballVX;
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

    public void moveSlime()
    {
        if(super.ballX < 500 && serveType != -1)
            serveType = -1;
        int i = whereWillBallCross(125);
        howManyFrames(125);
        int j;
        if(super.p2Y != 0 && super.p2X < 575)
            j = 0;
        else
            j = 23 + (int)(15D * Math.random());
        if(super.ballVX == 0 && super.ballX == 800 || serveType != -1)
        {
            if(serveType == -1)
            {
                if(super.p1X > 250)
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
                if(super.ballY < 300 && super.ballVY < -3)
                {
                    move(1);
                    move(2);
                    return;
                }
                break;

            case 1: // '\001'
                if(super.ballY < 300 && super.ballVY < 0)
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
            if(Math.abs(super.p2X - 800) < 20)
            {
                move(3);
                return;
            }
            if(super.p2X > 800)
            {
                move(0);
                return;
            }
            if(super.p2X < 800)
                move(1);
            return;
        }
        if(Math.abs(super.p2X - i) < j)
        {
            if(super.p2Y != 0 || super.p2Fire && Math.random() < 0.29999999999999999D)
                return;
            if((super.p2X >= 900 && super.ballX > 830 || super.p2X <= 580 && super.ballX < 530) && Math.abs(super.ballX - super.p2X) < 100)
                jump();
            else
            if(square(super.ballX - super.p2X) * 2 + square(super.ballY - super.p2Y) < square(170) && super.ballX != super.p2X)
                jump();
            else
            if(super.ballVX * super.ballVX + super.ballVY * super.ballVY < 20 && super.ballX - super.p2X < 30 && super.ballX != super.p2X)
                jump();
            else
            if(Math.abs(super.ballX - super.p2X) < 150 && super.ballY > 50 && super.ballY < 400 && Math.random() < 0.5D)
                jump();
        }
        if(super.p2Y == 0 && serveType == -1)
        {
            if(Math.abs(super.p2X - i) < j)
            {
                move(3);
                return;
            }
            if(i + j < super.p2X)
            {
                move(0);
                return;
            }
            if(i + j > super.p2X)
            {
                move(1);
                return;
            }
        } else
        if(serveType == -1)
        {
            if(super.p2X < 575)
                return;
            if(super.p2X > 900)
            {
                move(1);
                return;
            }
            if(Math.abs(super.p2X - super.ballX) < j)
            {
                move(3);
                return;
            }
            if(super.ballX < super.p2X)
            {
                move(0);
                return;
            }
            if(super.ballX > super.p2X)
                move(1);
        }
    }

    private void jump()
    {
        if(Math.random() < 0.40000000000000002D)
            move(2);
    }

    private final double JUMPINESS = 0.40000000000000002D;
    private int serveType;
}
