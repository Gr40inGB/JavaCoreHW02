package org.gr40in.app;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;

import java.io.PrintStream;
import java.util.Arrays;

public class GameApp extends Game {
    private final static int SIZE = 6;
    //    private Color[][] gameField = new Color[SIZE][SIZE];
    private Color playerMark = Color.GREEN;
    private Color aiMark = Color.RED;
    private Color empty = Color.WHITE;
    private int countToWin = 4;
    private int allTurns = SIZE * SIZE;

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (allowToPick(x, y)) {
            setCellColor(x, y, playerMark);
            checkWin(x, y, playerMark, 1);
            aiTurn();
        }
    }

    private boolean checkVector(int x, int xV, int y, int yV, Color mark) {
        return x + xV >= 0 &&
                x + xV < SIZE &&
                y + yV >= 0 &&
                y + yV < SIZE &&
                getCellColor(x + xV, y + yV) == mark;

    }

    private boolean checkWin(int x, int y, Color mark, int markCount) {
        int toLeft = -1;
        int toRight = 1;
        while (checkVector(x, toLeft--, y, 0, mark)) markCount++;
        while (checkVector(x, toRight++, y, 0, mark)) markCount++;
        if (!checkWinner(markCount, mark)) {
            markCount = 1;
        }

        int toUp = -1;
        int toDown = 1;
        while (checkVector(x, 0, y, toUp--, mark)) markCount++;
        while (checkVector(x, 0, y, toDown++, mark)) markCount++;

        int toNorthOstX = 1;
        int toNorthOstY = -1;
        int toSouthWestX = -1;
        int toSouthWestY = 1;

        while (checkVector(x, toNorthOstX++, y, toNorthOstY--, mark)) markCount++;
        while (checkVector(x, toSouthWestX--, y, toSouthWestY++, mark)) markCount++;


        if (!checkWinner(markCount, mark)) {
            markCount = 1;
        }

        int toNorthWestX = -1;
        int toNorthWestY = -1;
        int toSouthOstX = 1;
        int toSouthOstY = 1;

        while (checkVector(x, toNorthWestX--, y, toNorthWestY--, mark)) markCount++;
        while (checkVector(x, toSouthOstX++, y, toSouthOstY++, mark)) markCount++;


        if (!checkWinner(markCount, mark)) {
            markCount = 1;
        }

        return false;

    }

    private boolean checkWinner(int count, Color mark) {
        if (count == countToWin) {
            System.out.println(mark + " is a winner!");
            return true;
        }
        return false;
    }

    private void aiTurn() {
        int x = getRandomNumber(SIZE);
        int y = getRandomNumber(SIZE);

        if (allowToPick(x, y)) {
            setCellColor(x, y, aiMark);
            checkWin(x, y, aiMark, 1);
        } else aiTurn();
        System.out.println(x + "  " + y);
    }

    private boolean allowToPick(int x, int y) {
        return getCellColor(x, y) == empty;
    }


    @Override
    public void initialize() {
        setScreenSize(SIZE, SIZE);
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                setCellColor(y, x, empty);
            }
        }
    }


}
