package com.oag.core.ssimout.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class CompareSSIMFiles {

	private static int RC_OK = 0;
	private static int RC_GEN_FAIL = 1;
	private static int RC_INVALID_PARAMS = 2;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.print(ColorCodes.BLUE);
		System.out.print("SSIM Comparison");
		System.out.println(LineUtil.ANSI_RESET);

		int RC = RC_OK;

		if (args.length != 2) {
			System.err.println("Excepted 2 args of files to compare");
			RC = RC_INVALID_PARAMS;
		} else {
			System.out.print(LineUtil.ANSI_YELLOW);
			System.out.println("Files");
			System.out.println("File1 " + args[0]);
			System.out.println("File2 " + args[1]);
			System.out.print(LineUtil.ANSI_RESET);

			File f1 = new File(args[0].replace("\\", "/"));
			File f2 = new File(args[1].replace("\\", "/"));
			boolean f1Check = checkFilePath(f1);
			boolean f2Check = checkFilePath(f2);

			if (f1Check && f2Check) {
				compare(f1, f2);
			} else {
				RC = RC_INVALID_PARAMS;
			}

		}
		System.exit(RC);
	}

	private static int compare(File f1, File f2) {
		try {
			// compare files.
			BufferedReader br1 = new BufferedReader(new FileReader(f1));
			BufferedReader br2 = new BufferedReader(new FileReader(f2));

			boolean linesLeft = true;
			int f1LineCount = 0;
			int f2LineCount = 0;

			int f1T1Count = 0;
			int f2T1Count = 0;
			int f1T2Count = 0;
			int f2T2Count = 0;
			int f1T3Count = 0;
			int f2T3Count = 0;
			int f1T4Count = 0;
			int f2T4Count = 0;
			int f1T5Count = 0;
			int f2T5Count = 0;

			List<LineComparator> comps = new ArrayList<LineComparator>(1);
			comps.add(new LineMatchesOnBytes(0, 194));
			CompositeComparator cc = new CompositeComparator(comps);

			Set<String> file1Carriers = new HashSet<String>();
			Set<String> file2Carriers = new HashSet<String>();

			Scanner scanner = new Scanner(System.in);
			boolean echoLines = false;
			
			System.out.println("Echo mismatched lines? (Y | N)");
			String in = scanner.next();
			if ("Y".equalsIgnoreCase(in)) {
				echoLines=true;
			}
			
			int matchLines=0;
			int mismatchedLines=0;
			
			HashMap<String,ArrayList<String>> designators1 = new HashMap<String,ArrayList<String>>(10);
			HashMap<String,ArrayList<String>> designators2 = new HashMap<String,ArrayList<String>>(10);
			
			while (linesLeft) {
				String line1 = br1.readLine();
				String line2 = br2.readLine();

				linesLeft = line1 != null || line2 != null;

				if (line1 != null) {
					f1LineCount++;
					switch (line1.charAt(0)) {
					case '1':
						f1T1Count++;
						break;
					case '2':
						f1T2Count++;
						String cxr = line1.substring(2, 4).trim();
						if (!designators1.containsKey(cxr)) {
							designators1.put(cxr, new ArrayList<String>(100));
						}
						file1Carriers.add(cxr);
						break;
					case '3':
						String cxr1 = line1.substring(2, 4).trim();
						String fltno=line1.substring(4,9).trim();
						String suffix = line1.substring(1,2).trim();
						String desig = fltno+suffix;
						if (!designators1.get(cxr1).contains(desig)) {
							designators1.get(cxr1).add(desig);
						}
						f1T3Count++;
						break;
					case '4':
						f1T4Count++;
						break;
					case '5':
						f1T5Count++;
						break;
					}
				}
				if (line2 != null) {
					f2LineCount++;
					switch (line2.charAt(0)) {
					case '1':
						f2T1Count++;
						break;
					case '2':
						f2T2Count++;
						file2Carriers.add(line2.substring(2, 4).trim());
						String cxr3 = line2.substring(2, 4).trim();
						if (!designators2.containsKey(cxr3)) {
							designators2.put(cxr3, new ArrayList<String>(100));
						}
						break;
					case '3':
						String cxr4 = line2.substring(2, 4).trim();
						String fltno=line2.substring(4,9).trim();
						String suffix = line2.substring(1,2).trim();
						String desig = fltno+suffix;
						if (!designators2.get(cxr4).contains(desig)) {
							designators2.get(cxr4).add(desig);
						}
						f2T3Count++;
						break;
					case '4':
						f2T4Count++;
						break;
					case '5':
						f2T5Count++;
						break;
					}
				}
				int rc = cc.compare(line1, line2);
				if (echoLines && rc!=0) {
					System.out.println();
					System.out.print(ColorCodes.RESET);
					LineUtil.compareAndPrint(System.out, line1, line2);
					System.out.println("Echo mismatched lines? (Y | N)");
					in = scanner.next();
					if ("N".equalsIgnoreCase(in)) {
						echoLines=false;
					}
				}
				if (rc != 0) {
					mismatchedLines++;
				} else {
					matchLines++;
				}
				
				if (f1LineCount>0) {
					if (f1LineCount%1000==0) {
						System.out.print(ColorCodes.CYAN);
						System.out.print(".");
						System.out.print(ColorCodes.RESET);
					}
				}
			}

			System.out.println();
			System.out.print(ColorCodes.GREEN);
			System.out.println("File 1 line count: " + f1LineCount);
			System.out.println("File 2 line count: " + f2LineCount);
			System.out.println("Matched count: " + matchLines +" mismatched count: " + mismatchedLines);
			System.out.print(ColorCodes.PURPLE);

			System.out.println("Record type counts");
			System.out.println("Rec Type\tFile1\tFile2");
			System.out.println("1\t\t" + f1T1Count + "\t" + f2T1Count);
			System.out.println("2\t\t" + f1T2Count + "\t" + f2T2Count);
			System.out.println("3\t\t" + f1T3Count + "\t" + f2T3Count);
			System.out.println("4\t\t" + f1T4Count + "\t" + f2T4Count);
			System.out.println("5\t\t" + f1T5Count + "\t" + f2T5Count);
			System.out.println();
			System.out.println();
			System.out.println("Carriers:");
			System.out.print("File1:  ");
			List<String> sortedCarriers=new ArrayList<String>(file1Carriers);
			Collections.sort(sortedCarriers);
			
			for (String c : sortedCarriers) {
				System.out.print(c);
				System.out.print(" ");
			}

			sortedCarriers.clear();
			System.out.println();
			System.out.print("File2:  ");
			sortedCarriers=new ArrayList<String>(file2Carriers);
			Collections.sort(sortedCarriers);
			
			for (String c : sortedCarriers) {
				System.out.print(c);
				System.out.print(" ");
			}

			System.out.println();
			Set<String> allCarriers = new HashSet(file1Carriers);
			allCarriers.addAll(file2Carriers);
			List<String> allCarriersList = new ArrayList<String>(allCarriers);
			Collections.sort(allCarriersList);
			
			System.out.println("Services Comparison:");
			for(String c: allCarriers) {
				System.out.println(c);
				List<String> flts1 = designators1.containsKey(c) ? designators1.get(c) : new ArrayList<String>(0);
				List<String> flts2 = designators2.containsKey(c) ? designators2.get(c): new ArrayList<String>(0);
				int flts1Size = flts1.size();
				int flts2Size = flts2.size();
				int matched=0;
				int mismatched=0;
				List<String> mismatchedList = new ArrayList<String>();
				if(flts1.containsAll(flts2)) {
					matched = flts1.size();
				} else {
					if (flts1Size>flts2Size) {
						for (String f: flts1) {
							if (flts2.contains(f)) {
								matched++;
							} else {
								mismatchedList.add(f);
								mismatched++;
							}
						}
						flts2.removeAll(flts1);
						for (String f: flts2) {
							mismatched++;
							mismatchedList.add(f);
						}
					} else {
						for (String f: flts2) {
							if (flts1.contains(f)) {
								matched++;
							} else {
								mismatchedList.add(f);
								mismatched++;
							}
						}
						flts1.removeAll(flts2);
						for (String f: flts1) {
							mismatched++;
							mismatchedList.add(f);
						}
						
					}
				}
				System.out.println("Num matched services: "+matched);
				System.out.println("Num mismatched services: "+mismatched);
				if (mismatched>0){
					System.out.println("Mismatched services:");
					Collections.sort(mismatchedList);
					for (String f: mismatchedList) {
						System.out.print(f);
						System.out.print(" ");
					}
					System.out.println();
				}
			}
			
			System.out.print(ColorCodes.RESET);
			System.out.println();

		} catch (IOException ioe) {
			log(ioe);
		}
		return RC_OK;
	}

	private static void log(Throwable t) {

	}

	protected static boolean checkFilePath(File f) {
		if (f.exists()) {
			if (!f.canRead()) {
				System.err.println("Cannot read file : " + f.getAbsolutePath());
				return false;
			}
		} else {
			System.err.println("File : " + f.getAbsolutePath()
					+ " does not exist");
			return false;
		}
		return true;
	}

}
