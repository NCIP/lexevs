package edu.mayo.informatics.lexgrid.convert.directConversions.claml;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * This is a quick && dirty program to collate the IND information found in two or more spreadsheets.
 */
public class IndMainXlsMerger {
//    private static String dirPath = "X:\\ontologies\\icd10sources\\IND\\Vol 2.4 Parasitic diseases\\";
//    private static String[] xlsNames = { "Parasitic7321H_OUT_ICD10.xls", 
//                                        "Parasitic7322H_OUT_ICD10.xls" };
//    private static String outputName = "Vol2_Parasitic_Diseases.xls";
//    
    private static String dirPath = "X:\\ontologies\\icd10sources\\IND\\Vol 4 - Diseases of the Digestive System\\";
    private static String[] xlsNames = { "DigestiveSystem7676H_OUT_ICD10.xls", 
                                        "DigestiveSystem7678H_OUT_ICD10.xls", 
                                        "DigestiveSystem7679H_OUT_ICD10.xls", 
                                        "DigestiveSystem7680H_OUT_ICD10.xls"};
    private static String outputName = "Vol4_Diseases_Of_The_Digestive_System.xls";
//    
//    private static String dirPath = "X:\\ontologies\\icd10sources\\IND\\Vol 6 - Metabolic disorders\\";
//    private static String[] xlsNames = { "Endocrine8138H_OUT_ICD10.xls", 
//                                        "Endocrine8158H_OUT_ICD10.xls", 
//                                        "Endocrine8196H_OUT_ICD10.xls", 
//                                        "Endocrine8250H_OUT_ICD10.xls",
//                                        "Endocrine8410H_OUT_ICD10.xls"};
//    private static String outputName = "Vol6_Metabolic_Disorders.xls";
//    
//    private static String dirPath = "X:\\ontologies\\icd10sources\\IND\\Vol 8 - Diseases of the female reproductive system\\";
//    private static String[] xlsNames = { "femalereproductive8013H_OUT_ICD10.xls", 
//                                        "femalereproductive8014H_OUT_ICD10.xls"};
//    private static String outputName = "Vol8_Diseases_Of_The_Female_Reproductive_System.xls";

    private static int numRows_ = 0;
    private static int numCols_ = 0;
    private static int totalNumRows_ = 0;
    
    private static ArrayList<String> contents_ = new ArrayList<String>();
    private static String[] headers_ = new String[] 
                            {"Term", "IND Code", "ICD10 Code", "Exact Match", "ICD10 Code 2", 
                            "Exact Match 2", "Synonym", "Definition", "Note", "Linguistic Note"};
    
