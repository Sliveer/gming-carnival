package fr.gming.cmd.carnival;

import java.util.Random;

public class Snail {

	private boolean stuck = false;
	private boolean hasJockey = true;
	private String color;
	private int position = 0;
	private Random random = new Random();

	public Snail(String color) {
		this.color = color;
	}

	public int addPos(int toAdd) {
		position += toAdd;
		return position;
	}

	public int move() {

		if (stuck) {
			stuck = false;
			System.out.print(" (stuck) ");
			return 0;
		}

		int moveNb = 8;

		if (hasJockey) {
			int roll = random.nextInt(20) + 1;
			moveNb = 9;

			if (roll <= 7) {
				moveNb = 6;
			} else if (roll < 12) {
				moveNb = 7;
			} else if (roll >= 17) {
				moveNb = 10;
			}
			System.out.print(" (rolled " + roll + ") ");
		} else {
			System.out.print(" (no jockey) ");
		}

		position += moveNb;

		if (position >= 49) {
			System.out.print("\t*** FINISHED ***\t");
		}

		return moveNb;

	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setStuck(boolean stuck) {
		this.stuck = stuck;
	}

	public void setHasJockey(boolean hasJockey) {
		this.hasJockey = hasJockey;
	}

}
