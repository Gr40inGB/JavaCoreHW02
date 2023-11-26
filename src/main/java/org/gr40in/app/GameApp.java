package org.gr40in.app;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;


public class GameApp extends Game {
    private final static int SIZE = 5;
    private final Color PLAYER_MARK = Color.GREEN;
    private final Color AI_MARK = Color.RED;
    private final Color EMPTY = Color.WHITE;

    private final int COUNT_TO_WIN = 4;
    private final int ALL_TURNS = SIZE * SIZE;
    private int countPick = 0;
    private boolean gameStop = false;

    @Override
    public void start() {
        countPick = 0;
        gameStop = false;
        super.start();
    }


    @Override
    public void onMouseLeftClick(int x, int y) {
        if (!gameStop && countPick < ALL_TURNS) {
            if (allowToPick(x, y)) {
                countPick++;
                setCellColor(x, y, PLAYER_MARK);
                if (checkWin(x, y, PLAYER_MARK, COUNT_TO_WIN)) showWinner(PLAYER_MARK);
                if (countPick == ALL_TURNS) nobodyWon();
                aiTurn();

            }
        }
    }

    private void aiTurn() {

        int x = getRandomNumber(SIZE);
        int y = getRandomNumber(SIZE);
        boolean anti_player_maked = false;

        for (int rows = 0; rows < SIZE; rows++) {
            for (int columns = 0; columns < SIZE; columns++) {
                if (allowToPick(columns, rows)) {
                    setCellColor(columns, rows, PLAYER_MARK);
                    if ((!anti_player_maked) && checkWin(columns, rows, PLAYER_MARK, COUNT_TO_WIN + 1)) {
                        setCellColor(columns, rows, AI_MARK);
                        x = columns;
                        y = rows;
                        anti_player_maked = true;
                        break;
                    } else setCellColor(columns, rows, EMPTY);

                }
            }
            if (anti_player_maked) break;
        }
        for (int rows = 0; rows < SIZE; rows++) {
            for (int columns = 0; columns < SIZE; columns++) {
                if (allowToPick(columns, rows)) {
                    setCellColor(columns, rows, PLAYER_MARK);
                    if ((!anti_player_maked) && checkWin(columns, rows, PLAYER_MARK, COUNT_TO_WIN)) {
                        setCellColor(columns, rows, AI_MARK);
                        x = columns;
                        y = rows;
                        anti_player_maked = true;
                        break;
                    } else setCellColor(columns, rows, EMPTY);

                }
            }
            if (anti_player_maked) break;
        }

        for (int rows = 0; rows < SIZE; rows++) {
            for (int columns = 0; columns < SIZE; columns++) {
                if (allowToPick(columns, rows)) {
                    setCellColor(columns, rows, PLAYER_MARK);

                    if ((!anti_player_maked) && checkWin(columns, rows, PLAYER_MARK, COUNT_TO_WIN - 1)) {
                        setCellColor(columns, rows, AI_MARK);
                        x = columns;
                        y = rows;
                        anti_player_maked = true;
                        break;
                    } else setCellColor(columns, rows, EMPTY);
                }
            }
            if (anti_player_maked) break;
        }

        if (!anti_player_maked) {
            if (allowToPick(x, y)) {
                countPick++;
                setCellColor(x, y, AI_MARK);

            } else if (countPick < ALL_TURNS) aiTurn();
//            else return;
        }
        if (checkWin(x, y, AI_MARK, COUNT_TO_WIN)) showWinner(AI_MARK);
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

    private int plusator(int x, int xV, int y, int yV, Color mark) {
        if (x + xV >= 0 &&
                x + xV < SIZE &&
                y + yV >= 0 &&
                y + yV < SIZE &&
                getCellColor(x + xV, y + yV) == mark)
            return 1 + plusator(x, xV + (xV == 0 ? 0 : (xV > 0 ? 1 : -1)), y, yV + (yV == 0 ? 0 : (yV > 0 ? 1 : -1)), mark);
        else return 0;
    }


    private boolean checkWin(int x, int y, Color mark, int winCountManual) {
        int markCount = 1;
//        int toLeft = -1;
//        int toRight = 1;
//        while (checkVector(x, toLeft--, y, 0, mark)) markCount++;
//        while (checkVector(x, toRight++, y, 0, mark)) markCount++;
//        if (checkWinner(markCount, mark)) return true;
//        if (mark == PLAYER_MARK) System.out.println(getCountOnLine(x, -1, y, 0, COUNT_TO_WIN - 1));
//        if (mark == PLAYER_MARK) System.out.println(getCountOnLine(x, 1, y, 0, COUNT_TO_WIN - 1));


        if (plusator(x, -1, y, 0, mark) + plusator(x, 1, y, 0, mark) + 1 >= winCountManual) return true;


        markCount = 1;
        int toUp = -1;
        int toDown = 1;
        while (checkVector(x, 0, y, toUp--, mark)) markCount++;
        while (checkVector(x, 0, y, toDown++, mark)) markCount++;
//        System.out.println(markCount);
        if (checkWinner(markCount, mark, winCountManual)) return true;

        markCount = 1;
        int toNorthOstX = 1;
        int toNorthOstY = -1;
        int toSouthWestX = -1;
        int toSouthWestY = 1;
        while (checkVector(x, toNorthOstX++, y, toNorthOstY--, mark)) markCount++;
        while (checkVector(x, toSouthWestX--, y, toSouthWestY++, mark)) markCount++;
        if (checkWinner(markCount, mark, winCountManual)) return true;


        markCount = 1;
        int toNorthWestX = -1;
        int toNorthWestY = -1;
        int toSouthOstX = 1;
        int toSouthOstY = 1;

        while (checkVector(x, toNorthWestX--, y, toNorthWestY--, mark)) markCount++;
        while (checkVector(x, toSouthOstX++, y, toSouthOstY++, mark)) markCount++;

        return checkWinner(markCount, mark, winCountManual);

    }

    private int getCountOnLine(int x, int xV, int y, int yV, int search_distance) {

        if (search_distance <= 0) return 0;
        if (x + xV >= 0 &&
                x + xV < SIZE &&
                y + yV >= 0 &&
                y + yV < SIZE) {
            if (getCellColor(x + xV, y + yV) == PLAYER_MARK)
                return 1 + getCountOnLine(
                        x, xV + (xV == 0 ? 0 : (xV > 0 ? 1 : -1)),
                        y, yV + (yV == 0 ? 0 : (yV > 0 ? 1 : -1)), search_distance - 1);
            else if (getCellColor(x + xV, y + yV) == EMPTY)
                return 0 + getCountOnLine(
                        x, xV + (xV == 0 ? 0 : (xV > 0 ? 1 : -1)),
                        y, yV + (yV == 0 ? 0 : (yV > 0 ? 1 : -1)), search_distance - 1);
            else return 0;
        } else return 0;

    }

    private boolean checkWinner(int count, Color mark, int winCountManual) {
        if (count >= winCountManual) {
            System.out.println(mark + " is a winner!");
            return true;
        }
        return false;
    }

    private boolean allowToPick(int x, int y) {
        return getCellColor(x, y) == EMPTY;
    }


    @Override
    public void initialize() {
        setScreenSize(SIZE, SIZE);
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                setCellColor(x, y, EMPTY);
            }
        }
        showMessageDialog(Color.GRAY, "Click to continue;\n Escape to exit, Space to restart", Color.BLACK, 20);
    }


}
