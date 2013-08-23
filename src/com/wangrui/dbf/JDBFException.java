package com.wangrui.dbf;

import java.io.PrintStream;
import java.io.PrintWriter;
public class JDBFException extends Exception
{
  private Throwable detail;
  public JDBFException(String paramString)
  {
    this(paramString, null);
  }
  public JDBFException(Throwable paramThrowable)
  {
    this(paramThrowable.getMessage(), paramThrowable);
  }
  public JDBFException(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    this.detail = paramThrowable;
  }
  public String getMessage()
  {
    if (this.detail == null)
      return getMessage();
    return getMessage();
  }
  public void printStackTrace(PrintStream paramPrintStream)
  {
    if (this.detail == null)
      printStackTrace(paramPrintStream);
    else
      synchronized (paramPrintStream)
      {
        paramPrintStream.println(this);
        this.detail.printStackTrace(paramPrintStream);
      }
  }
  public void printStackTrace()
  {
    printStackTrace(System.err);
  }
  public void printStackTrace(PrintWriter paramPrintWriter)
  {
    if (this.detail == null)
      printStackTrace(paramPrintWriter);
    else
      synchronized (paramPrintWriter)
      {
        paramPrintWriter.println(this);
        this.detail.printStackTrace(paramPrintWriter);
      }
  }
}
