package machine;

import java.util.Scanner;

enum State {
    CHOOSING_ACTION, CHOOSING_COFFEE, FILLING_WATER, FILLING_MILK, FILLING_BEANS, FILLING_CUPS,  EXITING
}

public class CoffeeMachine {
    State state;
    int waterTank;
    int milkTank;
    int beansTank;
    int cupsTank;
    int dollarBalance;

    public CoffeeMachine() {
        this.waterTank = 400;
        this.milkTank = 540;
        this.beansTank = 120;
        this.cupsTank = 9;
        this.dollarBalance = 550;
        this.state = State.CHOOSING_ACTION;
    }

    public void updateUserPrompt() {
        switch (state) {
            case CHOOSING_ACTION:
                System.out.println("Write action (buy, fill, take, remaining, exit):");
                break;
            case CHOOSING_COFFEE:
                System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
                break;
            case FILLING_WATER:
                System.out.println("Write how many ml of water you want to add:");
                break;
            case FILLING_MILK:
                System.out.println("Write how many ml of milk you want to add:");
                break;
            case FILLING_BEANS:
                System.out.println("Write how many grams of coffee beans you want to add:");
                break;
            case FILLING_CUPS:
                System.out.println("Write how many disposable cups of coffee you want to add:");
                break;
        }
    }

    public void processInput(String input) {
        switch (state) {

            case CHOOSING_ACTION:
                executeAction(input);
                break;
            case CHOOSING_COFFEE:
                brewCoffee(input);
                break;
            case FILLING_WATER:
                this.waterTank += Integer.parseInt(input);
                this.state = State.FILLING_MILK;
                break;
            case FILLING_MILK:
                this.milkTank += Integer.parseInt(input);
                this.state = State.FILLING_BEANS;
                break;
            case FILLING_BEANS:
                this.beansTank += Integer.parseInt(input);
                this.state = State.FILLING_CUPS;
                break;
            case FILLING_CUPS:
                this.cupsTank += Integer.parseInt(input);
                this.state = State.CHOOSING_ACTION;
                break;
            case EXITING:
                break;
        }
    }

    private void executeAction(String action) {
        switch (action) {
            case "buy":
                this.state = State.CHOOSING_COFFEE;
                break;
            case "fill":
                this.state = State.FILLING_WATER;
                break;
            case "take":
                this.withdraw();
                break;
            case "remaining":
                this.displaySupply();
                break;
            case "exit":
                this.state = State.EXITING;
                break;
        }
    }

    private void brewCoffee(String coffee) {
        int neededWater = 0;
        int neededMilk = 0;
        int neededBeans = 0;
        int price = 0;

        switch (coffee) {
            case "1":
                neededWater = 250;
                neededBeans = 16;
                price = 4;
                break;
            case "2":
                neededWater = 350;
                neededMilk = 75;
                neededBeans = 20;
                price = 7;
                break;
            case "3":
                neededWater = 200;
                neededMilk = 100;
                neededBeans = 12;
                price = 6;
                break;
            case "back":
                this.state = State.CHOOSING_ACTION;
                return;
        }

        boolean hasEnoughWater = this.waterTank >= neededWater;
        boolean hasEnoughMilk = this.milkTank >= neededMilk;
        boolean hasEnoughBeans = this.beansTank >= neededBeans;
        boolean hasEnoughCups = this.cupsTank > 0;
        boolean hasError = true;
        String message = "";

        if (!hasEnoughWater) {
            message = "Sorry, not enough water!";
        } else if (!hasEnoughMilk) {
            message = "Sorry, not enough milk!";
        } else if (!hasEnoughBeans) {
            message = "Sorry, not enough beans!";
        } else if (!hasEnoughCups) {
            message="Sorry, not enough cups!";
        } else {
            hasError = false;
            message = "I have enough resources, making you a coffee!";
        }

        if (!hasError) {
            this.waterTank -= neededWater;
            this.milkTank -= neededMilk;
            this.beansTank-= neededBeans;
            this.cupsTank--;
            this.dollarBalance += price;
        }

        System.out.println(message);

        this.state = State.CHOOSING_ACTION;
    }

    private void withdraw() {
        System.out.println("I gave you $" + this.dollarBalance);
        this.dollarBalance = 0;
    }

    private void displaySupply() {
        System.out.println("The coffee machine has:");
        System.out.println(this.waterTank + " ml of water");
        System.out.println(this.milkTank + " ml of milk");
        System.out.println(this.beansTank + " g of coffee beans");
        System.out.println(this.cupsTank + " disposable cups");
        System.out.println("$" + this.dollarBalance + " of money");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        CoffeeMachine coffeeMachine = new CoffeeMachine();
        while (coffeeMachine.state != State.EXITING) {
            coffeeMachine.updateUserPrompt();
            String action = scanner.next();
            coffeeMachine.processInput(action);
        }
    }
}
