import java.util.Arrays;
import java.util.Formatter;

import javax.swing.Spring;

/**
 * A big number class that can handle natural numbers bigger than
 * 2^32, which cannot be handled by a standard int variable.
 * <p>The DecimalBigInt is stored in an int array in little endian
 * format with each digit contating at most 9 decimal digits.
 * </p><p>
 * @author tylergaugler
 *</p>
 */


public class DecimalBigInt implements Comparable<DecimalBigInt>{
	
	/**
	 * The base of each of the digits in the array 
	 *will be 10^9 
	 */
	public final static int RADIX= 1000000000;
	
	/**
	 * The number of digits of the decimal number fitting
	 * into one DecimalBigInt digit
	 */
	public final static int RADIX_DECIMAL_DIGITS=9;
	
	/**
	 * array to store decimal digits
	 */
	private int[] digits;
	
	  public final static DecimalBigInt ZERO = new DecimalBigInt();
	  public final static DecimalBigInt ONE = new DecimalBigInt(1);
	  
	  /**
	   * creates a DecimalBigInt from an array of ints. First, finds the number
	   * of leading zeros and then instantiates the index of {@link digits} to 
	   * the first non zero to the length of the inputted array.
	   * @param digits array of values, each between 0 (inclusive) 
	   * and {@link Radix} (exclusive)
	   */
	  
	  public DecimalBigInt(int... digits){
		  int ZeroCount=0;
		  boolean zero=true;
		  
		  for(int digit : digits){
			  if(digit < 0 || digit >= RADIX){
				  throw new IllegalArgumentException(digit+" is out of bounds!");
			  }
			  
			  if(zero){
				  if(digit != 0){
					  zero=false;
				  }
				  else {
				  ZeroCount++;
			  }
			 }
			  
		 }
		 this.digits= Arrays.copyOfRange(digits,ZeroCount,digits.length);
	 }
	 

	  /**
	   * String converter to help debug
	   */
	  
	  public String toString(){
	
		  return "Big: "+ Arrays.toString(digits);
	
}
	  
	  
	  
	  //changes
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  

}
