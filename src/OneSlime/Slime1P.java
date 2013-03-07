package OneSlime;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Slime1P.java

import java.applet.Applet;
import java.awt.*;
import java.io.PrintStream;

public class Slime1P extends Applet
    implements Runnable
{

    public void init()
    {
        System.out.println("One Slime: http://www.student.uwa.edu.au/~wedgey/slime1/");
        Object obj = new CrapAI();
        obj = new DannoAI();
        obj = new DannoAI2();
        nWidth = size().width;
        nHeight = size().height;
        fInPlay = fEndGame = false;
        promptMsg = "Click the mouse to play!";
        screen = getGraphics();
        screen.setFont(new Font(screen.getFont().getName(), 1, 15));
        slimeColText = (new String[] {
            "Inferior Human Controlled Slime ", "The Pathetic White Slime ", "Angry Red Slimonds ", "The Slime Master ", "Psycho Slime ", "Psycho Slime "
        });
        slimeColours = (new Color[] {
            Color.yellow, Color.white, Color.red, Color.black, Color.blue, Color.blue
        });
        loserText1 = (new String[] {
            "You are a loser!", slimeColText[2] + "gives you the gong!", slimeColText[3] + "says \"You are seriously inept.\"", slimeColText[4] + "laughs at the pathetic slow opposition.", slimeColText[5] + "is still invincible!"
        });
        loserText2 = (new String[] {
            "Better luck next time.", "So who has the red face bombing out on level 2, huh?", "Congrats on reaching level 3.", "Congrats on reaching level 4!", "You fell at the last hurdle... but get up and try again!"
        });
        p1Col = 0;
        gameScore = 0;
        gameOver = true;
        paused = false;
        setAI();
        repaint();
    }

    private void setAI()
    {
        fP1PointsWon = 0;
        fP2PointsWon = 0;
        switch(aiMode)
        {
        case 0: // '\0'
            ai = new CrapAI();
            fP2Fire = false;
            SKY_COL = Color.blue;
            COURT_COL = Color.gray;
            BALL_COL = Color.yellow;
            break;

        case 1: // '\001'
            ai = new DannoAI();
            fP2Fire = false;
            SKY_COL = new Color(30, 80, 0);
            COURT_COL = Color.darkGray;
            BALL_COL = new Color(128, 128, 255);
            break;

        case 2: // '\002'
            ai = new DannoAI2();
            fP2Fire = false;
            SKY_COL = new Color(98, 57, 57);
            COURT_COL = new Color(0, 168, 0);
            BALL_COL = Color.white;
            break;

        case 3: // '\003'
            ai = new DannoAI2();
            fP2Fire = true;
            SKY_COL = Color.black;
            COURT_COL = Color.red;
            BALL_COL = Color.yellow;
            break;

        case 4: // '\004'
            ai = new DannoAI2();
            fP2Fire = true;
            SKY_COL = Color.black;
            COURT_COL = Color.red;
            BALL_COL = Color.yellow;
            fP2PointsWon = 5;
            break;
        }
        p2Col = aiMode + 1;
        ai.init(this, 2);
    }

    public void paint(Graphics g)
    {
        nWidth = size().width;
        nHeight = size().height;
        g.setColor(SKY_COL);
        g.fillRect(0, 0, nWidth, (4 * nHeight) / 5);
        g.setColor(COURT_COL);
        g.fillRect(0, (4 * nHeight) / 5, nWidth, nHeight / 5);
        g.setColor(Color.white);
        g.fillRect(nWidth / 2 - 2, (7 * nHeight) / 10, 4, nHeight / 10 + 5);
        FontMetrics fontmetrics = g.getFontMetrics();
        if(gameOver)
        {
            screen.drawString("Slime Volleyball: One Slime", nWidth / 2 - screen.getFontMetrics().stringWidth("Slime Volleyball: One Slime") / 2, nHeight / 2 - fontmetrics.getHeight());
            g.setColor(Color.white);
            g.drawString("Code base by Quin Pendragon", nWidth / 2 - fontmetrics.stringWidth("Code base by Quin Pendragon") / 2, nHeight / 2 + fontmetrics.getHeight() * 2);
            g.drawString("AI and Mod by Daniel Wedge", nWidth / 2 - fontmetrics.stringWidth("AI and Mod by Daniel Wedge") / 2, nHeight / 2 + fontmetrics.getHeight() * 3);
            g.drawString("Latest version is at http://www.student.uwa.edu.au/~wedgey/", nWidth / 2 - fontmetrics.stringWidth("Latest version is at http://www.student.uwa.edu.au/~wedgey/") / 2, nHeight / 2 + (fontmetrics.getHeight() * 9) / 2);
            if(aiMode != 0)
                promptMsg = "Click the mouse to play or press C to continue...";
            else
                promptMsg = "Click the mouse to play!";
            drawScores();
            DrawStatus();
            drawPrompt();
            promptMsg = "";
            return;
        }
        if(!fInPlay)
        {
            g.setColor(Color.white);
            screen.drawString("Level " + (aiMode + 1) + " clear!", nWidth / 2 - screen.getFontMetrics().stringWidth("Level " + aiMode + " clear!") / 2, nHeight / 3);
            g.drawString("Your score: " + gameScore, nWidth / 2 - fontmetrics.stringWidth("Your score: " + gameScore) / 2, nHeight / 2 - fontmetrics.getHeight());
            if(fP1PointsWon == 6)
            {
                g.drawString("Level bonus: " + ((1000 * fP1PointsWon) / (fP1PointsWon + fP2PointsWon)) * scale() + " points", nWidth / 2 - fontmetrics.stringWidth("Level bonus: " + ((1000 * fP1PointsWon) / (fP1PointsWon + fP2PointsWon)) * scale() + " points") / 2, nHeight / 2 + fontmetrics.getHeight());
                g.drawString("Time bonus: " + ((gameTime >= 0x493e0L ? 0L : 0x493e0L - gameTime) / 1000L) * (long)scale() + " points", nWidth / 2 - fontmetrics.stringWidth("Time bonus: " + ((gameTime >= 0x493e0L ? 0L : 0x493e0L - gameTime) / 1000L) * (long)scale() + " points") / 2, nHeight / 2 + fontmetrics.getHeight() * 2);
                if(fP2PointsWon == 0)
                    g.drawString("Flawless Victory: " + 1000 * scale() + " points", nWidth / 2 - fontmetrics.stringWidth("Flawless Victory: " + 1000 * scale() + " points") / 2, nHeight / 2 + fontmetrics.getHeight() * 3);
            }
            promptMsg = "Click the mouse to continue...";
            drawPrompt();
            promptMsg = "";
            drawScores();
            return;
        } else
        {
            drawScores();
            return;
        }
    }

    public boolean handleEvent(Event event)
    {
label0:
        switch(event.id)
        {
        default:
            break;

        case 503: // Event.MOUSE_MOVE
            showStatus("Slime Volleyball: One Slime: http://www.student.uwa.edu.au/~wedgey/");
            break;

        case 501: // Event.MOUSE_DOWN
            mousePressed = true;
            if(fInPlay)
                break;
            fEndGame = false;
            fInPlay = true;
            p1X = 200;
            p1Y = 0;
            p2X = 800;
            p2Y = 0;
            p1XV = 0;
            p1YV = 0;
            p2XV = 0;
            p2YV = 0;
            ballX = 200;
            ballY = 400;
            ballVX = 0;
            ballVY = 0;
            hitNetSinceTouched = false;
            promptMsg = "";
            if(gameScore != 0 && aiMode < 4)
                aiMode++;
            if(gameOver)
            {
                aiMode = 0;
                gameOver = false;
                gameScore = 0;
            }
            setAI();
            repaint();
            gameThread = new Thread(this);
            gameThread.start();
            break;

        case 401: // Event.KEY_PRESS
        case 403: // Event.KEY_ACTION
            switch(event.key)
            {
            default:
                break;

            case 75: // 'K'
            case 107: // 'k'
                aiMode = 0;
                // fall through

            case 76: // 'L'
            case 108: // 'l'
                fP1PointsWon = fP2PointsWon = 0;
                setAI();
                gameScore = 0;
                fP1Touched = fP2Touched = false;
                fP1Touches = fP2Touches = 0;
                hitNetSinceTouched = false;
                p1X = 200;
                p1Y = 0;
                p2X = 800;
                p2Y = 0;
                p1XV = 0;
                p1YV = 0;
                p2XV = 0;
                p2YV = 0;
                ballX = 200;
                ballY = 400;
                ballVX = 0;
                ballVY = 0;
                startTime = System.currentTimeMillis();
                paused = false;
                repaint();
                break label0;

            case 65: // 'A'
            case 97: // 'a'
            case 1006: 
                moveP1Left();
                break label0;

            case 68: // 'D'
            case 100: // 'd'
            case 1007: 
                moveP1Right();
                break label0;

            case 87: // 'W'
            case 119: // 'w'
            case 1004: 
                moveP1Jump();
                break label0;

            case 67: // 'C'
            case 99: // 'c'
                if(gameOver)
                {
                    fEndGame = false;
                    fInPlay = true;
                    p1X = 200;
                    p1Y = 0;
                    p2X = 800;
                    p2Y = 0;
                    p1XV = 0;
                    p1YV = 0;
                    p2XV = 0;
                    p2YV = 0;
                    ballX = 200;
                    ballY = 400;
                    ballVX = 0;
                    ballVY = 0;
                    hitNetSinceTouched = false;
                    promptMsg = "";
                    gameOver = false;
                    gameScore = 0;
                    setAI();
                    repaint();
                    gameThread = new Thread(this);
                    gameThread.start();
                }
                break label0;

            case 80: // 'P'
            case 112: // 'p'
                if(!paused)
                {
                    pausedTime = System.currentTimeMillis();
                    paused = true;
                } else
                {
                    startTime += System.currentTimeMillis() - pausedTime;
                    paused = false;
                }
                break;
            }
            break;

        case 402: // Event.KEY_RELEASE
        case 404: // Event.KEY_ACTION_RELEASE
            switch(event.key)
            {
            default:
                break label0;

            case 65: // 'A'
            case 97: // 'a'
            case 1006: 
                if(p1XV < 0)
                    p1XV = 0;
                break label0;

            case 68: // 'D'
            case 100: // 'd'
            case 1007: 
                break;
            }
            if(p1XV > 0)
                p1XV = 0;
            break;
        }
        return false;
    }

    public void moveP1Left()
    {
        p1XV = fP1Fire ? -16 : -8;
        if(p1X == 200 && ballX == 200 && !fP2Touched && !fServerMoved)
            fServerMoved = true;
    }

    public void moveP1Right()
    {
        p1XV = fP1Fire ? 16 : 8;
        if(p1X == 200 && ballX == 200 && !fP2Touched && !fServerMoved)
            fServerMoved = true;
    }

    public void moveP1Stop()
    {
        p1XV = 0;
    }

    public void moveP1Jump()
    {
        if(p1Y == 0)
            p1YV = fP1Fire ? 45 : 31;
    }

    public void moveP2Left()
    {
        p2XV = fP2Fire ? -16 : -8;
        if(p2X == 800 && ballX == 800 && !fP1Touched && !fServerMoved)
            fServerMoved = true;
    }

    public void moveP2Right()
    {
        p2XV = fP2Fire ? 16 : 8;
        if(p2X == 800 && ballX == 800 && !fP1Touched && !fServerMoved)
            fServerMoved = true;
    }

    public void moveP2Stop()
    {
        p2XV = 0;
    }

    public void moveP2Jump()
    {
        if(p2Y == 0)
            p2YV = fP2Fire ? 45 : 31;
    }

    private void doAI()
    {
        int ai1[] = {
            ballX, ballY, ballVX, ballVY, p1X, p1Y, p1XV, p1YV, p2X, p2Y, 
            p2XV, p2YV
        };
        ai.saveVars(ai1, fP1Fire, fP2Fire);
        ai.moveSlime();
    }

    private void MoveSlimers()
    {
        doAI();
        p1X += p1XV;
        if(p1X < 50)
            p1X = 50;
        if(p1X > 445)
            p1X = 445;
        if(p1YV != 0)
        {
            p1Y += p1YV -= fP1Fire ? 4 : 2;
            if(p1Y < 0)
            {
                p1Y = 0;
                p1YV = 0;
            }
        }
        p2X += p2XV;
        if(p2X > 950)
            p2X = 950;
        if(p2X < 555)
            p2X = 555;
        if(p2YV != 0)
        {
            p2Y += p2YV -= fP2Fire ? 4 : 2;
            if(p2Y < 0)
            {
                p2Y = 0;
                p2YV = 0;
            }
        }
    }

    private void DrawSlimers()
    {
        superFlash = !superFlash;
        int i = nWidth / 10;
        int j = nHeight / 10;
        int k = nWidth / 50;
        int l = nHeight / 25;
        int i1 = (ballX * nWidth) / 1000;
        int j1 = (4 * nHeight) / 5 - (ballY * nHeight) / 1000;
        int k1 = (p1OldX * nWidth) / 1000 - i / 2;
        int l1 = (7 * nHeight) / 10 - (p1OldY * nHeight) / 1000;
        screen.setColor(SKY_COL);
        screen.fillRect(k1, l1, i, j);
        k1 = (p2OldX * nWidth) / 1000 - i / 2;
        l1 = (7 * nHeight) / 10 - (p2OldY * nHeight) / 1000;
        screen.setColor(SKY_COL);
        screen.fillRect(k1, l1, i, j);
        MoveBall();
        k1 = (p1X * nWidth) / 1000 - i / 2;
        l1 = (7 * nHeight) / 10 - (p1Y * nHeight) / 1000;
        screen.setColor(!fP1Fire || !superFlash ? slimeColours[p1Col] : Color.white);
        screen.fillArc(k1, l1, i, 2 * j, 0, 180);
        int i2 = p1X + 38;
        int j2 = p1Y - 60;
        k1 = (i2 * nWidth) / 1000;
        l1 = (7 * nHeight) / 10 - (j2 * nHeight) / 1000;
        int k2 = k1 - i1;
        int l2 = l1 - j1;
        int i3 = (int)Math.sqrt(k2 * k2 + l2 * l2);
        screen.setColor(Color.white);
        screen.fillOval(k1 - k, l1 - l, k, l);
        screen.setColor(Color.black);
        screen.fillOval(k1 - (4 * k2) / i3 - (3 * k) / 4, l1 - (4 * l2) / i3 - (3 * l) / 4, k / 2, l / 2);
        k1 = (p2X * nWidth) / 1000 - i / 2;
        l1 = (7 * nHeight) / 10 - (p2Y * nHeight) / 1000;
        screen.setColor(!fP2Fire || !superFlash ? slimeColours[p2Col] : Color.white);
        screen.fillArc(k1, l1, i, 2 * j, 0, 180);
        i2 = p2X - 18;
        j2 = p2Y - 60;
        k1 = (i2 * nWidth) / 1000;
        l1 = (7 * nHeight) / 10 - (j2 * nHeight) / 1000;
        k2 = k1 - i1;
        l2 = l1 - j1;
        i3 = (int)Math.sqrt(k2 * k2 + l2 * l2);
        screen.setColor(Color.white);
        screen.fillOval(k1 - k, l1 - l, k, l);
        screen.setColor(Color.black);
        screen.fillOval(k1 - (4 * k2) / i3 - (3 * k) / 4, l1 - (4 * l2) / i3 - (3 * l) / 4, k / 2, l / 2);
        if(!fP1Fire && !fP2Fire)
            superFlash = false;
    }

    private void MoveBall()
    {
        int i = (30 * nHeight) / 1000;
        int j = (ballOldX * nWidth) / 1000;
        int k = (4 * nHeight) / 5 - (ballOldY * nHeight) / 1000;
        screen.setColor(SKY_COL);
        screen.fillOval(j - i, k - i, i * 2, i * 2);
        ballY += --ballVY;
        ballX += ballVX;
        if(!fEndGame)
        {
            int l = (ballX - p1X) * 2;
            int i1 = ballY - p1Y;
            int j1 = l * l + i1 * i1;
            int k1 = ballVX - p1XV;
            int l1 = ballVY - p1YV;
            if(i1 > 0 && j1 < 15625 && j1 > 25)
            {
                int i2 = (int)Math.sqrt(j1);
                int k2 = (l * k1 + i1 * l1) / i2;
                ballX = p1X + (l * 63) / i2;
                ballY = p1Y + (i1 * 125) / i2;
                if(k2 <= 0)
                {
                    ballVX += p1XV - (2 * l * k2) / i2;
                    if(ballVX < -15)
                        ballVX = -15;
                    if(ballVX > 15)
                        ballVX = 15;
                    ballVY += p1YV - (2 * i1 * k2) / i2;
                    if(ballVY < -22)
                        ballVY = -22;
                    if(ballVY > 22)
                        ballVY = 22;
                }
                if(fServerMoved)
                {
                    fP1Touched = true;
                    fP1Touches++;
                    fP1TouchesTot++;
                    fP2Touches = 0;
                    fP1HitStill = p1YV == 0 && p1XV == 0;
                    hitNetSinceTouched = false;
                }
            }
            l = (ballX - p2X) * 2;
            i1 = ballY - p2Y;
            j1 = l * l + i1 * i1;
            k1 = ballVX - p2XV;
            l1 = ballVY - p2YV;
            if(i1 > 0 && j1 < 15625 && j1 > 25)
            {
                int j2 = (int)Math.sqrt(j1);
                int l2 = (l * k1 + i1 * l1) / j2;
                ballX = p2X + (l * 63) / j2;
                ballY = p2Y + (i1 * 125) / j2;
                if(l2 <= 0)
                {
                    ballVX += p2XV - (2 * l * l2) / j2;
                    if(ballVX < -15)
                        ballVX = -15;
                    if(ballVX > 15)
                        ballVX = 15;
                    ballVY += p2YV - (2 * i1 * l2) / j2;
                    if(ballVY < -22)
                        ballVY = -22;
                    if(ballVY > 22)
                        ballVY = 22;
                }
                if(fServerMoved)
                {
                    fP2Touched = true;
                    fP1Touches = 0;
                    fP2Touches++;
                    fP2TouchesTot++;
                    fP2HitStill = p2YV == 0 && p2XV == 0;
                    hitNetSinceTouched = false;
                }
            }
            if(ballX < 15)
            {
                ballX = 15;
                ballVX = -ballVX;
            }
            if(ballX > 985)
            {
                ballX = 985;
                ballVX = -ballVX;
            }
            if(ballX > 480 && ballX < 520 && ballY < 140)
                if(ballVY < 0 && ballY > 130)
                {
                    ballVY *= -1;
                    ballY = 130;
                } else
                if(ballX < 500)
                {
                    ballX = 480;
                    ballVX = ballVX < 0 ? ballVX : -ballVX;
                    hitNetSinceTouched = true;
                } else
                {
                    ballX = 520;
                    ballVX = ballVX > 0 ? ballVX : -ballVX;
                    hitNetSinceTouched = true;
                }
        }
        j = (ballX * nWidth) / 1000;
        k = (4 * nHeight) / 5 - (ballY * nHeight) / 1000;
        screen.setColor(BALL_COL);
        screen.fillOval(j - i, k - i, i * 2, i * 2);
    }

    private void drawScores()
    {
        Graphics g = screen;
        g.getFontMetrics();
        int i = nHeight / 15;
        g.setColor(SKY_COL);
        g.fillRect(0, 0, nWidth, i + 22);
        int j = 20;
        for(int k = 0; k < 6; k++)
        {
            if(fP1PointsWon >= k + 1)
            {
                g.setColor(slimeColours[p1Col]);
                g.fillOval(j, 30 - i / 2, i, i);
            }
            g.setColor(Color.white);
            g.drawOval(j, 30 - i / 2, i, i);
            j += i + 10;
        }

        j = nWidth - 20 - 6 * (i + 10);
        for(int l = 0; l < 6; l++)
        {
            if(fP2PointsWon >= 6 - l)
            {
                g.setColor(slimeColours[p2Col]);
                g.fillOval(j, 30 - i / 2, i, i);
            }
            g.setColor(Color.white);
            g.drawOval(j, 30 - i / 2, i, i);
            j += i + 10;
        }

    }

    private String MakeTime(long l)
    {
        String s = "";
        long l1 = (l / 1000L) % 60L;
        long l2 = (l / 60000L) % 60L;
        s = s + l2 + ":";
        if(l1 < 10L)
            s = s + "0";
        s = s + l1;
        return s;
    }

    private void DrawStatus()
    {
        Graphics g = screen;
        int i = nHeight / 20;
        g.setColor(SKY_COL);
        FontMetrics fontmetrics = screen.getFontMetrics();
        String s = "Score: " + gameScore + (fInPlay ? "   Time: " + MakeTime((paused ? pausedTime : System.currentTimeMillis()) - startTime) : "");
        int j = fontmetrics.stringWidth(s);
        int k = nWidth / 2 - j / 2 - 10;
        g.fillRect(k, 0, j + 20, i + 22);
        g.setColor(Color.white);
        screen.drawString(s, nWidth / 2 - fontmetrics.stringWidth(s) / 2, fontmetrics.getHeight() * 2);
    }

    public void drawPrompt()
    {
        screen.setColor(COURT_COL);
        screen.fillRect(0, (4 * nHeight) / 5 + 6, nWidth, nHeight / 5 - 10);
        drawPrompt(promptMsg, 0);
    }

    public void drawPrompt(String s, int i)
    {
        FontMetrics fontmetrics = screen.getFontMetrics();
        screen.setColor(Color.white);
        screen.drawString(s, (nWidth - fontmetrics.stringWidth(s)) / 2, (nHeight * 4) / 5 + fontmetrics.getHeight() * (i + 1) + 10);
    }

    public void run()
    {
        drawPrompt();
        superFlash = false;
        scoringRun = 0;
        fP1Touches = 0;
        fP2Touches = 0;
        fP1TouchesTot = 0;
        fP2TouchesTot = 0;
        fP1Clangers = 0;
        fP2Clangers = 0;
        fP1Aces = 0;
        fP2Aces = 0;
        fP1Winners = 0;
        fP2Winners = 0;
        fP1Frames = 0L;
        fP2Frames = 0L;
        fP1Super = 0;
        fP2Super = 0;
        fP1HitStill = false;
        fP2HitStill = false;
        fServerMoved = false;
        drawScores();
        fP1Touched = fP2Touched = false;
        hitNetSinceTouched = false;
        boolean flag = false;
        boolean flag4 = false;
        boolean flag5 = false;
        gameOver = false;
        startTime = System.currentTimeMillis();
        while(gameThread != null && !gameOver) 
        {
            if(!paused)
            {
                p1OldX = p1X;
                p1OldY = p1Y;
                p2OldX = p2X;
                p2OldY = p2Y;
                ballOldX = ballX;
                ballOldY = ballY;
                MoveSlimers();
                DrawSlimers();
                DrawStatus();
            }
            if(ballY < 35)
            {
                long l = System.currentTimeMillis();
                if(ballX > 500)
                    fP1PointsWon++;
                else
                    fP2PointsWon++;
                if(ballX <= 500 && (fP1Touches >= 3 || hitNetSinceTouched && fP1Touches > 0 || !fP2Touched || fP1HitStill && fP1Touches > 0))
                {
                    fP1Clangers++;
                    boolean flag1 = true;
                } else
                if(ballX > 500 && (fP2Touches >= 3 || hitNetSinceTouched && fP2Touches > 0 || !fP1Touched || fP2HitStill && fP2Touches > 0))
                {
                    fP2Clangers++;
                    boolean flag2 = true;
                }
                if(fP1Touched && !fP2Touched && ballX >= 500)
                {
                    fP1Aces++;
                    flag4 = true;
                    gameScore += 200 * scale();
                } else
                if(fP2Touched && !fP1Touched && ballX < 500)
                {
                    fP2Aces++;
                    flag4 = true;
                } else
                if(ballX > 500 && fP1Touches > 0)
                {
                    fP1Winners++;
                    flag5 = true;
                    gameScore += 100 * scale();
                } else
                if(ballX <= 500 && fP2Touches > 0)
                {
                    fP2Winners++;
                    flag5 = true;
                }
                if(ballX > 500 && !flag5 && !flag4)
                    gameScore += 50 * scale();
                promptMsg = ballX > 500 ? slimeColText[p1Col] : slimeColText[p2Col];
                if(fP1PointsWon == 6 || fP2PointsWon == 6)
                    promptMsg += "wins!";
                else
                if(flag4)
                    promptMsg += "aces the serve!";
                else
                if(flag5)
                    promptMsg += "scores a winner!";
                else
                if(ballX > 500 && !fP1Touched && fP2Touched || ballX <= 500 && fP1Touched && !fP2Touched)
                    promptMsg += "laughs at his opponent's inability to serve!";
                else
                if(fP1PointsWon == fP2PointsWon)
                    promptMsg += "draws level!";
                else
                if(ballX > 500 && fP1PointsWon == fP2PointsWon + 1 || ballX <= 500 && fP1PointsWon + 1 == fP2PointsWon)
                    promptMsg += "takes the lead!";
                else
                    promptMsg += "scores!";
                int i = ballX;
                drawPrompt();
                drawScores();
                DrawStatus();
                boolean flag3 = false;
                flag4 = false;
                flag5 = false;
                mousePressed = false;
                sleep(1500L, true);
                if(fP1PointsWon == 6 || fP2PointsWon == 6)
                    finishGame();
                promptMsg = "";
                drawPrompt();
                p1X = 200;
                p1Y = 0;
                p2X = 800;
                p2Y = 0;
                p1XV = 0;
                p1YV = 0;
                p2XV = 0;
                p2YV = 0;
                ballX = i < 500 ? 800 : 200;
                ballY = 400;
                ballVX = 0;
                ballVY = 0;
                fP1Touched = fP2Touched = false;
                fServerMoved = false;
                repaint();
                startTime += System.currentTimeMillis() - l;
            }
            if(gameThread != null)
                try
                {
                    Thread.sleep(20L);
                }
                catch(InterruptedException _ex) { }
        }
        fEndGame = true;
        fInPlay = false;
        promptMsg = "";
        repaint();
    }

    private void finishGame()
    {
        if(fP1PointsWon == 6)
        {
            gameTime = System.currentTimeMillis() - startTime;
            if(fP1PointsWon == 6)
            {
                gameScore += ((1000 * fP1PointsWon) / (fP1PointsWon + fP2PointsWon)) * scale();
                gameScore += ((gameTime >= 0x493e0L ? 0L : 0x493e0L - gameTime) / 1000L) * (long)scale();
            }
            if(fP2PointsWon == 0)
                gameScore += 1000 * scale();
            if(aiMode == 4)
            {
                aiMode = 5;
                gameOver(true);
            }
        } else
        {
            gameOver(false);
        }
        fInPlay = false;
        gameThread = null;
    }

    private void gameOver(boolean flag)
    {
        FontMetrics fontmetrics = screen.getFontMetrics();
        drawScores();
        DrawStatus();
        Graphics g = getGraphics();
        FontMetrics fontmetrics1 = g.getFontMetrics();
        if(!flag)
        {
            g.setColor(COURT_COL);
            g.fillRect((nWidth - max(fontmetrics1.stringWidth(loserText1[aiMode]), fontmetrics1.stringWidth(loserText2[aiMode]))) / 2 - 30, nHeight / 2 - fontmetrics1.getAscent() * 5, max(fontmetrics1.stringWidth(loserText1[aiMode]), fontmetrics1.stringWidth(loserText2[aiMode])) + 60, fontmetrics1.getAscent() * 5 + fontmetrics.getAscent() * 2);
            g.setColor(Color.white);
            g.drawString(loserText1[aiMode], (nWidth - fontmetrics1.stringWidth(loserText1[aiMode])) / 2, nHeight / 2 - fontmetrics1.getAscent() * 3);
            g.drawString(loserText2[aiMode], (nWidth - fontmetrics1.stringWidth(loserText2[aiMode])) / 2, nHeight / 2 - fontmetrics1.getAscent() * 2);
            screen.drawString("GAME OVER", (nWidth - fontmetrics.stringWidth("GAME OVER")) / 2, nHeight / 2 + fontmetrics.getAscent());
        } else
        {
            fatality(g);
            g.setColor(Color.white);
            screen.drawString("YOU WIN!", (nWidth - fontmetrics.stringWidth("YOU WIN!")) / 2, nHeight / 2);
            g.drawString("The Slimes bow down before the new Slime King!", (nWidth - fontmetrics1.stringWidth("The Slimes bow down before the new Slime King!")) / 2, nHeight / 2 + fontmetrics1.getAscent());
        }
        sleep(3000L, false);
        gameOver = true;
    }

    private void fatality(Graphics g)
    {
    }

    private void drawP1()
    {
        int i = nWidth / 10;
        int j = nHeight / 10;
        int k = nWidth / 50;
        int l = nHeight / 25;
        int i1 = (ballX * nWidth) / 1000;
        int j1 = (4 * nHeight) / 5 - (ballY * nHeight) / 1000;
        int k1 = (p1OldX * nWidth) / 1000 - i / 2;
        int l1 = (7 * nHeight) / 10 - (p1OldY * nHeight) / 1000;
        screen.setColor(SKY_COL);
        screen.fillRect(k1, l1, i, j);
        k1 = (p2OldX * nWidth) / 1000 - i / 2;
        l1 = (7 * nHeight) / 10 - (p2OldY * nHeight) / 1000;
        screen.setColor(SKY_COL);
        screen.fillRect(k1, l1, i, j);
        MoveBall();
        k1 = (p1X * nWidth) / 1000 - i / 2;
        l1 = (7 * nHeight) / 10 - (p1Y * nHeight) / 1000;
        screen.setColor(!fP1Fire || !superFlash ? slimeColours[p1Col] : Color.white);
        screen.fillArc(k1, l1, i, 2 * j, 0, 180);
        int i2 = p1X + 38;
        int j2 = p1Y - 60;
        k1 = (i2 * nWidth) / 1000;
        l1 = (7 * nHeight) / 10 - (j2 * nHeight) / 1000;
        int k2 = k1 - i1;
        int l2 = l1 - j1;
        int i3 = (int)Math.sqrt(k2 * k2 + l2 * l2);
        screen.setColor(Color.white);
        screen.fillOval(k1 - k, l1 - l, k, l);
        screen.setColor(Color.black);
        screen.fillOval(k1 - (4 * k2) / i3 - (3 * k) / 4, l1 - (4 * l2) / i3 - (3 * l) / 4, k / 2, l / 2);
    }

    private int max(int i, int j)
    {
        if(i > j)
            return i;
        else
            return j;
    }

    private void sleep(long l, boolean flag)
    {
        if(gameThread != null)
        {
            for(int i = 0; (long)i < l / 20L; i++)
                try
                {
                    Thread.sleep(20L);
                }
                catch(InterruptedException _ex) { }

        }
    }

    private int scale()
    {
        return (int)Math.pow(2D, aiMode);
    }

    public void destroy()
    {
        if(gameThread != null)
        {
            gameThread.stop();
            gameThread = null;
        }
    }

    public Slime1P()
    {
    }

    private int nWidth;
    private int nHeight;
    private int p1X;
    private int p1Y;
    private int p2X;
    private int p2Y;
    private int p1Col;
    private int p2Col;
    private int p1OldX;
    private int p1OldY;
    private int p2OldX;
    private int p2OldY;
    private int p1XV;
    private int p1YV;
    private int p2XV;
    private int p2YV;
    private int ballX;
    private int ballY;
    private int ballVX;
    private int ballVY;
    private int ballOldX;
    private int ballOldY;
    private Graphics screen;
    private String promptMsg;
    private boolean mousePressed;
    private boolean fInPlay;
    private boolean fP1Fire;
    private boolean fP2Fire;
    private boolean superFlash;
    private boolean fP1Touched;
    private boolean fP2Touched;
    private int fP1Touches;
    private int fP2Touches;
    private int fP1TouchesTot;
    private int fP2TouchesTot;
    private int fP1Clangers;
    private int fP2Clangers;
    private int fP1Aces;
    private int fP2Aces;
    private int fP1Winners;
    private int fP2Winners;
    private int fP1PointsWon;
    private int fP2PointsWon;
    private boolean fP1HitStill;
    private boolean fP2HitStill;
    private long fP1Frames;
    private long fP2Frames;
    private int fP1Super;
    private int fP2Super;
    private boolean fServerMoved;
    private boolean hitNetSinceTouched;
    private Thread gameThread;
    private boolean fEndGame;
    private long startTime;
    private long gameTime;
    private long crossedNetTime;
    private long pausedTime;
    private boolean paused;
    private int scoringRun;
    private int oldScoringRun;
    private String slimeColText[];
    private Color slimeColours[];
    private String loserText1[];
    private String loserText2[];
    private Color SKY_COL;
    private Color COURT_COL;
    private Color BALL_COL;
    private final int pointsToWin = 6;
    private int aiMode;
    private SlimeAI ai;
    private int gameScore;
    private boolean gameOver;
}
