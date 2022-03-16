import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.*;

public class Wordle {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String BLACK = "\033[0;30m";

    /*
    color codes
    0 - black
    1 - none
    2 - yellow
    3 - green
     */


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

    /**
     * This function runs the game in a loop until q is entered
     * @param word answer of the game
     */
    public void runGame(String word){
        //initialize variables and objects
        int guesses = 0;    //keeps track of guesses used
        String prevGuesses = "";    //keeps track of previous guesses
        boolean isGameOver = false; //keeps track of if the game is over

        //parallel array of type string containing the letters of the alphabet
        ArrayList<String> alphabet = new ArrayList<>();
        initializeAlpha(alphabet);
        //parallel array of type int containing the corresponding color codes
        ArrayList<Integer> color = new ArrayList<>(26);
        for(int i = 0; i < 26; i++){
            color.add(1);   //initialize all colors to none
        }

        printRules();

        //runs until won or guesses are used
        while (guesses < 6 && !isGameOver){
            String guess;

            //print display
            printDisplay(guesses, prevGuesses);
            printAlphabet(alphabet);

            //user input
            guess = input();
            System.out.println();

            //check and edit the word
            String result = checkWord(guess, word, alphabet, color);
            if(guess.equals(word)){    //check if the answer is correct
                isGameOver = true;
            }

            //updateds previous guesses
            prevGuesses += result + "\n";
            System.out.println("==========================");

            guesses++;  //updated num of guesses used
        }

        //prints statement depending of results
        if(isGameOver){
            System.out.println("Winner winner chicken dinner!");
        }
        else {
            System.out.println("The correct answer was: " + word);
            System.out.println("Next time!");
        }
    }

    /**
     * This function prints the game display
     * @param guesses amount of guesses used
     * @param word the previous guesses
     */
    public void printDisplay(int guesses, String word){
        System.out.println(word);   //prints previous guesses
        for(int i = 1; i <= 6 - guesses; i++){  //prints guess boxes based on guesses left
            System.out.println("_ _ _ _ _");
        }
    }
    /**
    This function returns a random 5 letter word from a txt file
     */
    public String getWord(){
        ArrayList<String> words = new ArrayList<>();    //list of words
        try{
            File file = new File(Objects.requireNonNull(getClass().getResource("words")).getPath());
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()){  //runs until end of file
                words.add(scan.nextLine()); //adds words to the list of words
            }
        }
        catch(FileNotFoundException e){ //catches errors
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //random index
        int random = (int)(Math.random() * (words.size()));
        return words.get(random).toUpperCase(); //returns word from random index
    }

    /**
     * This function checks a word and updates the color of letters based on if they match
     * @param guess the word being checked
     * @param answer the correct word
     * @param alpha parallel list of color coded letters from the alphabet
     * @param color parallel list that indicated the color of each letter
     * @return returns the updated string with color coding
     */
    public String checkWord(String guess, String answer, ArrayList<String> alpha,
                            ArrayList<Integer> color){
        StringBuilder updatedGuess = new StringBuilder();   //keeps updated guess
        for(int i = 0; i < 5; i++){
            //checks if char is in the right place
            if(guess.charAt(i) == answer.charAt(i)){
                //green char is added to updated guess
                updatedGuess.append(ANSI_GREEN).append(guess.charAt(i)).append(ANSI_RESET);
                //alphabet is updated
                updateAlphabet(alpha, color, Character.toLowerCase(guess.charAt(i)), 3);
            }
            //checks if the answer contains the char
            else if(answer.contains(String.valueOf(guess.charAt(i)))){
                //yellow char is added to updated guess
                updatedGuess.append(ANSI_YELLOW).append(guess.charAt(i)).append(ANSI_RESET);
                //alphabet is updated
                updateAlphabet(alpha, color, Character.toLowerCase(guess.charAt(i)), 2);
            }
            else{
                //normal char is added to updated guess
                updatedGuess.append(guess.charAt(i));
                //alphabet is updated
                updateAlphabet(alpha, color, Character.toLowerCase(guess.charAt(i)), 0);
            }
            //adds spaces between chars
            updatedGuess.append(" ");
        }
        return updatedGuess.toString();
    }

    /**
     * Prints the rules of the game
     */
    public void printRules(){
        System.out.println("\nHOW TO PLAY");
        System.out.println("""
                Guess the WORDLE in six tries.

                Each guess must be a valid five-letter word. Hit the enter button to submit.

                After each guess, the color of the tiles will change to show how close your guess was to the word.
                """);

        System.out.println("Examples");
        System.out.println(ANSI_GREEN + "W" + ANSI_RESET + " E A R Y");

        System.out.println("The letter W is in the word and in the correct spot.");
        System.out.println("P " + ANSI_YELLOW + "I" + ANSI_RESET + " L L S");
        System.out.println("The letter I is in the word but in the wrong spot.");

        System.out.println("V A G U E");
        System.out.println("The letter U is not in the word in any spot.");

    }

    /**
     * This function takes input until valid input is acquired
     * @return returns valid input
     */
    public String input(){
        Scanner scan = new Scanner(System.in);
        System.out.print(">");
        String guess = scan.next(); //input
        if(guess.length() != 5){    //checks if valid length
            System.out.print("Invalid word length, try again\n>");
            guess = scan.next();    //takes new input
        }
        guess = guess.toUpperCase();    //changed guess to uppercase
        return guess;
    }

    /**
     * This function updates the parallel list of the alphabet with corrisponding color coding
     * @param alpha parallel list of the alphabet
     * @param color parallel list of the color of the letters in the alphabet
     * @param c char that is being changed
     * @param change an integer that represents which color to be changed to
     */
    public void updateAlphabet(ArrayList<String> alpha, ArrayList<Integer> color, char c, int change){
        String alphabet = "abcdefghijklmnopqrstuvwxyz"; //alphabet
        int index = alphabet.indexOf(Character.toString(c));    //index of char
        if(color.get(index) != 3 && change == 3){   //checks if needed to be changed to green
            color.set(index, 3);    //changes color code
            alpha.set(index, (ANSI_GREEN + c + ANSI_RESET));    //updates color
        }
        else if(color.get(index) == 1 && change ==  2){ //checks if need to be changed to yellow
            color.set(index, 2);    //changes color code
            alpha.set(index, (ANSI_YELLOW + c + ANSI_RESET));   //updates color
        }
        else if(color.get(index) == 1 && change == 0){  //checks if needed to be changed to black
            color.set(index, 0);    //changes color code
            alpha.set((index), (BLACK + c + ANSI_RESET));   //updates color
        }
    }

    /**
     * This function initializes an arraylist of type string to the letters of the alphabet
     * @param alpha the arraylist to be initialized
     */
    public void initializeAlpha(ArrayList<String> alpha){
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for(int i = 0; i < 26; i++){
            alpha.add(Character.toString(alphabet.charAt(i)));  //adds letters of the alphabet
        }
    }

    /**
     * This function prints an arraylist of type string
     * @param alpha the arraylist to be printed
     */
    public void printAlphabet(ArrayList<String> alpha){
        for(int i = 0; i < 10; i++){
            System.out.print(alpha.get(i) + " ");
        }
        System.out.println();
        for(int i = 10; i < 20; i++){
            System.out.print(alpha.get(i) + " ");
        }
        System.out.println();
        System.out.print("    ");
        for(int i = 20; i < 26; i++){
            System.out.print(alpha.get(i) + " ");
        }
        System.out.println();
    }
}



