package SlimeGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Panel;
import java.util.concurrent.*;

import javax.swing.JFrame;


public class SlimeV2 implements Callable<Integer>, Constants {
    public static final int STARTING_POINTS = 1;

    public enum ServeSide {
        LEFT,
        RIGHT
    }

    SlimePanel panel = new SlimePanel();
    boolean shouldDraw;
    int nWidth;
    int nHeight;
    int nPointsScored;
    String promptMsg;
    boolean mousePressed;
    boolean fInPlay;
    boolean fEndGame;
    long startTime;
    long gameTime;
    long frames = 0;
    Side sides[];
    Player players[];
    Ball balls[];
    SlimeAI ai[];


    public SlimeV2(boolean draw, ServeSide side, SlimeAI ai1, SlimeAI ai2) {
        shouldDraw = draw;
        sides = new Side[2];
        sides[0] = new Side(this, true,
                50, 445,
                Color.red, "Big Red Slime", STARTING_POINTS);
        sides[1] = new Side(this, false,
                555, 950,
                Color.green, "Magic Green Slime", STARTING_POINTS);

        players = new Player[2];
        players[0] = new Player(sides[0], 200);
        players[1] = new Player(sides[1], 800);

        ai = new SlimeAI[2];
        if (ai1 != null) {
            ai1.initialize(this, players[0]);
        }
        if (ai2 != null) {
            ai2.initialize(this, players[1]);
        }
        ai[0] = ai1;
        ai[1] = ai2;

        balls = new Ball[1];

        if (side == ServeSide.LEFT) {
            // LEFT
            balls[0] = new Ball(this, 200, 400);
        } else {
            // RIGHT
            balls[0] = new Ball(this, 800, 400);
        }
    }

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


    public void updateParticipantsState() {
        int i;
        for (i = 0; i < players.length; i++) {
            players[i].updateState();
        }
        for (i = 0; i < balls.length; i++) {
            balls[i].updateState();
        }
    }

    public boolean ballLostAt(Ball b, int lost_at) {
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
        System.out.println(promptMsg);
        panel.drawPrompt();
        if (shouldDraw) {
            try {
                Thread.sleep(2500L);
            } catch (InterruptedException _ex) {
            }
        } else {
//            gameTime += 2500L;
        }
        promptMsg = "";
        panel.drawPrompt();
    
        /* Restore state for the next point */
        if (!game_over) {
            for (i = 0; i < players.length; i++) {
                players[i].resetState();
            }
            for (i = 0; i < balls.length; i++) {
                balls[i].resetState();
            }
            b.ballX = (lost_at >= 500) ? 200 : 800;
            panel.repaint();
        }

        return game_over;
    }

    public Integer call() {
        startGame();
        boolean game_over = false;
        int i;

        startTime = System.currentTimeMillis();
        while (!game_over) {
            frames++;
            gameTime = System.currentTimeMillis() - startTime;

            processAIcommands();
            updateParticipantsState();
            panel.drawParticipantsOnto();
            panel.drawStatus();

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
                    game_over = ballLostAt(b, lost_at);
                    startTime += System.currentTimeMillis() - l;
                }
            }

