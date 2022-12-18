package com.codingPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Coding {

	public static void main(String[] args) {
		_1024Game game = new _1024Game();
		System.out.println("starting the 1024 game Press WSDA just like other games");
		_1024Game.playGame();
		System.out.println("Ending Game");
	}

	public int strStr(String haystack, String needle) {
		for (int i = 0; i < haystack.length(); i++) {
			if (haystack.charAt(i) == needle.charAt(0)) {
				boolean notMatch = false;
				for (int j = 0; j < haystack.length(); j++) {
					if (j + i >= needle.length()) {
						break;
					}
					if (haystack.charAt(j + i) != needle.charAt(j)) {
						notMatch = true;
						break;
					}
				}
				if (notMatch == false) {
					return i;
				}
			}
		}
		return -1;
	}

	static class _1024Game {
		static int[][] dp;
		static int[][] tmp;
		static List<Integer> openPositions;
		static final int NOT_POSSIBLE = -2;
		static Scanner scanner = new Scanner(System.in);

		public _1024Game() {
			dp = new int[4][4];
			tmp = new int[4][4];
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					dp[i][j] = -1;
				}
			}

			openPositions = new ArrayList<>();
			for (int i = 0; i < 16; i++) {
				openPositions.add(i);
			}

			int position1 = getOpenPosition();
			putPosition(dp, position1);

			int position2 = getOpenPosition();
			putPosition(dp, position2);

			printPosition(dp);
		}

		private static void printPosition(int[][] matrix) {
			System.out.println("--------------------- ");
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					String v = matrix[i][j] == -1 ? " _ " : " " + matrix[i][j] + " ";
					System.out.print(v);
				}
				System.out.println();
			}
			System.out.println("--------------------- ");
		}

		private static void putPosition(int[][] matrix, int position) {
			int i = position / 4;
			int j = position % 4;
			matrix[i][j] = getRandomValue();
		}

		private static void putPosition(int[][] matrix, int position, int val) {
			int i = position / 4;
			int j = position % 4;
			matrix[i][j] = val;
		}

		private static int getRandomValue() {

			int v = (int) (Math.random() * 100);
			return v % 2 == 0 ? 2 : 4;
		}

		private static int getOpenPosition() {
			int SIZE = openPositions.size();
			if (SIZE == 0) {
				return -1;
			}
			int index = (int) (Math.floor(Math.random() * 100) % SIZE);
			int v = openPositions.get(index);
			openPositions.remove(index);
			return v;
		}

		private static int playGame() {
			boolean isPossibleToPlay = true, positionAdded = false, mergeDone = false;
			while (isPossibleToPlay) {

				System.out.println("enter an integer");
				String key = scanner.next();

				int position = getOpenPosition();
				if (position != -1) { // put value
					putPosition(dp, position);
					positionAdded = true;
				}
				System.out.println("pushed dp is ");
				printPosition(dp);
				System.out.println();
				// now merge
				switch (key) {
				case "W":
					System.out.println("mergeUpwards called");
					mergeDone = mergeUpward();
					break;
				case "S":
					System.out.println("mergeDownWards called");
					mergeDone = mergeDownward();
					break;
				case "D":
					System.out.println("mergeRight called");

					mergeDone = mergeRight();
					break;
					
				case "A":
					System.out.println("mergeLeft called");
					mergeDone = mergeLeft();
				default:
					System.out.println("default case");
					break;
				}

				// Copy back
				copyValue(tmp, dp); // from to
				printPosition(dp);
				System.out.println();
				recalculateOpenPositions();

				isPossibleToPlay = positionAdded || mergeDone;
			}
			return 0;
		}

		private static boolean mergeLeft() {
			for (int i = 0; i < 4; i++) { // each column by column
				int jLoc = 0;
				for (int j = 0; j < 4; j++) {

					int v = dp[i][j];
					dp[i][j] = -1; // reset this value and put to new position.
					int position = i * 4 + jLoc;
					if (v != -1) {
						putPosition(dp, position, v); // put done now merge
						jLoc++;
					}
				}
			}
			System.out.println("pushed dp is ");
			printPosition(dp);
			clearMatrix(tmp);

			boolean mergeDone = false;

			// Merge logic
			for (int i = 0; i < 4; i++) { // each column by column
				int jLoc = 0;
				for (int j = 0; j < 4; j++) {
					int first = dp[i][j];
					int second = j + 1 < 4 ? dp[i][j+1] : NOT_POSSIBLE;
					if (first != -1) { // merge them
						if (first == second) {
							tmp[i][jLoc] = 2 * (dp[i][j]);
							j++; // jump i by 1 more;
							mergeDone = true;
						} else {
							tmp[i][jLoc] = dp[i][j];
						}
						jLoc++;
					}
				}
			}
			return mergeDone;
		}

		private static boolean mergeRight() {
			for (int i = 0; i < 4; i++) { // each column by column
				int jLoc = 3;
				for (int j = 3; j >=0; j--) {
					int v = dp[i][j];
					dp[i][j] = -1; // reset this value and put to new position.
					int position = i * 4 + jLoc;
					if (v != -1) {
						putPosition(dp, position, v); // put done now merge
						jLoc--;
					}
				}
			}
			System.out.println("pushed dp is ");
			printPosition(dp);
			clearMatrix(tmp);

			boolean mergeDone = false;

			// Merge logic
			for (int i = 0; i < 4; i++) { // each column by column
				int jLoc = 3;
				for (int j = 3; j >=0; j--) {
					int first = dp[i][j];
					int second = j - 1 >=0 ? dp[i][j-1] : NOT_POSSIBLE;
					if (first != -1) { // merge them
						if (first == second) {
							tmp[i][jLoc] = 2 * (dp[i][j]);
							j--; // jump i by 1 more;
							mergeDone = true;
						} else {
							tmp[i][jLoc] = dp[i][j];
						}
						jLoc--;
					}
				}
			}
			return mergeDone;
		}

		private static void recalculateOpenPositions() {
			openPositions = new ArrayList<>();
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (dp[i][j] == -1) {
						int position = i * 4 + j;
						openPositions.add(position);
					}
				}
			}

		}

		private static boolean mergeUpward() {

			for (int j = 0; j < 4; j++) { // each column by column
				int iLoc = 0;
				for (int i = 0; i < 4; i++) {

					int v = dp[i][j];
					dp[i][j] = -1; // reset this value and put to new position.
					int position = iLoc * 4 + j;
					if (v != -1) {
						putPosition(dp, position, v); // put done now merge
						iLoc++;
					}
				}
			}
			System.out.println("pushed dp is ");
			printPosition(dp);
			clearMatrix(tmp);

			boolean mergeDone = false;

			// Merge logic
			for (int j = 0; j < 4; j++) { // each column by column
				int iLoc = 0;
				for (int i = 0; i < 4; i++) {
					int first = dp[i][j];
					int second = i + 1 < 4 ? dp[i + 1][j] : NOT_POSSIBLE;
					if (first != -1) { // merge them
						if (first == second) {
							tmp[iLoc][j] = 2 * (dp[i][j]);
							iLoc++;
							i++; // jump i by 1 more;
							mergeDone = true;
						} else {
							tmp[iLoc][j] = dp[i][j];
							iLoc++;
						}
					}
				}
			}
			return mergeDone;
		}

		private static void copyValue(int[][] from, int[][] to) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					to[i][j] = from[i][j];
				}
			}
		}

		private static void clearMatrix(int[][] tmp2) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					tmp2[i][j] = -1;
				}
			}
		}

		private static boolean mergeDownward() {
			for (int j = 0; j < 4; j++) { // each column by column
				int iLoc = 3;
				for (int i = 3; i >= 0; i--) {

					int v = dp[i][j];
					dp[i][j] = -1; // reset this value and put to new position.
					int position = iLoc * 4 + j;
					if (v != -1) {
						putPosition(dp, position, v); // put done now merge
						iLoc--;
					}
				}
			}
			System.out.println("pushed dp is ");
			printPosition(dp);
			clearMatrix(tmp);

			boolean mergeDone = false;

			// Merge logic
			for (int j = 0; j < 4; j++) { // each column by column
				int iLoc = 3;
				for (int i = 3; i >= 0; i--) {
					int first = dp[i][j];
					int second = i - 1 >= 0 ? dp[i - 1][j] : NOT_POSSIBLE;
					if (first != -1) { // merge them
						if (first == second) {
							tmp[iLoc][j] = 2 * (dp[i][j]);
							iLoc--;
							i--; // jump i by 1 more;
							mergeDone = true;
						} else {
							tmp[iLoc][j] = dp[i][j];
							iLoc--;
						}
					}
				}
			}
			return mergeDone;
		}
	}
}
