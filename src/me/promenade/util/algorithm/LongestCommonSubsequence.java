package me.promenade.util.algorithm;

/**
 * implement algorithm longest common subsequence via dynamic programming
 * 
 * @author ade
 * @category algorithm
 */
public class LongestCommonSubsequence {

	/**
	 * Using dynamic programming
	 * 
	 * state transform function:
	 * lcs(str1, str2) = 
	 * 	1)str1[0] + lcs(tail(str1), tail(str2)) if str1[0] = str2[0]
	 *  2)max{lcs(str1, tail(str2)), lcs(tail(str1), str2)} if str1[0] != str2[0]
	 * 
	 * time: O(mn)
	 * space: O(mn)
	 * 
	 * @param str1
	 * @param str2
	 * @return longest common subsequece of input two string
	 */
	public static String lcs(String str1, String str2){
		if( str1 == null || str2 == null || str1.length()==0 || str2.length() == 0 ){
			throw new IllegalArgumentException();
		}
		
		StringBuffer common = new StringBuffer();
		
		int[][] table = new int[str1.length()+1][str2.length()+1];
		
		int i, j;
		
		for( i=0; i<str1.length(); i++ ){
			table[i][0] = 0;
		}
		
		for( j=0; j<str2.length(); j++ ){
			table[0][j] = 0;
		}
		
//		printMatrix( table, str1, str2);
	
		for( i=1; i<=str1.length(); i++ )
			for( j=1; j<=str2.length(); j++ ){
				if( str1.charAt(i-1) == str2.charAt(j-1) ){
					table[i][j] = table[i-1][j-1] + 1;
				}else{
					table[i][j] = table[i-1][j-1];
				}
			}
		
		//Util.printMatrix( table, str1, str2);
		
		return common.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		lcs( "ebca", "fabc" );
	}

}
