package com.wangrui.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.*;

public class CompoundIcon implements Icon {
	private Icon[] icons = null;

	public CompoundIcon(Icon[] icons) {
		this.icons = icons;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		int hei = getIconHeight();
		for (int i = 0; i < icons.length; i++) {
			Icon icon = icons[i];
			int w = icon.getIconWidth();
			int h = icon.getIconHeight();

			icon.paintIcon(c, g, x, y + (hei - h) / 2);
			x += w;
		}
	}

	public int getIconWidth() {
		int wid = 0;
		for (int i = 0; i < icons.length; i++) {
			wid += icons[i].getIconWidth();
		}
		return wid;
	}

	public int getIconHeight() {
		int hei = 0;
		for (int i = 0; i < icons.length; i++) {
			hei = Math.max(hei, icons[i].getIconHeight());
		}
		return hei;
	}

	public static void main(String[] args) {
		Icon[] icons = new Icon[] {
				new ImageIcon("D:/Management/image/application_form.png") };
		Icon compoundIcon = new CompoundIcon(icons);
		JLabel l = new JLabel(compoundIcon);
		l.setBorder(BorderFactory.createLineBorder(Color.black));
		JFrame f = new JFrame();
		f.getContentPane().add(l, BorderLayout.CENTER);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

	}
}