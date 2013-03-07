import java.awt.*;

public class SlimeV2 extends Panel implements Runnable, Constants {
    int nWidth;
    int nHeight;
    int nPointsScored;
    String promptMsg;
    boolean mousePressed;
    boolean fInPlay;
    boolean fEndGame;
    long startTime;
    long gameTime;
    Side sides[];
    Player players[];
    Ball balls[];

    /**
     * *******************************************************************
     */

    public SlimeV2() {
        sides = new Side[2];
        sides[0] = new Side(this, true,
                50, 445,
                Color.red, "Big Red Slime");
        sides[1] = new Side(this, false,
                555, 950,
                Color.green, "Magic Green Slime");

        players = new Player[2];
        players[0] = new Player(sides[0], 200);
        players[1] = new Player(sides[1], 800);

        balls = new Ball[1];
        balls[0] = new Ball(this, 200, 400);
    }

    /**
     * *******************************************************************
     */

    public Dimension getPreferredSize() {
        return new Dimension(640, 480);
    }

    /**
     * *******************************************************************
     */

    public int getGroundPos(int height) {
        return (4 * height) / 5;
    }

    public int getGroundSize(int height) {
        return height / 5;
    }

    public int getNetPos(int height) {
        return (7 * height) / 10;
    }

    public int getNetHeight(int height) {
        return height / 10 + 5;
    }

    public int getNetWidth(int width) {
        return 4;
    }

    /**
     * *******************************************************************
     */

    public boolean handleEvent(Event event) {
        switch (event.id) {
            default:
                break;

            case Event.MOUSE_MOVE:
                break;

            case Event.MOUSE_DOWN:
                mousePressed = true;
                if (!fInPlay) {
                    startGame();
                }
                break;

            case Event.KEY_ACTION:
            case Event.KEY_PRESS:
                if (fEndGame)
                    break;
                switch (event.key) {
                    case 'A':
                    case 'a':
                        players[0].startMoveLeft();
                        break;

                    case 'D':
                    case 'd':
                        players[0].startMoveRight();
                        break;

                    case 'W':
                    case 'w':
                        players[0].startJump();
                        break;

                    case Event.LEFT:
                    case 'J':
                    case 'j':
                        players[1].startMoveLeft();
                        break;

                    case Event.RIGHT:
                    case 'L':
                    case 'l':
                        players[1].startMoveRight();
                        break;

                    case Event.UP:
                    case 'I':
                    case 'i':
                        players[1].startJump();
                        break;

                    case ' ':
                        mousePressed = true;
                        break;

                    default:
                        break;
                }
                break;

            case Event.KEY_ACTION_RELEASE:
            case Event.KEY_RELEASE:
                switch (event.key) {
                    case 'A':
                    case 'a':
                        players[0].stopMoveLeft();
                        break;

                    case 'D':
                    case 'd':
                        players[0].stopMoveRight();
                        break;

                    case Event.LEFT:
                    case 'J':
                    case 'j':
                        players[1].stopMoveLeft();
                        break;

                    case Event.RIGHT:
                    case 'L':
                    case 'l':
                        players[1].stopMoveRight();
                        break;

                    default:
                        break;
                }
                break;
        }
        return false;
    }

    /**
     * *******************************************************************
     */

    public void drawCentered(Graphics g, String s, int y) {
        FontMetrics fontmetrics = g.getFontMetrics();

        g.drawString(s,
                (nWidth / 2) - (fontmetrics.stringWidth(s) / 2),
                y);
    }

    /**
     * *******************************************************************
     */

