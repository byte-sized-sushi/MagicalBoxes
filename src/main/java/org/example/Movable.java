package org.example;

import com.googlecode.lanterna.Symbols;

import java.util.concurrent.ThreadLocalRandom;

public class Movable
{
    protected int x;
    protected int y;
    protected int oldX;
    protected int oldY;
    protected char symbol;


    public Movable(int x, int y, char symbol)
    {
        this.x = x;
        this.y = y;
        this.symbol = symbol;

        oldX = x;
        oldY = y;
    }

    /***
     * Den här konstruktorn är mest för att initiera föremål som skall
     * placeras slumpmässigt inom ett område.
     * @param width     Totala bredden på området
     * @param height    Totala höjden på området
     */
    public Movable(int width, int height)
    {
        this.x = ThreadLocalRandom.current().nextInt(2, width - 2);
        this.y = ThreadLocalRandom.current().nextInt(2, height - 2);

        oldX = x;
        oldY = y;

        this.symbol = Symbols.HEART;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getOldX() { return oldX; }
    public int getOldY() { return oldY; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }


    public void setCurrentPos(int xOffset, int yOffset)
    {
        x += xOffset;
        y += yOffset;
    }

    public void setOldPos()
    {
        oldX = x;
        oldY = y;
    }
}
