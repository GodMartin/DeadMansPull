package Logistic;

public class Inventory {
    private int cigarettes;
    private int magnifiers;

    public Inventory() {
        this.cigarettes = 0;
        this.magnifiers = 0;
    }

    public int getCigarettes() {
        return cigarettes;
    }

    public void addCigarette() {
        this.cigarettes++;
    }

    public void useCigarette() {
        if (this.cigarettes > 0) {
            this.cigarettes--;
        } else {
            throw new IllegalStateException("No cigarettes left to use.");
        }
    }

    public int getMagnifiers() {
        return magnifiers;
    }

    public void addMagnifier() {
        this.magnifiers++;
    }

    public void useMagnifier() {
        if (this.magnifiers > 0) {
            this.magnifiers--;
        } else {
            throw new IllegalStateException("No magnifiers left to use.");
        }
    }
}