    public void paint(Graphics g) {
        int ground_pos;
        int ground_size;
        int net_pos;
        int net_height;
        int net_width;

        nWidth = size().width;
        nHeight = size().height;

    /* Draw sky and ground */
        ground_pos = getGroundPos(nHeight);
        ground_size = getGroundSize(nHeight);
        g.setColor(Color.blue);
        g.fillRect(0, 0, nWidth, ground_pos);
        g.setColor(Color.gray);
        g.fillRect(0, ground_pos, nWidth, nHeight / 5);

    /* Draw net */
        net_pos = getNetPos(nHeight);
        net_height = getNetHeight(nHeight);
        net_width = getNetWidth(nWidth);
        g.setColor(Color.white);
        g.fillRect(nWidth / 2 - (net_width / 2), net_pos,
                net_width, net_height);

    /* Draw other details */
        drawScores(g);
        drawPrompt(g);

    /* Draw prompt for starting the game */
        if (!fInPlay) {
            FontMetrics fm;

            g.setFont(new Font(g.getFont().getName(), 1, 15));
            fm = g.getFontMetrics();
            g.setColor(Color.white);
            drawCentered(g, "Slime Volleyball!",
                    nHeight / 2 - fm.getHeight());

            g.setFont(new Font(g.getFont().getName(), 1, 12));
            fm = g.getFontMetrics();
            g.setColor(Color.white);
            drawCentered(g, "Original by Quin Pendragon",
                    nHeight / 2 + fm.getHeight() * 2);
        }
    }

    /**
     * *******************************************************************
     */

    String microsecsToTime(long l) {
        long l1 = (l / 10L) % 100L;
        long l2 = (l / 1000L) % 60L;
        long l3 = (l / 60000L) % 60L;
        long l4 = l / 0x36ee80L;
        String s = "";
        if (l4 < 10L)
            s += "0";
        s += l4;
        s += ":";
        if (l3 < 10L)
            s += "0";
        s += l3;
        s += ":";
        if (l2 < 10L)
            s += "0";
        s += l2;
        s += ":";
        if (l1 < 10L)
            s += "0";
        s += l1;
        return s;
    }

    /**
     * *******************************************************************
     */

    public void drawPrompt(Graphics g, String s) {
        FontMetrics fm = g.getFontMetrics();
        g.setColor(Color.lightGray);
        drawCentered(g, s,
                (nHeight * 4) / 5 + fm.getHeight() + 10);
    }

    public void drawPrompt(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(0, (4 * nHeight) / 5 + 6, nWidth, nHeight / 5 - 10);
        drawPrompt(g, promptMsg);
    }

    /**
     * *******************************************************************
     */

    void drawStatus(Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int i = nHeight / 20;
        g.setColor(Color.blue);
        int j = nWidth / 2 + ((sides[0].score - 5) * nWidth) / 24;

        String s = ("Points: " + nPointsScored +
                "   Elapsed: " + microsecsToTime(gameTime));

        int k = fm.stringWidth(s);

        g.fillRect(j - k / 2 - 5, 0, k + 10, i + 22);
        g.setColor(Color.white);
        g.drawString(s, j - k / 2, fm.getAscent() + 20);
    }

    /**
     * *******************************************************************
     */

    void drawScores(Graphics g) {
        int i;
        int k = nHeight / 20;

        g.setColor(Color.blue);
        g.fillRect(0, 0, nWidth, k + 22);
        for (i = 0; i < sides.length; i++) {
            sides[i].drawScoreOnto(g);
        }
    }

    /**
     * *******************************************************************
     */

    public void drawParticipantsOnto(Graphics g) {
        int i;
        for (i = 0; i < players.length; i++) {
            players[i].drawOnto(g);
        }
        for (i = 0; i < balls.length; i++) {
            balls[i].drawOnto(g);
        }
    }

    /**
     * *******************************************************************
     */

    public void updateParticipantsState() {
        int i;
        for (i = 0; i < players.length; i++) {
            players[i].updateState();
        }
        for (i = 0; i < balls.length; i++) {
            balls[i].updateState();
        }
    }

    /**
     * *******************************************************************
     */

