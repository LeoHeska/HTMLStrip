// HTMLStrip Version 1.0
// Author:  Leo Heska
// Distributed by SourceForge.net under the GPL

import java.util.*;
import java.io.*;

public class HTMLStrip {
   private String html;
   private PrintWriter out;
   private String inFileSpec = null;
   private String outFileSpec = null;
   private String regExpFileSpec = null;
   private String[][] pairs;
   private String[][] StdPairs = {
      // See RegularExpressions.lst for documentation of all these.
      {"<!DOCTYPE[^>]+>", ""},
      {"<body lang=[^>]+>", "<body>"},
      {" xmlns[^>]+>", ""},
      {"\\s?class=\\w+", ""},
      {"\\s+style='[^']+'", ""},
   // {"<(meta|link|/?o:|/?style|/?div|/?st\\d|/?span|!\\[)[^>]*?>",""},
      // Note http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6337993
      // This bug makes the above blow up on all but tiny input files.
      // Replace the above perfectly valid regexp with the below ones
      {"<meta[^>]*?>",""},
      {"<link[^>]*?>",""},
      {"</?o:[^>]*?>",""},
      {"</?style[^>]*?>",""},
      {"</?div[^>]*?>",""},
      {"</?st\\d[^>]*?>",""},
      {"</?span[^>]*?>",""},
      {"<!\\[[^>]*?>",""},
      // end of broken-apart regexps, avoiding (x|y)* constructs

      // {"<!--(\\w|\\W)+?-->", ""},
      // break the above into the below to avoid the same Java bug
      {"<!--\\w+?-->", ""},
      {"<!--\\W+?-->", ""},

      {"(<[^>]+>)+&nbsp;(</\\w+>)+", ""},
      {"\\s+v:\\w+=\"[^\"]+\"", ""},
      {"<p class=[^>]+>",""},
      {"<p>",""},
      {"</p>","<br><br>"},
      {"<br[^>]*?>","<br>"},
      {"(\\n\\r){2,}", ""},
      {"(\\r){2,}", ""},
      {"(\\n){2,}", ""},
      {"ô", "&ldquo;"},
      {"ö", "&rdquo;"},
      {"Ú", "&mdash;"}
   };
   
   public HTMLStrip() {}
   
   public HTMLStrip(String inFS, String outFS, String regExpFS) {
      try {
         inFileSpec = inFS;
         outFileSpec = outFS;
         regExpFileSpec = regExpFS;
         html = readStringFromFile(inFileSpec);
         out = new PrintWriter(outFileSpec);
         if (regExpFileSpec == null) {
            pairs = new String[StdPairs.length][2];
            System.arraycopy(StdPairs, 0, pairs, 0, StdPairs.length);
         } else buildPairsFromFile(regExpFileSpec);
      } catch (Exception e) {System.err.println(e);}
   }
   
   void buildPairsFromFile(String regExpFileSpec) {
      String line;
      String begLine;
      String fromExp;
      String toExp;
      Vector fromV = new Vector();
      Vector toV = new Vector();
      int tabPos;
      int numEntries;
      try {
         FileReader fr = new FileReader(regExpFileSpec);
         BufferedReader br = new BufferedReader(fr);
         while(br.ready()) {
            line = br.readLine();
               if (line == null) continue;
            begLine = line.substring(0, 2);
            if (begLine.equals("--") || begLine.equals("//")) continue;
            tabPos = line.indexOf('\t');
            fromExp = line.substring(0, tabPos);
            toExp = line.substring(tabPos + 1);
            fromV.add(fromExp);
            toV.add(toExp);
         }
         
         numEntries = fromV.size(); // should also equal toV.size()
         pairs = new String[numEntries][2];
         
         for (int i = 0; i < numEntries; i++) {
            pairs[i][0] = (String)fromV.get(i);
            pairs[i][1] = (String)toV.get(i);
         }
      } catch (Exception e) {System.err.println(e);}
   }
   
   private String readStringFromFile(String FileName) {
      String FileContents;
      int size;
      
      try {
         FileInputStream f = new FileInputStream(FileName);
         size = f.available();
         byte bytearray[] = new byte[size];
         f.read(bytearray);
         FileContents = new String(bytearray, 0, size);
         f.close();
      } catch (Exception e) {
         FileContents = null;
         System.out.println(e);
      }
      
      return FileContents;
   }
   
   public static void main(String[] args) {
      if (args.length < 2) {
         System.out.println(
               "Usage:  java HTMLStrip InputFileName OutputFileName " +
               "{ReplacementExpressionsFileName}");
         System.exit(1);
      }
      
      String regExpsFS = null;

      if (args.length >= 3) regExpsFS = args[2];
      
      HTMLStrip hs = new HTMLStrip(args[0], args[1], regExpsFS);
      hs.cleanHTML();
   }
   
   private void cleanHTML() {
      try {
         System.out.println(html.length() + " characters read from " +
               "file " + inFileSpec);
         
         for (int i = 0; i < pairs.length; i++) 
            html = html.replaceAll(pairs[i][0], pairs[i][1]);
         
         out.print(html);
         out.close();
         System.out.println(html.length() + " cleaned characters " +
               "written to file " + outFileSpec);
      } catch (Exception e) {System.err.println(e);}
   }
}
