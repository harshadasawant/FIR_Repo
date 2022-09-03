package com.police.fir.utility;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;

public class ReadingTextFromPDF {

    public static void main(String[] args) throws Exception {
        ReadingTextFromPDF demo = new ReadingTextFromPDF();
        demo.run();

    }

    private void run() throws Exception {

        File filesFolder=new File("C:\\Software\\Harshada\\FIR_Report");
        if(filesFolder.isDirectory()) {
            String filsNames[]=filesFolder.list();
            if(filsNames!=null && filsNames.length>0) {
                for(String name:filsNames) {
                    System.out.println("FileName---------------------------"+name);
                    PDDocument document = PDDocument.load(new File("C:\\Software\\Harshada\\FIR_Report\\"+name));
                    String text = extractTextFromScannedDocument(document,name);
                    document.close();
	    			/*System.out.println(text);
	    			String[] lines = text.split(System.getProperty("line.separator"));
	    			System.out.println(lines);
	    			for(String findname:lines) {
	    				if(findname.trim().contains("Name"))
	    				{
	    					System.out.println("Your Name ==="+findname);
	    				}
	    				else {
	    					//System.out.println("Not Found ===");
	    				}
	    			}*/

                }
            }

        }

    }

    public static String extractTextFromScannedDocument(PDDocument document,String name) throws IOException, TesseractException {
        FileWriter myWriter = new FileWriter("c:\\temp\\filename.txt",true);
        // Extract images from file
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder out = new StringBuilder();

        ITesseract _tesseract = new Tesseract();
        System.out.println(_tesseract.toString());
        _tesseract.setDatapath("C:\\firpdf\\tessdata");
        _tesseract.setLanguage("eng+hin");

        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

            // Create a temp image file
            File tempFile = File.createTempFile("tempfile_" + page, ".png");
            //System.out.println(tempFile.getAbsolutePath());
            ImageIO.write(bufferedImage, "png", tempFile);

            String result = _tesseract.doOCR(tempFile);
            if(page==0) {
                System.out.println("Page==0");
                String[] lines = result.split("\\n");
                myWriter.write("---------------------------------------------------");
                myWriter.write("File Name="+name+"\n");
                int count=0;
                for(String findname:lines) {

                    if(findname.trim().startsWith("(a)Name"))
                    { String cname[] = findname.split(" ");
                        String s = "";
                        for(int i=1;i< cname.length;i++){
                            s += cname[i];
                        }
                        myWriter.write("YourName ==="+findname+"\n");
                    }
                    if(findname.trim().startsWith("(b)Date/Year of Birth"))
                    {
                        String arg[] = findname.split(" ");
                        if(!arg[6].equals("Nationality")){
                            System.out.println("year ==="+arg[6]);
                        }

                        myWriter.write("(b)Date/Year of Birth ==="+findname+"\n");
                    }
                    if(findname.trim().startsWith("(c)Passport"))
                    {
                        String arg[] = findname.split(" ");
                        for(int i=0; i< arg.length;i++){
                            System.out.println(arg[i]);
                        }
                        myWriter.write("(c)Passport No ==="+findname+"\n");
                    }
                    if(findname.trim().startsWith("(e)Address"))
                    {
                        String arg[] = findname.split(" ");
                        myWriter.write("(e)Address ==="+findname+"\n");
                    }


                }
                break;
            }
            //System.out.println(result);
            out.append(result);

            // Delete temp file
            tempFile.delete();


        }
        myWriter.close();
        return out.toString();

    }
}