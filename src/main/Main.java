package main;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JOptionPane;

public class Main extends Thread {
	private static Integer value = 0;

	private static final Object lockGet = new Object();
	private static final Object lockPut = new Object();

	private static final Set<Integer> setItens = new HashSet<Integer>();

	private static int getValue() {
		synchronized (Main.lockGet) {
			return Main.value++;
		}
	}

	private static void checkItem(Integer value) {
		synchronized (Main.lockPut) {
			if (Main.setItens.contains(value)) {
				throw new RuntimeException("item repetido: " + value);
			}

			Main.setItens.add(value);
		}
	}

	private Main() {
		super();
	}

	@Override
	public void run() {
		Integer value = Main.getValue();

		System.out.println("threadId=" + this.getId() + " -- " + "value=" + value);

		Main.checkItem(value);
	}

	public static void main(String[] args) {
		JOptionPane.showMessageDialog(null, "O João é um cara legal!");

		Integer totalThreads = 100000;

		List<Main> list = new CopyOnWriteArrayList<Main>();

		for (int i = 0; i < totalThreads; i++) {
			Main main = new Main();

			list.add(main);
		}

		for (Main listItem : list) {
			listItem.start();
		}
	}
}
