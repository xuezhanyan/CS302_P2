
//
//TODO  file header comment

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

//TODO class header comment

public class Sticks {
	final static int MIN_STICKS = Config.MIN_STICKS;
	final static int MAX_STICKS = Config.MAX_STICKS;
	final static int MIN_ACTION = Config.MIN_ACTION;
	final static int MAX_ACTION = Config.MAX_ACTION;
	final static int MIN_GAMES = Config.MIN_GAMES;
	final static int MAX_GAMES = Config.MAX_GAMES;

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
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		// welcome message
		System.out.println("Welcome to the Game of Sticks!");
		System.out.println("==============================");
		System.out.println("");

		// get user name
		System.out.print("What is your name? ");
		String userName = input.nextLine().trim();
		System.out.println("Hello " + userName + ".");

		// get initial # of sticks
		int initialStickNum = 0; // store initial # of sticks
		do {
			initialStickNum = promptUserForNumber(input, "How many sticks are there on the table initially (" + MIN_STICKS + "-" + MAX_STICKS + ")?", MIN_STICKS, MAX_STICKS);
		} while (initialStickNum < MIN_STICKS || initialStickNum > MAX_STICKS);

		// menu
		System.out.println("");
		System.out.println("Would you like to:");
		System.out.println(" 1) Play against a friend");
		System.out.println(" 2) Play against computer (basic)");
		System.out.println(" 3) Play against computer with AI");
		char choose;

		boolean flag1 = false;
		while (!flag1) {
			System.out.print("Which do you choose (1,2,3)? ");
			choose = input.next().charAt(0);
			input. nextLine();
			switch (choose) {
			case '1': {
				System.out.println("");
				// get friend name
				System.out.print("What is your friend's name? ");
				String friendName = input.nextLine().trim();
				System.out.println("Hello " + friendName + ".");
				System.out.println("");

				System.out.println("There are " + initialStickNum + " sticks on the board.");

				int remainStick = 0;

				remainStick = initialStickNum;

				playAgainstFriend(input, remainStick, userName, friendName);

				flag1 = true;
				break;
			}

			case '2': {
				System.out.println("");
				playAgainstComputer(input, initialStickNum, userName, null);
				
				flag1 = true;
				break;
			}

			case '3': {
				System.out.println("");
				int numberOfGamesToPlay = 0;
				do {
					numberOfGamesToPlay = promptUserForNumber(input, "How many games should the AI learn from ("+MIN_GAMES+" to "+MAX_GAMES+")?", MIN_GAMES , MAX_GAMES);
				} while (numberOfGamesToPlay < MIN_GAMES || numberOfGamesToPlay > MAX_GAMES);
				
				int[][] strategyTable = trainAi( initialStickNum,  numberOfGamesToPlay);
				playAgainstComputer(input, initialStickNum, userName,strategyTable);
				
				char answer=' ';
				do{
					System.out.print("Would you like to see the strategy table (Y/N)? ");
					answer= input.next().charAt(0);
				}while ((answer!='Y')&&(answer!='y')&&(answer!='N')&&(answer!='n'));
				//System.out.println("");
				if ((answer=='Y')||(answer=='y'))
					System.out.print(strategyTableToString(strategyTable));
				
				flag1 = true;
				break;
			}

			default: {

				if (!Character.isDigit(choose)) {
					System.out.println("Error: expected a number between 1 and 3 but found: " + choose);
				} else {
					System.out.println("Please enter a number between 1 and 3.");
				}
				flag1 = false;
				break;
			}

			}
		}

