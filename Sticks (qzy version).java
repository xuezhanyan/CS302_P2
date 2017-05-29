// Title:            Sticks
// Files:            Config.java, Sticks.java, TestSticks.java
// Semester:         (302) Summer 2016
//
// Author:           Zhaoyin Qin
// Email:            zqin23@wisc.edu
// CS Login:         zhaoyin
// Lecturer's Name:  Jim Williams
// Lab Section:      322
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name:     Yu Huang
//Partner Email:     yhuang299@wisc.edu
// Partner CS Login: yhuang
// Lecturer's Name:  Gary Dahl
// Lab Section:      345
// 
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
//    _X_ Write-up states that Pair Programming is allowed for this assignment.
//    _X_ We have both read the CS302 Pair Programming policy.
//    _X_ We have registered our team prior to the team registration deadline.
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

import java.util.Arrays;
import java.util.Scanner;

/**
 * In the game of Sticks there is a pile of sticks on the table. Each player
 * takes a turn in picking up 1 to 3 sticks. Whoever picks up the last stick,
 * loses the game.
 * 
 * @author Zhaoyin Qin, Yu Huang
 */

public class Sticks {

	/**
	 * This is the main method for the game of Sticks. In milestone 1 this
	 * contains the whole program for playing against a friend. In milestone 2
	 * this contains the welcome, name prompt, how many sticks question, menu,
	 * calls appropriate methods and the thank you message at the end. One
	 * method called in multiple places is promptUserForNumber. When the menu
	 * choice to play against a friend is chosen, then playAgainstFriend method
	 * is called. When the menu choice to play against a computer is chosen,
	 * then playAgainstComputer method is called. If the computer with AI option
	 * is chosen then trainAI is called before calling playAgainstComputer.
	 * Finally, call strategyTableToString to prepare a strategy table for
	 * printing.
	 * 
	 * @param args
	 *            (unused)
	 */

	// prompt message for initial sticks
	public static final String INITIAL_STICKS = "How many sticks are "
			+ "there on the table " + "initially (" + Config.MIN_STICKS + "-"
			+ Config.MAX_STICKS + ")? ";
	// prompt message for number of training
	public static final String INITIAL_GAMES = "How many games should "
			+ "the AI learn from " + "(" + Config.MIN_GAMES + " to "
			+ Config.MAX_GAMES + ")? ";
	// prompt message for choosing menu
	public static final String MENU_CHOICE = "Which do you choose (1,2,3)? ";

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		System.out.println("Welcome to the Game of Sticks!");
		System.out.println("==============================" + "\n");

		// prompt user to enter name
		System.out.print("What is your name? ");
		// eliminates leading and trailing spaces
		String player1 = input.nextLine().trim();
		System.out.println("Hello " + player1 + ".");
		// prompt user to enter initial sticks
		int numberSticks = promptUserForNumber(input, INITIAL_STICKS,
				Config.MIN_STICKS, Config.MAX_STICKS);
		System.out.println("");

		System.out.println("Would you like to:");
		System.out.println(" 1) Play against a friend");
		System.out.println(" 2) Play against computer (basic)");
		System.out.println(" 3) Play against computer with AI");
		// prompt user to enter choice
		int sectionNumber = promptUserForNumber(input, MENU_CHOICE, 1, 3);
		System.out.println("");

		switch (sectionNumber) {
		case 1:
			// prompt user to enter name
			System.out.print("What is your friend's name? ");
			// take name from user and eliminates leading and trailing spaces
			String player2 = input.nextLine().trim();
			System.out.println("Hello " + player2 + ".");
			System.out.println("");

			playAgainstFriend(input, numberSticks, player1, player2);
			break;

		case 2:
			playAgainstComputer(input, numberSticks, player1, null);
			break;

		case 3:
			// prompt user to enter number of training
			int numberOfGames = promptUserForNumber(input, INITIAL_GAMES,
					Config.MIN_GAMES, Config.MAX_GAMES);
			int[][] strategy = trainAi(numberSticks, numberOfGames);
			playAgainstComputer(input, numberSticks, player1, strategy);
			// prompt user to check strategy table
			System.out.print(
					"Would you like to see the " + "strategy table (Y/N)? ");
			if (input.nextLine().toLowerCase().equals("y"))
				// print strategy table
				System.out.print(strategyTableToString(strategy));
			break;
		default:
			break;
		}
		System.out.println("");
		System.out.println("=========================================");
		System.out.println("Thank you for playing the Game of Sticks!");

