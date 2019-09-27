package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer
 * with any number of digits, which overcomes the computer storage length
 * limitation of an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;

	/**
	 * Number of digits in this integer
	 */
	int numDigits;

	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as: 5 --> 3 --> 2
	 * 
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 * 5 --> 3 --> 2 (No zeros after the last 2)
	 */
	DigitNode front;

	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}

	/**
	 * Parses an input integer string into a corresponding BigInteger instance. A
	 * correctly formatted integer would have an optional sign as the first
	 * character (no sign means positive), and at least one digit character
	 * (including zero). Examples of correct format, with corresponding values
	 * Format Value +0 0 -0 0 +123 123 1023 1023 0012 12 0 0 -123 -123 -001 -1 +000
	 * 0
	 * 
	 * Leading and trailing spaces are ignored. So " +123 " will still parse
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12 345" will not parse as an
	 * integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the
	 * BigInteger constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) throws IllegalArgumentException {

		// Check for empty string input
		if ("".equals(integer)) {
			throw new IllegalArgumentException();
		}

		BigInteger intList = new BigInteger();

		// If integer has non integer values, throw exception

		// Check if integer is negative or positive
		if (integer.charAt(0) == '-') {
			intList.negative = true;
		} else {
			intList.negative = false;
		}

		int zeroCounter = 0;
		intList.front = null;

		// Count the leading zeros
		try {
			while (zeroCounter < integer.length() && integer.charAt(zeroCounter) == '0' || integer.charAt(zeroCounter + 1) == '0' && (integer.charAt(zeroCounter) == '+' || integer.charAt(zeroCounter) == '-')) {
						zeroCounter++;
			}
		}
		catch(Exception e) {
		}

		integer = integer.substring(zeroCounter, integer.length()); // If integer has leading zeros, delete them

		// Create the LinkedList
		for (int i = 0; i < integer.length(); i++) {
			if (integer.charAt(i) != '+' && integer.charAt(i) != '-') {
				if (Character.getNumericValue((integer.charAt(i))) < 0 || Character.getNumericValue((integer.charAt(i))) > 9)
					throw new IllegalArgumentException();
				intList.front = new DigitNode(Character.getNumericValue((integer.charAt(i))), intList.front);
				intList.numDigits++;
			}
			else {
				if(i != 0)
					throw new IllegalArgumentException();
			}
		}
		return intList;
	}

	/**
	 * Adds the first and second big integers, and returns the result in a NEW
	 * BigInteger object. DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative. (Which
	 * means this method can effectively subtract as well.)
	 * 
	 * @param first  First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		BigInteger result = new BigInteger();
		DigitNode ptr1 = first.front;
		DigitNode ptr2 = second.front;
		DigitNode resultptr = result.front;
		int carry = 0;
		int borrow = 0;
		boolean firstgreater = false;
		if (first.front == null)
			return result = second;
		if (second.front == null)
			return result = first;
		if (!first.negative && !second.negative) {
			while (ptr1 != null || ptr2 != null) {
				if (result.front == null) {
					if (ptr1.digit + ptr2.digit >= 10) {
						result.front = new DigitNode((ptr1.digit + ptr2.digit) - 10, null);
						resultptr = result.front;
						carry++;
					} else {
						result.front = new DigitNode(ptr1.digit + ptr2.digit + carry, null);
						resultptr = result.front;
						carry = 0;
					}
				} else {
					if (ptr1 == null) {
						resultptr.next = new DigitNode(ptr2.digit + carry, null);
						resultptr = resultptr.next;
						carry = 0;
					} else if (ptr2 == null) {
						resultptr.next = new DigitNode(ptr1.digit + carry, null);
						resultptr = resultptr.next;
						carry = 0;
					} else {
						if (ptr1.digit + ptr2.digit >= 10) {
							resultptr.next = new DigitNode((ptr1.digit + ptr2.digit) - 10, null);
							resultptr = resultptr.next;
							carry++;
						} else {
							resultptr.next = new DigitNode(ptr1.digit + ptr2.digit + carry, null);
							resultptr = resultptr.next;
							carry = 0;
						}
					}
				}
				if (ptr1 != null)
					ptr1 = ptr1.next;
				if (ptr2 != null)
					ptr2 = ptr2.next;
			}
			if (carry != 0) {
				resultptr.next = new DigitNode(carry, null);
			}
			return result;
		}
		// negatives
		else {
			if (first.numDigits > second.numDigits) {
				firstgreater = true;
			} else if (first.numDigits < second.numDigits) {
				firstgreater = false;
			} else {
				DigitNode negptr1 = first.front;
				DigitNode negptr2 = second.front;
				while (negptr1 != null && negptr2 != null) {
					if (negptr1.digit > negptr2.digit) {
						firstgreater = true;
					} else if (negptr1.digit < negptr2.digit) {
						firstgreater = false;

					}
					negptr1 = negptr1.next;
					negptr2 = negptr2.next;
				}
			}
			DigitNode greaterptr = null;
			DigitNode lessptr = null;
			if (firstgreater) {
				greaterptr = first.front;
				lessptr = second.front;
				result.negative = first.negative;
			} else {
				greaterptr = second.front;
				lessptr = first.front;
				result.negative = second.negative;
			}

			while (greaterptr != null || lessptr != null) {
				if (result.front == null) {
					if (greaterptr.digit - lessptr.digit < 0) {
						if (first.negative && second.negative) {
							result.front = new DigitNode((greaterptr.digit + lessptr.digit) - 10, null);
							borrow++;
						} else {
							if (greaterptr.digit == 0) {
								if (borrow != 0) {
									result.front = new DigitNode(9 - lessptr.digit, null);
									borrow = 0;
								} else {
									result.front = new DigitNode((greaterptr.digit - lessptr.digit) + 10, null);
									borrow++;
								}
							} else {
								result.front = new DigitNode((greaterptr.digit - lessptr.digit) + 10, null);
								borrow++;
							}
						}
						resultptr = result.front;
					} else {
						if (first.negative && second.negative) {
							if (greaterptr.digit + lessptr.digit >= 10) {
								result.front = new DigitNode((greaterptr.digit + lessptr.digit) - 10, null);
								borrow++;
							} else {
								result.front = new DigitNode(greaterptr.digit + lessptr.digit + borrow, null);
								borrow = 0;
							}
						} else {
							result.front = new DigitNode(greaterptr.digit - lessptr.digit - borrow, null);
							borrow = 0;
						}
						resultptr = result.front;

					}
				} else {
					if (greaterptr == null) {
						if (lessptr.digit == 0) {
							resultptr.next = new DigitNode(9, null);
							borrow++;
						} else {
							resultptr.next = new DigitNode(lessptr.digit - borrow, null);
							borrow = 0;
						}
						resultptr = resultptr.next;
					} else if (lessptr == null) {
						if (greaterptr.digit == 0) {
							resultptr.next = new DigitNode(9, null);
							borrow++;
						} else {
							resultptr.next = new DigitNode(greaterptr.digit - borrow, null);
							borrow = 0;
						}
						resultptr = resultptr.next;
					} else {
						if (greaterptr.digit - lessptr.digit < 0) {
							if (first.negative && second.negative) {
								if (greaterptr.digit + lessptr.digit >= 10) {
									resultptr.next = new DigitNode((greaterptr.digit + lessptr.digit) - 10, null);
									borrow++;
								} else {
									resultptr.next = new DigitNode(greaterptr.digit + lessptr.digit + borrow, null);
									borrow = 0;
								}
							} else {
								if (greaterptr.digit == 0) {
									if (borrow != 0) {
										resultptr.next = new DigitNode(9 - lessptr.digit, null);
										borrow = 0;
									} else {
										resultptr.next = new DigitNode((greaterptr.digit - lessptr.digit) + 10, null);
										borrow++;
									}
								} else {
									if (borrow != 0) {
										if (greaterptr.digit - lessptr.digit - borrow < 0) {
											resultptr.next = new DigitNode(
													(greaterptr.digit - lessptr.digit) + 10 - borrow, null);
										} else {
											resultptr.next = new DigitNode(
													(greaterptr.digit - lessptr.digit) + 10 - borrow, null);
											borrow = 0;
										}
									} else {
										resultptr.next = new DigitNode((greaterptr.digit - lessptr.digit) + 10, null);
										borrow++;
									}
								}
							}
							resultptr = resultptr.next;
						} else {
							if (first.negative && second.negative) {
								if (greaterptr.digit + lessptr.digit >= 10) {
									resultptr.next = new DigitNode((greaterptr.digit + lessptr.digit) - 10, null);
									borrow++;
								} else {
									resultptr.next = new DigitNode(greaterptr.digit + lessptr.digit + borrow, null);
									borrow = 0;
								}

							} else {
								if (greaterptr.digit - lessptr.digit - borrow < 0) {
									resultptr.next = new DigitNode((greaterptr.digit - lessptr.digit) + 10 - borrow,
											null);
								} else {
									resultptr.next = new DigitNode(greaterptr.digit - lessptr.digit - borrow, null);
									borrow = 0;
								}
							}
							resultptr = resultptr.next;

						}
					}

				}
				if (greaterptr != null)
					greaterptr = greaterptr.next;
				if (lessptr != null)
					lessptr = lessptr.next;
			}
			if (borrow != 0) {
				resultptr.next = new DigitNode(borrow, null);
			}
			

			return result;
		}

	}

	/**
	 * Returns the BigInteger obtained by multiplying the first big integer with the
	 * second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first  First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big
	 *         integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {

		/* IMPLEMENT THIS METHOD */

		// following line is a placeholder for compilation
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
			retval = curr.digit + retval;
		}

		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
}