		System.out.println("");
		input.close();
		System.out.println("=========================================");
		System.out.println("Thank you for playing the Game of Sticks!");

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
	static int promptUserForNumber(Scanner input, String prompt, int min, int max) {

		int returnValue = 0;
		System.out.print(prompt+" ");

		if (input.hasNextInt()) {
			returnValue = input.nextInt();
			if (returnValue <= max && returnValue >= min) {

			} 
			else {
				System.out.println("Please enter a number between " + min + " and " + max + ".");

			}
			input.nextLine();
		} 
		else {
			String junk = input.nextLine();
			System.out.println("Error: expected a number between " + min + " and " + max + " but found: " + junk);

		}

		return returnValue;
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
	static void playAgainstFriend(Scanner input, int startSticks, String player1Name, String player2Name) {
		int runTimes = 0;
		int takenStick = 0;
		while (true) {
			if (runTimes % 2 == 0) {

				do {

					takenStick = promptUserForNumber(input, player2Name + ": How many sticks do you take (" + MIN_ACTION + "-" + Math.min(MAX_ACTION,startSticks)+ ")?", MIN_ACTION, Math.min(MAX_ACTION, startSticks));

				} while (takenStick < MIN_ACTION || takenStick > Math.min(MAX_ACTION, startSticks));

			} else {

				do {

					takenStick = promptUserForNumber(input, player1Name + ": How many sticks do you take (" + MIN_ACTION + "-" + Math.min(MAX_ACTION,startSticks)+ ")?", MIN_ACTION, Math.min(MAX_ACTION, startSticks));

				} while (takenStick < MIN_ACTION || takenStick > Math.min(MAX_ACTION, startSticks));
			}
			runTimes++;
			startSticks = startSticks - takenStick;

			//game result
			if (startSticks == 0) {
				if (runTimes % 2 == 0) {
					System.out.println(player2Name + " wins. " + player1Name + " loses.");
					break;
				} else {
					System.out.println(player1Name + " wins. " + player2Name + " loses.");
					break;
				}
			} else if (startSticks == 1)
				System.out.println("There is " + startSticks + " stick on the board.");
			else
				System.out.println("There are " + startSticks + " sticks on the board.");

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
		int pick = 0;
		if (sticksRemaining < MAX_ACTION && sticksRemaining > 0)
			pick = MIN_ACTION;
		else if (sticksRemaining <= 0)
			pick = 0;
		else
			pick = Config.RNG.nextInt(MAX_ACTION - MIN_ACTION+1) + MIN_ACTION; //randomly choose a number between MIN_ACTION and MAX_ACTION
			
			
		return pick; 
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
	static void playAgainstComputer(Scanner input, int startSticks, String playerName, int[][] strategyTable) {
		int runTimes = 0;
		int takenStick = 0;
		System.out.println("There are "+startSticks+" sticks on the board.");
		while (true) {
			if (runTimes % 2 == 1) {

				do {				
					if (strategyTable==null)
						takenStick = basicChooseAction(startSticks);
					else
						takenStick = aiChooseAction( startSticks, strategyTable[startSticks-1]);
					
					if (takenStick==1)
						System.out.println("Computer selects "+ takenStick + " stick.");
					else
					System.out.println("Computer selects "+ takenStick + " sticks.");

				} while (takenStick < MIN_ACTION || takenStick > Math.min(MAX_ACTION, startSticks));

			} else {

				do {
					takenStick = promptUserForNumber(input, playerName+": How many sticks do you take (" + MIN_ACTION + "-" + Math.min(MAX_ACTION,startSticks)+ ")?", MIN_ACTION, Math.min(MAX_ACTION, startSticks));

				} while (takenStick < MIN_ACTION || takenStick > Math.min(MAX_ACTION, startSticks));
			}
			runTimes++;
			startSticks = startSticks - takenStick;

			//game result
			if (startSticks == 0) {
				if (runTimes % 2 == 0) {
					System.out.println(playerName + " wins. " + "Computer" + " loses.");
					break;
				} else {
					System.out.println("Computer" + " wins. " + playerName + " loses.");
					break;
				}
			} else if (startSticks == 1)
				System.out.println("There is " + startSticks + " stick on the board.");
			else
				System.out.println("There are " + startSticks + " sticks on the board.");

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
		int returnValue = 0;

		// check special cases returning 0
		if ((actionRanking == null) || (sticksRemaining <= 0) || (actionRanking.length == 0))
			returnValue = 0;
		else {
			// Calculate total possibility
			int totalPossibility = 0;
			for (int i = 0; i < actionRanking.length; i++) {
				totalPossibility = totalPossibility + actionRanking[i];
			}
			// create array containing output*possibility
			// size of array is equal to total number of possibilities and store
			// with expected return action with possibility
			// e.g. [1,7,2] --> [1,2,2,2,2,2,2,2,3,3]
			int returnValueProbably = 1;
			int indexOfoutput_possibility = 0;
			int[] output_possibility = new int[totalPossibility];
			for (int m = 0; m < actionRanking.length; m++) {
				for (int n = 0; n < actionRanking[m]; n++) {
					output_possibility[indexOfoutput_possibility] = returnValueProbably;
					indexOfoutput_possibility++;
				}
				returnValueProbably++;
			}

			if (sticksRemaining < MAX_ACTION)
				returnValue = MIN_ACTION;
			else
				returnValue = output_possibility[Config.RNG.nextInt(totalPossibility)];
		}

		return returnValue;
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
		if (actionRanking==null)
			return;
		else{
			for(int i =0; i< actionRanking.length;i++)
				actionRanking[i]=1;
		}
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
		String returnValue = "";
		returnValue = returnValue + sticksLeft + "\t";
		for (int i = 0; i < actionRanking.length; i++) {
			returnValue = returnValue + actionRanking[i];
			if (i != actionRanking.length - 1)
				returnValue = returnValue + ",";
			else
				returnValue = returnValue + "\n";
		}
		return returnValue; 
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
		actionRanking[action-1]=Math.max(1, actionRanking[action-1]-1);
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
		actionRanking[action-1]= actionRanking[action-1]+1;
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
		int [][] returnArray = new int [startSticks][MAX_ACTION-MIN_ACTION+1];
		for (int i=0; i<returnArray.length;i++)
			initializeActionRanking(returnArray[i]);
		return returnArray; 
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
		String returnString = "\nStrategy Table\nSticks	Rankings\n" ;
		for (int i = strategyTable.length-1; i >=0; i--) {
				returnString = returnString + actionRankingToString((i+1),strategyTable[i]);
		}

		
		return returnString; 
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
	static void updateStrategyTableOnWin(int[][] strategyTable, int[] actionHistory) {
		// update strategyTable by actionHistory
		for (int j = 0; j < actionHistory.length; j++) {
			if (actionHistory[j] != 0)
				updateActionRankingOnWin(strategyTable[j],actionHistory[j] );
		}
		// clear actionHistory array
		for (int i = 0; i < actionHistory.length; i++)
			actionHistory[i] = 0;
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
	static void updateStrategyTableOnLoss(int[][] strategyTable, int[] actionHistory) {
		// update strategyTable by actionHistory
		for (int j = 0; j < actionHistory.length; j++) {
			if (actionHistory[j] != 0)
				updateActionRankingOnLoss(strategyTable[j],actionHistory[j] );
		}
		// clear actionHistory array
		for (int i = 0; i < actionHistory.length; i++)
			actionHistory[i] = 0;
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
	static int playAiVsAi(int startSticks, int[][] player1StrategyTable, int[] player1ActionHistory,
			int[][] player2StrategyTable, int[] player2ActionHistory) {

		int runTimes = 0;
		int takenStickP1 = 0;
		int takenStickP2 = 0;
		while (true) {
			if (runTimes % 2 == 1) {
				takenStickP2 = aiChooseAction(startSticks, player2StrategyTable[startSticks - 1]);
				player2ActionHistory[startSticks-1]=takenStickP2;//store action into player1ActionHistory
				startSticks = startSticks - takenStickP2;
			} else {
				takenStickP1 = aiChooseAction(startSticks, player1StrategyTable[startSticks - 1]);
				player1ActionHistory[startSticks-1]=takenStickP1;//store action into player1ActionHistory
				startSticks = startSticks - takenStickP1;
			}
			runTimes++;

			if (startSticks == 0) {
				if (runTimes % 2 == 0) {
					return 1;
				} else {
					return 2;
				}
			}
		}
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
		if ((startSticks<MIN_STICKS)||(startSticks>MAX_STICKS)||(numberOfGamesToPlay<1))
			return null;
		int[][] player1StrategyTable = createAndInitializeStrategyTable(startSticks);
		int[][] player2StrategyTable = createAndInitializeStrategyTable(startSticks);
		int[] player1ActionHistory = new int[startSticks];
		int[] player2ActionHistory = new int[startSticks];
		int numP1Win =0;
		int numP2Win =0;
		
		for (int i=0; i<numberOfGamesToPlay;i++){
			if (playAiVsAi( startSticks, player1StrategyTable, player1ActionHistory,player2StrategyTable, player2ActionHistory)==1){
				updateStrategyTableOnWin( player1StrategyTable,  player1ActionHistory) ;
				updateStrategyTableOnLoss( player2StrategyTable,  player2ActionHistory) ;
				numP1Win++;
			}
			else{
				updateStrategyTableOnWin(player2StrategyTable,  player2ActionHistory) ;
				updateStrategyTableOnLoss(player1StrategyTable,  player1ActionHistory) ;
				numP2Win++;
			}
		}
		if (numP1Win>= numP2Win)
			return player1StrategyTable;
		else 
			return player2StrategyTable;
			
	}

}
