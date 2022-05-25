package dyplom.e_commerce.entities.export;

import dyplom.e_commerce.entities.Category;
import dyplom.e_commerce.entities.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryCsvExporter extends AbstractExporter{

    public void export(List<Category> categoryList, HttpServletResponse httpServletResponse) throws IOException {
        super.setResponseHeader(httpServletResponse,"text/csv", ".csv");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Category Id", "Name", "Alias", "Enabled"};
        String[] fieldMapping = {"id", "name", "alias", "enabled"};
        csvWriter.writeHeader(csvHeader);
        for (Category c: categoryList) {
            csvWriter.write(c, fieldMapping);
        }
        csvWriter.close();
    }
}
