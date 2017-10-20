import java.util.*;
import java.io.*;

public class pegSolitaire
{
	static int World[][] = new int[7][7];
	static int Size = World.length;
	static int left = 1;
	static int up = 2;
	static int down = 3;
	static int right = 4;
	static int Moves[][] = new int[4][Size*Size];   //store row, col, dir and possible moves, for each move, on every possible element
	static int Count = 0; 							//count number of jumps
	static int Goal = 0;
	static boolean Solved = false;
	static int MaxCount = 0; 						//counts the highest value count reached
	static long Jumps = 0;							//total jumps
	static int depth = 0;							//should be the same value as Count
	static TreeMap<Integer, Integer> Map = new TreeMap<Integer, Integer>();//a tree to store and sort the hash values
	
	public static void readFile(String filename)
	{
		File file = new File(filename);
		try
		{
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextInt())
			{
				for(int row=0; row<Size; row++)
				{
					for(int col=0; col<Size; col++)
					{
						World[row][col] = scanner.nextInt();
					}
				}
				Goal = scanner.nextInt();
			}
			scanner.close();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		//printGrid();
		
	}
	
	
	public static int countPosMoves(int row, int col)
	{//before a jump is made the number of possible moves from that position are counted, might be used for backtracking
		int ret = 0;
		if(col > 1)
		{
			if(World[row][col-1] == 2 && World[row][col-2] == 2)
			{
				ret ++;
			}
		}
		if(col < Size-2)
		{
			if(World[row][col+1] == 2 && World[row][col+2] == 2)
			{
				ret ++;
			}
		}
		if(row > 1)
		{
			if(World[row-1][col] == 2 && World[row-2][col] == 2)
			{
				ret ++;
			}
		}
		if(row < Size-2)
		{
			if(World[row+1][col] == 2 && World[row+2][col] == 2)
			{
				ret ++;
			}
		}
		return(ret);
	}
	
		
	public static int jump(int row, int col, int dir)
	{
		int rc = 0;
		if (World[row][col] == 1)
		{
			if (dir == right)
			{
				if(col < 2)
				{
					rc = 0;
				}
				else
				if (World[row][col-1] != 2 || World[row][col-2] != 2)
				{
					rc = 0;
				}
				else
				if(World[row][col-1] == 2 && World[row][col-2] == 2)
				{
					World[row][col-1] = 1;
					World[row][col-2] = 1;
					World[row][col] = 2;
					rc = 1;
				}
			}
			else
			if (dir == left)
			{
				if(col > Size-3)
				{
					rc = 0;
				}
				else
					if (World[row][col+1] != 2 || World[row][col+2] != 2)
					{
						rc = 0;
					}
					else
					if(World[row][col+1] == 2 && World[row][col+2] == 2)
					{
						World[row][col+1] = 1;
						World[row][col+2] = 1;
						World[row][col] = 2;
						rc = 2;
					}
			}
			else
			if (dir == down)
			{
				if(row < 2)
				{
					rc = 0;
				}
				else
				if (World[row-1][col] != 2 || World[row-2][col] != 2)
				{
					rc = 0;
				}
				else
				if(World[row-1][col] == 2 && World[row-1][col] == 2)
				{
					World[row-1][col] = 1;
					World[row-2][col] = 1;
					World[row][col] = 2;
					rc = 3;
				}
			}
			else
			if (dir == up)
			{
				if(row > Size-3)
				{
					rc = 0;
				}
				else
				if (World[row+1][col] != 2 || World[row+2][col] != 2)
				{
					rc = 0;
				}
				else
				if(World[row+1][col] == 2 && World[row+2][col] == 2)
				{
					World[row+1][col] = 1;
					World[row+2][col] = 1;
					World[row][col] = 2;
					rc = 4;
				}
			}
		}
		if(rc > 0)
		{//if a jump was made, update the Moves array with its details
			Moves[0][Count] = row;
			Moves[1][Count] = col;
			Moves[2][Count] = dir;
			Count ++;
			if (Count > MaxCount)
			{
				MaxCount = Count;
				System.out.format("Depth = %d  total jumps = %d  hash = %d\n", MaxCount, Jumps, Map.size());
			}
		}
		return(rc);
	}
	
	public static void undo(int row, int col, int dir)
	{//opposite of jump, to reverse a move when backtracking
		if (dir == right)
		{
			World[row][col-1] = 2;
			World[row][col-2] = 2;
			World[row][col] = 1;
		}
		else
		if (dir == left)
		{
			World[row][col+1] = 2;
			World[row][col+2] = 2;
			World[row][col] = 1;
		}
		else
		if (dir == down)
		{
			World[row-1][col] = 2;
			World[row-2][col] = 2;
			World[row][col] = 1;
		}
		else
		if (dir == up)
		{
			World[row+1][col] = 2;
			World[row+2][col] = 2;
			World[row][col] = 1;
		}
		Count --;
		Moves[0][Count] = 0;
		Moves[1][Count] = 0;
		Moves[2][Count] = 0;
		
	}

