package org.educa.dao;

import generated.Productos;
import jakarta.xml.bind.JAXBException;
import org.educa.entity.SummaryEntity;

import java.io.File;
import java.io.IOException;

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

}
