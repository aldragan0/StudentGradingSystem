package utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class ExportPDF {

    private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public static <K, V> void generatePDF(File file, Map<K, V> map, String title){
        OutputStream outStream;
        try {
            outStream = new FileOutputStream(file);
            Document document = new Document();
            PdfWriter.getInstance(document, outStream);
            document.open();
            Paragraph paragraph = new Paragraph(title, titleFont);
            addEmptyLine(paragraph, 1);
            document.add(paragraph);
            map.forEach((k,v)-> {
                try {
                    PdfPTable table = new PdfPTable(2);

                    PdfPCell cell1 = new PdfPCell(new Paragraph(k.toString()));
                    PdfPCell cell2 = new PdfPCell(new Paragraph(v.toString()));

                    table.addCell(cell1);
                    table.addCell(cell2);

                    document.add(table);

                } catch (DocumentException e) {
                    e.printStackTrace();
                }

            });

            document.close();
            outStream.close();

        }catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }
}
