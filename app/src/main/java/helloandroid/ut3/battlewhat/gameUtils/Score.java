package helloandroid.ut3.battlewhat.gameUtils;

import androidx.annotation.NonNull;

public class Score {
    private int scorePoint;

    public Score() {
        this.scorePoint = 0;
    }

    public int getScorePoint() {
        return scorePoint;
    }

    public void addPoint(int scorePoint) {
        this.scorePoint += scorePoint;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder score = new StringBuilder();
        int length = String.valueOf(scorePoint).length();

        int NB_DIGITS = 3;
        for(int i = length; i < NB_DIGITS; i++) {
            score.append("0");
        }
        score.append(scorePoint);
        return score.toString();
    }
}
