package game;

public class MockRandom implements IRandomValueGenerator{
	
	private Integer[] nums;

	
	public MockRandom()
	{
	}

	public void initializeArray(int length)
	{
		nums=new Integer[length];
	}
	
	public Integer[] generateRandomArray(int length)
	{		
		return nums;
	}
	
	public void setNums(Integer[] nums2)
	{
		this.nums=nums2;
	}
}
