package edu.mayo.informatics.lexgrid.convert.directConversions.claml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;

/**
 * 
 */
public class IndMerger {

    private static IndReaderDoc docReader_;
    private static IndReaderXls xlsReader_;
    
    private static String docPath_ = "";
    private static String xlsPath_ = "";
    private static String outputPath_ = "";
    
    private static LinkedHashMap<String, IndTerm> indTermMap_;
    private static LinkedHashMap<String, LinkedHashMap<String,ArrayList<IndTerm>>> chapMap_;

    public IndMerger() throws LgConvertException, FileNotFoundException {
    }
    
    /**
     * 
     */
    public void merge(String docPath, String xlsPath, String outputPath) throws Exception {
        docPath_ = docPath;
        xlsPath_ = xlsPath;
        outputPath_ = outputPath;
        
        indTermMap_ = new  LinkedHashMap<String, IndTerm>();

        docReader_ = new IndReaderDoc();
        indTermMap_ = docReader_.getTermMap(indTermMap_, docPath_);
        
        xlsReader_ = new IndReaderXls();
        chapMap_ = xlsReader_.getTermMap(indTermMap_, xlsPath_);

        
        WritableWorkbook wb = Workbook.createWorkbook(new File(outputPath_));
        WritableCellFormat format = new WritableCellFormat();
        format.setWrap(true);
        format.setShrinkToFit(true);
        format.setVerticalAlignment(VerticalAlignment.TOP);
        
        int lastSlash = docPath_.lastIndexOf("\\");
        int lastDot = docPath_.lastIndexOf(".");
        String sheetName = docPath_.substring(lastSlash+1, lastDot);
        WritableSheet sheet = wb.createSheet(sheetName, 0); 

        setHeaders(sheet);
        
        WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
        
        WritableCellFormat headerFormatCenter = new WritableCellFormat();
        headerFormatCenter.setAlignment(Alignment.CENTRE);
        headerFormatCenter.setFont(font);
        headerFormatCenter.setWrap(true);
        
        WritableCellFormat headerFormatLeft = new WritableCellFormat();
        headerFormatLeft.setAlignment(Alignment.LEFT);
        headerFormatLeft.setFont(font);
        //headerFormatLeft.setWrap(true);
        
      Set<String> chapters = chapMap_.keySet();
      int row = 1;
      for (String chapter : chapters) 
      {
          sheet.addCell(new Label(0, row, chapter));
          row++;
          
          LinkedHashMap<String,ArrayList<IndTerm>> subChapMaps = chapMap_.get(chapter);
          Set<String> subChapters = subChapMaps.keySet();
          for (String subChapter : subChapters) 
          {
              if(subChapter.startsWith("PHYLUM") || subChapter.startsWith("ORDER") || subChapter.startsWith("FAMILY"))
              {   // parasite diseases don't format these correctly so end up on level with genus
                  sheet.addCell(new Label(0, row, subChapter));
              }
              else
              {
                  sheet.addCell(new Label(1, row, subChapter));
              }
              if(!subChapter.equalsIgnoreCase("") && !subChapter.equalsIgnoreCase(" "))
              {
                  row++;
              }
              
              ArrayList<IndTerm> indTerms = subChapMaps.get(subChapter);
              for (IndTerm indTerm : indTerms) 
              {
                    int col = 2;
                    if(indTerm!=null)
                    {
                        sheet.addCell(new Label(col, row, indTerm.getName(), headerFormatLeft));
                        col++;
                        sheet.addCell(new Label(col, row, indTerm.getDocCode(), headerFormatCenter));
                        col++;
                        sheet.addCell(new Label(col, row, indTerm.getXlsCode(), headerFormatCenter));
                        col++;
                        sheet.addCell(new Label(col, row, indTerm.getExactMatch(), headerFormatCenter));
                        col++;
                        sheet.addCell(new Label(col, row, indTerm.getXlsCode2(), headerFormatCenter));
                        col++;
                        sheet.addCell(new Label(col, row, indTerm.getExactMatch2(), headerFormatCenter));
                        col++;
                        sheet.addCell(new Label(col, row, indTerm.getSynonym(), headerFormatLeft));
                        col++;
                        sheet.addCell(new Label(col, row, indTerm.getDefinition(), headerFormatLeft));
                        col++;
                        sheet.addCell(new Label(col, row, indTerm.getNote(), headerFormatLeft));
                        col++;
                        sheet.addCell(new Label(col, row, indTerm.getLinguisticNote(), headerFormatLeft));
                        col++;
                        
                        row++;
                    }
              }
              row++;
          }
          row++;
      }
        wb.write();
        wb.close();
    }
    
    
    private void setHeaders(WritableSheet sheet) throws RowsExceededException, WriteException
    {
        WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

        WritableCellFormat headerFormatCenter = new WritableCellFormat();
        headerFormatCenter.setAlignment(Alignment.CENTRE);
        headerFormatCenter.setFont(font);
        
        // Add the column headers to the spreadsheet
        int column = 2;

        Label label = new Label(column++, 0, "Term", headerFormatCenter);
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
