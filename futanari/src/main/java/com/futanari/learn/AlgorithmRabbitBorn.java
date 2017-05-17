package com.futanari.learn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TotooriaHyperion on 2017/5/17.
 */
public class AlgorithmRabbitBorn {

	public static void main(String[] args) {
		// 以对为单位
		// 3 month old rabbit(a pare) can create another pare.
		// lifetime of rabbits is 2 years
		Long[] init = new Long[]{1L,0L,0L};
		for(int i=1;i<=26;i++) {
			System.out.println(allRabbit(init,i));
			System.out.println(calculateFibonacci(i));
		}
		System.out.println(allRabbit(init,60));
		System.out.println(calculateFibonacci(60));
		// 4 month old rabbit(a pare) can create another pare.
		// lifetime of rabbits is 2 years
		Long[] init2 = new Long[]{1L,0L,0L,0L};
		for(int i=1;i<=26;i++) {
			System.out.println(allRabbit4array(init2,i));
		}
		System.out.println(allRabbit4array(init2,100));
//		System.out.println(allRabbit4array(init2,100));
		Long[][] a = new Long[][]{{1L,1L},{0L,1L}};
		printMatrix(a);
		Long[][] ret = matrixMultiply(a,a);
		printMatrix(ret);
		Long[][] rabbitMatrix = generateRabbitMatrix(24,4);
//		printMatrix(rabbitMatrix);
		for(int i=1;i<=26;i++) {
			System.out.println(calculateRabbitsUseMatrix(rabbitMatrix,i));
		}

		//test matrixPow
		for(int i=1;i<10;i++) {
			printMatrix(matrixPow(a,i));
		}

		for(int i=1;i<=26;i++) {
			System.out.println(calculateRabbitsUseMatrixLogn(rabbitMatrix,i));
		}

	}

	private static Long allRabbit(Long[] start,int time) {
		Long[] prev = start;
		Long[] next = new Long[3];
		List<Long> toDeath = new ArrayList<>();
		if (time == 1) {
			next = prev;
			toDeath.add(prev[0]);
		} else {
			for(int i=1;i<time;i++) {
				next = nextMonthRabbit(prev);
				toDeath.add(prev[0]);
				int key = i + 1 - 24;
				if (key > 0) {
					next[2] -= toDeath.get(key - 1);
				}
				prev = next;
			}
		}
		Long sum = 0L;
		for(Long a : next) {
			sum += a;
		}
		return sum;
	}

	private static Long[] nextMonthRabbit(Long[] prev) {
		Long[] next = new Long[3];
		// a->b b->c+a c->c+a
		// _a = b + c, _b = a, _c = b + c
		// prev = a+b+c;
		// next = b+c + a + b+c = a+b+c + b+c = prev + b+c = prev + ^a(ie:old)
		next[0] = prev[1] + prev[2];
		next[1] = prev[0];
		next[2] = prev[1] + prev[2];
		return next;
	}


	private static int calculateFibonacci(int n) {
		return calculateFibonacci(n,1,1);
	}

	private static int calculateFibonacci(int n,int t1,int t2) {
		if (n == 1) {
			return t1;
		} else if (n == 2) {
			return t2;
		} else {
			return calculateFibonacci(n-1,t2,t1+t2);
		}
	}

	private static Long allRabbit4array(Long[] start, int time) {
		Long[] prev = start;
		Long[] next = new Long[4];
		List<Long> toDeath = new ArrayList<>();
		if (time == 1) {
			next = prev;
			toDeath.add(prev[0]);
		} else {
			for(int i=1;i<time;i++) {
				next = rabbit4array(prev);
				toDeath.add(prev[0]);
				int key = i + 1 - 24;
				if (key > 0) {
					next[3] -= toDeath.get(key - 1);
				}
				prev = next;
			}
		}
		Long sum = 0L;
		for(Long a : next) {
			sum += a;
		}
		return sum;
	}

	// a->b b->c c->d+a d->d+a
	// _a = c+d,_b = a,_c = b,_d = c+d
	// prev = a+b+c+d
	// next = c+d + a + b + c+d = prev + c+d
	// next2 = (b+c+d) + (c+d) + (a) + (b+c+d)
	//       = a+b+c+d + b+c+d + c+d
	//       = prev + c+d + b+c+d
	//       = next + b+c+d
	//       = prev + next - a
	private static Long[] rabbit4array(Long[] prev) {
		Long[] next = new Long[4];
		next[0] = prev[2] + prev[3];
		next[1] = prev[0];
		next[2] = prev[1];
		next[3] = prev[2] + prev[3];
		return next;
	}

