package lattice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

/**
 * Static methods to improve your life
 * @author danny
 *
 */
public class Helper {

	/**
	 * Reads and returns a single line of input entered by the user;
	 * Terminates the program if an exception is thrown
	 */
	public static String readLine() {
		InputStreamReader streamIn = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(streamIn, 1);
		
		String line = null;
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.out.println("Error in Helper.readLine: " +
					e.getMessage());
			System.exit(-1);
		}
		return line;
	}
	
	/**
	 * Creates an error message box and displays the error message.
	 * Should be used as a helper function
	 * @param s The string to be displayed.
	 */
	public static void msgBox(String s) {
		JOptionPane.showMessageDialog(null, s, "Error",
				JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * A shortcut for System.out.println (it is only useful with static imports)
	 * @param s
	 */
	public static void p(String s) {
		System.out.println(s);
	}
	
	/**
	 * Creates an error message and displays it with special formatting.
	 * Should be used as a helper function
	 * @param className The name of the class the error occured
	 * @param funcName The name of the function the error occured (may include parameters if the function is overloaded.
	 * @param info Specific information about the error
	 */
	public static void error(String className, String funcName, String info) {
		System.out.println("**********************ERROR**********************");
		System.out.println("Non-fatal error occured at " + funcName + " in "
				+ className);
		System.out.println(info);

	}
	
	/**
	 * This rounds a number to a certain number of decimal places
	 * @param number The number to round
	 * @param decimalPlaces The number of decimal places to round to
	 * @return
	 */
	public static double round(double number, int decimalPlaces) {
		number = number * Math.pow(10,decimalPlaces);
		number = Math.round(number);
		number = number / Math.pow(10,decimalPlaces);
		return number;
	}
	
	/**
	 * Rounds a double  and converts it to an integer
	 * @param d
	 * @return
	 */
	public static final int toInt(double d) {
		return (int)Math.floor(d + 0.5d);
	}
	
	/**
	 * Sleeps the thread for a certain period of time.
	 * @param time The number of milliseconds to sleep (1000ms to 1s)
	 */
	public static void delay(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * This compresses a number into a maximum of 3 digits
	 * It uses k, M, B, T system
	 * @param number
	 * @return
	 */
	public static String compress(double number) {
		long n = Math.round(Math.abs(number));
		
		//Trillion
		if (n >= Math.pow(10, 12)) {
			return (int)Math.round(number/Math.pow(10, 12)) + "T";
		}
		//Billion
		else if (n >= Math.pow(10, 9)) {
			return (int)Math.round(number/Math.pow(10, 9)) + "B";
		}
		//Million
		else if (n >= Math.pow(10, 6)) {
			return (int)Math.round(number/Math.pow(10, 6)) + "M";
		}
		//Thousands
		else if (n >= Math.pow(10, 3)) {
			return (int)Math.round(number/Math.pow(10, 3)) + "k";
		}
		else {
			return Integer.toString((int)Math.round(number));
		}
			
		
		
	}
	
	
}
