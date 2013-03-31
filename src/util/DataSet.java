package util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;


public class DataSet {
	ArrayList<Double> data;
	
	public DataSet(int length) {		
		data = new ArrayList<Double>(length);
	}
	
	public DataSet() {
		this(10000);
	}
	
	public double min() {
		double min = data.get(0);
		for (int i = 1; i < data.size(); i++) {
			if (data.get(i) < min) min = data.get(i);
		}
		return min;
	}
	
	public double max() {
		double max = data.get(0);
		for (int i = 1; i < data.size(); i++) {
			if (data.get(i) > max) max = data.get(i);
		}
		return max;
	}
	
	public double avg() {
		
		return sum()/data.size();
	}
	
	public void add (double d) {
		data.add(d);
	}
	
	public double median() {
		Collections.sort(data);
		if (data.size() % 2 == 1) {
			return data.get(data.size()/2);
		}
		else {
			return (data.get(data.size()/2 - 1) + data.get(data.size()/2))/2;
		}
	}
	
	public double sum() {
		double sum = 0;
		for (int i = 0; i < data.size(); i++) {
			sum += data.get(i);
		}
		return sum;
	}
	
	public double range() {
		return max() - min();
	}
	
	public void write(String filename) {
		try {
			PrintWriter out = new PrintWriter(new File(filename));
			for (Double d: data) {
				out.println(d);
			}
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public String toString() {
		String s = " min: " + min() + " max: " + max() + " avg: " + avg() + " median: " + median();
		return s;
	}
	
//	public static void main(String[] args) {
//		DataSet set = new DataSet(6);
//		set.data[0] = 3;
//		set.data[1] = 8;
//		set.data[2] = 6;
//		set.data[3] = 2;
//		set.data[4] = 1;
//		set.data[5] = -2;
//		
//		System.out.println(set.max());
//		System.out.println(set.min());
//		System.out.println(set.avg());
//		System.out.println(set.median());
//		
//	}
}
