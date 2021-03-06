
import greenfoot.*;
import util.ActorPosition;
import util.WeltSetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Charakter extends Figur {

    private FigurTyp typ;

    public void laufen() {
        if (theWorldsEnd(getCurrentDirection(), 1, this)) {
            showWarning("", //Optional fuer den englischen Text.
                    "Der Charakter kann sich nicht bewegen, da die Welt zu Ende ist!");
            return;
        }

        Factory.laufen(this);
        Greenfoot.delay(1);
    }

    /**
     * Charakter dreht sich um 90 Grad nach links
     */

    public void nachLinksDrehen() {
        setCurrentDirection(getCurrentDirection().rotationLeft());
        resetImage();
        Greenfoot.delay(1);
    }

    /**
     * Charakter dreht sich um 90 Grad nach rechts
     */

    public void nachRechtsDrehen() {
        setCurrentDirection(getCurrentDirection().rotationRight());
        resetImage();
        Greenfoot.delay(1);
    }

    @Override
    public void act() {
        if (Greenfoot.isKeyDown("left") || Greenfoot.isKeyDown("a")) {
            nachLinksDrehen();
        } else if (Greenfoot.isKeyDown("right") || Greenfoot.isKeyDown("d")) {
            nachRechtsDrehen();
        } else if (Greenfoot.isKeyDown("up") || Greenfoot.isKeyDown("w")) {
            laufen();
        }
        Greenfoot.delay(1);

    }


    protected Charakter(FigurTyp typ, ActorPosition startPosition) {
        super(createImage(typ.path), 4, 4, startPosition);
        this.typ = typ;
    }


    public FigurTyp getTyp() {
        return typ;
    }

    interface Aktion {
        void apply(Aktioner arg1);
    }

    interface Aktioner {
        void aktion();
    }

    protected void moveActors(Direction direction, Figur... actors) {
        moveActors(direction, null, (z) -> {
        }, actors);
    }

    /**
     * Beweget den/die Actor(s) um einen Schritt in die ausgewaehlte Richtung, dabei wird die Bewegung animiert.
     *
     * @param actors    der/die Actor(s) der/die bewegt werden sollen
     * @param direction in welche Richtung  sie gehen sollen
     */
    protected void moveActors(Direction direction, Aktioner x, Aktion op, Figur... actors) {
        WeltSetup foo = Factory.getSetup();
        if (direction.equals(Direction.LEFT) || direction.equals(Direction.UP)) {
            if (direction.equals(Direction.UP))
                for (Figur figure : actors) {
                    figure.setLocationWithoutOffset(figure.getX(),
                            modulo((figure.getY() - 1), foo.getHeight()));
                }

            else if (direction.equals(Direction.LEFT))
                for (Figur figure : actors) {
                    figure.setLocationWithoutOffset(
                            modulo((figure.getX() - 1), foo.getWidth()),
                            figure.getY());
                }

            for (int j = 0, k = getWorld().getCellSize() * 2; j < 5; j++, k = k - getWorld().getCellSize() * 2 / 5) {
                changeAnimationImage(k, direction, actors);
                op.apply(x);
                Greenfoot.delay(2);
            }
            for (Figur figure : actors) {
                figure.resetImage();
            }
        } else {
            for (int j = 0, k = 0; j < 5; j++, k = k + getWorld().getCellSize() * 2 / 5) {
                changeAnimationImage(k, direction, actors);
                op.apply(x);
                Greenfoot.delay(2);
            }

            if (direction.equals(Direction.DOWN))
                for (Figur figure : actors) {
                    figure.resetImage();
                    figure.setLocationWithoutOffset(figure.getX(),
                            modulo((figure.getY() + 1), foo.getHeight()));
                }
            else if (direction.equals(Direction.RIGHT))
                for (Figur figure : actors) {
                    figure.resetImage();
                    figure.setLocationWithoutOffset(
                            modulo((figure.getX() + 1), foo.getWidth()), figure.getY());
                }
        }

    }

    /**
     * Ein spezielle Modulo-Operator der ausschliesslich positive Zahlen zurueck gibt.
     *
     * @param a der erste Wert
     * @param b der zweite Wert
     * @return Das Ergebnis der Modulo-Pperation
     */
    private int modulo(int a, int b) {
        return (a % b + b) % b;
    }


    protected Object getObjectInFront(Direction direction, int steps, Class<?> clazz) {
        int x = getX();
        int y = getY();
        WeltSetup foo = Factory.getSetup();

        switch (direction) {
            case RIGHT:
                x = modulo((x + steps), foo.getWidth());
                break;

            case DOWN:
                y = modulo((y + steps), foo.getHeight());
                break;

            case LEFT:
                x = modulo((x - steps), foo.getWidth());
                break;

            case UP:
                y = modulo((y - steps), foo.getHeight());
                break;

            default: // Not a valid direction
                return null;
        }


        List<?> objects = getWorld().erhalteSpielfeld().gibObjekteAuf(x, y, clazz);

        if (objects != null && objects.size() > 0) {
            return objects.get(0);
        } else {
            return null;
        }
    }

    public void verbrennen() {
        final String KOERPER_FILE = "geysir_death.png";

        GreenfootImage figurZaubern = new GreenfootImage(KOERPER_FILE);
        figurZaubern.scale(240, 240);

        HashMap<Direction, ArrayList<GreenfootImage>> bilderContainer = loadImage(figurZaubern, 4, 4);

        for (GreenfootImage img : bilderContainer.get(getCurrentDirection())) {
            setImage(img);
            Greenfoot.delay(2);
        }
        resetImage();
        Greenfoot.delay(2);
        reset();
    }

    /**
     * Ueberprueft, ob die Welt vor dem Character, zu Ende ist.
     *
     * @return boolean
     */

    public boolean istWeltzuEnde() {
        return theWorldsEnd(getCurrentDirection(), 1, this);
    }

    /**
     * Gibt die Blickrichtung des Charakters zurueck
     * Blickrichtung implementiert Praedikate mit denen auf die Himmelsrichtung
     * geprueft werden kann. Vereinfacht die Implementierung der letzten beiden
     * Aufgabenteile von Kap.3
     *
     * @return Blickrichtung
     */
    public Blickrichtung blickrichtung() {
        switch (getCurrentDirection()) {
            case UP:
                return Blickrichtung.NORDEN;
            case RIGHT:
                return Blickrichtung.OSTEN;
            case DOWN:
                return Blickrichtung.SUEDEN;
            case LEFT:
                return Blickrichtung.WESTEN;
            default:
                return null;
        }
    }
}
