# Solitaire

A Java-based Solitaire game built for Data Structures class. Extra features include scoreboard, etc.

# Data Structures
The primary class (Solitaire) uses a Vector/ArrayList type data structure to store 52 cards. The Vector was chosen since there is a considerable reduction of cards as they are distributed to the Piles and the Stock.

Pile 
The Pile (column) is instantiated seven times where each instance is stored as an array element. The Pile utilizes a LinkedList data structure to hold the shuffled cards which have been distributed to it.

Stock
The Stock uses the LinkedList data structure to hold the shuffled cards (those left after distributing to the Piles). It also uses the a stack to store the three cards drawn at a time from those shuffled. Only the top is accessible/viewable. User must use top card before any cards under it can be accessed.

Cabinet
The Cabinet class is instantiated four times to match the four card suits. It makes use of the Stack data structure as it accumulates one card at a time and only releases the topmost card in its stack.

![Conceptual design](https://github.com/rpparas/Solitaire/blob/master/howTo/images/sol.png?raw=true)
