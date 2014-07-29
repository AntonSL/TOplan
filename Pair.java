/**
 * Just a pair of ints to store coordinates or anything else
 * that is represented by pair of ints
 * @author troy
 *
 */
class Pair {
	
	private int x=-1;
	private int y=-1;
	
	Pair(){};
	
	Pair(int x, int y)
	{
		this.x=x;
		this.y=y;
	}
	
	void setX(int x)
	{
		this.x=x;
	}
	
	void setY(int y)
	{
		this.y=y;
	}
	
	int getX()
	{
		return this.x;
	}
	
	int getY()
	{
		return this.y;
	}

}
