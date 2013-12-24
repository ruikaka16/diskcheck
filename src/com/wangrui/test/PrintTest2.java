package com.wangrui.test;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PrintTest2 extends JFrame implements ActionListener {

 private int id ;
 public static void main(String[] e){
  new PrintTest2(1);
 }
 public PrintTest2(int id) {
  super("PrintTest");
  this.id = id;
  setSize(543, 468);
  addWindowListener(new WindowAdapter() {
   public void windowClosing(WindowEvent e) {
    System.exit(0);
   }
  });
  Container contentPane = getContentPane();
  canvas = new PrintPanel();
  contentPane.add(canvas, "Center");
  JPanel buttonPanel = new JPanel();
  printButton = new JButton("打印");
  buttonPanel.add(printButton);
  printButton.addActionListener(this);
  pageSetupButton = new JButton("页面设置");
  buttonPanel.add(pageSetupButton);
  pageSetupButton.addActionListener(this);
  contentPane.add(buttonPanel, "North");
  this.setVisible(true);
 }

 public void actionPerformed(ActionEvent event) {
  Object source = event.getSource();
  if (source == printButton) {
   PrinterJob printJob = PrinterJob.getPrinterJob();
   if (pageFormat == null)
    pageFormat = printJob.defaultPage();
   printJob.setPrintable(canvas, pageFormat);
   if (printJob.printDialog()) {
    try {
     printJob.print();
    } catch (PrinterException exception) {
     JOptionPane.showMessageDialog(this, exception);
    }
   }
  } else if (source == pageSetupButton) {
   PrinterJob printJob = PrinterJob.getPrinterJob();
   if (pageFormat == null)
    pageFormat = printJob.defaultPage();
   pageFormat = printJob.pageDialog(pageFormat);
  }
 }

 private JButton printButton;
 private JButton pageSetupButton;
 private PrintPanel canvas;
 private PageFormat pageFormat;

 class PrintPanel extends JPanel implements Printable {
  public void paintComponent(Graphics g) {
   super.paintComponent(g);
   Graphics2D g2 = (Graphics2D) g;
   drawPage(g2);
  }

  public int print(Graphics g, PageFormat pf, int page)
    throws PrinterException {
   if (page >= 1)
    return Printable.NO_SUCH_PAGE;
   Graphics2D g2 = (Graphics2D) g;
   g2.setPaint(Color.black);
//   Rectangle2D page1 = new Rectangle2D.Double(0, 0, pf.getImageableX(), pf.getImageableY()); //绘制页面矩形

//   g2.fill(page1);
   g2.translate(pf.getImageableX(), pf.getImageableY());
   g2.draw(new Rectangle2D.Double(0, 0, pf.getImageableWidth(), pf
     .getImageableHeight()));
//   g2.setPaint(Color.black);
   drawPage(g2);
   return Printable.PAGE_EXISTS;
  }

  public void drawPage(Graphics2D g2) {
   g2.drawString("00000000", 10, 100);
   /*FontRenderContext context = g2.getFontRenderContext();
   Font f = new Font("宋体", Font.PLAIN, 12);
   GeneralPath clipShape = new GeneralPath();
   TextLayout layout = new TextLayout("2007", f, context);
   AffineTransform transform = AffineTransform.getTranslateInstance(0,
     0);
   layout = new TextLayout("10", f, context);
   transform = AffineTransform.getTranslateInstance(180, 92);
   layout = new TextLayout("31", f, context);
   transform = AffineTransform.getTranslateInstance(220, 92);
   Shape outline = layout.getOutline(transform);
   clipShape.append(outline, false);
   layout = new TextLayout("货物", f, context);
   transform = AffineTransform.getTranslateInstance(138, 144);
   outline = layout.getOutline(transform);
   clipShape.append(outline, false);
   g2.draw(clipShape);
   g2.clip(clipShape);
   final int NLINES = 50;
   Point2D p = new Point2D.Double(0, 0);
   for (int i = 0; i < NLINES; i++) {
    double x = (2 * getWidth() * i) / NLINES;
    double y = (2 * getHeight() * (NLINES - 1 - i)) / NLINES;
    Point2D q = new Point2D.Double(x, y);
    g2.draw(new Line2D.Double(p, q));
   }*/
  }
 }
}
