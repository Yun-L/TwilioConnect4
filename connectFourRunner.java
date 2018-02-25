/*
 * Connect Four Twilio Game
 * by Mitchell Ryzuk and Eric Lai
 * 2/25/2018
 */
package com.Twilio;

import static spark.Spark.*;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.twiml.messaging.Message;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.type.PhoneNumber;

public class connectFourRunner{

	//Find your Account Sid and Token at twilio.com/user/account
	public static final String ACCOUNT_SID = "AC56a672682dcd4d88cfd4ee4934591f44";
	public static final String AUTH_TOKEN = "908a03c7acc9db4e822e0f51406bb60a";
	
	static String currentMessage = ""; //input from phone
	static String phoneNumber = ""; //most recent phone number
	static Boolean output = false; //for sending messages back when there's an input
	
	//sets the user's name
	//index is 0 (player 1) or 1 (player 2)
	public static String[] setName(String[] names, int index) {
		currentMessage = "";
		while (currentMessage.length() == 0) { //this loop ensures that we stay in the method waiting for user input
			System.out.println("");
		}
		
		names[index] = currentMessage;
		return names;
		
	}
	
	//adds phone number to an array
	public static String setNumber(String number){
		while (true) {
			currentMessage = "";
			while (currentMessage.length() == 0) { //nothing is entered
				System.out.println("");
			}
			if (currentMessage.trim().replace(" ", "").length() == 10) { //if there are 10 digits, try making it into a phone number
				try {
					long test = Long.parseLong(currentMessage); //seeing if the given value contains all numbers
					return "+1" + currentMessage; //returns concatenated String with +1 (US Phone #) then the number
				}
				catch (NumberFormatException nfe) {
					MessageCreator askNum = new MessageCreator(new PhoneNumber(number), new PhoneNumber("+12016895466"), 
							"Please enter the phone number of the person who you want to play against.");
					askNum.create();	
				}
			}
			//if something was entered 
			if (currentMessage.trim().replace(" ", "").length() > 0) {
				MessageCreator askNum = new MessageCreator(new PhoneNumber(number), new PhoneNumber("+12016895466"), 
						"Please enter the phone number of the person who you want to play against.");
				askNum.create();
			}
		}		
	}
	public static void main(String[] args) throws InterruptedException {
			// TODO Auto-generated method stub

			Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
			Boolean running = true;
			Boolean playing = false;
			String[] numbers = new String[2];
			String[] names = new String[2];
			
			
			post("/", (req, res) -> { //Spark post function, communicating with HTTP server
	        	
				//Capturing the message and phone number that was received into String variables for manipulation in the program
	        	currentMessage = req.queryParams("Body");
	        	phoneNumber = req.queryParams("From");
	        	
	        	//Replying back after input, this will never execute
	        	if (output) {
	        		Body body = new Body.Builder(currentMessage).build();
		        	
			       	Message sms = new Message.Builder()
			       			.body(body)
			       			.build();
			        	
			       MessagingResponse twiml = new MessagingResponse.Builder()
		        			.message(sms)
		        			.build();
					return twiml.toXml();
	        	}
	        	
	        	return null;  	
	        	
	        });
			
			
			//This loop will keep running until program is terminated
			while (running) {
				
				//When user sends "CONNECT4" to the Twilio number, the game will start
				if (currentMessage.toUpperCase().replaceAll(" ", "").equals("CONNECT4")) {
					numbers[0] = phoneNumber;
					
					//asking for name and calling setName function
					MessageCreator welcome = new MessageCreator(new PhoneNumber(numbers[0]), new PhoneNumber("+12016895466"), 
							"Welcome to Connect Four. Please enter your name.");
					welcome.create();
					names = setName(names, 0);
					
					//asking for the phone number of the opponent and calling setNumber function
					MessageCreator askNum = new MessageCreator(new PhoneNumber(numbers[0]), new PhoneNumber("+12016895466"), 
							names[0] + ", please enter the phone number of the person who you want to play against.");
					askNum.create();
					numbers[1] = setNumber(numbers[0]);
				}
				
				//if a 10 digit phone number was entered
				if (currentMessage.length() == 10) {
					//testing if input was actually a phone number by parsing currentMessage into a long variable
					try {
						long test = Long.parseLong(currentMessage);
						
						//setting phone number and asking for input
						numbers[1] = "+1" + currentMessage;
						currentMessage = "";
						MessageCreator player2 = new MessageCreator(new PhoneNumber(numbers[1]), new PhoneNumber("+12016895466"), 
								names[0] + " would like to play Connect Four with you.\n" 
								+ "Type 'y' for yes, 'n' for no.");
						player2.create();
					}
					catch (NumberFormatException nfe) { //preventing error if currentMessage couldn't be parsed
						
					}	
				}
				
				//different responses if player 2 says yes or no
				if (currentMessage.trim().toLowerCase().equals("y")) {
					if (numbers[1] != null) { //will not execute if there is no value in numbers[1]
						
						//asking for player 2's name
						MessageCreator askName = new MessageCreator(new PhoneNumber(numbers[1]), new PhoneNumber("+12016895466"), 
								"Please enter your name.");
						askName.create();
						names = setName(names, 1);
						
						//game begins
						MessageCreator player1Start = new MessageCreator(new PhoneNumber(numbers[0]), new PhoneNumber("+12016895466"), 
								names[0] + ", Game on! You go first.");
						player1Start.create();
						MessageCreator player2Start = new MessageCreator(new PhoneNumber(numbers[1]), new PhoneNumber("+12016895466"), 
								names[1] + ", Game on! You go second.");
						player2Start.create();
						
						playing = true; //allows game loop to run
						currentMessage = "";
					}
				}
				if(currentMessage.trim().toLowerCase().equals("n")) {
					if (numbers[1] != null) {
						//sends an individual message to each phone
						MessageCreator player1End = new MessageCreator(new PhoneNumber(numbers[0]), new PhoneNumber("+12016895466"), 
								"The person you wanted to play with does not want to play right now. Type CONNECT4 to try again.");
						player1End.create();
						MessageCreator player2End = new MessageCreator(new PhoneNumber(numbers[1]), new PhoneNumber("+12016895466"), 
								"Have a nice day!");
						player2End.create();
						
						//resetting values
						currentMessage = "";
						numbers = new String[2];
					}				
				}
				
				Game h = new Game(); //game object
				int currentPlayer = 0; //setting current player (0 is player 1, 1 is player 2)
				int[] emojis = {128308,128309}; //array containing the decimal Unicode values of the red and blue circle emojis
				
				//game loop
				while(playing == true) {
					int choice = -1; //where the user chooses to go
					
					//prints the board and who's turn it is to each person's phone
					for (int i = 0; i < 2; i++) {
						MessageCreator askNum = new MessageCreator(new PhoneNumber(numbers[i]), new PhoneNumber("+12016895466"), 
								names[currentPlayer] + "'s turn.");
						askNum.create();
						
						askNum = new MessageCreator(new PhoneNumber(numbers[i]), new PhoneNumber("+12016895466"), 
								h.toStringB().toString());
						askNum.create();
					}
					
					currentMessage = "";
					
					//when a valid choice hasn't been determined yet
					while (choice < 0 || choice > 7) {
						//asks user to pick a spot on the board
						MessageCreator askNum = new MessageCreator(new PhoneNumber(numbers[currentPlayer]), new PhoneNumber("+12016895466"), 
								names[currentPlayer] + ", Choose your column.");
						askNum.create();
						//this declaration prevents an infinite messaging loop
						currentMessage = "";
						while (currentMessage.length() == 0) {
							System.out.println("");
						}
						//attempts to parse the user input as an int
						try {
							choice = Integer.parseInt(currentMessage.trim());
						}
						catch (NumberFormatException nfe) {
							
						}
					}
					
					h.addElement(emojis[currentPlayer], choice); //places the user's choice on the board
					
					/*
					 * winCondition() returns either 0, 1 or 2
					 * 0 = current player wins
					 * 1 = tie
					 * 2 = no win yet
					 */
					
					//if there's a winner
					if (h.winCondition(emojis[currentPlayer]) == 0) {
						//send message <current player> wins! nice! to both players
						for ( int i = 0; i < 2; i++) {
							MessageCreator winner = new MessageCreator(new PhoneNumber(numbers[i]), new PhoneNumber("+12016895466"), 
									h.toStringB() + "\n" + names[currentPlayer] + " Wins! Nice!");
							winner.create();
							playing = false;
							currentMessage = "";
						}
					}
					//if there's a tie
					if (h.winCondition(emojis[currentPlayer]) == 1) {
						for (int i = 0; i < 2; i++) {
							MessageCreator tie = new MessageCreator(new PhoneNumber(numbers[i]), new PhoneNumber("+12016895466"), 
									h.toStringB() + "\n" + "No one wins. You are both losers.");
							tie.create();
							playing = false;
							currentMessage = "";
						}
					}
					
					//switching the current player
					if (currentPlayer == 0) {
						currentPlayer = 1;
					}
					else {
						currentPlayer = 0;
					}
				}	
			}	 		 
		}	
}