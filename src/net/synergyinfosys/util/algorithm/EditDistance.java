package net.synergyinfosys.util.algorithm;

import java.util.LinkedList;
import java.util.List;


/**
 * 
 * Solve via dynamic programming
 * 
 * 
 * 
 * Here is state transform function:
 * d('', '') = 0               -- '' = empty string
 * d(s, '')  = d('', s) = |s|  -- i.e. length of s
 * d(s1+ch1, s2+ch2)
 *	  = min( d(s1, s2) + if ch1=ch2 then 0 else 1 fi,
 *	         d(s1+ch1, s2) + 1,
 *	         d(s1, s2+ch2) + 1 )
 *
 *
 * at matrix:
 *   up    | is delete
 *   angel \ if same value then no change else update
 *   left  - is insert
 * @author ade
 *
 */
public class EditDistance {
	
	public static LinkedList<EditOperation> getEditDistance(String s, String t) {
		String[] sArr = s.split("");
		String[] tArr = t.split("");
		
		return getEditDistance( sArr, tArr);
	}

	public static LinkedList<EditOperation> getEditDistance(Object[] s, Object[] t) {
		
		System.out.println("Calculating edit distance:");
		System.out.println("From String:\t");
		for( Object ss: s ){
			System.out.print( "-" + ss );
		}
		System.out.println();
		System.out.println("To String:\t");
		for( Object ss: t ){
			System.out.print( "-" + ss );
		}
		System.out.println();
		int m = s.length;//s.length();
		int n = t.length;//t.length();

		//initial the matrix
		int[][] d = new int[m + 1][n + 1];
		for (int i = 0; i <= m; i++) {
			d[i][0] = i;
		}
		for (int j = 0; j <= n; j++) {
			d[0][j] = j;
		}
		
		//fill the matrix via state transform function
		for (int j = 1; j <= n; j++) {
			for (int i = 1; i <= m; i++) {
				if ( s[i - 1].equals(t[j - 1]) ) {//(s.charAt(i - 1) == t.charAt(j - 1)) {
					d[i][j] = d[i - 1][j - 1];
				} else {
					d[i][j] = Util.min((d[i - 1][j] + 1), (d[i][j - 1] + 1), (d[i - 1][j - 1] + 1));
				}
			}
		}	
		
		Util.printMatrix(d, s, t);
		
		//find the path and edit operation
		int i=m, j=n;
		int iMinus, jMinus;
		int distance, min;
		
		LinkedList<EditOperation> diff = new LinkedList<EditOperation>();
		
		while( i>0 && j>0 ){
			distance = d[i][j];
			min = Integer.MAX_VALUE;
			
			System.out.print( distance + "\t" );
			
			System.out.print(s[i-1] + "\t" + t[j-1] + "\t");
			
			iMinus = jMinus = 0;
			if( d[i-1][j] < min ){
				min = d[i-1][j];
				iMinus = -1;
				jMinus = 0;
			}
			
			if( d[i][j-1] < min ){
				min = d[i][j-1];
				iMinus = 0;
				jMinus = -1;
			}
			
			if( d[i-1][j-1] <= min ){
				min = d[i-1][j-1];
				iMinus = -1;
				jMinus = -1;
			}
			
			if( iMinus == -1 && jMinus == -1 ){
				System.out.print("\\ ");
				//due to cost is much higher when comparing two long String, we just compare two integer number here.
				if( d[i-1][j-1] == d[i][j] ){
					System.out.print("no change");
				} else {
					System.out.print("update");
					diff.addFirst(new EditOperation(EditOperator.UPDATE, i-1, String.valueOf(s[i-1]), String.valueOf(t[j-1])));
				}
			} else if (  iMinus == -1 && jMinus == 0  ){
				System.out.print("| delete");
				diff.addFirst(new EditOperation(EditOperator.DELETE, i-1, String.valueOf(s[i-1]), String.valueOf(t[j-1])));
			}  else if (  iMinus == 0 && jMinus == -1  ){
				System.out.print("- insert");
				diff.addFirst(new EditOperation(EditOperator.INSERT, i-1, String.valueOf(s[i-1]), String.valueOf(t[j-1])));
			} 
			
			System.out.println();
			
			i+=iMinus;
			j+=jMinus;
		}
		
		while( i>0 ){
			System.out.print("| delete");
			diff.addFirst(new EditOperation(EditOperator.DELETE, i-1, String.valueOf(s[i-1]), String.valueOf(t[j])));
			i--;
		}
		
		while( j>0 ){
			System.out.print("- insert");
			diff.addFirst(new EditOperation(EditOperator.INSERT, i-1, String.valueOf(s[i]), String.valueOf(t[j-1])));
			j--;
		}
	
		for( EditOperation eo: diff ){
			System.out.println( eo );
		}
		
		//optimize INSERT
		//in this linked list, if there is continuous same operation at same position, combine them!
//		int p=0, q = 0;
//		while(p < diff.size()-1){
//			EditOperation pEO = diff.get(p);
//			
//			q++;
//			EditOperation qEO = diff.get(q);
//			System.out.println(String.format("p=%d, q=%d",  p, q));
//			while( pEO.getOperator() == EditOperator.INSERT && pEO.getOperator() == qEO.getOperator() && pEO.getIndex() == qEO.getIndex()  ){
//				q++;
//				pEO.setTo(pEO.getTo() + qEO.getTo());
//				qEO = diff.get(q);
//			}
//			
//			//combine and remove
//			if( q-p > 2 )
//				diff.subList(p+1, q-1).clear();
//			
//			//set p
//			p = q;
//		}
//		
//		System.out.println("After optimize");
//		for( EditOperation eo: diff ){
//			System.out.println( eo );
//		}
		
		System.out.println("End of edit distance\n");
		return diff;
	}
	
