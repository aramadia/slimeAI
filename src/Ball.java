import java.awt.*;

public class Ball {
    int initialX;
    int initialY;

    int ballX;
    int ballY;
    int ballVX;
    int ballVY;
    int ballOldX;
    int ballOldY;

    boolean isLost;

    SlimeV2 game;

    public Ball(SlimeV2 game, int initialX, int initialY) {
        this.initialX = initialX;
        this.initialY = initialY;
        this.game = game;
        resetState();
    }

    public void resetState() {
        ballX = initialX;
        ballY = initialY;
        ballVX = 0;
        ballVY = 0;
        isLost = false;
    }

    public void updateState() {
        ballOldX = ballX;
        ballOldY = ballY;

        ballY += --ballVY;
        ballX += ballVX;
        if (!game.fEndGame) {
            int i;
            for (i = 0; i < game.players.length; i++) {
                game.players[i].intersectWithBall(this);
            }

            if (ballX < 15) {
                ballX = 15;
                ballVX = -ballVX;
            }
            if (ballX > 985) {
                ballX = 985;
                ballVX = -ballVX;
            }

            if (ballX > 480 && ballX < 520 && ballY < 140) {
                if (ballVY < 0 && ballY > 130) {
                    ballVY *= -1;
                    ballY = 130;
                } else {
                    if (ballX < 500) {
                        ballX = 480;
                        ballVX = ballVX >= 0 ? -ballVX : ballVX;
                    } else {
                        ballX = 520;
                        ballVX = ballVX <= 0 ? -ballVX : ballVX;
                    }
                }
            }
        }

        isLost |= (ballY < 35);
    }

    public void drawOnto(Graphics g) {
        int k = (30 * game.nHeight) / 1000;
        int i = (ballOldX * game.nWidth) / 1000;
        int j = (4 * game.nHeight) / 5 - (ballOldY * game.nHeight) / 1000;
        g.setColor(Color.blue);
        g.fillOval(i - k, j - k, k * 2, k * 2);
        i = (ballX * game.nWidth) / 1000;
        j = (4 * game.nHeight) / 5 - (ballY * game.nHeight) / 1000;
        g.setColor(Color.yellow);
        g.fillOval(i - k, j - k, k * 2, k * 2);
    }

    public boolean isLost() {
        return isLost;
    }
}
