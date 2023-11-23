package org.gr40in.app;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;

import java.io.PrintStream;
import java.util.Arrays;

public class GameApp extends Game {
    private final static int SIZE = 3;
    //    private Color[][] gameField = new Color[SIZE][SIZE];
    private final Color playerMark = Color.GREEN;
    private final Color aiMark = Color.RED;
    private final Color empty = Color.WHITE;
    private final int countToWin = 3;
    private final int allTurns = SIZE * SIZE;
    private int countPick = 0;
    boolean gameStop = false;

    @Override
    public void start() {
        countPick = 0;
        gameStop = false;
        super.start();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (!gameStop) {
            if (allowToPick(x, y)) {
                countPick++;
                setCellColor(x, y, playerMark);
                if (checkWin(x, y, playerMark)) showWinner(playerMark);
                if (countPick == allTurns) nobodyWon();
                aiTurn();
            }
        }
    }

    private void aiTurn() {
        int x = getRandomNumber(SIZE);
        int y = getRandomNumber(SIZE);

        if (allowToPick(x, y)) {
            setCellColor(x, y, aiMark);
            if (checkWin(x, y, aiMark)) showWinner(aiMark);
        } else aiTurn();
//        System.out.println(x + "  " + y);
    }

    @Override
    public void onKeyPress(Key key) {
        if (key.equals(Key.ESCAPE)) System.exit(0);
        if (key.equals(Key.SPACE)) start();
    }

    private void showWinner(Color mark) {
        gameStop = true;
        showMessageDialog(Color.BLACK, mark + " wins!", mark, 66);
    }

    private void nobodyWon() {
        showMessageDialog(Color.BLACK, "nobody won!", Color.WHITE, 55);
    }


    /**
     * @param x    start x coordinate
     * @param xV   x offset - can be negative and positive
     * @param y    start y coordinate
     * @param yV   y offset - can be negative and positive
     * @param mark - mark whose turn
     * @return true if we have appropriate mark on the "way" =)
     */
    private boolean checkVector(int x, int xV, int y, int yV, Color mark) {
        return x + xV >= 0 &&
                x + xV < SIZE &&
                y + yV >= 0 &&
                y + yV < SIZE &&
                getCellColor(x + xV, y + yV) == mark;
    }

    private boolean checkWin(int x, int y, Color mark) {
        int markCount = 1;
        int toLeft = -1;
        int toRight = 1;
        while (checkVector(x, toLeft--, y, 0, mark)) markCount++;
        while (checkVector(x, toRight++, y, 0, mark)) markCount++;
        if (checkWinner(markCount, mark)) return true;

        markCount = 1;
        int toUp = -1;
        int toDown = 1;
        while (checkVector(x, 0, y, toUp--, mark)) markCount++;
        while (checkVector(x, 0, y, toDown++, mark)) markCount++;
        if (checkWinner(markCount, mark)) return true;

        markCount = 1;
        int toNorthOstX = 1;
        int toNorthOstY = -1;
        int toSouthWestX = -1;
        int toSouthWestY = 1;
        while (checkVector(x, toNorthOstX++, y, toNorthOstY--, mark)) markCount++;
        while (checkVector(x, toSouthWestX--, y, toSouthWestY++, mark)) markCount++;
        if (checkWinner(markCount, mark)) return true;


        markCount = 1;
        int toNorthWestX = -1;
        int toNorthWestY = -1;
        int toSouthOstX = 1;
        int toSouthOstY = 1;

        while (checkVector(x, toNorthWestX--, y, toNorthWestY--, mark)) markCount++;
        while (checkVector(x, toSouthOstX++, y, toSouthOstY++, mark)) markCount++;

        return checkWinner(markCount, mark);

    }

    private boolean checkWinner(int count, Color mark) {
        if (count == countToWin) {
            System.out.println(mark + " is a winner!");
            return true;
        }
        return false;
    }

    private boolean allowToPick(int x, int y) {
        return getCellColor(x, y) == empty;
    }


    @Override
    public void initialize() {
        setScreenSize(SIZE, SIZE);
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                setCellColor(x, y, empty);
            }
        }

        showMessageDialog(Color.GRAY, "Click to continue;\n Escape to exit, Space to restart", Color.BLACK, 20);

    }


}