	public static String getMerge( String original, List<EditOperation> diff ){
		String[] sArr = original.split("");
		
		return getMerge( sArr, diff);
	}
	
	public static String getMerge( Object[] original, List<EditOperation> diff ){
		System.out.println("Start Merging...");
		StringBuffer sb = new StringBuffer();
		
		int index = 0;
		int i;
//		int insertIndex = -1;
		int to;
		for( EditOperation eo: diff ){
			//first copy all no-update String
			to = eo.getOperator()==EditOperator.INSERT?eo.getIndex():eo.getIndex()-1;
			for(i=index; i<=to; i++){
				sb.append(original[i]);
			}
			index = i;
			
			//then do operation
			switch( eo.getOperator() ){
			case INSERT:
//				if( insertIndex != eo.getIndex() ){
//					sb.append(original.charAt(index));
//				}
				sb.append(eo.getTo());
//				insertIndex = eo.getIndex();
				break;
			case UPDATE:
				sb.append(eo.getTo());
				break;
			case DELETE:
				break;
			}
			index = eo.getIndex()+1;
			
			System.out.println( "->" + eo.toString() + "\t" +  sb.toString());
		}
		//do the tail stuff
		for( i = index; i<original.length; i++ ){
			sb.append(original[i]);
		}
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		LinkedList<EditOperation> diff = null;
		String from, to;
		from="sitting";
		to = "kitten";
		diff = getEditDistance( from, to );
		System.out.println( ">>>" + getMerge(from, diff) );
		/*
		from="congratulation";
		to="destinations";
		diff = getEditDistance( from, to );
		System.out.println( ">>>" + getMerge(from, diff) );
		
		from="sheep";
		to="sheepbeep";
		diff = getEditDistance( from, to );
		System.out.println( ">>>" + getMerge(from, diff) );
		
		from="fuck";
		to="suck";
		diff = getEditDistance( from, to );
		System.out.println( ">>>" + getMerge(from, diff) );
		
		from="what are you fucking doing";
		to="what the hell is this";
		diff = getEditDistance( from, to );
		System.out.println( ">>>" + getMerge(from, diff) );
		
		from="nn1tx8";
		to="u6kak8u";
		    
		diff = getEditDistance( from, to );
		System.out.println( ">>>" + getMerge(from, diff) );*/
		
		
		from="abc";
		to = "ab";
		diff = getEditDistance( from, to );
		System.out.println( ">>>" + getMerge(from, diff) );
		
		
//		32,5e6o9ppmcx,mwvpe1s2u8	
//	0	1	
//36,1c3c0w04zr,rajg1z41gu	1	1	
//32,5e6o9ppmcx,mwvpe1s2u8	2	1	
//1	32,5e6o9ppmcx,mwvpe1s2u8	32,5e6o9ppmcx,mwvpe1s2u8	| delete
//1	36,1c3c0w04zr,rajg1z41gu	32,5e6o9ppmcx,mwvpe1s2u8	\ update
		String[] from1 = new String[]{ "36,1c3c0w04zr,rajg1z41gu", "32,5e6o9ppmcx,mwvpe1s2u8"};
		String[] to1 = new String[]{ "32,5e6o9ppmcx,mwvpe1s2u8" };
		diff = getEditDistance( from1, to1 );
		System.out.println( ">>>" + getMerge(from1, diff) );
		
		from1 = new String[]{ "a", "b"};
		to1 = new String[]{ "b" };
		diff = getEditDistance( from1, to1 );
		System.out.println( ">>>" + getMerge(from1, diff) );
		
		from1 = new String[]{ "a"};
		to1 = new String[]{ "e", "b", "a" };
		diff = getEditDistance( from1, to1 );
		System.out.println( ">>>" + getMerge(from1, diff) );
	}

}
