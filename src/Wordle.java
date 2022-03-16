import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Wordle {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";



    public static void main(String[] args) {
        Wordle game = new Wordle();
        String word;

        Scanner scan = new Scanner(System.in);
        String input = "";
        while(!input.equals("q") && !input.equals("Q")) {
            word = game.getWord();
            game.runGame(word);
            System.out.print("Press any button to play again or 'q' to quit: ");
            input = scan.nextLine();
        }
    }

    public void runGame(String word){
        int guesses = 0;
        String prevGuesses = "";
        boolean isGameOver = false;
        Scanner scan = new Scanner(System.in);

        printRules();

        while (guesses < 6 && !isGameOver){
            String guess = "";

            //print display
            printDisplay(guesses, prevGuesses);

            //user input
            guess = input(guess);
            System.out.println();

            String result = checkWord(guess, word);
            if(result.equals(word)){
                isGameOver = true;
            }

            prevGuesses += result + "\n";
            System.out.println("==========================");

            guesses++;
        }

        if(isGameOver){
            System.out.println("Winner winner chicken dinner!");
        }
        else {
            System.out.println("The correct answer was: " + word);
            System.out.println("Next time!");
        }
    }

    public void printDisplay(int guesses, String word){
        System.out.println(word);
        for(int i = 1; i <= 6 - guesses; i++){
            System.out.println("_ _ _ _ _");
        }
    }
/**
This function returns a random 5 letter word from a txt file
 */
    public String getWord(){
        ArrayList<String> words = new ArrayList<>();
        try{
            File file = new File(Objects.requireNonNull(getClass().getResource("words")).getPath());
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()){
                words.add(scan.nextLine());
            }
        }
        catch(FileNotFoundException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        int random = (int)(Math.random() * (words.size()));
        return words.get(random).toUpperCase();
    }

    public String checkWord(String guess, String answer){
        StringBuilder updatedGuess = new StringBuilder();
        for(int i = 0; i < 5; i++){
            if(guess.charAt(i) == answer.charAt(i)){
                updatedGuess.append(ANSI_GREEN).append(guess.charAt(i)).append(ANSI_RESET);
            }
            else if(answer.contains(String.valueOf(guess.charAt(i)))){
                updatedGuess.append(ANSI_YELLOW).append(guess.charAt(i)).append(ANSI_RESET);
            }
            else{
                updatedGuess.append(guess.charAt(i));
            }
            if(i != 4){
                updatedGuess.append(" ");
            }
        }
        return updatedGuess.toString();
    }

    public void printRules(){
        System.out.println("\nHOW TO PLAY");
        System.out.println("Guess the WORDLE in six tries.\n" +
                "\n" +
                "Each guess must be a valid five-letter word. Hit the enter button to submit.\n" +
                "\n" +
                "After each guess, the color of the tiles will change to show how close your guess was to the word.\n");

        System.out.println("Examples");
        System.out.println(ANSI_GREEN + "W" + ANSI_RESET + " E A R Y");

        System.out.println("The letter W is in the word and in the correct spot.");
        System.out.println("P " + ANSI_YELLOW + "I" + ANSI_RESET + " L L S");
        System.out.println("The letter I is in the word but in the wrong spot.");

        System.out.println("V A G U E");
        System.out.println("The letter U is not in the word in any spot.");

    }

    public String input(String guess){
        Scanner scan = new Scanner(System.in);
        System.out.print(">");
        guess = scan.next();
        if(guess.length() != 5){
            System.out.print("Invalid word length, try again\n>");
            guess = scan.next();
        }
        guess = guess.toUpperCase();
        return guess;
    }
}



