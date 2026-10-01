package org.educa.dao;

import generated.Productos;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.educa.entity.SummaryEntity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public Productos readXml(String fileXml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Productos.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Productos) unmarshaller.unmarshal(new File(fileXml));
    }

    @Override
    public void writeFile(SummaryEntity summaryEntity){
        try (FileWriter fileWriter = new FileWriter(summaryEntity.getFileName())) {
            fileWriter.write(summaryEntity.toPrint());
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
