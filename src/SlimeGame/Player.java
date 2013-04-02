package SlimeGame;

import java.awt.*;

public class Player implements Constants {
    public static final Color frenzyColours[] = new Color[]{
            Color.red, Color.green, Color.yellow, Color.white, Color.black
    };

    int frenzyColour;

    Side side;
    int initialX;

    int playerX;
    int playerY;
    int playerOldX;
    int playerOldY;
    int playerXV;
    int playerYV;
    int pBlink;
    int hits;

    public Player(Side side, int initialX) {
        this.side = side;
        this.initialX = initialX;
        resetState();
    }

    public void resetState() {
        playerX = initialX;
        playerY = 0;
        playerXV = 0;
        playerYV = 0;
        hits = 0;
    }

    public boolean onScoringRun() {
        boolean result;
        int current_run;

        current_run = side.scoringRun;
        if (current_run >= SCORING_RUN_FOR_SUPER) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }

    public void startMoveLeft() {
        if (onScoringRun()) {
            playerXV = -FAST_SPEED;
        } else {
            playerXV = -SLOW_SPEED;
        }
    }

    public void stopMoveLeft() {
        if (playerXV < 0) {
            playerXV = 0;
        }
    }

    public void startMoveRight() {
        if (onScoringRun()) {
            playerXV = FAST_SPEED;
        } else {
            playerXV = SLOW_SPEED;
        }
    }

    public void stopMoveRight() {
        if (playerXV > 0) {
            playerXV = 0;
        }
    }

    public void startJump() {
        if (playerY == 0) {
            if (onScoringRun()) {
                playerYV = HIGH_JUMP;
            } else {
                playerYV = LOW_JUMP;
            }
        }
    }

    public boolean updateState() {
        playerOldX = playerX;
        playerOldY = playerY;

        playerX += playerXV;
        if (playerX < side.minX)
            playerX = side.minX;
        if (playerX > side.maxX)
            playerX = side.maxX;

        if (playerYV != 0) {
            if (onScoringRun()) {
                playerYV -= HIGH_GRAVITY;
            } else {
                playerYV -= LOW_GRAVITY;
            }

            playerY += playerYV;

            if (playerY < 0) {
                playerY = 0;
                playerYV = 0;
            }
        }

        return false;
    }

    public void intersectWithBall(Ball b) {
        int l1 = (b.ballX - playerX) * 2;
        int i2 = b.ballY - playerY;
        int j2 = l1 * l1 + i2 * i2;
        int k2 = b.ballVX - playerXV;
        int l2 = b.ballVY - playerYV;

        if (i2 > 0 && j2 < 15625 && j2 > 25) {
            int l = (int) Math.sqrt(j2);
            int j1 = (l1 * k2 + i2 * l2) / l;
            b.ballX = playerX + (l1 * 63) / l;
            b.ballY = playerY + (i2 * 125) / l;
            if (j1 <= 0) {
                b.ballVX += playerXV - (2 * l1 * j1) / l;
                if (b.ballVX < -15)
                    b.ballVX = -15;
                if (b.ballVX > 15)
                    b.ballVX = 15;
                b.ballVY += playerYV - (2 * i2 * j1) / l;
                if (b.ballVY < -22)
                    b.ballVY = -22;
                if (b.ballVY > 22)
                    b.ballVY = 22;
            }
            hits++;
//            System.out.println(initialX%800 + " Consecutive hits " + hits);
        }
        side.setTouched();
    }

    public void drawOnto(Graphics screen) {
        int k1 = side.game.nWidth / 10;
        int j2 = side.game.nHeight / 10;
        int i3 = side.game.nWidth / 50;
        int j3 = side.game.nHeight / 25;
        int k3 = (side.game.balls[0].ballX * side.game.nWidth) / 1000;
        int l3 = (4 * side.game.nHeight) / 5 - (side.game.balls[0].ballY * side.game.nHeight) / 1000;
        int i = (playerOldX * side.game.nWidth) / 1000 - k1 / 2;
        int l = (7 * side.game.nHeight) / 10 - (playerOldY * side.game.nHeight) / 1000;
        int l4;
        int i5;

        screen.setColor(Color.blue);
        screen.fillRect(i, l, k1, j2);

        i = (playerX * side.game.nWidth) / 1000 - k1 / 2;
        l = (7 * side.game.nHeight) / 10 - (playerY * side.game.nHeight) / 1000;

        if (side.scoringRun >= SCORING_RUN_FOR_SUPER) {
            frenzyColour = (frenzyColour + 1) % frenzyColours.length;
            screen.setColor(frenzyColours[frenzyColour]);
        } else {
            screen.setColor(side.playersColour);
        }

        screen.fillArc(i, l, k1, 2 * j2, 0, 180);
        if (side.onLeft) {
            l4 = playerX + 38;
        } else {
            l4 = playerX - 18;
        }
        i5 = playerY - 60;

        i = (l4 * side.game.nWidth) / 1000;
        l = (7 * side.game.nHeight) / 10 - (i5 * side.game.nHeight) / 1000;
        int i4 = i - k3;
        int j4 = l - l3;
        int k4 = (int) Math.sqrt(i4 * i4 + j4 * j4);
        boolean flag = Math.random() < 0.01D;
        if (flag)
            pBlink = 5;
        if (pBlink == 0) {
            if (side.onLeft) {
                screen.setColor(Color.white);
            } else {
                screen.setColor(flag ? Color.gray : Color.white);
            }

            screen.fillOval(i - i3, l - j3, i3, j3);
            if (k4 > 0 && !flag) {
                screen.setColor(Color.black);
                screen.fillOval(i - (4 * i4) / k4 - (3 * i3) / 4, l - (4 * j4) / k4 - (3 * j3) / 4, i3 / 2, j3 / 2);
            }
        } else {
            pBlink--;
        }

        if (side.score > 8) {
            if (side.onLeft) {
                int j = (playerX * side.game.nWidth) / 1000;
                int i1 = (7 * side.game.nHeight) / 10 - ((playerY - 40) * side.game.nHeight) / 1000;
                int l1 = side.game.nWidth / 20;
                int k2 = side.game.nHeight / 20;
                int j5 = 0;
                do {
                    screen.setColor(Color.black);
                    screen.drawArc(j, i1 + j5, l1, k2, -30, -150);
                } while (++j5 < 3);
            } else {
                int i2 = side.game.nWidth / 20;
                int l2 = side.game.nHeight / 20;
                int k = (playerX * side.game.nWidth) / 1000 - i2;
                int j1 = (7 * side.game.nHeight) / 10 - ((playerY - 40) * side.game.nHeight) / 1000;
                int k5 = 0;
                do {
                    screen.setColor(Color.black);
                    screen.drawArc(k, j1 + k5, i2, l2, -10, -150);
                } while (++k5 < 3);
            }
        }
    }
}
