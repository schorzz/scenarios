


import greenfoot.*;
import interfaces.GreenfootWorld;
import interfaces.LeutwesenInterface;
import util.*;

import javax.swing.*;

/**
 * Basisklasse der Welt.
 */
public class LeereWelt extends World implements GreenfootWorld {

    private static final String WORLD_SETUP_FILE = "WeltSetup.json";

    private static WeltSetup setup;
    private Spielfeld playground;
    private Hintergrund background;

    // Size of one cell
    public static final int CELL_SIZE = 60;

    static {
        setup = Factory.createWorldSetup(WORLD_SETUP_FILE);
    }

    /**
     * Eine neue Welt erstellen.
     */
    public LeereWelt() {
        // Create the new world
        super(setup != null ? setup.getOuterWidth(): 10, setup != null ? setup.getOuterHeight() : 10, CELL_SIZE);
        //  Warn that there was no WORLD_SETUP_FILE specified.
        //TODO  Namen der Welt und weitere details ändern

        if (setup == null) {
            DialogUtils.showMessageDialogEdt(null, DialogUtils.setupNullMessage, "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        playground = new Spielfeld(this, setup);
        background = new Hintergrund(setup, CELL_SIZE);

        //  TODO Paintorder reparieren
        //        setPaintOrder(PAINT_ORDER)

        //TODO entscheide welchen default speed gesetzt werden soll
        Greenfoot.setSpeed(setup.getSpeed());
        addObject(new SoundButton(setup.isMute()), getWidth(), getHeight());

        setBackground(background.getBackground());
        // Initialize actors
        Factory.initActorsFromWorldSetup( setup,  playground);
Geysir x = new Geysir();
        Geysir x1 = new Geysir();
        Geysir x2 = new Geysir();
        Geysir x3 = new Geysir();
        Geysir x4 = new Geysir();

        addObject(x, 4, 5);
        addObject(x1, 5, 5);
        addObject(x2, 4, 3);
        addObject(x3, 8, 4);
        addObject(x4, 6, 3);

        new Animator(x,x1,x2,x3,x4).start();
    }

    //New
    @Override
    public void addObject(Actor object, int x, int y) {
        super.addObject(object, x, y);
        if (setup.isDark() && object instanceof Lichtwesen) {
             background.updateBackground(getObjects(LeutwesenInterface.class));
            setBackground(background.getBackground());
        }
    }

    @Override
    public void removeObject(Actor object) {
        super.removeObject(object);
        if (setup.isDark() && object instanceof Lichtwesen) {
            background.updateBackground(getObjects(LeutwesenInterface.class));
            setBackground(background.getBackground());
        }
    }



    protected WeltSetup getSetup() {
        return setup;
    }

    public void muteSound() {
        setup.setMute(true);
        save();
    }

    public void unmuteSound() {
        setup.setMute(false);
        save();
    }

    public void save() {
        WeltSetup.saveWorldSetup(setup);
    }

    public Spielfeld getPlayground() {
        return playground;
    }

}
