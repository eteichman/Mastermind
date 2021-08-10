package game;
import java.util.Random;

public class RegRandom implements IRandomValueGenerator{
	
	private Random rand;
	
	public RegRandom()
	{
		rand=new Random();
	}
		
	public Integer[] generateRandomArray(int length)
	{
		Integer[] nums=new Integer[length];
		for(int i=0; i<length; i++)
		{
			nums[i]=rand.nextInt(6)+1;
		}
		
		return nums;
	}
}