    public boolean ballLostAt(Graphics g, Ball b, int lost_at) {
        boolean game_over;
        boolean ball_touched;
        int i;

    /* Updates scores */
        nPointsScored++;
        if (lost_at <= 500) {
            sides[0].awardPoints(-1);
            sides[1].awardPoints(1);
        } else {
            sides[0].awardPoints(1);
            sides[1].awardPoints(-1);
        }
    
    /* Check if any side touched the ball or if any side is out
     * of lives. */
        ball_touched = false;
        game_over = false;
        for (i = 0; i < sides.length; i++) {
            ball_touched |= sides[i].didTouchBall();
            game_over |= (sides[i].getScore() == 0);
        }

    /* Build prompt */
        promptMsg = (lost_at <= 500 ? sides[1].toString() : sides[0].toString());
        promptMsg += " ";

        if (!ball_touched)
            promptMsg = "What can I say?";
        else if (sides[0].scoringRun == SCORING_RUN_FOR_SUPER ||
                sides[1].scoringRun == SCORING_RUN_FOR_SUPER)
            promptMsg += "is on fire!";
        else if ((lost_at > 500 && sides[0].touched && !sides[1].touched) ||
                (lost_at <= 500 && !sides[0].touched && sides[1].touched))
            promptMsg += "aces the serve!";
        else if ((lost_at > 500 && !sides[0].touched && sides[1].touched) ||
                (lost_at <= 500 && sides[0].touched && !sides[1].touched))
            promptMsg += "dies laughing! :P";
        else
            switch (sides[0].score) {
                case 0:
                case 10:
                    if (nPointsScored == 5)
                        promptMsg += "Wins with a QUICK FIVE!!!";
                    else if (sides[0].scoringRun == 8 ||
                            sides[1].scoringRun == 8)
                        promptMsg += "Wins with a BIG NINE!!!";
                    else
                        promptMsg += "Wins!!!";
                    break;

                case 4:
                    promptMsg += (lost_at >= 500) ? "Scores!" : "takes the lead!!";
                    break;

                case 6:
                    promptMsg += (lost_at <= 500) ? "Scores!" : "takes the lead!!";
                    break;

                case 5:
                    promptMsg += "Equalizes!";
                    break;

                default:
                    promptMsg += "Scores!";
                    break;
            }

    /* Update the prompt */
        drawPrompt(g);
        try {
            Thread.sleep(2500L);
        } catch (InterruptedException _ex) {
        }
        promptMsg = "";
        drawPrompt(g);
    
    /* Restore state for the next point */
        if (!game_over) {
            for (i = 0; i < players.length; i++) {
                players[i].resetState();
            }
            for (i = 0; i < balls.length; i++) {
                balls[i].resetState();
            }
            b.ballX = (lost_at >= 500) ? 200 : 800;
            repaint();
        }

        return game_over;
    }

    /**
     * *******************************************************************
     */

    public void run() {
        Graphics g;
        boolean game_over = false;
        int i;

    /* Get a graphics context for updating the screen */
        g = getGraphics();

        startTime = System.currentTimeMillis();
        while (!game_over) {
            gameTime = System.currentTimeMillis() - startTime;

            updateParticipantsState();
            drawParticipantsOnto(g);
            drawStatus(g);

	/* Check if any of the balls have hit the floor */

            for (i = 0; i < balls.length; i++) {
                boolean is_lost;
                Ball b;

                b = balls[i];
                is_lost = b.isLost();
                if (is_lost) {
                    int lost_at = b.ballX;
                    long l;

                    l = System.currentTimeMillis();
                    game_over = ballLostAt(g, b, lost_at);
                    startTime += System.currentTimeMillis() - l;
                }
            }

	/* Wait 20ms before next update */
            try {
                Thread.sleep(20L);
            } catch (InterruptedException _ex) {
            }
        }

        fEndGame = true;
        fInPlay = false;
        promptMsg = "Click the mouse to play...";
        repaint();
    }

    /**
     * *******************************************************************
     */

    public void startGame() {
        Thread game_thread;

        fEndGame = false;
        fInPlay = true;
        nPointsScored = 0;
        promptMsg = "";
        for (int i = 0; i < sides.length; i++) {
            sides[i].resetState();
        }
        for (int i = 0; i < players.length; i++) {
            players[i].resetState();
        }
        for (int i = 0; i < balls.length; i++) {
            balls[i].resetState();
        }
        repaint();
        game_thread = new Thread(this);
        game_thread.start();
    }

    /**
     * *******************************************************************
     */

    public static void main(String args[]) {
        SlimeV2 p;
        Frame f;
        Graphics g;
        p = new SlimeV2();
        f = new Frame();
        f.add(p);
        f.pack();
        p.nWidth = p.size().width;
        p.nHeight = p.size().height;
        p.fInPlay = p.fEndGame = false;
        p.promptMsg = "Click the mouse to play...";
        g = p.getGraphics();
        g.setFont(new Font(g.getFont().getName(), 1, 15));
        f.show();
    }

}