	public static int hash()
	{//we divide the grid into symmetries and sum them then multiply each by a different power of 10 to get a unique number
	 //that is shared by all equivalent rotations. Board state of 0 is ignored(out of bounds), can't use negative though, that is reserved
	 //for centre piece. Board state 1 adds 0 to the hash and board state 2 adds 1
		int value = 0;
		int temp = 0;
		temp = (World[0][0] + World[0][6] + World[6][0] + World[6][6] - 4)*100000000;
		if(temp > 0)
		{
			value += temp; 
		}
		temp = (World[0][1] + World[0][5] + World[1][0] + World[1][6] + World[5][0] + World[5][6] + World[6][1] + World[6][5]- 8)*10000000;
		if(temp > 0)
		{
			value += temp; 
		}
		temp = (World[0][2] + World[0][4] + World[2][0] + World[2][6] + World[4][0] + World[4][6] + World[6][2] + World[6][4]- 8)*1000000;
		if(temp > 0)
		{
			value += temp; 
		}
		temp = (World[0][3] + World[3][0] + World[3][6] + World[6][3] - 4)*100000;
		if(temp > 0)
		{
			value += temp; 
		}
		temp = (World[1][1] + World[1][5] + World[5][1] + World[5][5] - 4)*10000;
		if(temp > 0)
		{
			value += temp; 
		}
		temp = (World[1][2] + World[1][4] + World[2][1] + World[2][5] + World[4][1] + World[4][5] + World[5][2] + World[5][4]- 8)*1000;
		if(temp > 0)
		{
			value += temp; 
		}
		temp = (World[1][3] + World[3][1] + World[3][5] + World[5][3] - 4)*100;
		if(temp > 0)
		{
			value += temp; 
		}
		temp = (World[2][2] + World[2][4] + World[4][2]	 + World[4][4] - 4)*10;
		if(temp > 0)
		{
			value += temp; 
		}
		temp = (World[2][3] + World[3][2] + World[3][4] + World[4][3] - 4);
		if(temp > 0)
		{
			value += temp; 
		}

		if(World[3][3] == 2)
		{
			value = value*-1; 
		}
		return(value);
	}
		
	public static void check()
	{//the solved board just has 1 peg in the middle which has a hash value of 0
		if (hash() == Goal)
		{
			Solved = true;
		}
	}

	public static void solve()
	{
		//printGrid();
		depth ++;
		int hash = hash();
		//System.out.print(hash);
		int rc = 0;
		int dir = 1;
		int pos = 0;
		boolean check = Map.containsKey(hash);//looks for the key, returns the position or -1 if it's not found
		if(check == false)
		{//if hash value isn't in HashTable, the board state isn't dead so we try make a jump
			for (int row=0; row<Size; row++)
			{//we run through the grid trying every peg, till we get a valid jump
				for (int col=0;col<Size; col++)
				{
					dir = 1;
					pos = countPosMoves(row, col);
					while(dir < 5)
					{
						rc = jump(row, col, dir);
						if(rc == 0)
						{
							dir++;
						}
						if(rc > 0)
						{
							Moves[3][Count-1] = pos;
							Jumps++;
							if((Jumps % 1000000) == 0)
							{
								System.out.format("%d %d %d hash=%d\n",Jumps, depth, MaxCount, Map.size());
								//printGrid();
							}
							solve();//once a jump has been done we call the recursion
							check();//once we break out of the recursion, we check if the board is solved
							if(Solved == false)
							{//if not we need to save the current board state as dead and then back track
								Map.put(hash(), 1);//before we undo the move we add the board state to the HashTable so we don't check it again
								undo(Moves[0][Count-1], Moves[1][Count-1], Moves[2][Count-1]);
								//printGrid();
								dir++;
							}
						}
					}
				}
			}
		}
		depth --;
	}

	public static void printGrid()
	{//prints the grid in its current state and the Moves array to show which move was done from where
		System.out.println(" --------------");
		for (int row = 0; row<Size; row++)
		{
			System.out.print("|");
			for (int col=0; col<Size; col++)
			{
				System.out.format("%d ", World[row][col]);
			}
			System.out.println("|");
		}
		System.out.format("Count = %d\n", Count);
		System.out.println(" --------------");
		System.out.print("Row: ");
		for(int i = 0; i<Count; i++)
		{
			System.out.format("%d ", Moves[0][i]);
		}
		System.out.println();
		System.out.print("Col: ");
		for(int i = 0; i<Count; i++)
		{
			System.out.format("%d ", Moves[1][i]);
		}
		System.out.println();
		System.out.print("Dir: ");
		for(int i = 0; i<Count; i++)
		{
			System.out.format("%d ", Moves[2][i]);
		}
		System.out.println();
		System.out.print("pos: ");
		for(int i = 0; i<Count; i++)
		{
			System.out.format("%d ", Moves[3][i]);
		}
		System.out.println();
	}
	
	public static void printSolution()
	{
		int row = 0;
		int col = 0;
		for(int i=0; i<Count; i++)
		{
			if(Moves[2][i] == 1)
			{
				row = Moves[0][i];
				col = Moves[1][i]+2;
			}
			else
			if(Moves[2][i] == 2)
			{
				row = Moves[0][i]+2;
				col = Moves[1][i];
			}
			else
			if(Moves[2][i] == 3)
			{
				row = Moves[0][i]-2;
				col = Moves[1][i];
			}
			else
			if(Moves[2][i] == 4)
			{
				row = Moves[0][i];
				col = Moves[1][i]-2;
			}
			System.out.format("Jump from %d,%d to %d,%d\n", row, col, Moves[0][i], Moves[1][i]);
			//printGrid();
		}
	}
	
	public static void main(String args[])
	{
		Timer myTimer=new Timer();
		myTimer.start();
		String file = "src/6.txt";
		readFile(file);
		solve();
		myTimer.stop();
		if(Solved == true)
		{
			System.out.format("The problem was solved in %d moves after %d jumps in %dms\n", Count, Jumps, myTimer.getTime());
			//printSolution();
		}
		else
		{
			System.out.format("Couldn't find a solution after %d jumps in %dms\n", Jumps, myTimer.getTime());
		}
		//printGrid();
	}
}
