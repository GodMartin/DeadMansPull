package Logistic;

public class Player {

    public int numberOfLifes;

    public String name;

    public Player(String name) {
        this.name = name;
    }
    public int getNumberOfLifes() {
        return numberOfLifes;
    }

    public void setNumberOfLifes(int numberOfLifes) {
        this.numberOfLifes = numberOfLifes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void takeDamage() {
        numberOfLifes--;
    }

    public boolean isAlive() {
        return numberOfLifes > 0;
    }


}
