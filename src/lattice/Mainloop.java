package lattice;
import java.awt.Component;

public class Mainloop implements Runnable {
	private Component comp;
	
	public Mainloop(Component frame) {
		this.comp = frame;
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//game loop
		while (true) {

			comp.repaint();
			
			try {
				Thread.sleep(33);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//	Thread.yield();

		}
		
	}
}