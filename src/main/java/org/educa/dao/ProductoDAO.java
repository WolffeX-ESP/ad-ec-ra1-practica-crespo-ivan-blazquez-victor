package org.educa.dao;

import generated.Productos;
import jakarta.xml.bind.JAXBException;
import org.educa.entity.SummaryEntity;

public interface ProductoDAO {

    public Productos readXml(String fileXml) throws JAXBException;

    void writeFile(SummaryEntity summaryEntity);

}