    /**
     * @param args
     */
    public static void main(String[] args) 
    {
      try
      {
          for (String xlsName : xlsNames) 
          {
              readDataSheet(dirPath + xlsName);
          }

          System.out.println("started");
        writeDataSheet();
        System.out.println("complete");

      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }

    private static void readDataSheet(String xlsPath) throws Exception
    {
        WorkbookSettings ws = new WorkbookSettings();
        ws.setLocale(new Locale("en", "EN"));
        Workbook inWorkbook = Workbook.getWorkbook(new File(xlsPath),ws);
        Sheet inSheet  = inWorkbook.getSheet(0);
        
      numRows_ = inSheet.getRows();
      numCols_ = inSheet.getColumns();
      
      String tempContent = "";
      for(int row=0; row < numRows_; row++)
      {
          for(int col=0; col< numCols_; col++)
          {
              tempContent = inSheet.getCell(col, row).getContents();
              contents_.add(tempContent);
          }
          totalNumRows_++;
      }
      inWorkbook.close();
    }
    
    private static void writeDataSheet() throws Exception
    {
        WritableWorkbook outWorkbook = Workbook.createWorkbook(new File(dirPath +outputName));
        WritableSheet outSheet = outWorkbook.createSheet(outputName.substring(0, outputName.indexOf(".")), 0); 

        setHeaders(outSheet);

        WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
        
        WritableCellFormat formatCenter = new WritableCellFormat();
        formatCenter.setAlignment(Alignment.CENTRE);
        formatCenter.setFont(font);
        formatCenter.setWrap(true);
        
        WritableCellFormat formatLeft = new WritableCellFormat();
        formatLeft.setAlignment(Alignment.LEFT);
        formatLeft.setFont(font);
        formatLeft.setWrap(true);

        WritableCellFormat formatNoWrap = new WritableCellFormat();
        formatNoWrap.setAlignment(Alignment.LEFT);
        formatNoWrap.setFont(font);

        int count = 0;
        String content = "";

        List<String> header = Arrays.asList(headers_);  
          for(int row=1; row < totalNumRows_+1; row++)
          {
              for(int col=0; col< numCols_; col++)
              {
                  content = contents_.get(count);
                  if(!header.contains(content) && !content.equalsIgnoreCase(""))
                  {
                      switch(col) {
                          case 0:
                          case 1:
                              outSheet.addCell(new Label(col, row, content, formatNoWrap));
                              break;
                          case 2:
                          case 8:
                          case 9:
                          case 10:
                          case 11:
                              outSheet.addCell(new Label(col, row, content, formatLeft));
                              break;
                          case 3:
                          case 4:
                          case 5:
                          case 6:
                          case 7:
                              outSheet.addCell(new Label(col, row, content, formatCenter));
                              break;   
                      };
                  }
                  count++;
              }
          }
          
          outWorkbook.write();
          outWorkbook.close();  
    }
    
    private static void setHeaders(WritableSheet sheet) throws RowsExceededException, WriteException
    {
        WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

        WritableCellFormat headerFormatCenter = new WritableCellFormat();
        headerFormatCenter.setAlignment(Alignment.CENTRE);
        headerFormatCenter.setFont(font);
        headerFormatCenter.setBackground(Colour.GRAY_25);
        
        // Add the column headers to the spreadsheet
        int column = 0;

        Label label = new Label(column++, 0, "Chapter", headerFormatCenter);
        sheet.addCell(label);
        sheet.setColumnView(column-1, 15);
        
        label = new Label(column++, 0, "SubChapter", headerFormatCenter);
        sheet.addCell(label);
        sheet.setColumnView(column-1, 15);
        
        label = new Label(column++, 0, "Term", headerFormatCenter);
        sheet.addCell(label);
        sheet.setColumnView(column-1, 40);
        
        label = new Label(column++, 0, "IND Code", headerFormatCenter);
        sheet.addCell(label);
        sheet.setColumnView(column-1, 15);
        
        label = new Label(column++, 0, "ICD10 Code", headerFormatCenter);
        sheet.addCell(label);
        sheet.setColumnView(column-1, 15);
        
        label = new Label(column++, 0, "Exact Match", headerFormatCenter);
        sheet.addCell(label);
        sheet.setColumnView(column-1, 15);
        
        label = new Label(column++, 0, "ICD10 Code 2", headerFormatCenter);
        sheet.addCell(label);
        sheet.setColumnView(column-1, 15);
        
        label = new Label(column++, 0, "Exact Match 2", headerFormatCenter);
        sheet.addCell(label);
        sheet.setColumnView(column-1, 15);

        label = new Label(column++, 0, "Synonym", headerFormatCenter);
        sheet.addCell(label);
        sheet.setColumnView(column-1, 40);
        
        label = new Label(column++, 0, "Definition", headerFormatCenter);
        sheet.addCell(label);
        sheet.setColumnView(column-1, 40);
        
        label = new Label(column++, 0, "Note", headerFormatCenter);
        sheet.addCell(label);
        sheet.setColumnView(column-1, 40);
        
        label = new Label(column++, 0, "Linguistic Note", headerFormatCenter);
        sheet.addCell(label);
        sheet.setColumnView(column-1, 40);
    }
    
}


