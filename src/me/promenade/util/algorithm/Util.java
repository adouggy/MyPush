package me.promenade.util.algorithm;

public class Util {

	public static void printMatrix( int[][] table, Object[] str1, Object[] str2 ){
		int i, j;
		System.out.print( '\t' );
		System.out.print( '\t' );
		for( i=0; i<str2.length; i++ ){
			System.out.print(str2[i]);
			System.out.print('\t');
		}
		System.out.println();
		
		for( i=0; i<table.length; i++ ){
			if( i>0 ){
				System.out.print(str1[i-1]);
			}
			System.out.print('\t');
			for( j=0; j<table[i].length; j++ ){
				System.out.print( table[i][j]);
				System.out.print('\t');
			}
			System.out.println();
		}
	}
	
	public static int min(int a, int b, int c) {
		return (Math.min(Math.min(a, b), c));
	}

}
