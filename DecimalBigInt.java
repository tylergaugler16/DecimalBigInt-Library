import java.util.Arrays;
import java.util.Formatter;

import javax.swing.Spring;

/**
 * A big number class that can handle natural numbers bigger than
 * 2^32, which cannot be handled by a standard int variable.
 * <p>The DecimalBigInt is stored in an int array in little endian
 * format with each digit contating at most 9 decimal digits. Methods include:
 * addition, multiplication and a comparator. Division and subtraction 
 * still aren't available.
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
			  
			  //removes leading zeros
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
	  /**
	   * Converts a string in decimal to DecimalBigInt
	   * @param decimal string of decimal number
	   * @throws NumberFormatException if the number 
	   * is not in the correct format. ex: contains any characters
	   * outside of 0-9.
	   */
	  
	  public static DecimalBigInt valueOf(String decimal){
		  //length of the number
		  int decimal_length= decimal.length();
		  //number of "blocks" needed to store in DecimalBigInt format
		  int BigInt_length= ((decimal_length-1)/RADIX_DECIMAL_DIGITS) +1;
		   //to find number of digits in first block
		  int firstSome= decimal_length-(BigInt_length-1) * RADIX_DECIMAL_DIGITS;
		  
		 
		  int[] digits= new int[BigInt_length];
		  
		  for(int i=0;i<BigInt_length;i++){
			  
			  String block= 
					  decimal.substring(Math.max(firstSome+ (i-1)*RADIX_DECIMAL_DIGITS,0), firstSome+ i*RADIX_DECIMAL_DIGITS );
			//starting point is: 0, then firstSome, then firstSome+ number of blocks *RADIX_DECIMAL_DIGITS
			//ending point is: firstSome, then firstSome+ number of blocks *RADIX_DECIMAL_DIGITS
			  digits[i]= Integer.parseInt(block);
			//  
		  }
		  
		  return new DecimalBigInt(digits);
		  
		  
	  }
	  
	  /**
	   * Converts a long in decimal to DecimalBigInt
	   * @param number long value of decimal value
	   * @throws IllegalArgumentException if the value
	   * is less than 0
	   */
	  public static DecimalBigInt valueOf(long number){
		  if (number< 0){
			  throw new IllegalArgumentException(number+" is out of bounds!");
		  }
		  if(number == 0L){
			  return ZERO;
		  }
		  if(number == 1L){
			  return ONE;
		  }
		  //largest long is: 2^64, which will fits in RADIX^3
		  //so the most number of DecimalBigInt digits is 3
		  int[] digits= new int[3];
		  //start in the last digit of DecimalBigInt
		  int index=2;
		  
		  while(number > 0){
			  digits[index]=(int) (number%RADIX);
			  number/=RADIX;
			  index--;
			 }
		  return new DecimalBigInt(digits);
		 
	 }
	  
	  
	  /**
	   * takes a string of some arbirtary radix
	   * and converts to a DecimalBigint
	   * 
	   * @param number big endian representation of number 
	   * @param radix the radix used in representation between:
	   * {@link Character#MIN_RADIX} (2, inclusive) and
	   * {@link Character#MAX_RADIX} (36, inclusive).
	   * 
	   */
	  
	  
	  public static DecimalBigInt valueOf(String number, int radix){
		  if(radix < Character.MIN_RADIX || radix> Character.MAX_RADIX){
			  throw new IllegalArgumentException(radix+ "is not a valid radix");
			}
		  DecimalBigInt bigRadix = new DecimalBigInt(radix);
		  DecimalBigInt value= ZERO;
		  
		  for(char digit : number.toCharArray()){
			  //Character.digit() returns the numerical value of 
			  //digit in base radix.
			  int iDigit = Character.digit(digit,radix);
			  if(iDigit < 0){
				  throw new NumberFormatException(digit+ "is not a valid base "+radix+"-digit");
			  }
			 DecimalBigInt bigDigit= new DecimalBigInt(iDigit);
			 value = value.times(bigRadix).plus(bigDigit);
			  
		  }
		  return value;
		  
	  }
	  
	  /**
	   * takes an array of ints of an arbitrary radix
	   * and converts them to a DecimalBigInt
	   * @param digits array of ints reprentation of a number
	   * @param radix the radix used in representation
	   * 
	   */
	  
	  public static DecimalBigInt valueOf(int[] digits,int radix){
		  if(radix < 2){
			  throw new IllegalArgumentException(radix+ " is not a valid radix");
		  }
		  
		DecimalBigInt bigRadix= valueOf(radix);
		DecimalBigInt value = ZERO;
		
		for(int digit: digits){
			if(digit < 0 || radix > digit){
				throw new IllegalArgumentException(digit+" is out of range for base "+radix+"-digit");
			}
			DecimalBigInt bigDigit= DecimalBigInt.valueOf(digit);
			value= value.times(bigRadix).plus(bigDigit);
		}
		return value;
		
		
		  
	  }
	  
	  
	   /**
	   * formats the number as a decimal string
	   * 
	   */
	  public String ToDecimalString(){
		  //strings in java are immutable, so we use StringBuilder class
		  StringBuilder sb= new StringBuilder();
		  //ensures right format of sb
		  Formatter f= new Formatter(sb);
		  //formats the first digit, without including leading zeros
		  f.format("%d" , digits[0]);
		  //formats each other digit putting leading zeros if there are not exactly 9 digits
		  for(int i=1;i<digits.length;i++){
			  f.format("%09d", digits[i]);
		  }
		  
		  return sb.toString();
	 }
	  
	   /**
	   * Calculates the sum
	   * {@code this and that}
	   */
	  
	  public DecimalBigInt plus(DecimalBigInt that){
		  //initializes result array with the size of the highest number array plus 1
		  int[] result = new int[Math.max(this.digits.length,that.digits.length)+1];
		  
		  addDigits(result, result.length-1,this.digits);
		  addDigits(result,result.length-1,that.digits);
		  
		  return new DecimalBigInt(result);
		  
	 }
	  
	   /**
	   * adds all the digits from the addend array to the result array
	   */
	  
	  public void addDigits(int [] result, int resultIndex, int... addend){
		  int addendIndex= addend.length-1;
		  
		  while(addendIndex>=0){
			  addDigit(result, resultIndex, addend[addendIndex]);
			  addendIndex--;
			  resultIndex--;
		  }
		  
	  }
	  
	  /**
	   * adds one digit from addend to the correct digit of the result
	   * if there is a carry, it is recursively added to the next digit
	   * of the result
	   */
	  
	  public void addDigit(int[] result, int resultIndex, int addendDigit){
		  int sum = result[resultIndex]+ addendDigit;
		  //each digit can only hold up to RADIX, so the sum % RADIX is the result and
		  //the sum/RADIX will be the carry, because 
		  result[resultIndex]= sum % RADIX;
		  int carry= sum/RADIX;
		  if(carry > 0){
			  addDigit(result, resultIndex-1, carry);
		  }
		  
	  }
	  /**
	   * calculates the product
	   * {@code this and that}
	   */
	  
	  public DecimalBigInt times(DecimalBigInt that){
		  int[] result= new int[this.digits.length + that.digits.length];
		  multiplyDigits(result, result.length-1, this.digits,that.digits);
		  return new DecimalBigInt(result);
	  }
	  
	  /**
	   * multiplies all digits of two numbers and adds them to result
	   */
	  
	  private void multiplyDigits(int[] result, int resultIndex, int[] leftFactor, int[] rightFactor){
		  for(int i=0;i<leftFactor.length;i++){
			  for(int j=0;j<rightFactor.length;j++){
				  multiplyDigit(result,result.length -(i+j), leftFactor[leftFactor.length-i-1],rightFactor[rightFactor.length-j-1]);
			  }
		  }
	  }
	  
	  
	  
	   /**
	   * multiplies two digits and adds their products to the result array
	   * at the correct position
	   */
	  
	  private void multiplyDigit(int [] result,int resultIndex,int leftFactor,int rightFactor){
		  long product= (long)leftFactor * (long)rightFactor;
		  int produtcDigit= (int) (product % RADIX);
		  int carry= (int)(product/10);
		  
		  addDigits(result, resultIndex, carry, produtcDigit);
		  
		  
		  
	  }
	  
	  
	 /**
	  * first step of short division, E.g. divides a two digit number
	  * by a one- digit number (becaue if we have a two-digit number x with first 
	  * digit smaller than our divisor d, than x / d is a one-digit number, 
	  * and x % d is also a one-digit number, smaller than d, so we only
	  * need to divide two digits by our divisor) 
	  * 
	  * 
	  * @param result the array of the final result of the quotient 
	  * @param resultIndex the index of where to put the answer in the result array
	  * @param dividend the last digit of the dividend
	  * @param lastRemainder the first digit of the dividend (being the
      * 	remainder of the operation one digit to the left).
      *     This must be < divisor
      * @param divisor the divisor
      * @return the remainder of the division operation
      *           
	  */
	  
	  private int divideDigit(int [] result, int resultIndex, int dividend, int lastRemainder, int divisor){
		  //assert throws a runtime error if condition is false
		  assert divisor < RADIX;
		  assert lastRemainder < divisor;
		  
		  long ent = dividend + (long)RADIX * lastRemainder;
		 
		  long quotient= ent/dividend;
		  
		  long remainder= ent % dividend;
		  
		  assert quotient < RADIX;
		  assert remainder < divisor;
		  
		  //puts the quotient into correct DecimalBigInt digit
		  result[resultIndex]=(int)quotient;
		  //returns the remainder
		  return (int)remainder;
	  }
	  
	  /**
	   * short division algorithm
	   * 
	   * @param result the array of the final result of the quotient
	   * @param resultIndex the index of where to put the anwer in the result array
	   * @param dividend the array of the dividend's digits (will only be read, not written to)
	   * @param dividendIndex the index of the dividend array that we should start dividing.continues 
	   * to the end of the array
	   * @param  divisor the divisor, must be smaller thatn {@link #RADIX}
	   * @return the remainder which we be smaller than the {@link divisor} 
	   */
	  
	  private int divideDigits(int[] result, int resultIndex, int[] dividend, int dividendIndex, int divisor){
		  int remainder =0;
		  
		  for(;dividendIndex <dividend.length; dividendIndex++,resultIndex++){
			  remainder = divideDigit(result, resultIndex, dividend[dividendIndex],remainder,divisor);
		}
		  return remainder;
		  
	  }
	  
	  /**
	   * divides this number by a small number
	   * 
	   * @param divisor an integer that is >0 and < {@link #RADIX}
	   * @return the integer part of the quotient (ignores the remainder)
	   * @throws IllegalArgumentException if divisor < 0 or divisor > RADIX
	   */
	  
	 public DecimalBigInt divideBy(int divisor){
		 if(divisor < 0 || divisor > RADIX){
			 throw new IllegalArgumentException(divisor+" is not a valid divisor");
		 }
		 
		 int[] result= new int[digits.length];
		 divideDigits(result,0,digits,0,divisor);
		 return new DecimalBigInt(result);
	 }
	  
	 /**
	  * calculates a hascode for this object
	  */
	  
	 public int hashCode(){
		 int hash=0;
		 for (int digit: digits){
			 hash= hash * 13 + digit;
		 }
		 return hash;
	 }
	 
	 /**
	  * compares this object with another object for equality
	  * they are equal only if they are both DecimalBigInt objects
	  * that represent the same natural number
	  */
	 
	 public boolean equals(Object o){
		 //returns true if o is an instance of DecimalBigInt
		 // and they have the same DecimalBigInt value
		 return o instanceof DecimalBigInt && this.compareTo((DecimalBigInt)o)==0;
	 }
	 
	 /**
	  * compares this {@link DecimalBigInt} to another one
	  * returns -1 if this < that
	  * 		 0 if this == that
	  * 		 1 if this > that
	  */
	 
	 public int compareTo(DecimalBigInt that){
		 if(this.digits.length < that.digits.length){
			 return -1;
		 }
		 if(this.digits.length > that.digits.length){
			 return 1;
		 }
		 
		 //when they are the same length, compare each digit
		 for(int i=0;i<this.digits.length;i++){
			 if(this.digits[i] < that.digits[i]){
				 return -1;
			 }
			 if(this.digits[i] > that.digits[i]){
				 return 1;
			 }
		 }
		 //exact same digits, return 0
		 return 0;
	 }
	 

}
