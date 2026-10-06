package org.educa.dao;

import generated.Productos;
import jakarta.xml.bind.JAXBException;
import org.educa.entity.ProductoEntity;
import org.educa.entity.SummaryEntity;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ProductoDAO {

    /**
     * Read a xml
     *
     * @param fileXml path of the XML file to read
     * @return Productos object containing all the products in the file
     * @throws JAXBException if an error occurs while reading or parsing the XML
     */
    Productos readXml(String fileXml) throws JAXBException;

    /**
     * Write in a file text the resume of the inventory
     *
     * @param summaryEntity this is the entity with the data of the resume that will write
     */
    void writeFile(SummaryEntity summaryEntity);

    /**
     * Exports the inventory data to an Excel file using Apache POI.
     *
     * @param path    destination directory path.
     * @param productos is the list of products.
     * @throws JAXBException if an error occurs while processing the XML.
     * @throws IOException   if an I/O error occurs.
     * @throws ParseException if a text parsing error occurs.
     */
    void exportExcelDAO(String path, List<ProductoEntity> productos) throws JAXBException, IOException, ParseException;
}
