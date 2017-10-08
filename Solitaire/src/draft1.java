public class draft1 
{
	static int World[][] = {{0,0,1,1,1,0,0},{0,0,1,1,1,0,0},{1,1,1,1,1,1,1},{1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1},{0,0,1,1,1,0,0},{0,0,1,1,1,0,0}};
	// 0 is out of bound, 1 is empty peg, 2 is a peg
	static int Size = World.length;
	static int Start[] = {3,3};
	static int SR = Start[0]; //row
	static int SC = Start[1]; //column
	static int Moves[][] = new int[3][Size*Size]; //store row, col and dir, for each move, on every possible element
	static int Count = 0;
	static int Solved = 1;//0 means solved
	
	public static void printGrid(int[][] World)
	{
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
	}
	
	
	public static int jump(int row, int col, int dir)
	{
		int ret = 0;
		if (World[row][col] == 2)// if the current element is a peg
		{
			if (dir == 1)//peg was create by a right jump
			{
				if(col < 2)
				{
					ret = 0;
				}
				else
				if (World[row][col-1] != 1 || World[row][col-2] != 1)
				{
					ret = 0;
				}
				else
				{
					World[row][col-1] = 2;
					World[row][col-2] = 2;
					World[row][col] = 1;
					ret = 1;
				}
			}
			else
			if (dir == 2)//left jump
			{
				if(col > Size-3)
				{
					ret = 0;
				}
				else
					if (World[row][col+1] != 1 || World[row][col+2] != 1)
					{
						ret = 0;
					}
					else
					{
						World[row][col+1] = 2;
						World[row][col+2] = 2;
						World[row][col] = 1;
						ret = 2;
					}
			}
			else
			if (dir == 3)//down jump
			{
				if(row < 2)
				{
					ret = 0;
				}
				else
				if (World[row-1][col] != 1 || World[row-2][col] != 1)
				{
					ret = 0;
				}
				else
				{
					World[row-1][col] = 2;
					World[row-2][col] = 2;
					World[row][col] = 1;
					ret = 3;
				}
			}
			else
			if (dir == 4)//up jump
			{
				if(row > Size-3)
				{
					ret = 0;
				}
				else
				if (World[row+1][col] != 1 || World[row+2][col] != 1)
				{
					ret = 0;
				}
				else
				{
					World[row+1][col] = 2;
					World[row+2][col] = 2;
					World[row][col] = 1;
					ret = 4;
				}
			}
		}
		if(ret > 0)
		{
			Moves[0][Count] = row;
			Moves[1][Count] = col;
			Moves[2][Count] = dir;
			Count ++;
		}
		return(ret);
	}
	
	public static void undo(int row, int col, int dir)
	{
		if (dir == 1)
		{
			World[row][col-1] = 1;
			World[row][col-2] = 1;
			World[row][col] = 2;
		}
		else
		if (dir == 2)
		{
			World[row][col+1] = 1;
			World[row][col+2] = 1;
			World[row][col] = 2;
		}
		else
		if (dir == 3)
		{
			World[row-1][col] = 1;
			World[row-2][col] = 1;
			World[row][col] = 2;
		}
		else
		if (dir == 4)
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
		for (int row = 0; row<Size; row++)
		{
			for (int col=0; col<Size; col++)
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

	public static int doNextJump()
	{
		int rc = 0;
		int row = 0;
		int col = 0;
		int dir = 1;
		int moved = 0;
		while (row < Size && col < Size && moved == 0)
		{
			dir = 1;
			while(rc == 0 && dir < 5)
			{
				rc = jump(row, col, dir);
				if (rc > 0)
				{
					moved = 1;
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
		printGrid(World);
		Solved = check();
		if (Solved > 0)
		{
			if(rc == 0 && row == Size)//no move was possible
			{
				row = Moves[0][Count-1];//count is number of moves, we want index in list
				col = Moves[1][Count-1];
				dir = Moves[2][Count-1];
				undo(row, col, dir);
				while(dir == 4)
				{
					row = Moves[0][Count-1];
					col = Moves[1][Count-1];
					dir = Moves[2][Count-1];
					undo(row, col, dir);
				}
				if(dir < 4)
				{
					dir++;
					rc = jump(row, col, dir);
					while(rc == 0 && dir < 4)
					{
						dir++;
						rc = jump(row, col, dir);
					}
					if (rc == 0 && dir == 4)
					{
						while(dir == 4)
						{
							row = Moves[0][Count-1];
							col = Moves[1][Count-1];
							dir = Moves[2][Count-1];
							undo(row, col, dir);
						}
												
					}
				}
			}
			doNextJump();
		}
		return(rc);
	}
	public static void main(String args[])
	{
		World[SR][SC] = 2;
		doNextJump();
	}
}
