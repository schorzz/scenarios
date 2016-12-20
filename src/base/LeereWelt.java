


import greenfoot.*;
import interfaces.Animation;
import interfaces.GreenfootWorld;
import interfaces.LichtwesenInterface;
import util.*;

import javax.swing.*;

/**
 * Basisklasse der Welt.
 */
public class LeereWelt extends World implements GreenfootWorld {


    private Spielfeld playground;
    private Hintergrund background;

    // Size of one cell
    public static final int CELL_SIZE = 60;

    /**
     * Eine neue Welt erstellen.
     */
    public LeereWelt() {
        // Create the new world
        super(Factory.getSetup() != null ? Factory.getSetup().getOuterWidth(): 10, Factory.getSetup() != null ? Factory.getSetup().getOuterHeight() : 10, CELL_SIZE);
        //  Warn that there was no WORLD_SETUP_FILE specified.

        if (Factory.getSetup() == null) {
            DialogUtils.showMessageDialogEdt(null, DialogUtils.setupNullMessage, "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        playground = new Spielfeld(this, Factory.getSetup());
        background = new Hintergrund(Factory.getSetup(), CELL_SIZE);

        addObject(new SoundButton(Factory.getSetup().isMute()), getWidth(), getHeight());

        setBackground(background.getBackground());

        Factory.initActorsFromWorldSetup( Factory.getSetup(),  playground);
    }

    //New
    @Override
    public void addObject(Actor object, int x, int y) {
        super.addObject(object, x, y);
        if (Factory.getSetup().isDark() && object instanceof Lichtwesen) {
             background.updateBackground(getObjects(LichtwesenInterface.class));
            setBackground(background.getBackground());
        }
        if(object instanceof Animation)
            Factory.addAnimationObject((Animation) object);
    }

    @Override
    public void removeObject(Actor object) {
        super.removeObject(object);
        if (Factory.getSetup().isDark() && object instanceof Lichtwesen) {
            background.updateBackground(getObjects(LichtwesenInterface.class));
            setBackground(background.getBackground());
        }

        if(object instanceof Animation)
            Factory.removeAnimationObject((Animation) object);
    }


    protected WeltSetup getSetup() {
        return Factory.getSetup();
    }

    public Spielfeld getPlayground() {
        return playground;
    }

}