		input.close();
	}

	/**
	 * This method encapsulates the code for prompting the user for a number and
	 * verifying the number is within the expected bounds.
	 * 
	 * @param input
	 *            The instance of the Scanner reading System.in.
	 * @param prompt
	 *            The prompt to the user requesting a number within a specific
	 *            range.
	 * @param min
	 *            The minimum acceptable number.
	 * @param max
	 *            The maximum acceptable number.
	 * @return The number entered by the user between and including min and max.
	 */
	static int promptUserForNumber(Scanner input, String prompt, int min,
			int max) {
		boolean flag = true;
		int number = 0;
		while (flag) {
			System.out.print(prompt);
			if (!input.hasNextInt()) {
				// user does not enter a number
				String garbage = input.nextLine();
				System.out.println("Error: expected a number between " + min
						+ " and " + max + " but found: " + garbage);
			} else {
				number = input.nextInt();
				input.nextLine();
				// check if the number is in range
				if (number < min || number > max)
					System.out.println("Please enter a number between " + min
							+ " and " + max + ".");
				else
					flag = false;
			}
		}
		return number;
	}

	/**
	 * This method has one person play the Game of Sticks against another
	 * person.
	 * 
	 * @param input
	 *            An instance of Scanner to read user answers.
	 * @param startSticks
	 *            The number of sticks to start the game with.
	 * @param player1Name
	 *            The name of one player.
	 * @param player2Name
	 *            The name of the other player.
	 * 
	 *            As a courtesy, player2 is considered the friend and gets to
	 *            pick up sticks first.
	 * 
	 */
	static void playAgainstFriend(Scanner input, int startSticks,
			String player1Name, String player2Name) {
		int number = 0;
		while (startSticks > 0) {
			if (startSticks == 1)
				System.out.println("There is 1 stick on the board.");
			else
				System.out.println(
						"There are " + startSticks + " sticks on the board.");
			// prompt user to enter number of sticks to pick up
			number = promptUserForNumber(input,
					player2Name + ": How many sticks do you take ("
							+ Config.MIN_ACTION + "-"
							+ Math.min(Config.MAX_ACTION, startSticks) + ")? ",
					Config.MIN_ACTION,
					Math.min(Config.MAX_ACTION, startSticks));
			// decrement startSticks
			startSticks -= number;
			// check remaining number of sticks
			if (startSticks == 0) {
				System.out.println(
						player1Name + " wins. " + player2Name + " loses.");
				return;
			}
			if (startSticks == 1)
				System.out.println("There is 1 stick on the board.");
			else
				System.out.println(
						"There are " + startSticks + " sticks on the board.");
			// prompt user to enter number of sticks to pick up
			number = promptUserForNumber(input,
					player1Name + ": How many sticks do you take ("
							+ Config.MIN_ACTION + "-"
							+ Math.min(Config.MAX_ACTION, startSticks) + ")? ",
					Config.MIN_ACTION,
					Math.min(Config.MAX_ACTION, startSticks));
			// decrement startSticks
			startSticks -= number;
			// check remaining number of sticks
			if (startSticks == 0) {
				System.out.println(
						player2Name + " wins. " + player1Name + " loses.");
				return;
			}
		}
	}

	/**
	 * Make a choice about the number of sticks to pick up when given the number
	 * of sticks remaining.
	 * 
	 * Algorithm: If there are less than Config.MAX_ACTION sticks remaining,
	 * then pick up the minimum number of sticks (Config.MIN_ACTION). If
	 * Config.MAX_ACTION sticks remain, randomly choose a number between
	 * Config.MIN_ACTION and Config.MAX_ACTION. Use Config.RNG.nextInt(?) method
	 * to generate an appropriate random number.
	 * 
	 * @param sticksRemaining
	 *            The number of sticks remaining in the game.
	 * @return The number of sticks to pick up, or 0 if sticksRemaining is <= 0.
	 */
	static int basicChooseAction(int sticksRemaining) {
		if (sticksRemaining <= 0)
			return 0;
		else if (sticksRemaining < Config.MAX_ACTION)
			return Config.MIN_ACTION;
		else
			return Config.RNG.nextInt(Config.MAX_ACTION - Config.MIN_ACTION) 
					+ Config.MIN_ACTION;
	}

	/**
	 * This method has a person play against a computer. Call the
	 * promptUserForNumber method to obtain user input. Call the aiChooseAction
	 * method with the actionRanking row for the number of sticks remaining.
	 * 
	 * If the strategyTable is null, then this method calls the
	 * basicChooseAction method to make the decision about how many sticks to
	 * pick up. If the strategyTable parameter is not null, this method makes
	 * the decision about how many sticks to pick up by calling the
	 * aiChooseAction method.
	 * 
	 * @param input
	 *            An instance of Scanner to read user answers.
	 * @param startSticks
	 *            The number of sticks to start the game with.
	 * @param playerName
	 *            The name of one player.
	 * @param strategyTable
	 *            An array of action rankings. One action ranking for each stick
	 *            that the game begins with.
	 * 
	 */
	static void playAgainstComputer(Scanner input, int startSticks,
			String playerName, int[][] strategyTable) {
		int number = 0;
		while (startSticks > 0) {
			if (startSticks == 1)
				System.out.println("There is 1 stick on the board.");
			else
				System.out.println(
						"There are " + startSticks + " sticks on the board.");
			// prompt user to enter number of sticks to pick up
			number = promptUserForNumber(input,
					playerName + ": How many sticks do you take ("
							+ Config.MIN_ACTION + "-"
							+ Math.min(Config.MAX_ACTION, startSticks) + ")? ",
					Config.MIN_ACTION,
					Math.min(Config.MAX_ACTION, startSticks));
			// decrement startSticks
			startSticks -= number;
			// check remaining number of sticks
			if (startSticks == 0) {
				System.out.println("Computer wins. " + playerName + " loses.");
				return;
			}
			if (startSticks == 1)
				System.out.println("There is 1 stick on the board.");
			else
				System.out.println(
						"There are " + startSticks + " sticks on the board.");
			if (strategyTable == null)
				number = basicChooseAction(startSticks);
			else
				number = aiChooseAction(startSticks,
						strategyTable[startSticks - 1]);
			// check the number of sticks computer picks
			if (number == 1)
				System.out.println("Computer selects 1 stick.");
			else
				System.out.println("Computer selects " + number + " sticks.");
			startSticks -= number;
			// check remaining number of sticks
			if (startSticks == 0) {
				System.out.println(playerName + " wins. Computer loses.");
				return;
			}
		}

	}

	/**
	 * This method chooses the number of sticks to pick up based on the
	 * sticksRemaining and actionRanking parameters.
	 * 
	 * Algorithm: If there are less than Config.MAX_ACTION sticks remaining then
	 * the chooser must pick the minimum number of sticks (Config.MIN_ACTION).
	 * For Config.MAX_ACTION or more sticks remaining then pick based on the
	 * actionRanking parameter.
	 * 
	 * The actionRanking array has one element for each possible action. The 0
	 * index corresponds to Config.MIN_ACTION and the highest index corresponds
	 * to Config.MAX_ACTION. For example, if Config.MIN_ACTION is 1 and
	 * Config.MAX_ACTION is 3, an action can be to pick up 1, 2 or 3 sticks.
	 * actionRanking[0] corresponds to 1, actionRanking[1] corresponds to 2,
	 * etc. The higher the element for an action in comparison to other
	 * elements, the more likely the action should be chosen.
	 * 
	 * First calculate the total number of possibilities by summing all the
	 * element values. Then choose a particular action based on the relative
	 * frequency of the various rankings. For example, if Config.MIN_ACTION is 1
	 * and Config.MAX_ACTION is 3: If the action rankings are {9,90,1}, the
	 * total is 100. Since actionRanking[0] is 9, then an action of picking up 1
	 * should be chosen about 9/100 times. 2 should be chosen about 90/100 times
	 * and 1 should be chosen about 1/100 times. Use Config.RNG.nextInt(?)
	 * method to generate appropriate random numbers.
	 * 
	 * @param sticksRemaining
	 *            The number of sticks remaining to be picked up.
	 * @param actionRanking
	 *            The counts of each action to take. The 0 index corresponds to
	 *            Config.MIN_ACTION and the highest index corresponds to
	 *            Config.MAX_ACTION.
	 * @return The number of sticks to pick up. 0 is returned for the following
	 *         conditions: actionRanking is null, actionRanking has a length of
	 *         0, or sticksRemaining is <= 0.
	 * 
	 */
	static int aiChooseAction(int sticksRemaining, int[] actionRanking) {
		if (actionRanking == null || actionRanking.length == 0
				|| sticksRemaining <= 0)
			return 0;
		else if (sticksRemaining < Config.MAX_ACTION)
			return Config.MIN_ACTION;
		else {
			// total number of possibilities
			int sum = 0;
			// calculate total possibility
			for (int i = 0; i < actionRanking.length; i++)
				sum += actionRanking[i];
			// generate a random number from 1 ~ total probability
			int probability = Config.RNG.nextInt(sum) + 1;
			// current range of probability
			int current = actionRanking[0];
			// number of sticks to pick
			int action = 0;
			// check if the probability is in current range
			for (int i = 0; i < actionRanking.length; i++) {
				// pick corresponding sticks if probability is 
				// in current range
				if (probability <= current) {
					action = Config.MIN_ACTION + i;
					break;
				} else
					// increase the range 
					current += actionRanking[i + 1];
			}
			return action;
		}
	}

	/**
	 * This method initializes each element of the array to 1. If actionRanking
	 * is null then method simply returns.
	 * 
	 * @param actionRanking
	 *            The counts of each action to take. Use the length of the
	 *            actionRanking array rather than rely on constants for the
	 *            function of this method.
	 */
	static void initializeActionRanking(int[] actionRanking) {
		if (actionRanking != null)
			// initializes each element of the array to 1
			for (int i = 0; i < actionRanking.length; i++)
				actionRanking[i] = 1;
	}

	/**
	 * This method returns a string with the number of sticks left and the
	 * ranking for each action as follows.
	 * 
	 * An example: 10 3,4,11
	 * 
	 * The string begins with a number (number of sticks left), then is followed
	 * by 1 tab character, then a comma separated list of rankings, one for each
	 * action choice in the array. The string is terminated with a newline (\n)
	 * character.
	 * 
	 * @param sticksLeft
	 *            The number of sticks left.
	 * @param actionRanking
	 *            The counts of each action to take. Use the length of the
	 *            actionRanking array rather than rely on constants for the
	 *            function of this method.
	 * @return A string formatted as described.
	 */
	static String actionRankingToString(int sticksLeft, int[] actionRanking) {
		String output = sticksLeft + "\t";
		for (int i = 0; i < actionRanking.length; i++) {
			if (i == actionRanking.length - 1)
				// last element
				output += actionRanking[i];
			else
				// other elements with commas
				output += actionRanking[i] + ",";
		}
		// end with line separator
		output += "\n";
		return output;
	}

	/**
	 * This method updates the actionRanking based on the action. Since the game
	 * was lost, the actionRanking for the action is decremented by 1, but not
	 * allowing the value to go below 1.
	 * 
	 * @param actionRanking
	 *            The counts of each action to take. The 0 index corresponds to
	 *            Config.MIN_ACTION and the highest index corresponds to
	 *            Config.MAX_ACTION.
	 * @param action
	 *            A specific action between and including Config.MIN_ACTION and
	 *            Config.MAX_ACTION.
	 */
	static void updateActionRankingOnLoss(int[] actionRanking, int action) {
		// check if the action is in range
		if (action > Config.MAX_ACTION || action < Config.MIN_ACTION)
			return;
		else {
			if (actionRanking[action - Config.MIN_ACTION] == 1)
				return;
			else
				actionRanking[action - Config.MIN_ACTION]--;
		}
	}

	/**
	 * This method updates the actionRanking based on the action. Since the game
	 * was won, the actionRanking for the action is incremented by 1.
	 * 
	 * @param actionRanking
	 *            The counts of each action to take. The 0 index corresponds to
	 *            Config.MIN_ACTION and the highest index corresponds to
	 *            Config.MAX_ACTION.
	 * @param action
	 *            A specific action between and including Config.MIN_ACTION and
	 *            Config.MAX_ACTION.
	 */
	static void updateActionRankingOnWin(int[] actionRanking, int action) {
		// check if the action is in range
		if (action > Config.MAX_ACTION || action < Config.MIN_ACTION)
			return;
		else
			actionRanking[action - Config.MIN_ACTION]++;
	}

	/**
	 * Allocates and initializes a 2 dimensional array. The number of rows
	 * corresponds to the number of startSticks. Each row is an actionRanking
	 * with an element for each possible action. The possible actions range from
	 * Config.MIN_ACTION to Config.MAX_ACTION. Each actionRanking is initialized
	 * with the initializeActionRanking method.
	 * 
	 * @param startSticks
	 *            The number of sticks the game is starting with.
	 * @return The two dimensional strategyTable, properly initialized.
	 */
	static int[][] createAndInitializeStrategyTable(int startSticks) {
		int[][] strategyTable = new int[startSticks][Config.MAX_ACTION];
		for (int i = 0; i < strategyTable.length; i++)
			initializeActionRanking(strategyTable[i]);
		return strategyTable;
	}

	/**
	 * This formats the whole strategyTable as a string utilizing the
	 * actionRankingToString method. For example:
	 * 
	 * Strategy Table Sticks Rankings 10 3,4,11 9 6,2,5 8 7,3,1 etc.
	 * 
	 * The title "Strategy Table" should be proceeded by a \n.
	 * 
	 * @param strategyTable
	 *            An array of actionRankings.
	 * @return A string containing the properly formatted strategy table.
	 */
	static String strategyTableToString(int[][] strategyTable) {
		String output = "\nStrategy Table\nSticks   Rankings\n";
		for (int i = strategyTable.length - 1; i >= 0; i--)
			output += actionRankingToString(i + 1, strategyTable[i]);
		return output;
	}

	/**
	 * This updates the strategy table since a game was won.
	 * 
	 * The strategyTable has the set of actionRankings for each number of sticks
	 * left. The actionHistory array records the number of sticks the user took
	 * when a given number of sticks remained on the table. Remember that
	 * indexing starts at 0. For example, if actionHistory at index 6 is 2, then
	 * the user took 2 sticks when there were 7 sticks remaining on the table.
	 * For each action noted in the history, this calls the
	 * updateActionRankingOnWin method passing the corresponding action and
	 * actionRanking. After calling this method, the actionHistory is cleared
	 * (all values set to 0).
	 * 
	 * @param strategyTable
	 *            An array of actionRankings.
	 * 
	 * @param actionHistory
	 *            An array where the index indicates the sticks left and the
	 *            element is the action that was made.
	 */
	static void updateStrategyTableOnWin(int[][] strategyTable,
			int[] actionHistory) {
		for (int i = 0; i < actionHistory.length; i++) {
			updateActionRankingOnWin(strategyTable[i], actionHistory[i]);
		}
		// clear actionHistory
		Arrays.fill(actionHistory, 0);
	}

	/**
	 * This updates the strategy table for a loss.
	 * 
	 * The strategyTable has the set of actionRankings for each number of sticks
	 * left. The actionHistory array records the number of sticks the user took
	 * when a given number of sticks remained on the table. Remember that
	 * indexing starts at 0. For example, if actionHistory at index 6 is 2, then
	 * the user took 2 sticks when there were 7 sticks remaining on the table.
	 * For each action noted in the history, this calls the
	 * updateActionRankingOnLoss method passing the corresponding action and
	 * actionRanking. After calling this method, the actionHistory is cleared
	 * (all values set to 0).
	 * 
	 * @param strategyTable
	 *            An array of actionRankings.
	 * @param actionHistory
	 *            An array where the index indicates the sticks left and the
	 *            element is the action that was made.
	 */
	static void updateStrategyTableOnLoss(int[][] strategyTable,
			int[] actionHistory) {
		for (int i = 0; i < actionHistory.length; i++) {
			updateActionRankingOnLoss(strategyTable[i], actionHistory[i]);
		}
		// clear actionHistory
		Arrays.fill(actionHistory, 0);
	}

	/**
	 * This method simulates a game between two players using their
	 * corresponding strategyTables. Use the aiChooseAction method to choose an
	 * action for each player. Record each player's actions in their
	 * corresponding history array. This method doesn't print out any of the
	 * actions being taken. Player 1 should make the first move in the game.
	 * 
	 * @param startSticks
	 *            The number of sticks to start the game with.
	 * @param player1StrategyTable
	 *            An array of actionRankings.
	 * @param player1ActionHistory
	 *            An array for recording the actions that occur.
	 * @param player2StrategyTable
	 *            An array of actionRankings.
	 * @param player2ActionHistory
	 *            An array for recording the actions that occur.
	 * @return 1 or 2 indicating which player won the game.
	 */
	static int playAiVsAi(int startSticks, int[][] player1StrategyTable,
			int[] player1ActionHistory, int[][] player2StrategyTable,
			int[] player2ActionHistory) {
		// winner of the game
		int winner = 0;
		while (startSticks > 0) {
			// record player1 action in player1ActionHistory array
			player1ActionHistory[startSticks - 1] = aiChooseAction(startSticks,
					player1StrategyTable[startSticks - 1]);
			// decrement startSticks
			startSticks -= player1ActionHistory[startSticks - 1];
			// check remaining sticks
			if (startSticks == 0) {
				winner = 2;
				break;
			}
			//record player2 action in player2ActionHistory array
			player2ActionHistory[startSticks - 1] = aiChooseAction(startSticks,
					player2StrategyTable[startSticks - 1]);
			// decrement startSticks
			startSticks -= player2ActionHistory[startSticks - 1];
			// check remaining sticks
			if (startSticks == 0) {
				winner = 1;
				break;
			}
		}
		return winner;
	}

	/**
	 * This method has the computer play against itself many times. Each time it
	 * plays it records the history of its actions and uses those actions to
	 * improve its strategy.
	 * 
	 * Algorithm: 1) Create a strategy table for each of 2 players with
	 * createAndInitializeStrategyTable. 2) Create an action history for each
	 * player. An action history is a single dimension array of int. Each index
	 * in action history corresponds to the number of sticks remaining where the
	 * 0 index is 1 stick remaining. 3) For each game, 4) Call playAiVsAi with
	 * the return value indicating the winner. 5) Call updateStrategyTableOnWin
	 * for the winner and 6) Call updateStrategyTableOnLoss for the loser. 7)
	 * After the games are played then the strategyTable for whichever strategy
	 * won the most games is returned. When both players win the same number of
	 * games, return the first player's strategy table.
	 * 
	 * @param startSticks
	 *            The number of sticks to start with.
	 * @param numberOfGamesToPlay
	 *            The number of games to play and learn from.
	 * @return A strategyTable that can be used to make action choices when
	 *         playing a person. Returns null if startSticks is less than
	 *         Config.MIN_STICKS or greater than Config.MAX_STICKS. Also returns
	 *         null if numberOfGamesToPlay is less than 1.
	 */
	static int[][] trainAi(int startSticks, int numberOfGamesToPlay) {
		if (startSticks < Config.MIN_STICKS || startSticks > Config.MAX_STICKS
				|| numberOfGamesToPlay < 1)
			return null;
		else {
			// create and initial strategy stable for both players
			int[][] player1StrategyTable = createAndInitializeStrategyTable(
					startSticks);
			int[][] player2StrategyTable = createAndInitializeStrategyTable(
					startSticks);
			// create ActionHistory array for both players
			int[] player1ActionHistory = new int[startSticks];
			int[] player2ActionHistory = new int[startSticks];
			// number of times player1 wins
			int player1Winner = 0;
			// number of times player2 wins
			int player2Winner = 0;
			// simulates game for numberOfGamesToPlay times
			for (int i = 0; i < numberOfGamesToPlay; i++) {
				// winner of the game
				int winner = playAiVsAi(startSticks, player1StrategyTable,
						player1ActionHistory, player2StrategyTable,
						player2ActionHistory);
				if (winner == 1) {
					// player1 wins the game
					updateStrategyTableOnWin(player1StrategyTable,
							player1ActionHistory);
					updateStrategyTableOnLoss(player2StrategyTable,
							player2ActionHistory);
					// increment number of times player1 wins
					player1Winner++;
				} else {
					// player2 wins the game
					updateStrategyTableOnWin(player2StrategyTable,
							player2ActionHistory);
					updateStrategyTableOnLoss(player1StrategyTable,
							player1ActionHistory);
					// increment number of times player2 wins
					player2Winner++;
				}
			}
			if (player1Winner >= player2Winner)
				return player1StrategyTable;
			else
				return player2StrategyTable;
		}
	}
}
