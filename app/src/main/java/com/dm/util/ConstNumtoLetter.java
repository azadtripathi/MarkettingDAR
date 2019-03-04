package com.dm.util;
public class ConstNumtoLetter
  {
	 public  String NumbersToWords(int inputNumber)
	 {
	     int inputNo = inputNumber;

	     if (inputNo == 0)
	         return "Zero";

	     int[] numbers = new int[4];
	     int first = 0;
	     int u, h, t;
	     StringBuilder sb = new StringBuilder();

	     if (inputNo < 0)
	     {
	         sb.append("Minus ");
	         inputNo = -inputNo;
	     }

	     String[] words0 = {"" ,"One ", "Two ", "Three ", "Four ",
	             "Five " ,"Six ", "Seven ", "Eight ", "Nine "};
	     String[] words1 = {"Ten ", "Eleven ", "Twelve ", "Thirteen ", "Fourteen ",
	             "Fifteen ","Sixteen ","Seventeen ","Eighteen ", "Nineteen "};
	     String[] words2 = {"Twenty ", "Thirty ", "Forty ", "Fifty ", "Sixty ",
	             "Seventy ","Eighty ", "Ninety "};
	     String[] words3 = { "Thousand ", "Lakh ", "Crore " };

	     numbers[0] = inputNo % 1000; // units
	     numbers[1] = inputNo / 1000;
	     numbers[2] = inputNo / 100000;
	     numbers[1] = numbers[1] - 100 * numbers[2]; // thousands
	     numbers[3] = inputNo / 10000000; // crores
	     numbers[2] = numbers[2] - 100 * numbers[3]; // lakhs

	     for (int i = 3; i > 0; i--)
	     {
	         if (numbers[i] != 0)
	         {
	             first = i;
	             break;
	         }
	     }
	     for (int i = first; i >= 0; i--)
	     {
	         if (numbers[i] == 0) continue;
	         u = numbers[i] % 10; // ones
	         t = numbers[i] / 10;
	         h = numbers[i] / 100; // hundreds
	         t = t - 10 * h; // tens
	         if (h > 0) sb.append(words0[h] + "Hundred ");
	         if (u > 0 || t > 0)
	         {
	             if (h > 0 || i == 0) sb.append("and ");
	             if (t == 0)
	                 sb.append(words0[u]);
	             else if (t == 1)
	                 sb.append(words1[u]);
	             else
	                 sb.append(words2[t - 2] + words0[u]);
	         }
	         if (i != 0) sb.append(words3[i - 1]);
	     }
	     return sb.toString();
	 }
}

 