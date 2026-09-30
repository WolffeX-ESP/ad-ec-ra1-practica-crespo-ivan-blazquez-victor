package org.educa.service;

import generated.Producto;
import generated.Productos;
import jakarta.xml.bind.JAXBException;
import org.educa.dao.ProductoDAO;
import org.educa.dao.ProductoDAOImpl;
import org.educa.entity.ProductoEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {

    // DataBase of the first exercise
    private final ProductoDAO productoDAO = new ProductoDAOImpl();


    /**
     * First method is about the first exercise, reading the xml File and return a list with the products and specific methods calculations.
     * @param fileXml This is about the conversion to convert the xml to a File to read it from Java.
     * @return the variable "resultado" that is a list of "productoEntity" with the final price, cost and benefits.
     * @throws JAXBException it's like an IOException doing a try catch on the main.
     */
    public List<ProductoEntity> readFile(String fileXml) throws JAXBException {

        Productos productoData = productoDAO.readXml(fileXml);
        List<ProductoEntity> resultado = new ArrayList<>();
        for (Producto producto : productoData.getProducto()) {
            ProductoEntity productoEntity = new ProductoEntity();
            productoEntity.setProducto(producto);

            /*
            We had to investigate about the BigDecimal because we thought that is a primal type,
            we tried to do it like with basics maths but it didn't work,
            finally we discovered about this is am object and we have to use the specific methods.
             */

            BigDecimal precioFinal = producto.getPrecio().subtract(producto.getDescuento());
            productoEntity.setPrecioFinal(precioFinal);

            BigDecimal costes = producto.getCostes().getCostesAlmacenaje().add(producto.getCostes().getCostesEnvio());
            productoEntity.setCost(costes);

            BigDecimal beneficio = precioFinal.subtract(costes);
            productoEntity.setProfit(beneficio);
            resultado.add(productoEntity);
        }
        return resultado;
    }

    public void exportSummary(String path, String fileXml) throws JAXBException, IOException {
        //TODO: Implementar

    }

    public void exportExcel(String path, String fileXml) throws JAXBException, IOException, ParseException {
        //TODO: Implementar
    }
}
