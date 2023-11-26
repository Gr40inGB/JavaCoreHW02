package org.gr40in.app;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;

public class TicTacToe extends Game {

    private final static int SIZE = 5;
    private final int COUNT_TO_WIN = 4;

    private final Color PLAYER_MARK = Color.GREEN;
    private final Color AI_MARK = Color.RED;
    private final Color EMPTY = Color.WHITE;

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (allowToPick(x, y)) setCellColor(x, y, PLAYER_MARK);
        if (checkWin(x, y, PLAYER_MARK)) System.out.println("WIN" + x + "  " + y);

    }

    private boolean checkWin(int x, int y, Color mark) {
        // Left + Right
        if (calculatingMark(x, -1, y, 0, mark) +
                calculatingMark(x, 1, y, 0, mark) + 1 >= COUNT_TO_WIN) return true;
            // Up + Down
        else if (calculatingMark(x, 0, y, -1, mark) +
                calculatingMark(x, 0, y, 1, mark) + 1 >= COUNT_TO_WIN) return true;
            // SouthWest + NorthOst
        else if (calculatingMark(x, 1, y, -1, mark) +
                calculatingMark(x, -1, y, 1, mark) + 1 >= COUNT_TO_WIN) return true;
            // NorthWest + SouthOst
        else if (calculatingMark(x, -1, y, -1, mark) +
                calculatingMark(x, 1, y, 1, mark) + 1 >= COUNT_TO_WIN) return true;
        else return false;
    }


    private int calculatingMark(int x, int xV, int y, int yV, Color mark) {
        int currentX = x + xV;
        int currentY = y + yV;
        if (vectorInGameField(currentX, currentY) && sameMark(currentX, currentY, mark)) {
            return 1 + calculatingMark(currentX, xV, currentY, yV, mark);
        } else return 0;
    }

    private boolean vectorInGameField(int x, int y) {
        return x >= 0 &&
                x < SIZE &&
                y >= 0 &&
                y < SIZE;
    }

    private boolean sameMark(int x, int y, Color mark) {
        return getCellColor(x, y) == mark;
    }

    private boolean allowToPick(int x, int y) {
        return getCellColor(x, y) == EMPTY;
    }

    //region Game start init finish
    @Override
    public void start() {
        super.start();
    }

    @Override
    public void onKeyPress(Key key) {
        if (key.equals(Key.ESCAPE)) System.exit(0);
        if (key.equals(Key.SPACE)) start();
    }

    @Override
    public void initialize() {

        setScreenSize(SIZE, SIZE);

        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                setCellColor(x, y, EMPTY);
            }
        }
        showMessageDialog(Color.GRAY, "Click to continue; ESC to exit; SPACE to restart", Color.BLACK, 20);
    }
    //endregion

}
