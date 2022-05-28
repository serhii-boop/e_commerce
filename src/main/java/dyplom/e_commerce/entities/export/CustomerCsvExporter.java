package dyplom.e_commerce.entities.export;

import dyplom.e_commerce.entities.Category;
import dyplom.e_commerce.entities.Customer;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CustomerCsvExporter extends AbstractExporter {

    public void export(List<Customer> customerList, HttpServletResponse httpServletResponse) throws IOException {
        super.setResponseHeader(httpServletResponse,"text/csv", ".csv");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Customer Id", "Email",  "FirstName", "LastName", "Country", "City", "Address"};
        String[] fieldMapping = {"id", "email", "firstName", "lastName", "country", "city", "addressLine1"};
        csvWriter.writeHeader(csvHeader);
        for (Customer c: customerList) {
            csvWriter.write(c, fieldMapping);
        }
        csvWriter.close();
    }

}
