// Sample Mousecat Program
// Julian Genkins
// jzg
// March 21, 2012

//---------------------Description---------------------
// This program will create 4 cats which patrol around
// a hole at the very center of the board. Mice will come  
// from the NW and SE corners, along the diagonals, and 
// try to reach hole. Which one will make it!!!
//-----------------------------------------------------



size 25 25

begin 	
						
	//make 4 cats around middle
	cat c1 15 15 west ;
	cat c2 11 15 north ; 
	cat c3 11 11 east ; 
	cat c4 15 11 south ; 	//boom
	
	//make the hole they are guarding
	hole 13 13 ;
	
	//create some mice
	mouse tim 2 2 east ;
	mouse tony 4 4 east ;
	mouse ron 23 23 west ;
	mouse mark 17 17 west ;
	
	
	//get the mice to the center and make the cats patrol around
	repeat 13
		
		//move cats across one edge of square
		repeat 2
			move c1 ;
			move c2 ;
			move c3 ;
			move c4 ;
			
			//move mice once horizontal
			move tim ;
			move tony ;
			move ron ;
			move mark ;
			
			clockwise tim ;
			clockwise tony ;
			clockwise ron ;
			clockwise mark ;
			
			move c1 ;
			move c2 ;
			move c3 ;
			move c4 ;
			
			//move mice once vertical (back onto diagonal
			move tim ;
			move tony ;
			move ron ;
			move mark ;
			
			//turn mice and prepare for next repetition
			repeat 3
				clockwise tim ;
				clockwise tony ;
				clockwise ron ;
				clockwise mark ;
			end ;
			
		end ;
	
	//rotate cats on corners
	clockwise c1 ;
	clockwise c2 ;
	clockwise c3 ;
	clockwise c4 ;
	
	end ;

halt