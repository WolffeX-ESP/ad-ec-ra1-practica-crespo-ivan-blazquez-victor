package org.educa.dao;

import generated.Productos;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class ProductoDAOImpl {
    /**
     *
     *
     * @param fileXml path of the XML file to read
     * @return Productos object containing all the products in the file
     * @throws JAXBException if an error occurs while reading or parsing the XML
     */
    public Productos readXml(String fileXml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Productos.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Productos) unmarshaller.unmarshal(new File(fileXml));
    }
}
