package SlimeGame;

import java.awt.*;

public class Side {
    int minX;
    int maxX;
    SlimeV2 game;
    String description;

    Color playersColour;

    boolean onLeft;
    boolean touched;

    int score;
    int scoringRun;
    int initialScore;

    public Side(SlimeV2 game,
                boolean onLeft,
                int minX,
                int maxX,
                Color playersColour,
                String description,
                int initialScore) {
        this.game = game;
        this.onLeft = onLeft;
        this.minX = minX;
        this.maxX = maxX;
        this.playersColour = playersColour;
        this.description = description;
        this.initialScore = initialScore;
        resetState();
    }

    public Color getPlayersColour() {
        return playersColour;
    }

    public void resetState() {
        this.score = initialScore;
        this.scoringRun = 0;
        this.touched = false;
    }

    public void awardPoints(int n) {
        score += n;
        if (n >= 0) {
            scoringRun += n;
        } else {
            scoringRun = 0;
        }
    }

    public void drawScoreOnto(Graphics g) {
        int score_x_pos;
        int score_y_pos;
        int score_width;
        int i;

        score_width = game.nHeight / 20;
        score_y_pos = 20;

        for (i = 0; i < score; i++) {
            if (onLeft) {
                score_x_pos = ((i + 1) * game.nWidth) / 24;
            } else {
                score_x_pos = game.nWidth - ((i + 1) * game.nWidth) / 24 - score_width;
            }

            g.setColor(playersColour);
            g.fillOval(score_x_pos, score_y_pos,
                    score_width, score_width);

            g.setColor(Color.white);
            g.drawOval(score_x_pos, score_y_pos,
                    score_width, score_width);
        }
    }

    public int getScore() {
        return score;
    }

    public int getScoringRun() {
        return scoringRun;
    }

    public void setTouched() {
        touched = true;
    }

    public boolean didTouchBall() {
        return touched;
    }

    public String toString() {
        return description;
    }
}
