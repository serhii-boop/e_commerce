package dyplom.e_commerce.entities.export;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import dyplom.e_commerce.entities.Customer;
import dyplom.e_commerce.entities.User;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class CustomerPdfExporter extends AbstractExporter {

    public void export(List<Customer> customerList, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/pdf", ".pdf");

        Document document = new com.lowagie.text.Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
        font.setSize(18);

        Paragraph paragraph = new Paragraph("List of customers", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);
        table.setWidths(new float[] {1f, 3.5f, 2.5f, 2.5f, 2.5f, 2.5f, 3.5f});
        writeTableHeader(table);
        writeTableData(table, customerList);
        document.add(table);
        document.close();
    }

    private void writeTableData(PdfPTable table, List<Customer> customerList) {
        for (Customer customer: customerList) {
            table.addCell(String.valueOf(customer.getId()));
            table.addCell(customer.getEmail());
            table.addCell(customer.getFirstName());
            table.addCell(customer.getLastName());
            table.addCell(customer.getCountry());
            table.addCell(customer.getCity());
            table.addCell(customer.getAddressLine1());
        }
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.DARK_GRAY);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Id", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Email", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("First name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Last name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Country", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("City", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Address", font));
        table.addCell(cell);
    }
}
