package org.example;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class Main
{
    public static DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
    static Terminal terminal = null;

    public static void main(String[] args) throws IOException, InterruptedException {
        //region Terminalinitiering för föremålen
        terminal = defaultTerminalFactory.createTerminal();
        terminal.setCursorVisible(false);
        terminal.setCursorPosition(terminal.getTerminalSize().getColumns() / 2,
                terminal.getTerminalSize().getRows() / 2);
        terminal.flush();
        //endregion

        //region Instansering av allt som kommer att flytta på sig
        Movable player = new Movable(terminal.getTerminalSize().getColumns() / 2,
                                                terminal.getTerminalSize().getRows() / 2,
                                                    Symbols.FACE_WHITE);

        Movable treasure0 = new Movable(terminal.getTerminalSize().getColumns(), terminal.getTerminalSize().getRows());
        Movable treasure1 = new Movable(terminal.getTerminalSize().getColumns(), terminal.getTerminalSize().getRows());
        Movable treasure2 = new Movable(terminal.getTerminalSize().getColumns(), terminal.getTerminalSize().getRows());
        Movable treasure3 = new Movable(terminal.getTerminalSize().getColumns(), terminal.getTerminalSize().getRows());
        //endregion

        // Flytta alla spelarobjekten på skärmen
        do {
            moveMovable(player, treasure0, treasure1, treasure2);
        } while(true);
    }

    private static KeyType readUserInputType() throws IOException, InterruptedException {
        KeyStroke keyStroke = null;

        do {
            Thread.sleep(5);
            keyStroke = terminal.pollInput();
        } while(keyStroke == null);

        return keyStroke.getKeyType();
    }


    private static void moveMovable(Movable player, Movable... movables) throws IOException, InterruptedException
    {
        KeyType type = readUserInputType();
        String typeString = type.name();

        switch (typeString) {
            case "ArrowUp" -> player.setY(player.getY() - 1);
            case "ArrowDown" -> player.setY(player.getY() + 1);
            case "ArrowRight" -> player.setX(player.getX() + 1);
            case "ArrowLeft" -> player.setX(player.getX() - 1);
        }

        for (Movable movable : movables) {
            // Initsiera utseendet för föremålet som jagas
            terminal.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
            terminal.enableSGR(SGR.BOLD);

            terminal.setCursorPosition(movable.getOldX(), movable.getOldY());
            terminal.putCharacter(' ');

            //region Bestäm rörelsen för föremålen
            /*  slumpvariabeln används för att välja om det ska röra sig i x (1)
                eller y (0) riktning. */
            int xOffset = 0;
            int yOffset = 0;
            int rand = ThreadLocalRandom.current().nextInt(0, 2);

            if(rand == 1)
                xOffset = movable.getX() > player.getX() ? 1 : -1;
            else if (rand == 0)
                yOffset = movable.getY() > player.getY() ? 1 : -1;

            movable.setCurrentPos(xOffset, yOffset);
            //endregion

            terminal.setCursorPosition(movable.getX(), movable.getY());
            terminal.putCharacter(movable.symbol);

            movable.setOldPos();

            terminal.resetColorAndSGR();
            terminal.flush();
        }
        
        terminal.setCursorPosition(player.getOldX(), player.getOldY());
        terminal.putCharacter(' ');

        // Flytta spelaren
        terminal.setCursorPosition(player.getX(), player.getY());
        terminal.putCharacter(player.symbol);

        player.setOldPos();

        terminal.flush();
    }

    private static void moveMovable(Movable player) throws IOException, InterruptedException
    {
        KeyType type = readUserInputType();
        String typeString = type.name();

        switch (typeString) {
            case "ArrowUp" -> player.setY(player.getY() - 1);
            case "ArrowDown" -> player.setY(player.getY() + 1);
            case "ArrowRight" -> player.setX(player.getX() + 1);
            case "ArrowLeft" -> player.setX(player.getX() - 1);
            default -> {
            }
        }

        // Rensa upp karaktären bakom
        terminal.setCursorPosition(player.getOldX(), player.getOldY());
        terminal.putCharacter(' ');

        // Flytta spelaren
        terminal.setCursorPosition(player.getX(), player.getY());
        terminal.putCharacter(player.symbol);

        player.setOldPos();

        terminal.flush();
    }
}
