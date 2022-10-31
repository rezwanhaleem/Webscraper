package com.webscraper;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JButton;

public class FreeButton extends JButton
{

	private Color[] backgroundColor;

	public FreeButton()
	{
		this(null);
	}

	public FreeButton(String text)
	{
		super(text);
		super.setContentAreaFilled(false);
		super.setFocusPainted(false); // used for demonstration
		super.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		final Graphics2D g2 = (Graphics2D) g.create();
		if (getModel().isPressed())
		{
			g2.setPaint(new GradientPaint(new Point(0, 0), backgroundColor[0].darker(), new Point(0, getHeight()),
					backgroundColor[1].darker()));
		}
		else if (getModel().isRollover())
		{
			g2.setPaint(new GradientPaint(new Point(0, 0), backgroundColor[0].brighter(), new Point(0, getHeight()),
					backgroundColor[1].brighter()));
		}
		else
		{
			g2.setPaint(new GradientPaint(new Point(0, 0), backgroundColor[0], new Point(0, getHeight()),
					backgroundColor[1]));
		}
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.dispose();

		super.paintComponent(g);
	}

	@Override
	public void setContentAreaFilled(boolean b)
	{
	}

	public void setBackgroundColor(Color topColor, Color bottomColor)
	{
		this.backgroundColor = new Color[] { topColor, bottomColor };
	}
}
