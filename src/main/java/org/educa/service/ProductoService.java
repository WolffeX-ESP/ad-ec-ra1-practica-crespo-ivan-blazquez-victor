package org.educa.service;

import generated.Producto;
import generated.Productos;
import jakarta.xml.bind.JAXBException;
import org.educa.dao.ProductoDAO;
import org.educa.dao.ProductoDAOImpl;
import org.educa.entity.ProductoEntity;
import org.educa.entity.SummaryEntity;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
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

            // add final price with the percentual discount and doing a value.of
            BigDecimal precioFinal = producto.getPrecio().subtract(producto.getDescuento());
            productoEntity.setPrecioFinal(precioFinal);

            // add cost
            BigDecimal costes = producto.getCostes().getCostesAlmacenaje().add(producto.getCostes().getCostesEnvio());
            productoEntity.setCost(costes);

            // add benefits
            BigDecimal beneficio = precioFinal.subtract(costes);
            productoEntity.setProfit(beneficio);
            resultado.add(productoEntity);
        }
        return resultado;
    }

    /**
     * Read a product XML then calculates the total profit and exports a summary file
     *
     * @param path this is the destination directory path where the file will be saved
     * @param fileXml this is the path of the XML file
     * @throws JAXBException control if occur an error of unmarshalling or reading the XML file
     * @throws IOException if an error occcurs while creating or writing the summary
     */

    public void exportSummary(String path, String fileXml) throws JAXBException, IOException {
        // Read the list of products
        List<ProductoEntity> productoEntities = readFile(fileXml);

        // Calculate the total profit
        BigDecimal beneficioT = BigDecimal.valueOf(0);
        for (ProductoEntity p : productoEntities){
             beneficioT =  beneficioT.add(p.getProfit());
        }

        // Transform the name
        File xmlFile = new File(fileXml);
        String nombreXml = xmlFile.getName();
        String extension = nombreXml.replace(".xml", ".txt");
        String nombreTxt = nombreXml.replace("inventario_", "result_");

        // Check that the directory exists and create it.
        File directorio = new File(path);
        directorio.mkdirs();

        // Build the final TXT
        File txtFinal = new File(directorio, nombreTxt);

        // Create the entity resume
        SummaryEntity summaryEntity = new SummaryEntity();
        summaryEntity.setName(LocalDate.now().toString());
        summaryEntity.setNumberOfProducts(productoEntities.size());
        summaryEntity.setTotalProfit(beneficioT);
        summaryEntity.setFileName(txtFinal.getPath());
        summaryEntity.setFileAbsolutePath(txtFinal.getAbsolutePath());

        // This is the first write to generate de txt on the disk
        productoDAO.writeFile(summaryEntity);

        // Measures the file size
        summaryEntity.setFileSize(txtFinal.length());

        // This is de second write to save the final information with the correct lenght
        productoDAO.writeFile(summaryEntity);

    }

    public void exportExcel(String path, String fileXml) throws JAXBException, IOException, ParseException {
        //TODO: Implementar
    }
}