	        /* Wait 20ms before next update */
            if (shouldDraw) {
                try {
                    Thread.sleep(20L);
                } catch (InterruptedException _ex) {
                }
            }
        }

        System.out.println("GAME OVER!!!!");

        fEndGame = true;
        fInPlay = false;
        promptMsg = "Click the mouse to play...";
        panel.repaint();

        System.out.println("sides[0].score = " + sides[0].score);
        System.out.println("sides[1].score = " + sides[1].score);
        System.out.println("Num frames = " + frames);

        if (sides[0].score > sides[1].score) {
            return 0;
        } else {
            return 1;
        }
    }

    private void processAIcommands() {
        for (SlimeAI slimeAI : ai) {
            if (slimeAI != null) {
                slimeAI.moveSlime();
            }
        }
    }


    public void startGame() {
        fEndGame = false;
        fInPlay = true;
        nPointsScored = 0;
        promptMsg = "";
        for (Side side : sides) {
            side.resetState();
        }
        for (Player player : players) {
            player.resetState();
        }
        for (Ball ball : balls) {
            ball.resetState();
        }
        panel.repaint();
    }


    public static void main(String args[]) {
        SlimeAI human = null;
        SlimeAI loseAI = new LoseAI();
        SlimeAI crapSlimeAI = new CrapSlimeAI();
        SlimeAI dannoAI = new DannoAI();
        SlimeAI dannoAI2 = new DannoAI2();
        int winner = determineVictor(true, ServeSide.RIGHT, dannoAI, dannoAI2);
        System.out.println("winner = player " + winner);
    }

    /**
     *
     *
     * @param draw should draw the UI
     * @param serveSide
     * @param ai1 AI for player 1 (null for human player)
     * @param ai2 AI for player 2 (null for human player)
     * @return 0 if player1 wins, 1 if player2 wins
     */
    public static int determineVictor(boolean draw, ServeSide serveSide, SlimeAI ai1, SlimeAI ai2) {
        SlimeV2 game = new SlimeV2(draw, serveSide, ai1, ai2);

        if (draw) {
            JFrame f = new JFrame();

            f.add(game.panel);
            f.pack();
            game.nWidth = game.panel.size().width;
            game.nHeight = game.panel.size().height;
            game.fInPlay = game.fEndGame = false;
            game.promptMsg = "Click the mouse to play...";
            game.panel.setFont();
            f.show();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        int NTHREADS = 1;
        ExecutorService service = Executors.newFixedThreadPool(NTHREADS);
        Future<Integer> task = service.submit(game);

        int winner = -1;
        try {
            winner = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        service.shutdownNow();

        return winner;
    }

    /**
     * GUI Panel
     */
    private class SlimePanel extends Panel {

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(640, 480);
        }

        @Override
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

        @Override
        public void paint(Graphics g) {
            if (!shouldDraw) return;

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
            drawScores();
            drawPrompt();

            /* Draw prompt for starting the game */
            if (!fInPlay) {
                FontMetrics fm;

                g.setFont(new Font(g.getFont().getName(), 1, 15));
                fm = g.getFontMetrics();
                g.setColor(Color.white);
                drawCentered("Slime Volleyball!",
                        nHeight / 2 - fm.getHeight());

                g.setFont(new Font(g.getFont().getName(), 1, 12));
                fm = g.getFontMetrics();
                g.setColor(Color.white);
                drawCentered("Original by Quin Pendragon",
                        nHeight / 2 + fm.getHeight() * 2);
            }
        }

        public void setFont() {
            if (!shouldDraw) return;

            Graphics g = getGraphics();
            g.setFont(new Font(g.getFont().getName(), Font.BOLD, 15));
        }

        public void drawCentered(String s, int y) {
            if (!shouldDraw) return;

            Graphics g = getGraphics();
            FontMetrics fontmetrics = g.getFontMetrics();

            g.drawString(s,
                    (nWidth / 2) - (fontmetrics.stringWidth(s) / 2),
                    y);
        }

        public void drawPrompt(String s) {
            if (!shouldDraw) return;

            Graphics g = getGraphics();
            FontMetrics fm = g.getFontMetrics();
            g.setColor(Color.lightGray);
            drawCentered(s, (nHeight * 4) / 5 + fm.getHeight() + 10);
        }

        public void drawPrompt() {
            if (!shouldDraw) return;

            Graphics g = getGraphics();
            g.setColor(Color.gray);
            g.fillRect(0, (4 * nHeight) / 5 + 6, nWidth, nHeight / 5 - 10);
            drawPrompt(promptMsg);
        }

        void drawStatus() {
            if (!shouldDraw) return;

            Graphics g = getGraphics();
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

        void drawScores() {
            if (!shouldDraw) return;

            Graphics g = getGraphics();
            int i;
            int k = nHeight / 20;

            g.setColor(Color.blue);
            g.fillRect(0, 0, nWidth, k + 22);
            for (i = 0; i < sides.length; i++) {
                sides[i].drawScoreOnto(g);
            }
        }

        public void drawParticipantsOnto() {
            if (!shouldDraw) return;

            Graphics g = getGraphics();
            int i;
            for (i = 0; i < players.length; i++) {
                players[i].drawOnto(g);
            }
            for (i = 0; i < balls.length; i++) {
                balls[i].drawOnto(g);
            }
        }

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
    }

}
