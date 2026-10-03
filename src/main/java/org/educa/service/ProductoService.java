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
     * @param fileXml this is the absolute path of the XML file
     * @throws JAXBException control if occur an error of unmarshalling or reading the XML file
     * @throws IOException if an error occcurs while creating or writing the summary
     */

    public void exportSummary(String path, String fileXml) throws JAXBException, IOException {
        // Read the list of products and then calculate the total profit
        List<ProductoEntity> productoEntities = readFile(fileXml);
        BigDecimal beneficioTotal = calculateTotalBenefit(productoEntities);

        // Prepare the file and destination
        File file = buildNameTxt(path, fileXml);

        // Create the summary entity
        SummaryEntity summaryEntity = createSummaryEntity(productoEntities.size(), beneficioTotal, file);

        // This is the first write to generate the txt on the disk
        productoDAO.writeFile(summaryEntity);

        // Measures the file size
        summaryEntity.setFileSize(file.length());

        // This is de second write to save the final information with the correct lenght
        productoDAO.writeFile(summaryEntity);

    }

    /**
     * Sums the individual profit of all the products of the list.
     *
     * @param productoEntities list of product entities that contain the profit values.
     * @return the accumulated total profit.
     */

    private BigDecimal calculateTotalBenefit(List<ProductoEntity> productoEntities) {
        BigDecimal beneficioTot = BigDecimal.valueOf(0);
        for (ProductoEntity producto : productoEntities){
            beneficioTot = beneficioTot.add(producto.getProfit());
        }
        return beneficioTot;
    }

    /**
     * Transform the input XML filename to a TXT summary format
     * and then create the directories if it doesn't exist.
     *
     * @param path destination of directory path.
     * @param fileXml the input XML path.
     * @return an object with the directory and the new file name.
     */
    private File buildNameTxt(String path, String fileXml) {
        File xmlFile = new File(fileXml);
        String nombreXml = xmlFile.getName();

        String nombreTxt = nombreXml.replace("inventario_", "result_").replace(".xml", ".txt");

        File directorio = new File(path);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        return new File(directorio, nombreTxt);
    }

    /**
     * Builds and filled the SummaryEntity with the product counts, total profit
     * and file paths.
     *
     * @param productosCuenta total number of processed products.
     * @param beneficioTotal accumulate the profit sum.
     * @param finalFile the output file reference.
     * @return a filled SummaryEntity with all the instances
     */
    private SummaryEntity createSummaryEntity(int productosCuenta, BigDecimal beneficioTotal, File finalFile) {
        SummaryEntity summary = new SummaryEntity();
        summary.setName(LocalDate.now().toString());
        summary.setNumberOfProducts(productosCuenta);
        summary.setTotalProfit(beneficioTotal);
        summary.setFileName(finalFile.getPath());
        summary.setFileAbsolutePath(finalFile.getAbsolutePath());
        return summary;
    }

    public void exportExcel(String path, String fileXml) throws JAXBException, IOException, ParseException {
        //TODO: Implementar
    }
}
