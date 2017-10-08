public class draft2 
{
	static int World[][] = {{0,0,1,1,1,0,0},{0,0,1,1,1,0,0},{1,1,1,1,1,1,1},{1,1,1,1,1,1,1},{1,1,1,1,1,1,1},{0,0,1,1,1,0,0},{0,0,1,1,1,0,0}};
	// 0 is out of bound, 1 is empty peg, 2 is a peg
	static int Size = World.length;
	static int right = 1;
	static int left = 2;
	static int up = 3;
	static int down = 4;
	static int Start[] = {0,3};
	static int SR = Start[0]; //starting row
	static int SC = Start[1]; //starting column
	static int Moves[][] = new int[4][Size*Size]; //store row, col, dir and possible moves, for each move, on every possible element
	static int Count = 0; //count number of jumps
	static int Solved = 1;//0 means solved +1 for each unsolved peg
	static int BackCount = 0; //counts the number of backtracks
	static int MaxCount = 0; //counts the highest value count reached
	
	public static void printGrid(int[][] World)
	{//prints the grid in its current state and the Moves array to show which move was done from where
		//System.out.println(" --------------");
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
		//System.out.println(" --------------");
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
	
	public static int countPosMoves(int row, int col)
	{//before a jump is made the number of possible moves from that position are counted, helps for backtracking
		int rc = 0;
		if(col > 1)
		{
			if(World[row][col-1] == 1 && World[row][col-2] == 1)
			{
				rc ++;
			}
		}
		if(col < Size-2)
		{
			if(World[row][col+1] == 1 && World[row][col+2] == 1)
			{
				rc ++;
			}
		}
		if(row > 1)
		{
			if(World[row-1][col] == 1 && World[row-2][col] == 1)
			{
				rc ++;
			}
		}
		if(row < Size-2)
		{
			if(World[row+1][col] == 1 && World[row+2][col] == 1)
			{
				rc ++;
			}
		}
		return(rc);
	}
	
		
	public static int jump(int row, int col, int dir)
	{
		int rc = 0;
		if (World[row][col] == 2)// if the current element is a peg
		{
			if (dir == right)//peg was create by a right jump
			{
				if(col < 2)
				{
					rc = 0;
				}
				else
				if (World[row][col-1] != 1 || World[row][col-2] != 1)
				{
					rc = 0;
				}
				else
				if(World[row][col-1] == 1 && World[row][col-2] == 1)
				{
					World[row][col-1] = 2;
					World[row][col-2] = 2;
					World[row][col] = 1;
					rc = 1;
				}
			}
			else
			if (dir == left)//left jump
			{
				if(col > Size-3)
				{
					rc = 0;
				}
				else
					if (World[row][col+1] != 1 || World[row][col+2] != 1)
					{
						rc = 0;
					}
					else
					if(World[row][col+1] == 1 && World[row][col+2] == 1)
					{
						World[row][col+1] = 2;
						World[row][col+2] = 2;
						World[row][col] = 1;
						rc = 2;
					}
			}
			else
			if (dir == down)//down jump
			{
				if(row < 2)
				{
					rc = 0;
				}
				else
				if (World[row-1][col] != 1 || World[row-2][col] != 1)
				{
					rc = 0;
				}
				else
				if(World[row-1][col] == 1 && World[row-1][col] == 1)
				{
					World[row-1][col] = 2;
					World[row-2][col] = 2;
					World[row][col] = 1;
					rc = 3;
				}
			}
			else
			if (dir == up)//up jump
			{
				if(row > Size-3)
				{
					rc = 0;
				}
				else
				if (World[row+1][col] != 1 || World[row+2][col] != 1)
				{
					rc = 0;
				}
				else
				if(World[row+1][col] == 1 && World[row+2][col] == 1)
				{
					World[row+1][col] = 2;
					World[row+2][col] = 2;
					World[row][col] = 1;
					rc = 4;
				}
			}
		}
		if(rc > 0)
		{
			Moves[0][Count] = row;
			Moves[1][Count] = col;
			Moves[2][Count] = dir;
			Count ++;
			if (Count > MaxCount)
			{
				MaxCount = Count;
				System.out.format("Max Count = %d      backtrack = %d\n", MaxCount, BackCount);
			}
		}
		return(rc);
	}
	
	public static void undo(int row, int col, int dir)
	{
		BackCount++;
		if (dir == right)
		{
			World[row][col-1] = 1;
			World[row][col-2] = 1;
			World[row][col] = 2;
		}
		else
		if (dir == left)
		{
			World[row][col+1] = 1;
			World[row][col+2] = 1;
			World[row][col] = 2;
		}
		else
		if (dir == down)
		{
			World[row-1][col] = 1;
			World[row-2][col] = 1;
			World[row][col] = 2;
		}
		else
		if (dir == up)
		{
			World[row+1][col] = 1;
			World[row+2][col] = 1;
			World[row][col] = 2;
		}
		Count --;
		Moves[0][Count] = 0;
		Moves[1][Count] = 0;
		Moves[2][Count] = 0;
		
	}
	
	public static int check()
	{//the Solved puzzle will have 0s or 2s in every element, except for the starting point which needs to be a 1
	 // returns 0 if Solved and n for every unsolved peg 
		Solved = 0;
		for (int row = 0; row < Size; row++)
		{
			for (int col = 0; col < Size; col++)
			{
				if (row == SR && col == SC)
				{
					if(World[row][col] != 1)
					{
						Solved ++;
					}
				}
				else
				{
					if (World[row][col] == 1)
					{
						Solved ++;
					}
				}
			}
		}
		return(Solved);
	}

	public static void doNextJump(int row, int col)
	{
		int rc = 0;
		int dir = 1;
		int pos = 0;
		int restart = 0;
		while (row < Size && col < Size && Solved > 0)
		{
			dir = 1;
			pos = countPosMoves(row, col);
			while(rc == 0 && dir < 5)
			{
				rc = jump(row, col, dir);
				if (rc > 0)
				{
					Moves[3][Count-1] = pos; 
				}
				else
				{
					dir++;
				}
			}
			col++;
			if (col == Size)
			{
				col = 0;
				row++;
			}
		}
		//printGrid(World);
		Solved = check();
		if (Solved > 0)
		{
			if(rc == 0 && row == Size)//no move was possible
			{
				pos = Moves[3][Count-1];
				while(pos < 2 || rc == 0)
				{//backtrack till move is found that had more than 1 possibility
					row = Moves[0][Count-1];
					col = Moves[1][Count-1];
					dir = Moves[2][Count-1];
					pos = Moves[3][Count-1];
					undo(row, col, dir);
					//printGrid(World);
					if(pos > 1)
					{
						rc = 0;
						while(rc == 0 && dir < 5)
						{
							dir++;
							rc = jump(row, col, dir);
						}
						if(rc == 0)
						{//no possible move so undo current move
							row = Moves[0][Count-1];
							col = Moves[1][Count-1];
							dir = Moves[2][Count-1];
							pos = Moves[3][Count-1];
							undo(row, col, dir);
							//printGrid(World);
							if(col < Size-1)
							{
								restart = 1;
								doNextJump(row, col+1);
							}
							else
							if(row < Size-1)
							{
								restart = 1;
								doNextJump(row+1, 0);
							}
						}
					}
				}
			}
			if(restart == 0)
			{
				doNextJump(0,0);
			}
		}
	}
	public static void main(String args[])
	{
		World[SR][SC] = 2;
		doNextJump(0,0);
		if(Solved == 0)
		{
			System.out.format("The problem was solved in %d moves after %d backtracks", Count, BackCount);
		}
		
	}
}
