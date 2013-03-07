package OneSlime;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SlimeAI.java


abstract class SlimeAI
{

    public final int init(Slime1P slime1p, int i)
    {
        app = slime1p;
        player = i;
        return team;
    }

    public final void saveVars(int ai[], boolean flag, boolean flag1)
    {
        ballX = player != 2 ? 1000 - ai[0] : ai[0];
        ballY = ai[1];
        ballVX = player != 2 ? -ai[2] : ai[2];
        ballVY = ai[3];
        p1X = player != 2 ? 1000 - ai[8] : ai[4];
        p1Y = player != 2 ? ai[9] : ai[5];
        p1VX = player != 2 ? -ai[10] : ai[6];
        p1VY = player != 2 ? ai[11] : ai[7];
        p2X = player != 2 ? 1000 - ai[4] : ai[8];
        p2Y = player != 2 ? ai[5] : ai[9];
        p2VX = player != 2 ? -ai[6] : ai[10];
        p2VY = player != 2 ? ai[7] : ai[11];
        p1Fire = player != 2 ? flag1 : flag;
        p2Fire = player != 2 ? flag : flag1;
    }

    public abstract void moveSlime();

    protected final void move(int i)
    {
        if(player == 1)
            switch(i)
            {
            case 0: // '\0'
                app.moveP1Right();
                return;

            case 1: // '\001'
                app.moveP1Left();
                return;

            case 2: // '\002'
                app.moveP1Jump();
                return;

            case 3: // '\003'
                app.moveP1Stop();
                return;
            }
        else
        if(player == 2)
            switch(i)
            {
            case 0: // '\0'
                app.moveP2Left();
                return;

            case 1: // '\001'
                app.moveP2Right();
                return;

            case 2: // '\002'
                app.moveP2Jump();
                return;

            case 3: // '\003'
                app.moveP2Stop();
                return;
            }
    }

    SlimeAI()
    {
        team = -1;
    }

    protected int ballX;
    protected int ballY;
    protected int ballVX;
    protected int ballVY;
    protected int p1X;
    protected int p1Y;
    protected int p1VX;
    protected int p1VY;
    protected int p2X;
    protected int p2Y;
    protected int p2VX;
    protected int p2VY;
    protected boolean p1Fire;
    protected boolean p2Fire;
    protected int team;
    private int player;
    private Slime1P app;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int JUMP = 2;
    public static final int STOP = 3;
}