	private static Long[][] matrixMultiply(Long[][] a,Long[][] b) {
		int row = a.length;
		int col = b[0].length;
		int aCol = a[0].length;
		int bRow = b.length;
		if (aCol != bRow) {
			return null;
		}
		Long[][] ret = new Long[row][col];
		for(int i=0;i<row;i++) {
			for(int j=0;j<col;j++) {
				Long aSum = 0L;
				for(int k=0;k<aCol;k++) {
					aSum += a[i][k]*b[k][j];
				}
				ret[i][j] = aSum;
			}
		}
		return ret;
	}

	private static Long[][] matrixRowToCol(Long[] a) {
		Long[][] ret = new Long[a.length][1];
		for(int i=0;i<a.length;i++) {
			ret[i][0] = a[i];
		}
		return ret;
	}

	private static void printMatrix(Long[][] ret) {
		if (ret != null) {
			System.out.println("[");
			for (int i=0;i<ret.length;i++) {
				StringBuilder str = new StringBuilder();
				str.append("[");
				for(int j=0;j<ret[i].length;j++) {
					str.append(String.valueOf(ret[i][j])).append(",");
				}
				str.deleteCharAt(str.length() - 1);
				str.append("]");
				System.out.println(str.toString());
			}
			System.out.println("]");
		}
	}

	private static Long[][] generateRabbitMatrix(int n,int m) {
		Long[][] ret = new Long[n][n];
		for(int i=0;i<n - 1;i++) {
			for(int j=0;j<n;j++) {
				if (j - 1 == i) {
					ret[i][j] = 1L;
				} else {
					ret[i][j] = 0L;
				}
			}
		}
		for(int i=0;i<n;i++) {
			if(n - i < m - 1) {
				ret[n - 1][i] = 0L;
			} else {
				ret[n - 1][i] = 1L;
			}
		}
		return ret;
	}

	private static Long sumMatrix(Long[][] matrix) {
		if (matrix == null) {
			return 0L;
		}
		Long sum = 0L;
		for(int i=0;i<matrix.length;i++) {
			Long[] temp = matrix[i];
			for(int j=0;j<temp.length;j++) {
				sum += temp[j];
			}
		}
		return sum;
	}

	private static Long calculateRabbitsUseMatrix(Long[][] rabbitMatrix, int month) {
		Long[] initRow = new Long[rabbitMatrix.length];
		for (int i=0;i<initRow.length;i++) {
			initRow[i] = 0L;
		}
		initRow[initRow.length - 1] = 1L;
		Long[][] initCol = matrixRowToCol(initRow);
		Long[][] ret = initCol;
		for (int i=1;i<month;i++) {
			ret = matrixMultiply(rabbitMatrix,ret);
		}
		return sumMatrix(ret);
	}

	private static Long calculateRabbitsUseMatrixLogn(Long[][] rabbitMatrix, int month) {
		Long[] initRow = new Long[rabbitMatrix.length];
		for (int i=0;i<initRow.length;i++) {
			initRow[i] = 0L;
		}
		initRow[initRow.length - 1] = 1L;
		Long[][] init = matrixRowToCol(initRow);
		if (month == 1) {
			return sumMatrix(init);
		}
		Long[][] finalMatrix = matrixPow(rabbitMatrix,month - 1);
		Long[][] ret = matrixMultiply(finalMatrix,init);
		return sumMatrix(ret);
	}

	private static Long[][] matrixPow(Long[][] rootMatrix,Integer pow) {
		String[] binStr = Integer.toBinaryString(pow).split("");
		Long[][][] matrixs = new Long[binStr.length][rootMatrix.length][rootMatrix[0].length];
		Long[][] ret = null;
		for (int i=0;i<binStr.length;i++) {
			if (i == 0) {
				matrixs[0] = rootMatrix;
			} else {
				matrixs[i] = matrixMultiply(matrixs[i-1],matrixs[i-1]);
			}
			if ("1".equals(binStr[binStr.length - 1 - i])) {
				if (ret == null) {
					ret = matrixs[i];
				} else {
					ret = matrixMultiply(ret,matrixs[i]);
				}
			}
		}
		return ret;
	}

}
