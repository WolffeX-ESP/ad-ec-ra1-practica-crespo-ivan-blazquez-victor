package org.educa.dao;

import generated.Productos;
import jakarta.xml.bind.JAXBException;

public interface ProductoDAO {

    public Productos readXml(String fileXml) throws JAXBException;

}
