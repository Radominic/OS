import java.io.*;
import java.util.*;

public class SimpleShell {
	public static void main(String[] args) throws java.io.IOException {
		/*
		 * This SimpleShell is the code that creates Shell by creating new process. You
		 * can use linux commands such as ls,ps,cat,cd,history and so on. Output will be printed
		 * in shell. If you want to escape the shell, input 'exit'or'quit' command.
		 */
		// commandLine and BufferedReader for input command.
		String commandLine;
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		// Process Builder for create new process.
		ProcessBuilder pb = new ProcessBuilder();
		// File object to store current location of process.
		// Default will be the location of user home directory.
		File file = new File(System.getProperty("user.home"));
		// List to store history of commands.
		List<String> hlist = new ArrayList<String>();
		System.out.println("********gangmin_shell********");
		System.out.println("If you want to escape shell, input 'exit'or'quit'");
		System.out.println("*****************************");
		while (true) {
			// read what the user entered.
			System.out.print("gangmin_shell>");
			commandLine = console.readLine();
			// if the user entered a return, just loop again.
			if (commandLine.contentEquals(""))
				continue;
			// store command in the clist to pass at the process.
			String[] carr = commandLine.split(" ");
			List<String> clist = new ArrayList<String>();
			for (int i = 0; i < carr.length; i++) {
				// code for exit.
				if (carr[i].equals("exit") || carr[i].equals("quit")) {
					System.out.println("Goodbye");
					System.exit(0);
				}
				clist.add(carr[i]);
			}

			// try&catch to avoid I/Oexception.
			try {
				// !!command.
				// Take and execute the most recent command in the history.
				// If there is no history because command is the first, print error message.
				// Take the command out of history list and run it.
				// Wrong input will be not stored in history list.
				if (clist.get(0).equals("!!") && clist.size() == 1) {
					if (hlist.size() != 0) {
						clist = new ArrayList<String>();
						String[] temp = hlist.get(hlist.size() - 1).split(" ");
						for (int p = 0; p < temp.length; p++)
							clist.add(temp[p]);
					} else {
						System.out.println("error : there is no history!");
						continue;
					}
				}
				// !<number>command.
				else if (clist.size() == 1 && clist.get(0).charAt(0) == '!') {
					String str = clist.get(0).substring(1, clist.get(0).length());
					int num = 0;
					// try&catch to determine whether the characters following ! is a digit.
					try {
						num = Integer.parseInt(str);
						if (num < hlist.size()) {
							clist = new ArrayList<String>();
							String[] temp = hlist.get(num).split(" ");
							for (int p = 0; p < temp.length; p++)
								clist.add(temp[p]);
						}
						// If the user input a number greater than history size, print error.
						else {
							System.out.println("error : number is bigger than size of history!");
							continue;
						}
					} catch (Exception e) {
						System.out.println("error : you have to use '!<number>'");
						continue;
					}

				}

				// Save in the history.
				String cstr = "";
				for (int i = 0; i < clist.size(); i++)
					cstr += clist.get(i) + " ";
				hlist.add(cstr);

				// history command.
				if (clist.get(0).equals("history") && clist.size() == 1) {
					// print the history.
					for (int i = 0; i < hlist.size(); i++) {
						System.out.println(i + " " + hlist.get(i));
					}
					// if there is no history because the input is first, print error.
					if (hlist.size() == 0) {
						System.out.println("error : there is no history!");
					}
					continue;
				} 
				// history<number> command.
				// if you input history with <number>, from the most recent command, <number> history will be printed.
				else if (clist.get(0).equals("history") && clist.size() == 2) {
					// try&catch to determine whether the characters following history is a digit.
					try {
						int num = Integer.parseInt(clist.get(1));
						if (num <= hlist.size()) {
							for (int i = hlist.size() - num; i < hlist.size(); i++)
								System.out.println(i + " " + hlist.get(i));
						}
						// If number is greater than size of history, print error.
						else {
							System.out.println("error : number is bigger than size of history!");
						}
					} catch (Exception e) {
						System.out.println("error : you have to use 'history <number>'");
					}
					continue;
				}

				// cd command.
				// 'cd'command will move the location of process to user.home directory.
				if (clist.get(0).equals("cd")) {
					if (clist.size() == 1 && clist.get(0).equals("cd")) {
						file = new File(System.getProperty("user.home"));
						System.out.println(file.getAbsolutePath());
						pb.directory(file);
						continue;
					} 
					//cd command with more than 3 input is wrong command, print error.
					else if (clist.size() >= 3) {
						System.out.println("error : not proper usage!");
						continue;
					}
					//if use cd with other command.
					else if (clist.get(0).equals("cd")) {
						if (!clist.get(1).startsWith("/")) {
							// cd .. will take the process to upper directory.
							if (clist.get(1).equals("..")) {
								// If you are in root directory, you can't go to upper anymore.
								if (file.getAbsolutePath().equals("/")) {
									System.out.println("error : now is root directory, can not go to the top anymore!");
								} else {
									pb.directory(file.getParentFile());
									file = file.getParentFile();
									System.out.println(file.getAbsolutePath());
								}
								continue;
							} 
							//cd . will print current directory.
							else if (clist.get(1).equals(".")) {
								System.out.println(file.getAbsolutePath());
								continue;
							}
							// cd /dir will take the process to absolute path.
							else {
								File check = new File(file.getAbsolutePath() + "/" + clist.get(1));
								// to avoid fake directory, check using isDirectory function.
								if (check.isDirectory()) {
									pb.directory(check);
									file = check;
									System.out.println(file.getAbsolutePath());
								} else {
									System.out.println("error : there is no such directory!");
								}
								continue;
							}
						}
						// cd dir will take the process to relative path unless it is not fake directory.
						else {
							File check = new File(clist.get(1));
							if (check.isDirectory()) {
								pb.directory(check);
								file = check;
								System.out.println(file.getAbsolutePath());
							} else {
								System.out.println("error : there is no such directory!");
							}
							continue;
						}
					}
				}
				// input command to the process and start.
				pb.command(clist);
				Process process = pb.start();
				//out put the result of shell.
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line;
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
				br.close();
			} catch (IOException e) {
				System.out.println("error : command " + clist + " is not found, try again!");
			}

		}
	}
}